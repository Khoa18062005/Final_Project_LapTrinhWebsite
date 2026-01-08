package viettech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.OrderDAO;
import viettech.dao.OrderDetailDAO;
import viettech.dao.ProductDAO;
import viettech.entity.order.Order;
import viettech.entity.order.OrderDetail;
import viettech.entity.product.Product;
import viettech.entity.product.ProductImage;

import java.util.*;

public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderDAO orderDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final ProductDAO productDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderDetailDAO = new OrderDetailDAO();
        this.productDAO = new ProductDAO();
    }

    /**
     * ✅ LẤY TẤT CẢ ĐƠN HÀNG CỦA CUSTOMER
     */
    public List<Map<String, Object>> getOrdersByCustomerId(int customerId) {
        try {
            List<Order> orders = orderDAO.findByCustomerId(customerId);
            return buildOrderMaps(orders);
        } catch (Exception e) {
            logger.error("✗ Error getting orders for customer {}", customerId, e);
            return new ArrayList<>();
        }
    }

    /**
     * ✅ LẤY ĐƠN HÀNG THEO STATUS
     */
    public List<Map<String, Object>> getOrdersByStatus(int customerId, String status) {
        try {
            List<Order> orders = orderDAO.findByCustomerIdAndStatus(customerId, status);
            return buildOrderMaps(orders);
        } catch (Exception e) {
            logger.error("✗ Error getting orders by status", e);
            return new ArrayList<>();
        }
    }

    /**
     * ✅ ĐẾM SỐ ĐƠN HÀNG THEO TỪNG STATUS
     */
    public Map<String, Long> getOrderCountsByStatus(int customerId) {
        Map<String, Long> counts = new HashMap<>();

        try {
            // Đếm tất cả
            counts.put("all", (long) orderDAO.findByCustomerId(customerId).size());

            // Đếm từng status
            counts.put("pending", orderDAO.countByCustomerIdAndStatus(customerId, "pending"));
            counts.put("processing", orderDAO.countByCustomerIdAndStatus(customerId, "processing"));
            counts.put("shipping", orderDAO.countByCustomerIdAndStatus(customerId, "shipping"));
            counts.put("completed", orderDAO.countByCustomerIdAndStatus(customerId, "completed"));
            counts.put("cancelled", orderDAO.countByCustomerIdAndStatus(customerId, "cancelled"));
            counts.put("returned", orderDAO.countByCustomerIdAndStatus(customerId, "returned"));

            logger.debug("✓ Order counts: {}", counts);

        } catch (Exception e) {
            logger.error("✗ Error counting orders", e);
        }

        return counts;
    }

    /**
     * ✅ BUILD ORDER MAPS ĐỂ TRUYỀN VÀO JSP
     */
    private List<Map<String, Object>> buildOrderMaps(List<Order> orders) {
        List<Map<String, Object>> orderMaps = new ArrayList<>();

        for (Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();

            // Thông tin order
            orderMap.put("orderId", order.getOrderId());
            orderMap.put("orderNumber", order.getOrderNumber());
            orderMap.put("orderDate", order.getOrderDate());
            orderMap.put("status", order.getStatus());
            orderMap.put("totalAmount", order.getTotalPrice());

            // Lấy order details
            List<OrderDetail> details = orderDetailDAO.findByOrderId(order.getOrderId());
            List<Map<String, Object>> itemMaps = new ArrayList<>();

            for (OrderDetail detail : details) {
                Map<String, Object> itemMap = new HashMap<>();

                // ✅ QUAN TRỌNG: THÊM productId
                itemMap.put("productId", detail.getProductId());
                itemMap.put("productName", detail.getProductName());
                itemMap.put("variantInfo", detail.getVariantInfo());
                itemMap.put("quantity", detail.getQuantity());
                itemMap.put("price", detail.getUnitPrice());

                // Lấy hình ảnh sản phẩm
                String imageUrl = getProductImageUrl(detail.getProductId());
                itemMap.put("productImage", imageUrl);

                itemMaps.add(itemMap);
            }

            orderMap.put("items", itemMaps);
            orderMaps.add(orderMap);
        }

        return orderMaps;
    }

    /**
     * ✅ LẤY URL HÌNH ẢNH CỦA SẢN PHẨM
     * Ưu tiên: ảnh primary → ảnh đầu tiên → ảnh mặc định
     */
    private String getProductImageUrl(int productId) {
        try {
            // Lấy product kèm images (JOIN FETCH)
            Product product = productDAO.findByIdWithImages(productId);

            if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
                // Tìm ảnh primary đầu tiên
                for (ProductImage img : product.getImages()) {
                    if (img.isPrimary()) {
                        logger.debug("✓ Found primary image for product {}: {}", productId, img.getUrl());
                        return img.getUrl();
                    }
                }

                // Nếu không có primary, lấy ảnh đầu tiên
                String firstImageUrl = product.getImages().get(0).getUrl();
                logger.debug("✓ Using first image for product {}: {}", productId, firstImageUrl);
                return firstImageUrl;
            }

            // Không có ảnh nào
            logger.warn("✗ No image found for product {}, using default", productId);
            return "/assets/images/no-image.png";

        } catch (Exception e) {
            logger.error("✗ Error getting image for product ID: {}", productId, e);
            return "/assets/images/no-image.png";
        }
    }

    public Map<String, Object> getOrderDetail(int orderId) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                logger.warn("✗ Order not found with ID: {}", orderId);
                return null;
            }

            // Build order detail map
            Map<String, Object> orderMap = new HashMap<>();

            // ✅ Thông tin order đầy đủ
            orderMap.put("orderId", order.getOrderId());
            orderMap.put("orderNumber", order.getOrderNumber());
            orderMap.put("orderDate", order.getOrderDate().getTime()); // ← Timestamp để JS parse
            orderMap.put("status", order.getStatus());
            orderMap.put("subtotal", order.getSubtotal());
            orderMap.put("shippingFee", order.getShippingFee());
            orderMap.put("discount", order.getDiscount());
            orderMap.put("voucherDiscount", order.getVoucherDiscount());
            orderMap.put("totalAmount", order.getTotalPrice());
            orderMap.put("notes", order.getNotes());

            // ✅ Lấy order details
            List<OrderDetail> details = orderDetailDAO.findByOrderId(order.getOrderId());
            List<Map<String, Object>> itemMaps = new ArrayList<>();

            for (OrderDetail detail : details) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("orderDetailId", detail.getOrderDetailId());
                itemMap.put("productId", detail.getProductId());
                itemMap.put("productName", detail.getProductName());
                itemMap.put("variantInfo", detail.getVariantInfo());
                itemMap.put("quantity", detail.getQuantity());
                itemMap.put("price", detail.getUnitPrice());
                itemMap.put("subtotal", detail.getSubtotal());

                // Lấy hình ảnh sản phẩm
                String imageUrl = getProductImageUrl(detail.getProductId());
                itemMap.put("productImage", imageUrl);

                itemMaps.add(itemMap);
            }

            orderMap.put("items", itemMaps);

            logger.debug("✓ Order detail loaded for order ID: {}", orderId);
            return orderMap;

        } catch (Exception e) {
            logger.error("✗ Error getting order detail for ID: {}", orderId, e);
            return null;
        }
    }
}