package viettech.controller;

import viettech.dto.Shipper_dto;
import viettech.entity.user.User;
import viettech.service.ShipperService;
import viettech.util.CloudinaryUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/shipper")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ShipperServlet extends HttpServlet {

    private final ShipperService service = new ShipperService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra Session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. Kiểm tra Quyền (Shipper = Role 3)
        if (user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 3. Lấy dữ liệu Dashboard
        Shipper_dto data = service.getDashboardData(user.getUserId());

        if (data == null) {
            data = new Shipper_dto();
        }

        request.setAttribute("data", data);
        request.getRequestDispatcher("/WEB-INF/views/shipper.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Kiểm tra xác thực (Authentication)
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || user.getRoleID() != 3) {
            // Nếu là gọi AJAX (Ví dụ: cập nhật GPS ngầm), trả về lỗi 401
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }

        String action = request.getParameter("action");

        // --- CASE 1: CẬP NHẬT VỊ TRÍ GPS (AJAX) ---
        // Đưa case này lên đầu để return ngay, tránh các logic redirect thừa
        if ("updateLocation".equals(action)) {
            try {
                String latStr = request.getParameter("lat");
                String lonStr = request.getParameter("lon");

                if (latStr != null && lonStr != null) {
                    double lat = Double.parseDouble(latStr);
                    double lon = Double.parseDouble(lonStr);

                    // Gọi Service update
                    service.updateLocation(user.getUserId(), lat, lon);
                    System.out.println("DEBUG: GPS Updated for User " + user.getUserId() + ": " + lat + ", " + lon);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return; // KẾT THÚC REQUEST NGAY TẠI ĐÂY
        }

        // --- CASE 2: XỬ LÝ ĐƠN HÀNG (Accept / Complete) ---
        else if ("accept".equals(action) || "complete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int assignmentId = Integer.parseInt(idStr);
                    service.updateStatus(assignmentId, action, user.getUserId());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // --- CASE 3: CẬP NHẬT PROFILE & AVATAR ---
        else if ("updateProfile".equals(action)) {
            try {
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String phone = request.getParameter("phone");
                String password = request.getParameter("password");
                String vehiclePlate = request.getParameter("vehiclePlate");
                String licenseNumber = request.getParameter("licenseNumber");

                // Logic Avatar: null (giữ nguyên), "" (xóa), "link" (cập nhật)
                String avatarUrl = null;

                String deleteAvatar = request.getParameter("deleteAvatar");
                if ("true".equals(deleteAvatar)) {
                    avatarUrl = ""; // Đánh dấu là xóa

                    // (Optional) Xóa ảnh cũ trên Cloudinary nếu cần
                    if (user.getAvatar() != null) {
                        CloudinaryUtil.deleteImage(user.getAvatar());
                    }
                } else {
                    // Kiểm tra file upload
                    Part filePart = request.getPart("avatarFile");
                    if (filePart != null && filePart.getSize() > 0) {
                        String uploadedUrl = CloudinaryUtil.uploadAvatar(filePart);
                        if (uploadedUrl != null) {
                            avatarUrl = uploadedUrl;

                            // (Optional) Xóa ảnh cũ khi đã có ảnh mới
                            if (user.getAvatar() != null) {
                                CloudinaryUtil.deleteImage(user.getAvatar());
                            }
                        }
                    }
                }

                // Gọi Service
                service.updateProfile(
                        user.getUserId(), firstName, lastName, phone, password,
                        vehiclePlate, licenseNumber, avatarUrl
                );

                // Cập nhật lại Session để giao diện đổi ngay
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);

                if (avatarUrl != null) {
                    if (avatarUrl.isEmpty()) user.setAvatar(null);
                    else user.setAvatar(avatarUrl);
                }
                session.setAttribute("user", user);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Redirect lại trang Dashboard (Tránh Resubmit form khi F5)
        response.sendRedirect(request.getContextPath() + "/shipper");
    }
}