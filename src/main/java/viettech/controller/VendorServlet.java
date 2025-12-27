package viettech.controller;

import viettech.entity.user.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/vendor")
public class VendorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy Session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 2. Kiểm tra bảo mật (Chặn truy cập trái phép)
        if (user == null) {
            // Chưa đăng nhập -> Đá về trang login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (user.getRoleID() != 2) {
            // Đã đăng nhập nhưng không phải Vendor (ví dụ là Customer hoặc Shipper)
            // Đá về trang chủ
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 3. (Tùy chọn) Tại đây bạn có thể gọi VendorService để lấy số liệu doanh thu
        // ví dụ: request.setAttribute("data", vendorService.getDashboardData(user.getUserId()));

        // 4. Chuyển hướng vào trang giao diện
        request.getRequestDispatcher("/WEB-INF/views/vendor.jsp")
                .forward(request, response);
    }
}