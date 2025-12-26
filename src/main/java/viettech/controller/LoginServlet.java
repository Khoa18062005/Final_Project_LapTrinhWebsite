package viettech.controller;

import viettech.dto.Login_dto;
import viettech.service.LoginService;
import viettech.service.LoginService.AuthResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            redirectByRole(session, request, response);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Nhận dữ liệu từ form → chuyển vào DTO
        Login_dto dto = new Login_dto();
        dto.setEmail(request.getParameter("email"));
        dto.setPassword(request.getParameter("password"));

        // Gọi service để xác thực
        AuthResult authResult = loginService.authenticate(dto.getEmail(), dto.getPassword());

        if (authResult != null) {
            // Đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("user", authResult.getUser());
            session.setAttribute("role", authResult.getRole());

            redirectByRole(session, request, response);
        } else {
            // Đăng nhập thất bại
            request.setAttribute("error", "Email hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
        }
    }

    private void redirectByRole(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String role = (String) session.getAttribute("role");
        String contextPath = request.getContextPath();

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