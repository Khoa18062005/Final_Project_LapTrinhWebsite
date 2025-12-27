package viettech.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.dao.*;
import viettech.dto.Vendor_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.order.Order;
import viettech.entity.product.Product;
import viettech.entity.product.ProductApproval;
import viettech.entity.user.Vendor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VendorService {

    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductApprovalDAO approvalDAO = new ProductApprovalDAO();
    private final DeliveryAssignmentDAO deliveryAssignmentDAO = new DeliveryAssignmentDAO();
    private final Gson gson = new Gson();

    public Vendor_dto getDashboardData(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        Vendor_dto dto = new Vendor_dto();

        try {
            // 1. Lấy thông tin Vendor
            Vendor vendor = em.find(Vendor.class, vendorId);
            if (vendor == null) return null;
            dto.setVendorInfo(vendor);

            // 2. Lấy danh sách Sản phẩm (Products)
            String prodQuery = "SELECT p FROM Product p WHERE p.vendorId = :vid ORDER BY p.createdAt DESC";
            List<Product> products = em.createQuery(prodQuery, Product.class)
                    .setParameter("vid", vendorId)
                    .getResultList();
            dto.setProductList(products);
            dto.setTotalProducts(products.size());

            // Đếm sản phẩm chờ duyệt từ ProductApproval
            long pendingProds = approvalDAO.countPendingByVendorId(vendorId);
            dto.setPendingApprovals(pendingProds);

            // 3. Lấy danh sách Đơn hàng (Orders)
            // JOIN FETCH để tránh lỗi Lazy Loading khi lấy Customer và Address
            String orderQuery = "SELECT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "WHERE o.vendorId = :vid ORDER BY o.orderDate DESC";

            List<Order> orders = em.createQuery(orderQuery, Order.class)
                    .setParameter("vid", vendorId)
                    .getResultList();

            dto.setRecentOrders(orders);

            // 4. Tính toán thống kê Đơn hàng
            long newOrders = 0;
            double revenue = 0;
            Date startOfMonth = Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            for (Order o : orders) {
                // Đếm đơn mới (Pending hoặc Processing)
                if ("Processing".equalsIgnoreCase(o.getStatus()) || "Pending".equalsIgnoreCase(o.getStatus())) {
                    newOrders++;
                }

                // Tính doanh thu tháng này (Chỉ đơn Completed)
                if ("Completed".equalsIgnoreCase(o.getStatus()) && o.getOrderDate().after(startOfMonth)) {
                    revenue += o.getTotalPrice(); // Hoặc o.getSubtotal() tùy logic kinh doanh
                }
            }

            dto.setNewOrdersCount(newOrders);
            dto.setMonthlyRevenue(revenue);

            // 5. Lấy danh sách đơn cần giao cho Shipper (Ví dụ trạng thái: Confirmed hoặc Processing)
            // Lọc từ list orders đã lấy ở trên để đỡ query nhiều lần
            List<Order> shippingOrders = orders.stream()
                    .filter(o -> "Processing".equalsIgnoreCase(o.getStatus()) || "Confirmed".equalsIgnoreCase(o.getStatus()))
                    .toList(); // Java 16+, nếu thấp hơn dùng Collectors.toList()
            dto.setPendingShippingOrders(shippingOrders);

        } catch (Exception e) {
            logger.error("✗ Error getting dashboard data for vendor {}", vendorId, e);
        } finally {
            em.close();
        }
        return dto;
    }

    // ==================== PRODUCT MANAGEMENT ====================

    /**
     * Tạo yêu cầu thêm sản phẩm mới (cần admin phê duyệt)
     */
    public ProductApproval requestAddProduct(Map<String, Object> productData, int vendorId) {
        try {
            ProductApproval approval = new ProductApproval();
            approval.setVendorId(vendorId);
            approval.setActionType("ADD");
            approval.setStatus("PENDING");
            approval.setProductData(gson.toJson(productData));
            approval.setRequestedAt(new Date());

            approvalDAO.insert(approval);
            logger.info("✓ Vendor {} requested to add new product", vendorId);
            return approval;
        } catch (Exception e) {
            logger.error("✗ Failed to create add product request for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to create product add request", e);
        }
    }

    /**
     * Tạo yêu cầu cập nhật sản phẩm (cần admin phê duyệt)
     */
    public ProductApproval requestUpdateProduct(int productId, Map<String, Object> productData, int vendorId) {
        try {
            // Kiểm tra sản phẩm có thuộc vendor này không
            Product product = productDAO.findById(productId);
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
            if (product.getVendorId() != vendorId) {
                throw new RuntimeException("Product does not belong to this vendor");
            }

            ProductApproval approval = new ProductApproval();
            approval.setProductId(productId);
            approval.setVendorId(vendorId);
            approval.setActionType("UPDATE");
            approval.setStatus("PENDING");
            approval.setProductData(gson.toJson(productData));
            approval.setRequestedAt(new Date());

            approvalDAO.insert(approval);
            logger.info("✓ Vendor {} requested to update product {}", vendorId, productId);
            return approval;
        } catch (Exception e) {
            logger.error("✗ Failed to create update product request for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to create product update request", e);
        }
    }

    /**
     * Tạo yêu cầu xóa sản phẩm (cần admin phê duyệt)
     */
    public ProductApproval requestDeleteProduct(int productId, int vendorId) {
        try {
            // Kiểm tra sản phẩm có thuộc vendor này không
            Product product = productDAO.findById(productId);
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
            if (product.getVendorId() != vendorId) {
                throw new RuntimeException("Product does not belong to this vendor");
            }

            ProductApproval approval = new ProductApproval();
            approval.setProductId(productId);
            approval.setVendorId(vendorId);
            approval.setActionType("DELETE");
            approval.setStatus("PENDING");
            approval.setRequestedAt(new Date());

            approvalDAO.insert(approval);
            logger.info("✓ Vendor {} requested to delete product {}", vendorId, productId);
            return approval;
        } catch (Exception e) {
            logger.error("✗ Failed to create delete product request for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to create product delete request", e);
        }
    }

    /**
     * Lấy danh sách yêu cầu phê duyệt của vendor
     */
    public List<ProductApproval> getApprovalRequests(int vendorId) {
        try {
            return approvalDAO.findByVendorId(vendorId);
        } catch (Exception e) {
            logger.error("✗ Failed to get approval requests for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to get approval requests", e);
        }
    }

    // ==================== ORDER MANAGEMENT ====================

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public Order updateOrderStatus(int orderId, String newStatus, int vendorId) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                throw new RuntimeException("Order not found");
            }

            // Kiểm tra đơn hàng có thuộc vendor này không
            if (order.getVendorId() != vendorId) {
                throw new RuntimeException("Order does not belong to this vendor");
            }

            // Cập nhật trạng thái
            String oldStatus = order.getStatus();
            order.setStatus(newStatus);

            // Cập nhật các timestamp tương ứng
            if ("Completed".equalsIgnoreCase(newStatus)) {
                order.setCompletedAt(new Date());
            } else if ("Cancelled".equalsIgnoreCase(newStatus)) {
                order.setCancelledAt(new Date());
                order.setCancelledBy("Vendor");
            }

            orderDAO.update(order);
            logger.info("✓ Vendor {} updated order {} status from {} to {}",
                vendorId, orderId, oldStatus, newStatus);
            return order;
        } catch (Exception e) {
            logger.error("✗ Failed to update order status for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }

    /**
     * Lấy danh sách đơn hàng theo trạng thái
     */
    public List<Order> getOrdersByStatus(int vendorId, String status) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.vendorId = :vendorId AND o.status = :status ORDER BY o.orderDate DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("vendorId", vendorId);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("✗ Failed to get orders by status for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to get orders by status", e);
        } finally {
            em.close();
        }
    }

    /**
     * Hủy đơn hàng
     */
    public Order cancelOrder(int orderId, String reason, int vendorId) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                throw new RuntimeException("Order not found");
            }
            if (order.getVendorId() != vendorId) {
                throw new RuntimeException("Order does not belong to this vendor");
            }

            order.setStatus("Cancelled");
            order.setCancelReason(reason);
            order.setCancelledAt(new Date());
            order.setCancelledBy("Vendor");

            orderDAO.update(order);
            logger.info("✓ Vendor {} cancelled order {}", vendorId, orderId);
            return order;
        } catch (Exception e) {
            logger.error("✗ Failed to cancel order for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to cancel order", e);
        }
    }

    // ==================== SHIPPER ASSIGNMENT ====================

    /**
     * Phân chia đơn hàng cho shipper
     */
    public DeliveryAssignment assignShipperToOrder(int orderId, int shipperId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Kiểm tra đơn hàng
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                throw new RuntimeException("Order not found");
            }
            if (order.getVendorId() != vendorId) {
                throw new RuntimeException("Order does not belong to this vendor");
            }

            // Tạo Delivery Assignment
            DeliveryAssignment assignment = new DeliveryAssignment();
            assignment.setShipperId(shipperId);
            assignment.setAssignedAt(new Date());
            assignment.setStatus("ASSIGNED");

            // Tùy logic của bạn, có thể cần tạo Delivery entity trước
            assignment.setDeliveryId(orderId);

            deliveryAssignmentDAO.insert(assignment);

            // Cập nhật trạng thái đơn hàng
            order.setStatus("Assigned");
            orderDAO.update(order);

            logger.info("✓ Vendor {} assigned order {} to shipper {}", vendorId, orderId, shipperId);
            return assignment;
        } catch (Exception e) {
            logger.error("✗ Failed to assign shipper for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to assign shipper", e);
        } finally {
            em.close();
        }
    }

    /**
     * Lấy danh sách đơn hàng cần giao cho shipper
     */
    public List<Order> getOrdersReadyForShipping(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.vendorId = :vendorId " +
                    "AND (o.status = 'Processing' OR o.status = 'Confirmed' OR o.status = 'Ready') " +
                    "ORDER BY o.orderDate DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("vendorId", vendorId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("✗ Failed to get orders ready for shipping for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to get orders ready for shipping", e);
        } finally {
            em.close();
        }
    }
}

