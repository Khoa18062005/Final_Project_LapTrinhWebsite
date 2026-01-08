package viettech.controller;

import com.google.gson.Gson;
import viettech.entity.user.User;
import viettech.service.OrderService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/profile/orders/*")
public class OrderServlet extends HttpServlet {

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

        String pathInfo = request.getPathInfo();

        // ✅ API ENDPOINT: /profile/orders/api/{orderId}
        if (pathInfo != null && pathInfo.startsWith("/api/")) {
            handleOrderDetailAPI(request, response, user);
            return;
        }

        // Xử lý trang danh sách đơn hàng
        handleOrdersPage(request, response, user);
    }

    /**
     * ✅ API: LẤY CHI TIẾT ĐƠN HÀNG (JSON)
     */
    private void handleOrderDetailAPI(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Lấy orderId từ path: /profile/orders/api/123
            String pathInfo = request.getPathInfo(); // "/api/123"
            String orderIdStr = pathInfo.substring(5); // "123"
            int orderId = Integer.parseInt(orderIdStr);

            OrderService orderService = new OrderService();
            Map<String, Object> orderDetail = orderService.getOrderDetail(orderId);

            if (orderDetail == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Đơn hàng không tồn tại\"}");
                return;
            }

            // Kiểm tra xem đơn hàng có thuộc về user này không
            // (Nếu cần security check)

            // Trả về JSON
            Gson gson = new Gson();
            String json = gson.toJson(orderDetail);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid order ID\"}");
        } catch (Exception e) {
            System.err.println("❌ ERROR in handleOrderDetailAPI:");
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra!\"}");
        }
    }

    /**
     * ✅ XỬ LÝ TRANG ORDERS
     */
    private void handleOrdersPage(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        try {
            OrderService orderService = new OrderService();

            // Lấy status từ URL (?status=pending)
            String status = request.getParameter("status");

            // Load orders
            List<Map<String, Object>> orders;
            if (status == null || "all".equals(status)) {
                orders = orderService.getOrdersByCustomerId(user.getUserId());
            } else {
                orders = orderService.getOrdersByStatus(user.getUserId(), status);
            }

            // Load order counts
            Map<String, Long> orderCounts = orderService.getOrderCountsByStatus(user.getUserId());

            // Set attributes
            request.setAttribute("user", user);
            request.setAttribute("activePage", "orders");
            request.setAttribute("orders", orders);
            request.setAttribute("orderCounts", orderCounts);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/profile/orders.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ ERROR in handleOrdersPage:");
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi tải đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}