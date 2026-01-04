package viettech.controller;

import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/resend-forgot-otp")
public class ResendForgotOtpServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ResendForgotOtpServlet.class);
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();
        Gson gson = new Gson();

        try {
            String email = (String) SessionUtil.getAttribute(request, "forgotEmail");

            if (email == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Phiên làm việc đã hết hạn. Vui lòng thử lại từ đầu.");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            // Tìm customer
            Customer customer = userService.findCustomerByEmail(email);
            if (customer == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Không tìm thấy tài khoản với email này");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            // Tạo OTP mới
            String otp = EmailUtilBrevo.generateOTP();
            long otpTime = System.currentTimeMillis();

            // Gửi OTP
            boolean sent = EmailUtilBrevo.sendForgotPasswordOTP(
                    email,
                    otp,
                    customer.getFirstName() + " " + customer.getLastName()
            );

            if (sent) {
                // Cập nhật session với OTP mới
                SessionUtil.setAttribute(request, "forgotOtp", otp);
                SessionUtil.setAttribute(request, "forgotOtpTime", otpTime);

                jsonResponse.put("success", true);
                jsonResponse.put("message", "Đã gửi lại mã OTP thành công");
                logger.info("✓ Resent forgot password OTP to: {}", email);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Không thể gửi email. Vui lòng thử lại sau");
            }

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Lỗi hệ thống: " + e.getMessage());
            logger.error("✗ Error resending forgot password OTP", e);
        }
        response.getWriter().write(gson.toJson(jsonResponse));
    }
}