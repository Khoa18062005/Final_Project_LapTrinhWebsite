package viettech.controller;

import viettech.dto.Vendor_dto;
import viettech.dto.Vendor_dto;
import viettech.entity.user.User;
import viettech.service.VendorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/vendor")
public class VendorServlet extends HttpServlet {

    private final VendorService vendorService = new VendorService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra Session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 2. Bảo mật: Chỉ Vendor (Role = 2) được vào
//        if (user == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//        if (user.getRoleID() != 2) {
//            response.sendRedirect(request.getContextPath() + "/");
//            return;
//        }

        // 3. Lấy dữ liệu từ Service
        int vendorId = user.getUserId();
        Vendor_dto data = vendorService.getDashboardData(vendorId);

        if (data == null) {
            data = new Vendor_dto(); // Tránh null pointer bên JSP
        }

        // 4. Đẩy dữ liệu sang View
        request.setAttribute("data", data);
        request.getRequestDispatcher("/WEB-INF/views/vendor.jsp").forward(request, response);
    }
}