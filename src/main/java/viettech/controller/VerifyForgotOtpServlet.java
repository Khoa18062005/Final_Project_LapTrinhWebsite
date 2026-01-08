package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.util.EmailUtilBrevo;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verify-forgot-otp")
public class VerifyForgotOtpServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(VerifyForgotOtpServlet.class);
    private static final int OTP_DURATION = 90; // 90 giây

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = (String) SessionUtil.getAttribute(request, "forgotEmail");
        Long otpTime = (Long) SessionUtil.getAttribute(request, "forgotOtpTime");

        if (email == null || otpTime == null) {
            // Nếu chưa có email hoặc otpTime, quay lại trang quên mật khẩu
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        // Tính thời gian còn lại
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - otpTime) / 1000;
        int timeLeft = (int) (OTP_DURATION - elapsedSeconds);

        if (timeLeft < 0) {
            timeLeft = 0;
            // Xóa OTP đã hết hạn
            SessionUtil.removeAttribute(request, "forgotOtp");
        }

        // Che một phần email để hiển thị
        String[] parts = email.split("@");
        String maskedEmail = "";
        if (parts.length == 2) {
            String username = parts[0];
            String domain = parts[1];
            if (username.length() > 2) {
                String firstTwo = username.substring(0, 2);
                String lastTwo = username.substring(username.length() - 2);
                maskedEmail = firstTwo + "****" + lastTwo + "@" + domain;
            } else {
                maskedEmail = username + "@" + domain;
            }
        }

        // Truyền dữ liệu sang JSP
        request.setAttribute("maskedEmail", maskedEmail);
        request.setAttribute("timeLeft", timeLeft);
        request.setAttribute("isExpired", timeLeft <= 0);

        request.getRequestDispatcher("/WEB-INF/views/verify-otp-forgot.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String otp = request.getParameter("otp");
        String sessionOtp = (String) SessionUtil.getAttribute(request, "forgotOtp");
        Long otpTime = (Long) SessionUtil.getAttribute(request, "forgotOtpTime");

        // Validate OTP
        if (otp == null || sessionOtp == null || otpTime == null) {
            request.setAttribute("errorMessage", "Phiên làm việc đã hết hạn. Vui lòng thử lại.");
            doGet(request, response);
            return;
        }

        if (EmailUtilBrevo.verifyOTP(otp, sessionOtp, otpTime)) {
            // OTP hợp lệ, chuyển đến trang đặt lại mật khẩu
            SessionUtil.setAttribute(request, "otpVerified", true);
            response.sendRedirect(request.getContextPath() + "/reset-password");
        } else {
            request.setAttribute("errorMessage", "Mã OTP không đúng hoặc đã hết hạn");
            doGet(request, response);
        }
    }
}