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

        // ✅ Kiểm tra có phải user mới đăng ký không
        Boolean isNewUser = SessionUtil.getAttribute(req, "isNewUser", Boolean.class);
        if (isNewUser != null && isNewUser) {
            // User mới đăng ký → hiển thị welcome
            req.setAttribute("showWelcome", true);

            // Xóa flag để lần sau không hiển thị nữa
            SessionUtil.removeAttribute(req, "isNewUser");
        } else {
            // User cũ hoặc truy cập bình thường
            req.setAttribute("showWelcome", false);
        }

        // Forward tới trang index (có điều kiện hiển thị welcome)
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO: Xử lý cập nhật thông tin profile
        doGet(req, resp);
    }
}