package viettech.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import viettech.entity.user.User;
import viettech.service.ShipperNotificationApiService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Servlet để lấy thông báo cho Shipper
 * Dựa trên trạng thái đơn hàng từ delivery_assignments
 */
@WebServlet("/api/shipper/notifications")
public class ShipperNotificationApiServlet extends HttpServlet {

    private final ShipperNotificationApiService notificationService = new ShipperNotificationApiService();
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // Kiểm tra đăng nhập
            User user = (User) SessionUtil.getAttribute(request, "user");
            if (user == null || user.getRoleID() != 3) {
                result.put("success", false);
                result.put("message", "Unauthorized");
                result.put("notifications", new java.util.ArrayList<>());
                result.put("unreadCount", 0);
            } else {
                int shipperId = user.getUserId();

                // Lấy danh sách thông báo
                List<Map<String, Object>> notifications = notificationService.getShipperNotifications(shipperId);

                // Đếm số thông báo chưa đọc (đơn mới = Ready)
                int unreadCount = notificationService.countNewOrders();

                result.put("success", true);
                result.put("notifications", notifications);
                result.put("unreadCount", unreadCount);
                result.put("message", "Success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            result.put("notifications", new java.util.ArrayList<>());
            result.put("unreadCount", 0);
        }

        String jsonResponse = gson.toJson(result);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}

