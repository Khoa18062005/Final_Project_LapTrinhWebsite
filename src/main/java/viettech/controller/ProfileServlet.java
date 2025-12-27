package viettech.controller;

import viettech.entity.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 2,      // 2MB
    maxRequestSize = 1024 * 1024 * 5    // 5MB
)
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // Chưa đăng nhập -> redirect về trang chủ
            session.setAttribute("errorMessage", "Vui lòng đăng nhập để xem thông tin cá nhân!");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // Đã đăng nhập -> hiển thị trang profile
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
        
        // Lấy dữ liệu từ form
        String action = request.getParameter("action");
        
        if ("update_info".equals(action)) {
            // Cập nhật thông tin cá nhân
            updatePersonalInfo(request, response, user);
        } else if ("upload_avatar".equals(action)) {
            // Upload ảnh đại diện
            uploadAvatar(request, response, user);
        }
    }
    
    /**
     * Cập nhật thông tin cá nhân
     */
    private void updatePersonalInfo(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String dateOfBirth = request.getParameter("dateOfBirth");
            
            // TODO: Validate dữ liệu
            
            // TODO: Update database
            // userDAO.updateUser(user);
            
            // Cập nhật session
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setGender(gender);
            // user.setDateOfBirth(parse dateOfBirth);
            
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("successMessage", "Cập nhật thông tin thành công!");
            
            response.sendRedirect(request.getContextPath() + "/profile");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
    
    /**
     * Upload ảnh đại diện
     */
    private void uploadAvatar(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        try {
            Part filePart = request.getPart("avatar");
            
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                
                // TODO: Lưu file vào thư mục uploads
                // String uploadPath = getServletContext().getRealPath("/uploads/avatars");
                // filePart.write(uploadPath + File.separator + fileName);
                
                // TODO: Update database
                // user.setAvatar(fileName);
                // userDAO.updateUser(user);
                
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("successMessage", "Cập nhật ảnh đại diện thành công!");
            }
            
            response.sendRedirect(request.getContextPath() + "/profile");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Upload ảnh thất bại: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
}