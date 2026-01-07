package viettech.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Random;

/**
 * Email Utility using Brevo API (formerly Sendinblue)
 * Refactored for reusability
 * @author VietTech Team
 */
public class EmailUtilBrevo {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtilBrevo.class);
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String SENDER_EMAIL = "huyalex009@gmail.com";
    private static final String SENDER_NAME = "VietTech";

    /**
     * Tạo mã OTP ngẫu nhiên 6 số
     */
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Gửi OTP đăng ký tài khoản
     */
    public static boolean sendRegistrationOTP(String toEmail, String otp) {
        String subject = "Mã OTP xác thực tài khoản VietTech";
        String htmlContent = buildRegistrationOTPTemplate(otp);

        try {
            sendMail(toEmail, SENDER_EMAIL, subject, htmlContent, true);
            logger.info("✓ Registration OTP sent to: {}", toEmail);
            return true;
        } catch (IOException e) {
            logger.error("✗ Failed to send registration OTP to: {}", toEmail, e);
            return false;
        }
    }

    /**
     * Gửi OTP đổi email
     */
    public static boolean sendEmailChangeOTP(String toEmail, String otp, String userName) {
        String subject = "Mã OTP xác thực đổi email - VietTech";
        String htmlContent = buildEmailChangeOTPTemplate(otp, userName);

        try {
            sendMail(toEmail, SENDER_EMAIL, subject, htmlContent, true);
            logger.info("✓ Email change OTP sent to: {}", toEmail);
            return true;
        } catch (IOException e) {
            logger.error("✗ Failed to send email change OTP to: {}", toEmail, e);
            return false;
        }
    }

    /**
     * Gửi email thông thường
     */
    public static void sendMail(String to, String from, String subject, String body, boolean bodyIsHTML)
            throws IOException {

        try {
            // 1. Lấy API Key
            String apiKey = System.getenv("BREVO_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                logger.error("✗ Missing BREVO_API_KEY environment variable");
                throw new IOException("Missing BREVO_API_KEY environment variable");
            }

            // 2. Tạo JSON Body
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();

            // Sender
            ObjectNode senderNode = mapper.createObjectNode();
            senderNode.put("name", SENDER_NAME);
            senderNode.put("email", from);
            rootNode.set("sender", senderNode);

            // Receiver
            ArrayNode toArray = mapper.createArrayNode();
            ObjectNode toItem = mapper.createObjectNode();
            toItem.put("email", to);
            toArray.add(toItem);
            rootNode.set("to", toArray);

            // Subject & Body
            rootNode.put("subject", subject);
            if (bodyIsHTML) {
                rootNode.put("htmlContent", body);
            } else {
                rootNode.put("textContent", body);
            }

            String jsonString = mapper.writeValueAsString(rootNode);

            // 3. Gửi HTTP Request
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BREVO_API_URL))
                    .header("api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .build();

            // 4. Nhận response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            // 5. Kiểm tra kết quả
            if (statusCode >= 200 && statusCode < 300) {
                logger.info("✓ Email sent successfully via Brevo API to: {}", to);
            } else {
                logger.error("✗ Failed to send email via Brevo. Status: {}", statusCode);
                logger.error("Response Body: {}", responseBody);
                throw new IOException("Brevo API Error: " + responseBody);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("✗ Sending email interrupted", e);
            throw new IOException("Sending email interrupted", e);
        } catch (Exception e) {
            logger.error("✗ Error sending email via Brevo API", e);
            throw new IOException("Error sending email via Brevo API: " + e.getMessage(), e);
        }
    }

    /**
     * Kiểm tra OTP có hợp lệ không
     */
    public static boolean verifyOTP(String inputOTP, String savedOTP, long createdTime) {
        if (inputOTP == null || savedOTP == null) {
            return false;
        }

        if (!inputOTP.trim().equals(savedOTP)) {
            logger.warn("✗ OTP mismatch: input={}, saved={}", inputOTP, savedOTP);
            return false;
        }

        // Kiểm tra thời gian (90 giây)
        long currentTime = System.currentTimeMillis();
        long otpAge = currentTime - createdTime;
        if (otpAge > 90000) {
            logger.warn("✗ OTP expired. Age: {} ms", otpAge);
            return false;
        }

        logger.info("✓ OTP verified successfully");
        return true;
    }

    /**
     * Template OTP đăng ký tài khoản
     */
    private static String buildRegistrationOTPTemplate(String otp) {
        return buildOTPTemplate(
                "🎉 Chào mừng đến với VietTech!",
                "Cảm ơn bạn đã đăng ký tài khoản tại <strong>VietTech</strong>. Để hoàn tất đăng ký, vui lòng nhập mã OTP bên dưới:",
                otp,
                "Nếu bạn không yêu cầu đăng ký tài khoản, vui lòng bỏ qua email này."
        );
    }

    /**
     * Template OTP đổi email
     */
    private static String buildEmailChangeOTPTemplate(String otp, String userName) {
        return buildOTPTemplate(
                "🔐 Xác thực đổi email",
                "Xin chào <strong>" + userName + "</strong>,<br><br>" +
                        "Bạn đang thực hiện thay đổi địa chỉ email trên hệ thống <strong>VietTech</strong>. " +
                        "Để xác nhận đây là bạn, vui lòng nhập mã OTP bên dưới:",
                otp,
                "Nếu bạn không thực hiện thao tác này, vui lòng bỏ qua email này và bảo mật tài khoản của bạn."
        );
    }

    /**
     * Template OTP chung (tái sử dụng)
     */
    private static String buildOTPTemplate(String title, String description, String otp, String footer) {
        return String.format("""
                <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Mã OTP VietTech</title>
                </head>
                <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
                    <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f4f4f4; padding: 40px 0;">
                        <tr>
                            <td align="center">
                                <table width="600" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                                    <!-- Header -->
                                    <tr>
                                        <td align="center" style="padding: 30px 20px; background: linear-gradient(135deg, #0d6efd, #1e40af); border-radius: 10px 10px 0 0;">
                                            <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: bold;">
                                                %s
                                            </h1>
                                        </td>
                                    </tr>
                                    
                                    <!-- Body -->
                                    <tr>
                                        <td style="padding: 40px 30px;">
                                            <p style="margin: 0 0 20px; font-size: 16px; color: #333333; line-height: 1.6;">
                                                %s
                                            </p>
                                            
                                            <!-- OTP Box -->
                                            <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="margin: 30px 0;">
                                                <tr>
                                                    <td align="center">
                                                        <div style="background: linear-gradient(135deg, #f0f8ff, #e6f3ff); 
                                                                    border: 2px dashed #0d6efd; 
                                                                    border-radius: 8px; 
                                                                    padding: 25px; 
                                                                    text-align: center;">
                                                            <div style="font-size: 36px; 
                                                                        font-weight: bold; 
                                                                        color: #0d6efd; 
                                                                        letter-spacing: 8px; 
                                                                        font-family: 'Courier New', monospace;">
                                                                %s
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                            
                                            <p style="margin: 20px 0; font-size: 14px; color: #666666; line-height: 1.6;">
                                                <strong>⏰ Lưu ý:</strong> Mã OTP có hiệu lực trong <strong>90 giây</strong>.
                                            </p>
                                            
                                            <p style="margin: 20px 0; font-size: 14px; color: #999999; line-height: 1.6;">
                                                %s
                                            </p>
                                        </td>
                                    </tr>
                                    
                                    <!-- Footer -->
                                    <tr>
                                        <td align="center" style="padding: 20px; background-color: #f8f9fa; border-radius: 0 0 10px 10px;">
                                            <p style="margin: 0; font-size: 12px; color: #999999;">
                                                © 2025 <strong>VietTech</strong> - Sàn Thương Mại Điện Tử
                                            </p>
                                            <p style="margin: 5px 0 0; font-size: 12px; color: #999999;">
                                                📧 Email này được gửi tự động, vui lòng không trả lời.
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """, title, description, otp, footer);
    }

    public static boolean sendForgotPasswordOTP(String toEmail, String otp, String userName) {
        String subject = "Mã OTP đặt lại mật khẩu - VietTech";
        String htmlContent = buildForgotPasswordOTPTemplate(otp, userName);

        try {
            sendMail(toEmail, SENDER_EMAIL, subject, htmlContent, true);
            logger.info("✓ Forgot password OTP sent to: {}", toEmail);
            return true;
        } catch (IOException e) {
            logger.error("✗ Failed to send forgot password OTP to: {}", toEmail, e);
            return false;
        }
    }

    /**
     * Template OTP quên mật khẩu
     */
    private static String buildForgotPasswordOTPTemplate(String otp, String userName) {
        return buildOTPTemplate(
                "🔐 Đặt lại mật khẩu",
                "Xin chào <strong>" + userName + "</strong>,<br><br>" +
                        "Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản <strong>VietTech</strong> của bạn. " +
                        "Để tiếp tục, vui lòng nhập mã OTP bên dưới:",
                otp,
                "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này để bảo mật tài khoản."
        );
    }

    // ========== THÊM VÀO EmailUtilBrevo.java ==========

    /**
     * Gửi email xác nhận đơn Vendor
     */
    public static boolean sendVendorApplicationConfirmation(String toEmail, String fullName, String businessName) {
        String subject = "Xác nhận đơn đăng ký Đối tác Kinh doanh - VietTech";
        String htmlContent = buildVendorApplicationTemplate(fullName, businessName);

        try {
            sendMail(toEmail, SENDER_EMAIL, subject, htmlContent, true);
            logger.info("✓ Vendor application confirmation sent to: {}", toEmail);
            return true;
        } catch (IOException e) {
            logger.error("✗ Failed to send vendor application confirmation to: {}", toEmail, e);
            return false;
        }
    }

    /**
     * Gửi email xác nhận đơn Shipper
     */
    public static boolean sendShipperApplicationConfirmation(String toEmail, String fullName, String vehicleType) {
        String subject = "Xác nhận đơn đăng ký Tài xế Giao hàng - VietTech";
        String htmlContent = buildShipperApplicationTemplate(fullName, vehicleType);

        try {
            sendMail(toEmail, SENDER_EMAIL, subject, htmlContent, true);
            logger.info("✓ Shipper application confirmation sent to: {}", toEmail);
            return true;
        } catch (IOException e) {
            logger.error("✗ Failed to send shipper application confirmation to: {}", toEmail, e);
            return false;
        }
    }

    /**
     * Template email Vendor
     */
    private static String buildVendorApplicationTemplate(String fullName, String businessName) {
        return String.format("""
        <!DOCTYPE html>
        <html lang="vi">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Xác nhận đơn đăng ký</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
            <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f4f4f4; padding: 40px 0;">
                <tr>
                    <td align="center">
                        <table width="600" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                            
                            <!-- Header -->
                            <tr>
                                <td align="center" style="padding: 30px 20px; background: linear-gradient(135deg, #0d6efd, #1e40af); border-radius: 10px 10px 0 0;">
                                    <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: bold;">
                                        🎉 Cảm ơn bạn đã đăng ký!
                                    </h1>
                                </td>
                            </tr>
                            
                            <!-- Body -->
                            <tr>
                                <td style="padding: 40px 30px;">
                                    <p style="margin: 0 0 20px; font-size: 16px; color: #333333; line-height: 1.6;">
                                        Xin chào <strong>%s</strong>,
                                    </p>
                                    
                                    <p style="margin: 0 0 20px; font-size: 16px; color: #333333; line-height: 1.6;">
                                        Chúng tôi đã nhận được đơn đăng ký <strong>Đối tác Kinh doanh</strong> của bạn 
                                        với doanh nghiệp <strong>"%s"</strong>.
                                    </p>
                                    
                                    <div style="background: #e7f3ff; border-left: 4px solid #0d6efd; padding: 20px; border-radius: 8px; margin: 30px 0;">
                                        <h3 style="margin: 0 0 15px; color: #0d6efd; font-size: 18px;">📋 Các bước tiếp theo:</h3>
                                        <ol style="margin: 0; padding-left: 20px; color: #333; line-height: 1.8;">
                                            <li>Bộ phận nhân sự sẽ xem xét hồ sơ trong <strong>3-5 ngày làm việc</strong></li>
                                            <li>Nếu phù hợp, chúng tôi sẽ liên hệ qua <strong>email</strong> hoặc <strong>điện thoại</strong></li>
                                            <li>Bạn sẽ được mời tham gia <strong>buổi phỏng vấn trực tuyến</strong> hoặc <strong>trực tiếp</strong></li>
                                            <li>Sau phỏng vấn, chúng tôi sẽ thông báo kết quả trong <strong>1-2 ngày</strong></li>
                                        </ol>
                                    </div>
                                    
                                    <p style="margin: 20px 0; font-size: 14px; color: #666666; line-height: 1.6;">
                                        Nếu có thắc mắc, vui lòng liên hệ: <br>
                                        📧 Email: <a href="mailto:careers@viettech.vn" style="color: #0d6efd; text-decoration: none;">careers@viettech.vn</a><br>
                                        📞 Hotline: <strong>0866 448 892</strong>
                                    </p>
                                    
                                    <p style="margin: 20px 0; font-size: 16px; color: #333333;">
                                        Chúc bạn may mắn! 🍀
                                    </p>
                                </td>
                            </tr>
                            
                            <!-- Footer -->
                            <tr>
                                <td align="center" style="padding: 20px; background-color: #f8f9fa; border-radius: 0 0 10px 10px;">
                                    <p style="margin: 0; font-size: 12px; color: #999999;">
                                        © 2025 <strong>VietTech</strong> - Sàn Thương Mại Điện Tử
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """, fullName, businessName);
    }

    /**
     * Template email Shipper
     */
    private static String buildShipperApplicationTemplate(String fullName, String vehicleType) {
        return String.format("""
        <!DOCTYPE html>
        <html lang="vi">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Xác nhận đơn đăng ký</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
            <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f4f4f4; padding: 40px 0;">
                <tr>
                    <td align="center">
                        <table width="600" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                            
                            <!-- Header -->
                            <tr>
                                <td align="center" style="padding: 30px 20px; background: linear-gradient(135deg, #0d6efd, #1e40af); border-radius: 10px 10px 0 0;">
                                    <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: bold;">
                                        🚚 Cảm ơn bạn đã đăng ký!
                                    </h1>
                                </td>
                            </tr>
                            
                            <!-- Body -->
                            <tr>
                                <td style="padding: 40px 30px;">
                                    <p style="margin: 0 0 20px; font-size: 16px; color: #333333; line-height: 1.6;">
                                        Xin chào <strong>%s</strong>,
                                    </p>
                                    
                                    <p style="margin: 0 0 20px; font-size: 16px; color: #333333; line-height: 1.6;">
                                        Chúng tôi đã nhận được đơn đăng ký <strong>Tài xế Giao hàng</strong> của bạn 
                                        với phương tiện <strong>"%s"</strong>.
                                    </p>
                                    
                                    <div style="background: #e7f3ff; border-left: 4px solid #0d6efd; padding: 20px; border-radius: 8px; margin: 30px 0;">
                                        <h3 style="margin: 0 0 15px; color: #0d6efd; font-size: 18px;">📋 Các bước tiếp theo:</h3>
                                        <ol style="margin: 0; padding-left: 20px; color: #333; line-height: 1.8;">
                                            <li>Bộ phận nhân sự sẽ xem xét hồ sơ trong <strong>3-5 ngày làm việc</strong></li>
                                            <li>Nếu phù hợp, chúng tôi sẽ liên hệ qua <strong>email</strong> hoặc <strong>điện thoại</strong></li>
                                            <li>Bạn sẽ được mời tham gia <strong>buổi định hướng</strong> về quy trình giao hàng</li>
                                            <li>Sau định hướng, bạn có thể <strong>bắt đầu nhận đơn</strong> ngay lập tức</li>
                                        </ol>
                                    </div>
                                    
                                    <p style="margin: 20px 0; font-size: 14px; color: #666666; line-height: 1.6;">
                                        Nếu có thắc mắc, vui lòng liên hệ: <br>
                                        📧 Email: <a href="mailto:careers@viettech.vn" style="color: #0d6efd; text-decoration: none;">careers@viettech.vn</a><br>
                                        📞 Hotline: <strong>0866 448 892</strong>
                                    </p>
                                    
                                    <p style="margin: 20px 0; font-size: 16px; color: #333333;">
                                        Chúc bạn may mắn! 🍀
                                    </p>
                                </td>
                            </tr>
                            
                            <!-- Footer -->
                            <tr>
                                <td align="center" style="padding: 20px; background-color: #f8f9fa; border-radius: 0 0 10px 10px;">
                                    <p style="margin: 0; font-size: 12px; color: #999999;">
                                        © 2025 <strong>VietTech</strong> - Sàn Thương Mại Điện Tử
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """, fullName, vehicleType);
    }

    private EmailUtilBrevo() {
        throw new AssertionError("Cannot instantiate utility class");
    }
}