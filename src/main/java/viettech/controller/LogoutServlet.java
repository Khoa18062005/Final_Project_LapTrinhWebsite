package viettech.controller;

import viettech.util.CookieUtil;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ Xóa session bằng SessionUtil
        SessionUtil.invalidate(req);

        // ✅ Xóa cookies bằng CookieUtil
        CookieUtil.deleteCookies(req, resp, "userEmail", "userName");

        // ✅ Tạo session mới và set thông báo
        SessionUtil.setInfoMessage(req, "Bạn đã đăng xuất thành công!");

        // Redirect về trang chủ
        resp.sendRedirect(req.getContextPath() + "/");
    }
}