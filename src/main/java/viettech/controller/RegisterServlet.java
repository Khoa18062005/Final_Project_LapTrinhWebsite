package viettech.controller;

import viettech.dto.Register_dto;
import viettech.entity.user.Customer;
import viettech.service.UserService;
import viettech.util.EmailUtilBrevo;
import viettech.util.SessionUtil;
import viettech.util.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();
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

            // ✅ QUAN TRỌNG: Giữ lại thời gian OTP trong session
            // KHÔNG xóa otpTime để frontend tính thời gian còn lại

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
     * Xử lý khi đăng ký thành công
     */
    private void handleSuccessfulRegistration(HttpServletRequest req,
                                              HttpServletResponse resp,
                                              Register_dto dto) throws IOException {
        Customer newCustomer = userService.findCustomerByEmail(dto.getEmail());

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

        // ✅ Redirect về profile
        resp.sendRedirect(req.getContextPath() + "/");
    }

    /**
     * Xử lý khi email đã tồn tại
     */
    private void handleEmailExists(HttpServletRequest req,
                                   HttpServletResponse resp,
                                   Register_dto dto) throws ServletException, IOException {
        req.setAttribute("errorMessage", "Email này đã được sử dụng. Vui lòng đăng nhập.");
        req.setAttribute("email", dto.getEmail());

        // Forward về trang đăng nhập
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    /**
     * Xử lý khi đăng ký thất bại
     */
    private void handleRegistrationFailure(HttpServletRequest req,
                                           HttpServletResponse resp,
                                           Register_dto dto) throws ServletException, IOException {
        req.setAttribute("dto", dto);
        req.setAttribute("errorMessage", "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.");

        // Forward lại trang đăng ký
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}