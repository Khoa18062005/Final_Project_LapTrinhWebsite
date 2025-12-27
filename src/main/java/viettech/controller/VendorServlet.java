package viettech.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dto.Vendor_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.order.Order;
import viettech.entity.product.ProductApproval;
import viettech.entity.user.User;
import viettech.service.VendorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/vendor")
public class VendorServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(VendorServlet.class);
    private final VendorService vendorService = new VendorService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra Session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 2. Bảo mật: Chỉ Vendor (Role = 2) được vào
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (user.getRoleID() != 2) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 3. Lấy action từ request
        String action = request.getParameter("action");
        int vendorId = user.getUserId();

        try {
            // Get dashboard data for all pages (contains vendor info, stats, etc.)
            Vendor_dto data = vendorService.getDashboardData(vendorId);
            if (data == null) {
                data = new Vendor_dto();
            }
            request.setAttribute("data", data);

            if (action == null || action.equals("dashboard")) {
                // Dashboard - data already set above
            } else if (action.equals("approvals")) {
                // Lấy danh sách yêu cầu phê duyệt
                List<ProductApproval> approvals = vendorService.getApprovalRequests(vendorId);
                request.setAttribute("approvals", approvals);
            } else if (action.equals("orders")) {
                // Lấy đơn hàng theo status
                String status = request.getParameter("status");
                List<Order> orders;
                if (status != null && !status.isEmpty()) {
                    orders = vendorService.getOrdersByStatus(vendorId, status);
                } else {
                    orders = data.getRecentOrders();
                }
                request.setAttribute("orders", orders);
                request.setAttribute("status", status);
            } else if (action.equals("shipping")) {
                // Lấy đơn hàng cần giao
                List<Order> orders = vendorService.getOrdersReadyForShipping(vendorId);
                request.setAttribute("orders", orders);
            }

            // Forward to vendor.jsp - let JSP handle routing based on action parameter
            request.getRequestDispatcher("/WEB-INF/views/vendor.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Error in VendorServlet GET", e);
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/vendor.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra Session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || user.getRoleID() != 2) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            sendJsonResponse(response, false, "Unauthorized access", null);
            return;
        }

        int vendorId = user.getUserId();
        String action = request.getParameter("action");

        try {
            if (action == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                sendJsonResponse(response, false, "Action parameter is required", null);
                return;
            }

            switch (action) {
                case "addProduct":
                    handleAddProduct(request, response, vendorId);
                    break;
                case "updateProduct":
                    handleUpdateProduct(request, response, vendorId);
                    break;
                case "deleteProduct":
                    handleDeleteProduct(request, response, vendorId);
                    break;
                case "updateOrderStatus":
                    handleUpdateOrderStatus(request, response, vendorId);
                    break;
                case "cancelOrder":
                    handleCancelOrder(request, response, vendorId);
                    break;
                case "assignShipper":
                    handleAssignShipper(request, response, vendorId);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    sendJsonResponse(response, false, "Unknown action: " + action, null);
            }
        } catch (Exception e) {
            logger.error("Error in VendorServlet POST", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, false, "Có lỗi xảy ra: " + e.getMessage(), null);
        }
    }

    // ==================== HELPER METHODS ====================

    private void showDashboard(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws ServletException, IOException {
        Vendor_dto data = vendorService.getDashboardData(vendorId);
        if (data == null) {
            data = new Vendor_dto();
        }
        request.setAttribute("data", data);
        request.getRequestDispatcher("/WEB-INF/views/vendor.jsp").forward(request, response);
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            // Đọc JSON data từ request body
            Map<String, Object> productData = readJsonFromRequest(request);

            if (productData == null || productData.isEmpty()) {
                sendJsonResponse(response, false, "Product data is required", null);
                return;
            }

            ProductApproval approval = vendorService.requestAddProduct(productData, vendorId);
            sendJsonResponse(response, true, "Yêu cầu thêm sản phẩm đã được gửi và đang chờ phê duyệt", approval);
        } catch (Exception e) {
            logger.error("Error adding product", e);
            sendJsonResponse(response, false, "Không thể tạo yêu cầu thêm sản phẩm: " + e.getMessage(), null);
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            String productIdStr = request.getParameter("productId");
            if (productIdStr == null) {
                sendJsonResponse(response, false, "Product ID is required", null);
                return;
            }

            int productId = Integer.parseInt(productIdStr);
            Map<String, Object> productData = readJsonFromRequest(request);

            if (productData == null || productData.isEmpty()) {
                sendJsonResponse(response, false, "Product data is required", null);
                return;
            }

            ProductApproval approval = vendorService.requestUpdateProduct(productId, productData, vendorId);
            sendJsonResponse(response, true, "Yêu cầu cập nhật sản phẩm đã được gửi và đang chờ phê duyệt", approval);
        } catch (Exception e) {
            logger.error("Error updating product", e);
            sendJsonResponse(response, false, "Không thể tạo yêu cầu cập nhật sản phẩm: " + e.getMessage(), null);
        }
    }

    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            String productIdStr = request.getParameter("productId");
            if (productIdStr == null) {
                sendJsonResponse(response, false, "Product ID is required", null);
                return;
            }

            int productId = Integer.parseInt(productIdStr);
            ProductApproval approval = vendorService.requestDeleteProduct(productId, vendorId);
            sendJsonResponse(response, true, "Yêu cầu xóa sản phẩm đã được gửi và đang chờ phê duyệt", approval);
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            sendJsonResponse(response, false, "Không thể tạo yêu cầu xóa sản phẩm: " + e.getMessage(), null);
        }
    }

    private void handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            String orderIdStr = request.getParameter("orderId");
            String newStatus = request.getParameter("status");

            if (orderIdStr == null || newStatus == null) {
                sendJsonResponse(response, false, "Order ID and status are required", null);
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            Order order = vendorService.updateOrderStatus(orderId, newStatus, vendorId);
            sendJsonResponse(response, true, "Cập nhật trạng thái đơn hàng thành công", order);
        } catch (Exception e) {
            logger.error("Error updating order status", e);
            sendJsonResponse(response, false, "Không thể cập nhật trạng thái đơn hàng: " + e.getMessage(), null);
        }
    }

    private void handleCancelOrder(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            String orderIdStr = request.getParameter("orderId");
            String reason = request.getParameter("reason");

            if (orderIdStr == null) {
                sendJsonResponse(response, false, "Order ID is required", null);
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            if (reason == null || reason.trim().isEmpty()) {
                reason = "Vendor cancelled order";
            }

            Order order = vendorService.cancelOrder(orderId, reason, vendorId);
            sendJsonResponse(response, true, "Đơn hàng đã được hủy", order);
        } catch (Exception e) {
            logger.error("Error cancelling order", e);
            sendJsonResponse(response, false, "Không thể hủy đơn hàng: " + e.getMessage(), null);
        }
    }

    private void handleAssignShipper(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            String orderIdStr = request.getParameter("orderId");
            String shipperIdStr = request.getParameter("shipperId");

            if (orderIdStr == null || shipperIdStr == null) {
                sendJsonResponse(response, false, "Order ID and Shipper ID are required", null);
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            int shipperId = Integer.parseInt(shipperIdStr);

            DeliveryAssignment assignment = vendorService.assignShipperToOrder(orderId, shipperId, vendorId);
            sendJsonResponse(response, true, "Đã phân chia đơn hàng cho shipper thành công", assignment);
        } catch (Exception e) {
            logger.error("Error assigning shipper", e);
            sendJsonResponse(response, false, "Không thể phân chia shipper: " + e.getMessage(), null);
        }
    }

    /**
     * Đọc JSON data từ request body
     */
    private Map<String, Object> readJsonFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        if (sb.length() == 0) {
            return new HashMap<>();
        }

        return gson.fromJson(sb.toString(), Map.class);
    }

    /**
     * Gửi JSON response
     */
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, Object data)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        jsonResponse.put("data", data);

        try (PrintWriter out = response.getWriter()) {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }
}
