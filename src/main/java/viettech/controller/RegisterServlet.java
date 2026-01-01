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
            handleSuccessfulRegistration(req, resp, regist_dto);
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
                                              Register_dto dto) throws IOException {
        Customer newCustomer = userService.findCustomerByEmail(dto.getEmail());

        if (newCustomer == null) {
            logger.error("✗ Failed to retrieve newly registered customer: {}", dto.getEmail());
            throw new RuntimeException("Failed to retrieve customer after registration");
        }

        logger.info("✓ User registered successfully: {} (ID: {})", dto.getEmail(), newCustomer.getUserId());

        // ========== TẠO 2 THÔNG BÁO ==========
        createRegistrationNotifications(newCustomer);

        // ✅ Lưu user vào session
        SessionUtil.setAttribute(req, "user", newCustomer);

        // ✅ Đặt flag: user mới đăng ký (để hiển thị welcome message)
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
     * ========== TẠO 2 THÔNG BÁO ĐĂNG KÝ ==========
     * 1. Thông báo đăng ký thành công (ảnh register)
     * 2. Thông báo chào mừng (ảnh login)
     */
    private void createRegistrationNotifications(Customer customer) {
        int userId = customer.getUserId();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();

        try {
            // ========== THÔNG BÁO 1: ĐĂNG KÝ THÀNH CÔNG ==========
            logger.debug("Creating REGISTER notification for user: {}", userId);

            Notification registerNotification = NotificationTemplateUtil.createRegisterNotification(
                    userId,
                    firstName,
                    lastName
            );

            boolean registerSuccess = notificationService.createNotification(registerNotification);

            if (registerSuccess) {
                logger.info("✓ REGISTER notification created for user: {}", userId);
            } else {
                logger.warn("✗ Failed to create REGISTER notification for user: {}", userId);
            }

            // ========== THÔNG BÁO 2: CHÀO MỪNG ==========
            logger.debug("Creating WELCOME notification for user: {}", userId);

            Notification welcomeNotification = NotificationTemplateUtil.createWelcomeNotification(
                    userId,
                    firstName,
                    lastName
            );

            boolean welcomeSuccess = notificationService.createNotification(welcomeNotification);

            if (welcomeSuccess) {
                logger.info("✓ WELCOME notification created for user: {}", userId);
            } else {
                logger.warn("✗ Failed to create WELCOME notification for user: {}", userId);
            }

            // ========== TỔNG KẾT ==========
            if (registerSuccess && welcomeSuccess) {
                logger.info("✓ Both registration notifications created successfully for user: {}", userId);
            } else {
                logger.warn("⚠ Some registration notifications failed for user: {}", userId);
            }

        } catch (Exception e) {
            // Không cho lỗi notification ảnh hưởng đến đăng ký
            logger.error("✗ Failed to create registration notifications for user: {}", userId, e);
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

        // Forward về trang đăng nhập
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

        // Forward lại trang đăng ký
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}