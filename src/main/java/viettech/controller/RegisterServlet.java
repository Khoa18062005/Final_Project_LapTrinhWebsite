package viettech.controller;

import viettech.dto.Register_dto;
import viettech.entity.Notification;
import viettech.entity.user.Customer;
import viettech.service.NotificationService;
import viettech.service.UserService;
import viettech.util.EmailUtilBrevo;
import viettech.util.SessionUtil;
import viettech.util.CookieUtil;
import viettech.util.NotificationTemplateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private final UserService userService = new UserService();
    private final NotificationService notificationService = new NotificationService();
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 ngày

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Nhận dữ liệu từ form
        Register_dto regist_dto = new Register_dto();
        regist_dto.setFirstName(req.getParameter("firstName"));
        regist_dto.setLastName(req.getParameter("lastName"));
        regist_dto.setEmail(req.getParameter("email"));
        regist_dto.setPassword(req.getParameter("password"));
        regist_dto.setPhone(req.getParameter("phone"));
        regist_dto.setDateOfBirth(req.getParameter("dateOfBirth"));
        regist_dto.setGender(req.getParameter("gender"));

        // ========== LẤY MÃ GIỚI THIỆU (NẾU CÓ) ==========
        String referralCode = req.getParameter("referralCode");
        Customer referrer = null;

        // Kiểm tra mã giới thiệu nếu user nhập
        if (referralCode != null && !referralCode.trim().isEmpty()) {
            referrer = userService.findCustomerByReferralCode(referralCode.trim());

            if (referrer == null) {
                // Mã giới thiệu không hợp lệ
                logger.warn("✗ Invalid referral code: {}", referralCode);
                req.setAttribute("dto", regist_dto);
                req.setAttribute("errorMessage", "Mã giới thiệu không hợp lệ. Vui lòng kiểm tra lại!");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            } else {
                logger.info("✓ Valid referral code from user: {}", referrer.getUsername());
            }
        }

        String inputOTP = req.getParameter("otp");

        // ✅ XÁC THỰC OTP
        String savedOTP = (String) SessionUtil.getAttribute(req, "otp");
        String otpEmail = (String) SessionUtil.getAttribute(req, "otpEmail");
        Long otpTime = (Long) SessionUtil.getAttribute(req, "otpTime");

        if (!EmailUtilBrevo.verifyOTP(inputOTP, savedOTP, otpTime != null ? otpTime : 0)) {
            req.setAttribute("dto", regist_dto);
            req.setAttribute("errorMessage", "Mã OTP không đúng hoặc đã hết hạn!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // Kiểm tra email khớp
        if (!regist_dto.getEmail().equals(otpEmail)) {
            req.setAttribute("dto", regist_dto);
            req.setAttribute("errorMessage", "Email không khớp với email đã gửi OTP!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // Xóa OTP khỏi session
        SessionUtil.removeAttribute(req, "otp");
        SessionUtil.removeAttribute(req, "otpEmail");
        SessionUtil.removeAttribute(req, "otpTime");

        // Xử lý đăng ký
        int checkUser = userService.register(regist_dto);

        if (checkUser == 1) {
            handleSuccessfulRegistration(req, resp, regist_dto, referrer);
        } else if (checkUser == 2) {
            handleEmailExists(req, resp, regist_dto);
        } else {
            handleRegistrationFailure(req, resp, regist_dto);
        }
    }

    /**
     * ========== XỬ LÝ KHI ĐĂNG KÝ THÀNH CÔNG ==========
     */
    private void handleSuccessfulRegistration(HttpServletRequest req,
                                              HttpServletResponse resp,
                                              Register_dto dto,
                                              Customer referrer) throws IOException {
        Customer newCustomer = userService.findCustomerByEmail(dto.getEmail());
        userService.addCart(newCustomer);

        if (newCustomer == null) {
            logger.error("✗ Failed to retrieve newly registered customer: {}", dto.getEmail());
            throw new RuntimeException("Failed to retrieve customer after registration");
        }

        logger.info("✓ User registered successfully: {} (ID: {})", dto.getEmail(), newCustomer.getUserId());

        // ========== TẠO 2 THÔNG BÁO ĐĂNG KÝ CƠ BẢN ==========
        createRegistrationNotifications(newCustomer);

        // ========== XỬ LÝ MÃ GIỚI THIỆU (NẾU CÓ) ==========
        if (referrer != null) {
            handleReferralRewards(newCustomer, referrer);
        }

        // ✅ Lưu user vào session
        SessionUtil.setAttribute(req, "user", newCustomer);
        SessionUtil.setAttribute(req, "isNewUser", true);

        SessionUtil.setSuccessMessage(req, "Chào mừng " + newCustomer.getFirstName() + " " + newCustomer.getLastName() +
                " đến với VietTech! 🎉");

        // Lưu cookie
        CookieUtil.addCookie(resp, "userEmail", dto.getEmail(), COOKIE_MAX_AGE);
        String fullName = (dto.getFirstName() + " " + dto.getLastName()).trim();
        CookieUtil.addCookie(resp, "userName", fullName, COOKIE_MAX_AGE);

        logger.info("✓ Registration completed for user: {}", dto.getEmail());

        // ✅ Redirect về trang chủ
        resp.sendRedirect(req.getContextPath() + "/");
    }

    /**
     * ========== TẠO 2 THÔNG BÁO ĐĂNG KÝ CƠ BẢN ==========
     */
    private void createRegistrationNotifications(Customer customer) {
        int userId = customer.getUserId();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();

        try {
            // THÔNG BÁO 1: ĐĂNG KÝ THÀNH CÔNG
            logger.debug("Creating REGISTER notification for user: {}", userId);

            Notification registerNotification = NotificationTemplateUtil.createRegisterNotification(
                    userId, firstName, lastName
            );

            boolean registerSuccess = notificationService.createNotification(registerNotification);

            if (registerSuccess) {
                logger.info("✓ REGISTER notification created for user: {}", userId);
            } else {
                logger.warn("✗ Failed to create REGISTER notification for user: {}", userId);
            }

            // THÔNG BÁO 2: CHÀO MỪNG
            logger.debug("Creating WELCOME notification for user: {}", userId);

            Notification welcomeNotification = NotificationTemplateUtil.createWelcomeNotification(
                    userId, firstName, lastName
            );

            boolean welcomeSuccess = notificationService.createNotification(welcomeNotification);

            if (welcomeSuccess) {
                logger.info("✓ WELCOME notification created for user: {}", userId);
            } else {
                logger.warn("✗ Failed to create WELCOME notification for user: {}", userId);
            }

            if (registerSuccess && welcomeSuccess) {
                logger.info("✓ Both registration notifications created successfully for user: {}", userId);
            } else {
                logger.warn("⚠ Some registration notifications failed for user: {}", userId);
            }

        } catch (Exception e) {
            logger.error("✗ Failed to create registration notifications for user: {}", userId, e);
        }
    }

    /**
     * ========== XỬ LÝ THƯỞNG MÃ GIỚI THIỆU ==========
     * ← METHOD MỚI
     * Cộng điểm và tạo thông báo cho cả 2 bên
     */
    private void handleReferralRewards(Customer newCustomer, Customer referrer) {
        try {
            logger.info("Processing referral rewards for new user: {} (referred by: {})",
                    newCustomer.getEmail(), referrer.getUsername());

            // ========== CỘNG ĐIỂM CHO NGƯỜI GIỚI THIỆU (200 POINTS) ==========
            boolean referrerBonusSuccess = userService.addReferrerBonus(referrer);

            if (referrerBonusSuccess) {
                // Tạo thông báo cho người giới thiệu
                Notification referrerNotification = NotificationTemplateUtil.createReferralRewardNotification(
                        referrer.getUserId(),
                        referrer.getFirstName(),
                        referrer.getLastName(),
                        newCustomer.getFirstName() + " " + newCustomer.getLastName(),
                        UserService.getReferrerBonus()
                );

                boolean notifSuccess = notificationService.createNotification(referrerNotification);
                if (notifSuccess) {
                    logger.info("✓ Referrer reward notification created for user: {}", referrer.getUserId());
                }
            }

            // ========== CỘNG ĐIỂM CHO NGƯỜI ĐƯỢC GIỚI THIỆU (50 POINTS) ==========
            boolean referredBonusSuccess = userService.addReferredBonus(newCustomer);

            if (referredBonusSuccess) {
                // ===== LẤY MÃ GIỚI THIỆU (8 KÝ TỰ) =====
                String referrerCode = referrer.getReferralCode(); // "ABCD1234"

                // Tạo thông báo cho người được giới thiệu
                Notification referredNotification = NotificationTemplateUtil.createReferralWelcomeNotification(
                        newCustomer.getUserId(),
                        newCustomer.getFirstName(),
                        newCustomer.getLastName(),
                        referrerCode,  // ← Dùng mã 8 ký tự thay vì username
                        UserService.getReferredBonus()
                );

                boolean notifSuccess = notificationService.createNotification(referredNotification);
                if (notifSuccess) {
                    logger.info("✓ Referred welcome notification created for user: {}", newCustomer.getUserId());
                }
            }

            if (referrerBonusSuccess && referredBonusSuccess) {
                logger.info("✓ Referral rewards processed successfully");
            } else {
                logger.warn("⚠ Some referral rewards failed to process");
            }

        } catch (Exception e) {
            logger.error("✗ Failed to process referral rewards", e);
        }
    }

    /**
     * ========== XỬ LÝ KHI EMAIL ĐÃ TỒN TẠI ==========
     */
    private void handleEmailExists(HttpServletRequest req,
                                   HttpServletResponse resp,
                                   Register_dto dto) throws ServletException, IOException {
        logger.warn("✗ Registration failed - email already exists: {}", dto.getEmail());

        req.setAttribute("errorMessage", "Email này đã được sử dụng. Vui lòng đăng nhập.");
        req.setAttribute("email", dto.getEmail());

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    /**
     * ========== XỬ LÝ KHI ĐĂNG KÝ THẤT BẠI ==========
     */
    private void handleRegistrationFailure(HttpServletRequest req,
                                           HttpServletResponse resp,
                                           Register_dto dto) throws ServletException, IOException {
        logger.warn("✗ Registration failed for email: {}", dto.getEmail());

        req.setAttribute("dto", dto);
        req.setAttribute("errorMessage", "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.");
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}