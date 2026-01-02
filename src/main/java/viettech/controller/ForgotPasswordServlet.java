package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.entity.user.Customer;
import viettech.service.UserService;
import viettech.util.EmailUtilBrevo;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordServlet.class);
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        
        // Validate email
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập email");
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp")
                    .forward(request, response);
            return;
        }

        email = email.trim().toLowerCase();
        
        // Kiểm tra email có tồn tại trong hệ thống
        Customer customer = userService.findCustomerByEmail(email);
        
        if (customer == null) {
            // Vẫn hiển thị thông báo thành công (security best practice)
            request.setAttribute("infoMessage", 
                "Nếu email tồn tại trong hệ thống, chúng tôi đã gửi mã OTP. Vui lòng kiểm tra hộp thư.");
            
            // Forward tới trang OTP nhưng không có email trong session
            request.getRequestDispatcher("/WEB-INF/views/verify-otp-forgot.jsp")
                    .forward(request, response);
            return;
        }

        // Tạo và gửi OTP
        String otp = EmailUtilBrevo.generateOTP();
        long otpTime = System.currentTimeMillis();
        
        boolean sent = EmailUtilBrevo.sendForgotPasswordOTP(
            email, 
            otp, 
            customer.getFirstName() + " " + customer.getLastName()
        );
        
        if (sent) {
            // Lưu thông tin vào session
            SessionUtil.setAttribute(request, "forgotEmail", email);
            SessionUtil.setAttribute(request, "forgotOtp", otp);
            SessionUtil.setAttribute(request, "forgotOtpTime", otpTime);
            SessionUtil.setAttribute(request, "forgotUserId", customer.getUserId());
            
            logger.info("✓ Forgot password OTP sent to: {}", email);
            
            // Redirect tới trang verify OTP
            response.sendRedirect(request.getContextPath() + "/verify-forgot-otp");
        } else {
            request.setAttribute("errorMessage", "Không thể gửi email. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp")
                    .forward(request, response);
        }
    }
}