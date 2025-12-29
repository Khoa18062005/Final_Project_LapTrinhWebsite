package viettech.controller;

import viettech.dto.Profile_dto;
import viettech.entity.user.User;
import viettech.service.ProfileService;
import viettech.util.CloudinaryUtil;
import viettech.util.EmailUtilBrevo;
import viettech.util.SessionUtil;

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

    private final ProfileService profileService = new ProfileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(request, "user");

        if (user == null) {
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập để xem thông tin cá nhân!");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/profile/info");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        User user = (User) SessionUtil.getAttribute(request, "user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 1. ĐỌC DỮ LIỆU TỪ FORM → DTO
        Profile_dto dto = new Profile_dto();
        dto.setFirstName(request.getParameter("firstName"));
        dto.setLastName(request.getParameter("lastName"));
        dto.setEmail(request.getParameter("email"));
        dto.setPhone(request.getParameter("phone"));
        dto.setGender(request.getParameter("gender"));
        dto.setDay(request.getParameter("day"));
        dto.setMonth(request.getParameter("month"));
        dto.setYear(request.getParameter("year"));
        dto.setEmailOtp(request.getParameter("emailOtp"));

        try {
            // 2. XỬ LÝ EMAIL NẾU THAY ĐỔI
            if (!dto.getEmail().equals(user.getEmail())) {
                if (!handleEmailChange(request, dto, user)) {
                    response.sendRedirect(request.getContextPath() + "/profile/info");
                    return;
                }
            }

            // 3. XỬ LÝ AVATAR NẾU CÓ
            handleAvatarUpload(request, user);

            // 4. CẬP NHẬT THÔNG TIN QUA SERVICE
            boolean success = profileService.updateProfile(user, dto);

            if (success) {
                // 5. CẬP NHẬT SESSION
                SessionUtil.setAttribute(request, "user", user);
                SessionUtil.setSuccessMessage(request, "Cập nhật thông tin thành công!");
            } else {
                SessionUtil.setErrorMessage(request, "Cập nhật thất bại. Vui lòng thử lại!");
            }

        } catch (IllegalArgumentException e) {
            SessionUtil.setErrorMessage(request, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
        }

        // 6. REDIRECT VỀ PROFILE
        response.sendRedirect(request.getContextPath() + "/profile/info");
    }

    /**
     * Xử lý đổi email (verify OTP)
     * @return true nếu email hợp lệ, false nếu OTP sai
     */
    private boolean handleEmailChange(HttpServletRequest request, Profile_dto dto, User user) {
        String inputOTP = dto.getEmailOtp();
        String savedOTP = (String) SessionUtil.getAttribute(request, "emailOtp");
        String newEmail = (String) SessionUtil.getAttribute(request, "newEmail");
        Long otpTime = (Long) SessionUtil.getAttribute(request, "emailOtpTime");

        // Verify OTP
        if (!EmailUtilBrevo.verifyOTP(inputOTP, savedOTP, otpTime != null ? otpTime : 0)) {
            SessionUtil.setErrorMessage(request, "Mã OTP không đúng hoặc đã hết hạn!");
            return false;
        }

        // Kiểm tra email khớp
        if (!dto.getEmail().equals(newEmail)) {
            SessionUtil.setErrorMessage(request, "Email không khớp với email đã gửi OTP!");
            return false;
        }

        // Kiểm tra email đã tồn tại chưa (cho user khác)
        if (profileService.isEmailExistForOtherUser(dto.getEmail(), user.getUserId())) {
            SessionUtil.setErrorMessage(request, "Email này đã được sử dụng bởi tài khoản khác!");
            return false;
        }

        // Xóa OTP khỏi session
        SessionUtil.removeAttribute(request, "emailOtp");
        SessionUtil.removeAttribute(request, "emailOtpTime");
        SessionUtil.removeAttribute(request, "newEmail");

        return true;
    }

    /**
     * Xử lý upload avatar
     */
    private void handleAvatarUpload(HttpServletRequest request, User user)
            throws IOException, ServletException {

        Part avatarPart = request.getPart("avatarFile");

        if (avatarPart != null && avatarPart.getSize() > 0) {
            // Upload ảnh mới lên Cloudinary
            String newAvatarUrl = CloudinaryUtil.uploadAvatar(avatarPart);

            // Xóa ảnh cũ trên Cloudinary (nếu có)
            String oldAvatar = user.getAvatar();
            if (oldAvatar != null && !oldAvatar.isEmpty() && oldAvatar.contains("cloudinary.com")) {
                CloudinaryUtil.deleteImage(oldAvatar);
            }

            // Cập nhật URL ảnh mới
            user.setAvatar(newAvatarUrl);
        }
    }
}