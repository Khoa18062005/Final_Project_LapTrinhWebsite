package viettech.controller;

import viettech.entity.user.User;
import viettech.util.CloudinaryUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,      // 5MB
        maxRequestSize = 10 * 1024 * 1024   // 10MB
)
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("errorMessage", "Vui lòng đăng nhập để xem thông tin cá nhân!");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            // 1. Lấy thông tin text từ form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");

            // 2. Kiểm tra có file avatar không
            Part avatarPart = request.getPart("avatarFile");

            if (avatarPart != null && avatarPart.getSize() > 0) {
                // CÓ upload ảnh mới

                // Upload lên Cloudinary
                String newAvatarUrl = CloudinaryUtil.uploadAvatar(avatarPart);

                // Xóa ảnh cũ trên Cloudinary (nếu có)
                String oldAvatar = user.getAvatar();
                if (oldAvatar != null && !oldAvatar.isEmpty()
                        && oldAvatar.contains("cloudinary.com")) {
                    CloudinaryUtil.deleteImage(oldAvatar);
                }

                // Cập nhật URL ảnh mới
                user.setAvatar(newAvatarUrl);
            }

            // 3. Cập nhật thông tin khác
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setGender(gender);

            // 4. TODO: Lưu vào database
            // userDAO.updateUser(user);

            // 5. Cập nhật session
            session.setAttribute("user", user);
            session.setAttribute("successMessage", "Cập nhật thông tin thành công!");

        } catch (IllegalArgumentException e) {
            // Lỗi validation (file không hợp lệ)
            session.setAttribute("errorMessage", e.getMessage());

        } catch (Exception e) {
            // Lỗi khác
            e.printStackTrace();
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        // 6. Redirect về trang profile
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}