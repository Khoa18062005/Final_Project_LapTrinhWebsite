package viettech.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dto.Login_dto;
import viettech.entity.user.User;
import viettech.service.LoginService;
import viettech.service.LoginService.AuthResult;
import viettech.util.CookieUtil;
import viettech.util.SessionUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private final LoginService loginService = new LoginService();
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 ngày

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("auth") != null) {
            logger.debug("User already logged in, redirecting by role");
            redirectByRole(session, request, response);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Nhận dữ liệu từ form → chuyển vào DTO
        Login_dto dto = new Login_dto();
        dto.setEmail(request.getParameter("email"));
        dto.setPassword(request.getParameter("password"));

        logger.debug("Login attempt for email: {}", dto.getEmail());

        // Gọi service để xác thực
        AuthResult authResult = loginService.authenticate(dto);

        if (authResult != null) {
            // ✅ Đăng nhập thành công
            handleSuccessfulLogin(request, response, authResult);
        } else {
            // ❌ Đăng nhập thất bại
            handleLoginFailure(request, response, dto);
        }
    }

    private void handleSuccessfulLogin(HttpServletRequest request,
                                       HttpServletResponse response,
                                       AuthResult authResult) throws IOException {
        Object userObject = authResult.getUser();
        String role = authResult.getRole();

        // ✅ Cast về User (vì tất cả đều extends User)
        User user = (User) userObject;

        // ✅ Lưu user vào session
        SessionUtil.setAttribute(request, "auth", user);
        SessionUtil.setAttribute(request, "role", role);

        // ✅ Đặt flag: user đăng nhập (không phải mới đăng ký)
        SessionUtil.setAttribute(request, "isNewUser", false);

        // ✅ Set success message - GIỜ ĐÃ LẤY ĐƯỢC firstName!
        SessionUtil.setSuccessMessage(request,
                "Chào mừng quay trở lại, " + user.getFirstName() + "! 👋");

        // ✅ Lưu cookie
        CookieUtil.addCookie(response, "userEmail", user.getEmail(), COOKIE_MAX_AGE);
        String fullName = (user.getFirstName() + " " + user.getLastName()).trim();
        CookieUtil.addCookie(response, "userName", fullName, COOKIE_MAX_AGE);

        logger.info("✓ Login successful for user: {} (role: {})", user.getEmail(), role);

        // ✅ Redirect theo role
        redirectByRole(request.getSession(), request, response);
    }

    /**
     * Xử lý khi đăng nhập thất bại
     */
    private void handleLoginFailure(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Login_dto dto) throws ServletException, IOException {
        logger.warn("✗ Login failed for email: {}", dto.getEmail());

        request.setAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
        request.setAttribute("email", dto.getEmail());

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    /**
     * Redirect theo role của user
     */
    private void redirectByRole(HttpSession session,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        String role = (String) session.getAttribute("role");
        String contextPath = request.getContextPath();

        logger.debug("Redirecting user with role: {}", role);

        if ("admin".equals(role)) {
            response.sendRedirect(contextPath + "/admin");
        } else if ("vendor".equals(role)) {
            response.sendRedirect(contextPath + "/vendor");
        } else if ("shipper".equals(role)) {
            response.sendRedirect(contextPath + "/shipper");
        } else {
            response.sendRedirect(contextPath + "/"); // customer
        }
    }
}