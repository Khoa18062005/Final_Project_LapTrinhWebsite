package viettech.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.util.EmailUtilBrevo;  // ← Đổi import
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/send-otp")
public class SendOtpServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(SendOtpServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // Validate email
            if (email == null || email.trim().isEmpty()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Email không được để trống");
                response.getWriter().write(new Gson().toJson(jsonResponse));
                return;
            }

            // Tạo OTP
            String otp = EmailUtilBrevo.generateOTP();
            logger.info("Generated OTP for {}: {}", email, otp);

            // Gửi OTP qua email bằng Brevo
            boolean sent = EmailUtilBrevo.sendOTP(email, otp);

            if (sent) {
                // Lưu OTP vào session
                SessionUtil.setAttribute(request, "otp", otp);
                SessionUtil.setAttribute(request, "otpEmail", email);
                SessionUtil.setAttribute(request, "otpTime", System.currentTimeMillis());

                jsonResponse.put("success", true);
                jsonResponse.put("message", "OTP đã được gửi đến email của bạn");
                logger.info("✓ OTP sent successfully to: {}", email);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Không thể gửi OTP. Vui lòng thử lại");
                logger.error("✗ Failed to send OTP to: {}", email);
            }

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Lỗi server: " + e.getMessage());
            logger.error("✗ Error sending OTP", e);
        }

        response.getWriter().write(new Gson().toJson(jsonResponse));
    }
}