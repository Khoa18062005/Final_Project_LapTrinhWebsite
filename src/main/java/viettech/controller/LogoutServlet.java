package viettech.controller;
import viettech.dao.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate(); // Xóa session
        }
        
        // Xóa cookies
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userEmail".equals(cookie.getName()) || 
                    "userName".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                }
            }
        }
        
        // Thông báo và redirect
        HttpSession newSession = req.getSession();
        newSession.setAttribute("infoMessage", "Bạn đã đăng xuất thành công!");
        resp.sendRedirect(req.getContextPath() + "/");
    }
}