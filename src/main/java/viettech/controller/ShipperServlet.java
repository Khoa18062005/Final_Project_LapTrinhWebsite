package viettech.controller;

import viettech.dto.Shipper_dto;
import viettech.entity.user.User;
import viettech.service.ShipperService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/shipper")
public class ShipperServlet extends HttpServlet {

    private final ShipperService service = new ShipperService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ... (Giữ nguyên code doGet cũ) ...
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        int shipperId = user.getUserId();
        Shipper_dto data = service.getDashboardData(shipperId);
        if (data == null) data = new Shipper_dto();

        request.setAttribute("data", data);
        request.getRequestDispatcher("/WEB-INF/views/shipper.jsp").forward(request, response);
    }

    // --- THÊM HÀM doPost MỚI ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 1. Kiểm tra đăng nhập (Bảo mật)
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. Lấy tham số từ Form
        String action = request.getParameter("action"); // "accept" hoặc "complete"
        String idStr = request.getParameter("id");      // assignmentId

        if (action != null && idStr != null) {
            try {
                int assignmentId = Integer.parseInt(idStr);
                // 3. Gọi Service xử lý
                service.updateStatus(assignmentId, action);
                System.out.println("Shipper " + user.getUserId() + " performed: " + action + " on assignment " + assignmentId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 4. Load lại trang để cập nhật giao diện
        response.sendRedirect(request.getContextPath() + "/shipper");
    }
}