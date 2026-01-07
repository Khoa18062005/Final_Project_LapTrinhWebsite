package viettech.controller;

// VendorServlet - Updated:  product CRUD operations

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.OrderDAO;
import viettech.dto.Vendor_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;
import viettech.entity.order.Order;
import viettech.entity.order.OrderDetail;
import viettech.entity.product.Product;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/vendor")
public class VendorServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(VendorServlet.class);
    private final VendorService vendorService = new VendorService();
    private final OrderDAO orderDAO = new OrderDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra Session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 3. Lấy action từ request
        String action = request.getParameter("action");

        // --- API actions that must return JSON and must NOT forward JSP ---
        // Note: keep this early to avoid accidentally forwarding vendor.jsp (HTML).
        if ("statsData".equals(action)) {
            if (user == null || user.getRoleID() != 2) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                sendJsonResponse(response, false, "Unauthorized access", null);
                return;
            }

            int vendorId = user.getUserId();
            try {
                String period = request.getParameter("period");
                Map<String, Object> stats;
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> s = (Map<String, Object>) vendorService.getClass()
                            .getMethod("getVendorStats", int.class, String.class)
                            .invoke(vendorService, vendorId, period);
                    stats = s;
                } catch (Exception reflectEx) {
                    throw new RuntimeException("Stats API method not available: getVendorStats", reflectEx);
                }
                sendJsonResponse(response, true, "OK", stats);
            } catch (Exception e) {
                logger.error("Error getting vendor stats", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                sendJsonResponse(response, false, e.getMessage(), null);
            }
            return;
        }

        // From here onward, pages are HTML. Require authenticated vendor.
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (user.getRoleID() != 2) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        int vendorId = user.getUserId();

        try {
            // Get dashboard data for all pages (contains vendor info, stats, etc.)
            Vendor_dto data = vendorService.getDashboardData(vendorId);
            if (data == null) {
                logger.warn("VendorService returned null for vendor {}", vendorId);
                data = new Vendor_dto();
            } else {
                logger.info("Successfully got dashboard data for vendor {}", vendorId);
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
                logger.info("Setting {} orders for shipping page", orders != null ? orders.size() : 0);
                request.setAttribute("orders", orders);
            } else if (action.equals("products")) {
                // Lấy tất cả sản phẩm của vendor
                List<Product> products = vendorService.getAllProductsByVendor(vendorId);
                //log all products and size
                logger.debug("Products for vendor {}: {}", vendorId, products);
                logger.info("Setting {} products for products page", products != null ? products.size() : 0);
                request.setAttribute("products", products);
            } else if (action.equals("stats")) {
                // Page render only (HTML). Data will be loaded via AJAX from action=statsData.
                // Nothing else to do here.
            } else if (action.equals("getProduct")) {
                // Lấy thông tin sản phẩm để chỉnh sửa
                String productIdStr = request.getParameter("productId");
                if (productIdStr != null) {
                    try {
                        int productId = Integer.parseInt(productIdStr);
                        Product product = vendorService.getProductById(productId, vendorId);
                        if (product != null) {
                            // Convert to Map to avoid LazyInitializationException and circular references
                            // with Gson
                            Map<String, Object> productData = new HashMap<>();
                            productData.put("productId", product.getProductId());
                            productData.put("name", product.getName());
                            productData.put("categoryId", product.getCategoryId());
                            productData.put("brand", product.getBrand());
                            productData.put("basePrice", product.getBasePrice());
                            productData.put("status", product.getStatus());
                            productData.put("description", product.getDescription());
                            productData.put("specifications", product.getSpecifications());

                            sendJsonResponse(response, true, "Product retrieved successfully", productData);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        sendJsonResponse(response, false, "Invalid product ID format", null);
                        return;
                    }
                }
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                sendJsonResponse(response, false, "Product not found", null);
                return;
            } else if (action.equals("orderDetails")) {
                // Lấy chi tiết đơn hàng
                String orderIdStr = request.getParameter("orderId");
                if (orderIdStr != null) {
                    try {
                        int orderId = Integer.parseInt(orderIdStr);
                        List<OrderDetail> orderDetails = vendorService.getOrderDetails(orderId, vendorId);
                        if (orderDetails != null && !orderDetails.isEmpty()) {
                            sendJsonResponse(response, true, "Order details retrieved successfully", orderDetails);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        sendJsonResponse(response, false, "Invalid order ID format", null);
                        return;
                    }
                }
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                sendJsonResponse(response, false, "Order not found or access denied", null);
                return;
            } else if (action.equals("getShipper")) {
                // Lấy thông tin shipper cho đơn hàng
                String orderIdStr = request.getParameter("orderId");
                if (orderIdStr != null) {
                    try {
                        int orderId = Integer.parseInt(orderIdStr);
                        Shipper shipper = vendorService.getShipperForOrder(orderId, vendorId);
                        Map<String, Object> responseData = new HashMap<>();
                        if (shipper != null) {
                            // Use a simple map instead of the entity to avoid serialization issues
                            Map<String, Object> shipperData = new HashMap<>();
                            shipperData.put("userId", shipper.getUserId());
                            shipperData.put("firstName", shipper.getFirstName());
                            shipperData.put("lastName", shipper.getLastName());
                            shipperData.put("email", shipper.getEmail());
                            shipperData.put("phone", shipper.getPhone());
                            responseData.put("shipper", shipperData);
                        }
                        responseData.put("hasShipper", shipper != null);
                        sendJsonResponse(response, true,
                                shipper != null ? "Shipper information retrieved" : "No shipper assigned",
                                responseData);
                        return;
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        sendJsonResponse(response, false, "Invalid order ID format", null);
                        return;
                    } catch (Exception e) {
                        logger.error("Error getting shipper for order {}", orderIdStr, e);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        sendJsonResponse(response, false, "Error retrieving shipper information", null);
                        return;
                    }
                }
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                sendJsonResponse(response, false, "Order ID required", null);
                return;
            } else if (action.equals("getOrderDetails")) {
                // Lấy chi tiết đơn hàng bao gồm sản phẩm
                String orderIdStr = request.getParameter("orderId");
                if (orderIdStr != null) {
                    try {
                        int orderId = Integer.parseInt(orderIdStr);
                        List<OrderDetail> orderDetails = vendorService.getOrderDetails(orderId, vendorId);
                        // Use findByIdWithRelations to fetch customer and address eagerly
                        Order order = orderDAO.findByIdWithRelations(orderId);
                        if (order != null && order.getVendorId() == vendorId && orderDetails != null) {
                            // Use DTOs to avoid JSON serialization issues
                            VendorService.OrderInfoDTO orderDTO = new VendorService.OrderInfoDTO(order);
                            List<VendorService.OrderItemDTO> itemDTOs = orderDetails.stream()
                                    .map(VendorService.OrderItemDTO::new)
                                    .toList();

                            Map<String, Object> responseData = new HashMap<>();
                            responseData.put("order", orderDTO);
                            responseData.put("orderItems", itemDTOs);
                            sendJsonResponse(response, true, "Order details retrieved successfully", responseData);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        sendJsonResponse(response, false, "Invalid order ID format", null);
                        return;
                    } catch (Exception e) {
                        logger.error("Error getting order details for order {}", orderIdStr, e);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        sendJsonResponse(response, false, "Error retrieving order details", null);
                        return;
                    }
                }
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                sendJsonResponse(response, false, "Order not found or access denied", null);
                return;
            } else if (action.equals("getAvailableShippers")) {
                // Lấy danh sách shipper khả dụng
                GetAvailableShippers(request, response, vendorId);
                return;
            } else if (action.equals("updateOrderNotes")) {
                // Update order notes (vendor-side)
                String orderIdStr = request.getParameter("orderId");
                if (orderIdStr == null || orderIdStr.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    sendJsonResponse(response, false, "Missing orderId", null);
                    return;
                }

                int orderId;
                try {
                    orderId = Integer.parseInt(orderIdStr);
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    sendJsonResponse(response, false, "Invalid orderId", null);
                    return;
                }

                try {
                    String body = request.getReader().lines().reduce("", (a, b) -> a + b);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> payload = new com.google.gson.Gson().fromJson(body, Map.class);
                    String notes = payload != null && payload.get("notes") != null ? String.valueOf(payload.get("notes")) : "";

                    Order updated = vendorService.updateOrderNotes(orderId, notes, vendorId);
                    sendJsonResponse(response, true, "Updated notes successfully", updated != null ? updated.getOrderId() : null);
                    return;
                } catch (Exception ex) {
                    logger.error("Error updating order notes", ex);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    sendJsonResponse(response, false, ex.getMessage(), null);
                    return;
                }
            }

            // Forward to vendor.jsp - let JSP  routing based on action parameter
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
                    AddProduct(request, response, vendorId);
                    break;
                case "updateProduct":
                    UpdateProduct(request, response, vendorId);
                    break;
                case "deleteProduct":
                    DeleteProduct(request, response, vendorId);
                    break;
                case "updateOrderStatus":
                    UpdateOrderStatus(request, response, vendorId);
                    break;
                case "cancelOrder":
                    CancelOrder(request, response, vendorId);
                    break;
                case "assignShipper":
                    AssignShipper(request, response, vendorId);
                    break;
                case "broadcastDelivery":
                    BroadcastDelivery(request, response, vendorId);
                    break;
                case "getAvailableShippers":
                    GetAvailableShippers(request, response, vendorId);
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

    private void showDashboard(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws ServletException, IOException {
        Vendor_dto data = vendorService.getDashboardData(vendorId);
        if (data == null) {
            data = new Vendor_dto();
        }
        request.setAttribute("data", data);
        request.getRequestDispatcher("/WEB-INF/views/vendor.jsp").forward(request, response);
    }

    private void AddProduct(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            // Đọc JSON data từ request body
            Map<String, Object> productData = readJsonFromRequest(request);

            if (productData == null || productData.isEmpty()) {
                sendJsonResponse(response, false, "Product data is required", null);
                return;
            }

            // Thêm sản phẩm trực tiếp vào database
            Product product = vendorService.addProduct(productData, vendorId);
            sendJsonResponse(response, true, "Thêm sản phẩm thành công!", product);
        } catch (Exception e) {
            logger.error("Error adding product", e);
            sendJsonResponse(response, false, "Không thể thêm sản phẩm: " + e.getMessage(), null);
        }
    }

    private void UpdateProduct(HttpServletRequest request, HttpServletResponse response, int vendorId)
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

            // Cập nhật sản phẩm
            Product product = vendorService.updateProduct(productId, productData, vendorId);
            sendJsonResponse(response, true, "Cập nhật sản phẩm thành công!", product);
        } catch (Exception e) {
            logger.error("Error updating product", e);
            sendJsonResponse(response, false, "Không thể cập nhật sản phẩm: " + e.getMessage(), null);
        }
    }

    private void DeleteProduct(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            String productIdStr = request.getParameter("productId");
            if (productIdStr == null) {
                sendJsonResponse(response, false, "Product ID is required", null);
                return;
            }

            int productId = Integer.parseInt(productIdStr);
            // Xóa sản phẩm
            vendorService.deleteProduct(productId, vendorId);
            sendJsonResponse(response, true, "Xóa sản phẩm thành công!", null);
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            sendJsonResponse(response, false, "Không thể xóa sản phẩm: " + e.getMessage(), null);
        }
    }

    private void UpdateOrderStatus(HttpServletRequest request, HttpServletResponse response, int vendorId)
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

    private void CancelOrder(HttpServletRequest request, HttpServletResponse response, int vendorId)
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

    private void AssignShipper(HttpServletRequest request, HttpServletResponse response, int vendorId)
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

    private void BroadcastDelivery(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                sendJsonResponse(response, false, "Order ID is required", null);
                return;
            }

            int orderId;
            try {
                orderId = Integer.parseInt(orderIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                sendJsonResponse(response, false, "Invalid order ID format", null);
                return;
            }

            // call service
            vendorService.broadcastDeliveryRequest(orderId, vendorId);
            sendJsonResponse(response, true, "Đã gửi yêu cầu giao hàng tới các shipper đang rảnh", null);
        } catch (Exception e) {
            logger.error("Error broadcasting delivery request", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, false, "Không thể gửi yêu cầu giao hàng: " + e.getMessage(), null);
        }
    }

    private void GetAvailableShippers(HttpServletRequest request, HttpServletResponse response, int vendorId)
            throws IOException {
        try {
            List<Shipper> shippers = vendorService.getAvailableShippers();
            sendJsonResponse(response, true, "Available shippers retrieved successfully", shippers);
        } catch (Exception e) {
            logger.error("Error getting available shippers", e);
            sendJsonResponse(response, false, "Không thể tải danh sách shipper: " + e.getMessage(), null);
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
