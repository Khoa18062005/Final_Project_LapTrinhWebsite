package viettech.controller;

import viettech.entity.user.User;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet xử lý các trang menu profile
 * URL pattern: /profile/xxx
 */
@WebServlet({
    "/profile/info",
    "/profile/bank",
    "/profile/password",
    "/profile/orders",
    "/profile/notifications",
})
public class ProfileMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get current page from URL
        String path = request.getServletPath();
        String page = path.substring(path.lastIndexOf('/') + 1); // Extract "info", "bank", etc.

        // Set attributes
        request.setAttribute("user", user);
        request.setAttribute("activePage", page);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/profile/" + page + ".jsp")
                .forward(request, response);
    }
}