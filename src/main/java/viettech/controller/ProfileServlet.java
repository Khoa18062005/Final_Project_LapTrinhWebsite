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
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ProfileServlet extends HttpServlet {

    private final ProfileService profileService = new ProfileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(request, "user");

        if (user == null) {
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập để xem thông tin cá nhân!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("user", user);
        // Chuyển hướng đến servlet hiển thị hoặc JSP
        request.getRequestDispatcher("/WEB-INF/views/profile/info.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        User user = (User) SessionUtil.getAttribute(request, "user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. ĐỌC DỮ LIỆU TỪ FORM
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
            // 2. XỬ LÝ ĐỔI EMAIL (NẾU CÓ)
            if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
                if (!handleEmailChange(request, dto, user)) {
                    // Nếu lỗi OTP/Email thì quay lại ngay
                    response.sendRedirect(request.getContextPath() + "/profile");
                    return;
                }
            }

            // 3. XỬ LÝ AVATAR (UPLOAD HOẶC XÓA)
            // Logic này cập nhật trực tiếp vào object 'user'
            handleAvatarUpdate(request, user);

            // 4. GỌI SERVICE CẬP NHẬT DATABASE
            // Service sẽ lấy thông tin từ 'user' (avatar) và 'dto' (info) để update
            boolean success = profileService.updateProfile(user, dto);

            if (success) {
                // 5. CẬP NHẬT SESSION VỚI DỮ LIỆU MỚI
                // Cập nhật các trường thông tin vào object user trong session để hiển thị ngay
                user.setFirstName(dto.getFirstName());
                user.setLastName(dto.getLastName());
                user.setPhone(dto.getPhone());
                user.setEmail(dto.getEmail()); // Nếu đã verify thành công ở trên

                // (Các trường ngày sinh/gender cần xử lý trong DTO -> User mapping nếu muốn hiện ngay)

                SessionUtil.setAttribute(request, "user", user);
                SessionUtil.setSuccessMessage(request, "Cập nhật hồ sơ thành công!");
            } else {
                SessionUtil.setErrorMessage(request, "Cập nhật thất bại. Vui lòng thử lại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
        }

        // 6. REDIRECT
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    /**
     * Xử lý Cập nhật Avatar (Bao gồm cả Xóa và Upload mới)
     */
    private void handleAvatarUpdate(HttpServletRequest request, User user) throws IOException, ServletException {
        // Kiểm tra cờ xóa ảnh
        String deleteAvatarFlag = request.getParameter("deleteAvatar");

        if ("true".equals(deleteAvatarFlag)) {
            // --- TRƯỜNG HỢP 1: NGƯỜI DÙNG MUỐN XÓA ẢNH ---

            // Xóa ảnh cũ trên Cloudinary để tiết kiệm
            if (user.getAvatar() != null) {
                CloudinaryUtil.deleteImage(user.getAvatar());
            }

            // Set avatar trong DB về null (hoặc chuỗi rỗng tùy logic DB của bạn)
            user.setAvatar(null);

        } else {
            // --- TRƯỜNG HỢP 2: UPLOAD ẢNH MỚI (NẾU CÓ) ---
            Part avatarPart = request.getPart("avatarFile");

            if (avatarPart != null && avatarPart.getSize() > 0) {
                // 1. Upload ảnh mới
                String newAvatarUrl = CloudinaryUtil.uploadAvatar(avatarPart);

                if (newAvatarUrl != null) {
                    // 2. Nếu upload thành công, xóa ảnh cũ (nếu có)
                    if (user.getAvatar() != null) {
                        CloudinaryUtil.deleteImage(user.getAvatar());
                    }
                    // 3. Set URL mới cho user
                    user.setAvatar(newAvatarUrl);
                }
            }
        }
    }

    /**
     * Xử lý verify OTP khi đổi email
     */
    private boolean handleEmailChange(HttpServletRequest request, Profile_dto dto, User user) {
        String inputOTP = dto.getEmailOtp();
        String savedOTP = (String) SessionUtil.getAttribute(request, "emailOtp");
        String pendingEmail = (String) SessionUtil.getAttribute(request, "newEmail");
        Long otpTime = (Long) SessionUtil.getAttribute(request, "emailOtpTime");

        // 1. Kiểm tra OTP
        if (inputOTP == null || inputOTP.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "Vui lòng nhập mã OTP để xác nhận email mới!");
            return false;
        }

        if (!EmailUtilBrevo.verifyOTP(inputOTP, savedOTP, otpTime != null ? otpTime : 0)) {
            SessionUtil.setErrorMessage(request, "Mã OTP không đúng hoặc đã hết hạn!");
            return false;
        }

        // 2. Kiểm tra email nhập vào có khớp với email đã nhận OTP không
        if (!dto.getEmail().equals(pendingEmail)) {
            SessionUtil.setErrorMessage(request, "Email xác nhận không khớp!");
            return false;
        }

        // 3. Kiểm tra email đã tồn tại chưa (User khác)
        if (profileService.isEmailExistForOtherUser(dto.getEmail(), user.getUserId())) {
            SessionUtil.setErrorMessage(request, "Email này đã được sử dụng bởi tài khoản khác!");
            return false;
        }

        // Xóa session OTP sau khi thành công
        SessionUtil.removeAttribute(request, "emailOtp");
        SessionUtil.removeAttribute(request, "emailOtpTime");
        SessionUtil.removeAttribute(request, "newEmail");

        return true;
    }
}