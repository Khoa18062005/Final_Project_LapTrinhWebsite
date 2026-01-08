package viettech.controller;

import viettech.dto.Shipper_dto;
import viettech.entity.user.User;
import viettech.service.ShipperService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig; // <-- Bắt buộc để nhận file
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; // Import Part, HttpServletRequest, ...
import java.io.IOException;

@WebServlet("/shipper")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB (Lưu bộ nhớ đệm)
        maxFileSize = 1024 * 1024 * 10,      // 10MB (Kích thước file tối đa)
        maxRequestSize = 1024 * 1024 * 50    // 50MB (Tổng kích thước request)
)
public class ShipperServlet extends HttpServlet {

    private final ShipperService service = new ShipperService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // API: accept delivery from notification link
        String action = request.getParameter("action");
        if ("acceptDelivery".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;
            if (user == null || user.getRoleID() != 3) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized\"}");
                return;
            }

            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Missing orderId\"}");
                return;
            }

            int orderId;
            try {
                orderId = Integer.parseInt(orderIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Invalid orderId\"}");
                return;
            }

            try {
                // Find an assignment for this shipper+order and accept it via VendorService (DB handles lock/validation)
                int shipperId = user.getUserId();
                int assignmentId = ((viettech.service.ShipperService) service).findAssignmentIdForOrderAndShipper(orderId, shipperId);
                if (assignmentId <= 0) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"success\":false,\"message\":\"No assignment found for this order\"}");
                    return;
                }

                boolean ok;
                try {
                    Object result = new viettech.service.VendorService().getClass()
                            .getMethod("assignOrder", int.class, int.class)
                            .invoke(new viettech.service.VendorService(), assignmentId, shipperId);
                    ok = result instanceof Boolean && (Boolean) result;
                } catch (Exception reflectEx) {
                    throw new RuntimeException("assignOrder method not available", reflectEx);
                }
                if (ok) {
                    response.getWriter().write("{\"success\":true}");
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"success\":false,\"message\":\"Accept failed\"}");
                }
                return;
            } catch (Exception ex) {
                // If DB throws SQLSTATE=45000, surface as errorMessage for frontend
                String msg = ex.getMessage() != null ? ex.getMessage().replace("\"", "'") : "Unknown error";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"errorMessage\":\"" + msg + "\"}");
                return;
            }
        }

        // 1. Lấy Session hiện tại
        HttpSession session = request.getSession(false);

        // 2. Kiểm tra đăng nhập
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 3. Kiểm tra quyền (Shipper = Role 3)
        if (user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 4. Lấy dữ liệu Dashboard
        int shipperId = user.getUserId();
        Shipper_dto data = service.getDashboardData(shipperId);

        // 5. Xử lý trường hợp data rỗng
        if (data == null) {
            data = new Shipper_dto();
        }

        // 6. Đẩy dữ liệu và chuyển hướng
        request.setAttribute("data", data);
        request.getRequestDispatcher("/WEB-INF/views/shipper.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        // --- CASE 1: XỬ LÝ ĐƠN HÀNG (Nhận / Hoàn thành) ---
        if ("accept".equals(action) || "complete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int assignmentId = Integer.parseInt(idStr);
                    service.updateStatus(assignmentId, action);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // --- CASE 2: CẬP NHẬT PROFILE (BAO GỒM AVATAR, BIỂN SỐ, GPLX) ---
        else if ("updateProfile".equals(action)) {
            try {
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String phone = request.getParameter("phone");
                String password = request.getParameter("password");
                String vehiclePlate = request.getParameter("vehiclePlate");
                String licenseNumber = request.getParameter("licenseNumber");

                // --- XỬ LÝ ẢNH ĐẠI DIỆN ---
                String avatarUrl = user.getAvatar(); // Mặc định giữ ảnh cũ

                // 1. Kiểm tra cờ xóa ảnh (từ JS gửi lên)
                String deleteAvatar = request.getParameter("deleteAvatar");
                if ("true".equals(deleteAvatar)) {
                    avatarUrl = null; // Xóa ảnh trong DB (hoặc set link ảnh default)
                } else {
                    // 2. Upload ảnh mới (Sử dụng CloudinaryUtil vừa sửa)
                    Part filePart = request.getPart("avatarFile");

                    // Hàm uploadAvatar đã tự kiểm tra null và size, nên gọi thẳng
                    String uploadedUrl = viettech.util.CloudinaryUtil.uploadAvatar(filePart);

                    if (uploadedUrl != null) {
                        avatarUrl = uploadedUrl;
                        System.out.println("DEBUG: Đã upload ảnh mới lên Cloudinary: " + avatarUrl);
                    }
                }

                // Gọi Service cập nhật xuống Database (Thêm tham số avatarUrl)
                service.updateProfile(
                        user.getUserId(),
                        firstName,
                        lastName,
                        phone,
                        password,
                        vehiclePlate,
                        licenseNumber,
                        avatarUrl
                );

                // Cập nhật lại Session để giao diện hiển thị thông tin mới ngay lập tức
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                if (avatarUrl != null) {
                    user.setAvatar(avatarUrl);
                }

                session.setAttribute("user", user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Redirect để tránh lỗi resubmission khi F5
        response.sendRedirect(request.getContextPath() + "/shipper");
    }
}