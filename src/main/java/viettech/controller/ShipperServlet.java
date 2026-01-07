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
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB (Lưu bộ nhớ đệm)
        maxFileSize = 1024 * 1024 * 10,      // 10MB (Kích thước file tối đa)
        maxRequestSize = 1024 * 1024 * 50    // 50MB (Tổng kích thước request)
)
public class ShipperServlet extends HttpServlet {

    private final ShipperService service = new ShipperService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thiết lập Tiếng Việt cho response
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

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

        // Bắt buộc thiết lập UTF-8 ngay đầu tiên để đọc form tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

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

                if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {
                    double lat = Double.parseDouble(latStr);
                    double lon = Double.parseDouble(lonStr);

                    // Gọi Service update
                    service.updateLocation(user.getUserId(), lat, lon);
                    System.out.println("DEBUG: GPS Updated for User " + user.getUserId() + ": " + lat + ", " + lon);

                    // Phản hồi OK cho Client
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return; // KẾT THÚC REQUEST NGAY TẠI ĐÂY (Không redirect)
        }

        // --- CASE 2: XỬ LÝ ĐƠN HÀNG (Accept / Complete) ---
        else if ("accept".equals(action) || "complete".equals(action)) {
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
                String currentAvatar = user.getAvatar();

                String deleteAvatar = request.getParameter("deleteAvatar");

                if ("true".equals(deleteAvatar)) {
                    avatarUrl = ""; // Đánh dấu là xóa

                    // Nếu muốn xóa trên Cloudinary thì uncomment dòng dưới
                    // if (currentAvatar != null) CloudinaryUtil.deleteImage(currentAvatar);

                } else {
                    // Kiểm tra file upload
                    Part filePart = request.getPart("avatarFile");
                    if (filePart != null && filePart.getSize() > 0) {
                        // Upload ảnh mới
                        String uploadedUrl = CloudinaryUtil.uploadAvatar(filePart);
                        if (uploadedUrl != null) {
                            avatarUrl = uploadedUrl;

                            // Xóa ảnh cũ trên Cloudinary sau khi upload ảnh mới thành công (Tùy chọn)
                            // if (currentAvatar != null) CloudinaryUtil.deleteImage(currentAvatar);
                        }
                    }
                }

                // Gọi Service
                service.updateProfile(
                        user.getUserId(), firstName, lastName, phone, password,
                        vehiclePlate, licenseNumber, avatarUrl
                );

                // Cập nhật lại Session để giao diện đổi ngay lập tức
                if (firstName != null) user.setFirstName(firstName);
                if (lastName != null) user.setLastName(lastName);
                if (phone != null) user.setPhone(phone);

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