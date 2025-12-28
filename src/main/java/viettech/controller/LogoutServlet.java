package viettech.controller;

import viettech.dao.CustomerDAO;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.service.UserService;
import viettech.util.CookieUtil;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // 1. Lấy user từ session
            User user = (User) SessionUtil.getAttribute(req, "user");

            if (user != null && user instanceof Customer) {
                // 2. Cập nhật last_login_at = thời điểm logout
                user.setLastLoginAt(new Date());
                userService.UpdateLastLoginAt((Customer) user);
                System.out.println("✅ Updated last_login_at for user: " + user.getEmail());
            }
        } catch (Exception e) {
            // Log lỗi nhưng vẫn cho logout
            System.err.println("⚠️ Error updating last_login_at: " + e.getMessage());
            e.printStackTrace();
        }

        // 4. Xóa session
        SessionUtil.invalidate(req);

        // 5. Xóa cookies
        CookieUtil.deleteCookies(req, resp, "userEmail", "userName");

        // 6. Tạo session mới và set thông báo
        SessionUtil.setInfoMessage(req, "Bạn đã đăng xuất thành công!");

        // 7. Redirect về trang chủ
        resp.sendRedirect(req.getContextPath() + "/");
    }
}