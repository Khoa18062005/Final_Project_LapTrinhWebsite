package viettech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import viettech.entity.user.User;
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

@WebServlet("/send-email-otp")
public class SendEmailOtpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Lấy user từ session
            User user = (User) SessionUtil.getAttribute(req, "user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(mapper.writeValueAsString(response));
                return;
            }

            // Lấy email mới từ request
            String newEmail = req.getParameter("email");
            if (newEmail == null || newEmail.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email không được để trống");
                resp.getWriter().write(mapper.writeValueAsString(response));
                return;
            }

            // Kiểm tra email trùng với email cũ
            if (newEmail.equals(user.getEmail())) {
                response.put("success", false);
                response.put("message", "Email mới trùng với email hiện tại");
                resp.getWriter().write(mapper.writeValueAsString(response));
                return;
            }

            // Generate OTP
            String otp = EmailUtilBrevo.generateOTP();
            long otpTime = System.currentTimeMillis();

            // Gửi email
            String userName = user.getFirstName() + " " + user.getLastName();
            boolean sent = EmailUtilBrevo.sendEmailChangeOTP(newEmail, otp, userName);

            if (sent) {
                // Lưu OTP vào session
                SessionUtil.setAttribute(req, "emailOtp", otp);
                SessionUtil.setAttribute(req, "emailOtpTime", otpTime);
                SessionUtil.setAttribute(req, "newEmail", newEmail);

                response.put("success", true);
                response.put("message", "Mã OTP đã được gửi đến email mới");
            } else {
                response.put("success", false);
                response.put("message", "Không thể gửi email. Vui lòng thử lại sau");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
        }

        resp.getWriter().write(mapper.writeValueAsString(response));
    }
}