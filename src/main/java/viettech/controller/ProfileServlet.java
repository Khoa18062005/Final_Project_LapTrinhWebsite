package viettech.controller;

import viettech.entity.user.Customer;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Kiểm tra đã đăng nhập chưa
        if (!SessionUtil.isAuthenticated(req)) {
            SessionUtil.setErrorMessage(req, "Vui lòng đăng nhập để xem thông tin cá nhân!");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy thông tin user
        Customer customer = SessionUtil.getCurrentUser(req, Customer.class);
        req.setAttribute("customer", customer);

        // Forward tới trang profile
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO: Xử lý cập nhật thông tin profile
        doGet(req, resp);
    }
}