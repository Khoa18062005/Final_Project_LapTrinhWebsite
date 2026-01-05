package viettech.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.dao.*;
import viettech.dto.Vendor_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.order.Order;
import viettech.entity.order.OrderDetail;
import viettech.entity.product.Product;
import viettech.entity.product.ProductApproval;
import viettech.entity.user.Vendor;
import viettech.entity.user.Shipper;

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
            logger.info("Getting dashboard data for vendor {}", vendorId);

            // 1. Lấy thông tin Vendor
            Vendor vendor = em.find(Vendor.class, vendorId);
            if (vendor == null) {
                logger.warn("Vendor {} not found", vendorId);
                return null;
            }
            dto.setVendorInfo(vendor);
            logger.info("Found vendor: {}", vendor.getBusinessName());

            // 1.1. Lấy thông tin Warehouse của Vendor
            String warehouseQuery = "SELECT w FROM Warehouse w WHERE w.managerId = :vid";
            List<viettech.entity.storage.Warehouse> warehouses = em
                    .createQuery(warehouseQuery, viettech.entity.storage.Warehouse.class)
                    .setParameter("vid", vendorId)
                    .getResultList();
            if (!warehouses.isEmpty()) {
                dto.setWarehouseInfo(warehouses.get(0)); // Lấy warehouse đầu tiên
                logger.info("Found warehouse: {}", warehouses.get(0).getName());
            } else {
                logger.info("No warehouse found for vendor {}", vendorId);
            }

            // 2. Lấy danh sách Sản phẩm (Products)
            String prodQuery = "SELECT p FROM Product p WHERE p.vendorId = :vid ORDER BY p.createdAt DESC";
            List<Product> products = em.createQuery(prodQuery, Product.class)
                    .setParameter("vid", vendorId)
                    .getResultList();
            dto.setProductList(products);
            dto.setTotalProducts(products.size());
            logger.info("Found {} products for vendor {}", products.size(), vendorId);

            // Đếm sản phẩm chờ duyệt từ ProductApproval
            long pendingProds = approvalDAO.countPendingByVendorId(vendorId);
            dto.setPendingApprovals(pendingProds);
            logger.info("Found {} pending approvals for vendor {}", pendingProds, vendorId);

            // 3. Lấy danh sách Đơn hàng (Orders)
            // JOIN FETCH để tránh lỗi Lazy Loading khi lấy Customer và Address
            String orderQuery = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "WHERE o.vendorId = :vid ORDER BY o.orderDate DESC";

            List<Order> orders = em.createQuery(orderQuery, Order.class)
                    .setParameter("vid", vendorId)
                    .getResultList();

            dto.setRecentOrders(orders);
            logger.info("Found {} orders for vendor {}", orders.size(), vendorId);

            // 4. Tính toán thống kê Đơn hàng
            long newOrders = 0;
            double revenue = 0;
            Date startOfMonth = Date
                    .from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

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
            logger.info("Calculated stats: newOrders={}, revenue={}", newOrders, revenue);

            // 5. Lấy danh sách đơn cần giao cho Shipper (Ví dụ trạng thái: Confirmed hoặc
            // Processing)
            // Lọc từ list orders đã lấy ở trên để đỡ query nhiều lần
            List<Order> shippingOrders = orders.stream()
                    .filter(o -> "Processing".equalsIgnoreCase(o.getStatus())
                            || "Confirmed".equalsIgnoreCase(o.getStatus()))
                    .toList(); // Java 16+, nếu thấp hơn dùng Collectors.toList()
            dto.setPendingShippingOrders(shippingOrders);
            logger.info("Found {} orders ready for shipping", shippingOrders.size());

            logger.info("Successfully retrieved dashboard data for vendor {}", vendorId);
            return dto;

        } catch (Exception e) {
            logger.error("✗ Error getting dashboard data for vendor {}", vendorId, e);
            return null;
        } finally {
            em.close();
        }
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

    /**
     * Lấy tất cả sản phẩm của vendor
     */
    public List<Product> getAllProductsByVendor(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Use scalar vendorId field (present in Product entity) to avoid relying on optional/mis-mapped vendor relation.
            String jpql = "SELECT p FROM Product p WHERE p.vendorId = :vendorId ORDER BY p.createdAt DESC";

            return em.createQuery(jpql, Product.class)
                    .setParameter("vendorId", vendorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }


    /**
     * Lấy sản phẩm theo ID và kiểm tra quyền sở hữu
     */
    public Product getProductById(int productId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Match the list query: verify ownership via scalar vendorId
            String jpql = "SELECT p FROM Product p WHERE p.productId = :productId AND p.vendorId = :vendorId";

            List<Product> list = em.createQuery(jpql, Product.class)
                    .setParameter("productId", productId)
                    .setParameter("vendorId", vendorId)
                    .getResultList();

            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
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
            // Fetch relations needed by vendor.jsp (customer/address) to avoid LazyInitializationException
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "WHERE o.vendorId = :vendorId AND o.status = :status " +
                    "ORDER BY o.orderDate DESC";
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

    /**
     * Cập nhật ghi chú đơn hàng
     */
    public Order updateOrderNotes(int orderId, String notes, int vendorId) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null || order.getVendorId() != vendorId) {
                throw new RuntimeException("Order not found or access denied");
            }

            order.setNotes(notes);
            orderDAO.update(order);
            logger.info("Updated notes for order {}", orderId);
            return order;
        } catch (Exception e) {
            logger.error("Failed to update order notes for order {}", orderId, e);
            throw new RuntimeException("Failed to update order notes", e);
        }
    }

    /**
     * Lấy chi tiết đơn hàng bao gồm thông tin sản phẩm
     */
    public List<OrderDetail> getOrderDetails(int orderId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Kiểm tra đơn hàng có thuộc vendor này không
            Order order = orderDAO.findById(orderId);
            if (order == null || order.getVendorId() != vendorId) {
                return null;
            }

            String jpql = "SELECT od FROM OrderDetail od " +
                    "LEFT JOIN FETCH od.product p " +
                    "WHERE od.orderId = :orderId";
            TypedQuery<OrderDetail> query = em.createQuery(jpql, OrderDetail.class);
            query.setParameter("orderId", orderId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("✗ Failed to get order details for order {} vendor {}", orderId, vendorId, e);
            throw new RuntimeException("Failed to get order details", e);
        } finally {
            em.close();
        }
    }

    /**
     * Lấy thông tin shipper được gán cho đơn hàng
     */
    public Shipper getShipperForOrder(int orderId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Kiểm tra đơn hàng có thuộc vendor này không
            Order order = orderDAO.findById(orderId);
            if (order == null || order.getVendorId() != vendorId) {
                return null;
            }

            String jpql = "SELECT da.shipper " +
                    "FROM viettech.entity.delivery.DeliveryAssignment da " +
                    "JOIN da.delivery d " +
                    "WHERE d.orderId = :orderId";

            TypedQuery<Shipper> query = em.createQuery(jpql, Shipper.class);
            query.setParameter("orderId", orderId);
            List<Shipper> shippers = query.getResultList();
            return shippers.isEmpty() ? null : shippers.get(0);
        } catch (Exception e) {
            logger.error("✗ Failed to get shipper for order {} vendor {}", orderId, vendorId, e);
            throw new RuntimeException("Failed to get shipper information", e);
        } finally {
            em.close();
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

            // Ensure a Delivery exists for this order (DeliveryAssignment references deliveryId, not orderId)
            viettech.entity.delivery.Delivery delivery = em.createQuery(
                            "SELECT d FROM viettech.entity.delivery.Delivery d WHERE d.orderId = :orderId",
                            viettech.entity.delivery.Delivery.class)
                    .setParameter("orderId", orderId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (delivery == null) {
                int warehouseId = getWarehouseIdForVendor(vendorId);
                if (warehouseId <= 0) {
                    throw new RuntimeException("No active warehouse found for vendor");
                }

                delivery = new viettech.entity.delivery.Delivery();
                delivery.setOrderId(orderId);
                delivery.setWarehouseId(warehouseId);
                delivery.setAddressId(order.getAddressId());
                delivery.setStatus("ASSIGNED");
                em.getTransaction().begin();
                em.persist(delivery);
                em.getTransaction().commit();
            }

            // Tạo Delivery Assignment
            DeliveryAssignment assignment = new DeliveryAssignment();
            assignment.setDeliveryId(delivery.getDeliveryId());
            assignment.setShipperId(shipperId);
            assignment.setAssignedAt(new Date());
            assignment.setStatus("ASSIGNED");

            deliveryAssignmentDAO.insert(assignment);

            // Do NOT force order.status = 'Assigned' (other parts of the system use Processing/Confirmed/Ready/etc.).
            // Assignment state should be derived from Delivery/DeliveryAssignment.
            logger.info("Vendor {} assigned order {} to shipper {}", vendorId, orderId, shipperId);
            return assignment;
        } catch (Exception e) {
            logger.error("Failed to assign shipper for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to assign shipper", e);
        } finally {
            em.close();
        }
    }

    /**
     * Hủy phân chia shipper cho đơn hàng
     */
    public void unassignShipper(int orderId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null || order.getVendorId() != vendorId) {
                throw new RuntimeException("Order not found or access denied");
            }

            Integer deliveryId = em.createQuery(
                            "SELECT d.deliveryId FROM viettech.entity.delivery.Delivery d WHERE d.orderId = :orderId",
                            Integer.class)
                    .setParameter("orderId", orderId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (deliveryId == null) {
                return;
            }

            // Xóa assignment theo deliveryId
            em.getTransaction().begin();
            int deleted = em.createQuery(
                            "DELETE FROM viettech.entity.delivery.DeliveryAssignment da WHERE da.deliveryId = :deliveryId")
                    .setParameter("deliveryId", deliveryId)
                    .executeUpdate();
            em.getTransaction().commit();

            if (deleted > 0) {
                logger.info("Unassigned shipper from order {}", orderId);
            }
        } catch (Exception e) {
            logger.error("Failed to unassign shipper from order {}", orderId, e);
            throw new RuntimeException("Failed to unassign shipper", e);
        } finally {
            em.close();
        }
    }

    private int getWarehouseIdForVendor(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT w.warehouseId FROM viettech.entity.storage.Warehouse w " +
                                    "WHERE w.vendor.userId = :vendorId AND w.isActive = true",
                            Integer.class)
                    .setParameter("vendorId", vendorId)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(0);
        } finally {
            em.close();
        }
    }


    /**
     * Lấy danh sách đơn hàng cần giao với thông tin chi tiết
     */
    public List<Order> getOrdersReadyForShipping(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // IMPORTANT:
            // Hibernate cannot JOIN FETCH two @OneToMany bags in one query
            // (Order.orderDetails and Delivery.assignments) -> MultipleBagFetchException.
            // Also: Some OrderDetail rows may reference a Variant ID that no longer exists.
            // The shipping UI only needs snapshot fields from OrderDetail (productName/unitPrice/quantity),
            // so we must avoid initializing od.variant.
            String jpql = """
                SELECT DISTINCT o
                FROM Order o
                LEFT JOIN FETCH o.customer c
                LEFT JOIN FETCH o.address a
                LEFT JOIN FETCH o.delivery d
                WHERE o.vendorId = :vendorId
                  AND (o.status = 'Processing' OR o.status = 'Confirmed' OR o.status = 'Ready')
                ORDER BY o.orderDate DESC
            """;

            List<Order> orders = em.createQuery(jpql, Order.class)
                    .setParameter("vendorId", vendorId)
                    .getResultList();

            for (Order o : orders) {
                // Fetch ONLY snapshot fields to avoid touching broken Variant relation.
                List<Object[]> rows = em.createQuery(
                                "SELECT od.orderDetailId, od.orderId, od.productId, od.variantId, od.productName, od.variantInfo, od.quantity, od.unitPrice, od.discount, od.subtotal, od.status " +
                                        "FROM OrderDetail od WHERE od.orderId = :orderId",
                                Object[].class)
                        .setParameter("orderId", o.getOrderId())
                        .getResultList();

                List<OrderDetail> details = new java.util.ArrayList<>();
                for (Object[] r : rows) {
                    OrderDetail od = new OrderDetail();
                    // orderDetailId is generated; set it via reflection is not needed for UI
                    od.setOrderId(((Number) r[1]).intValue());
                    od.setProductId(((Number) r[2]).intValue());
                    od.setVariantId(((Number) r[3]).intValue());
                    od.setProductName((String) r[4]);
                    od.setVariantInfo((String) r[5]);
                    od.setQuantity(((Number) r[6]).intValue());
                    od.setUnitPrice(((Number) r[7]).doubleValue());
                    od.setDiscount(((Number) r[8]).doubleValue());
                    od.setSubtotal(((Number) r[9]).doubleValue());
                    od.setStatus((String) r[10]);
                    details.add(od);
                }
                o.setOrderDetails(details);

                // Fetch delivery assignments + shipper
                if (o.getDelivery() != null) {
                    List<DeliveryAssignment> assignments = em.createQuery(
                                    "SELECT da FROM viettech.entity.delivery.DeliveryAssignment da " +
                                            "LEFT JOIN FETCH da.shipper s " +
                                            "WHERE da.deliveryId = :deliveryId",
                                    DeliveryAssignment.class)
                            .setParameter("deliveryId", o.getDelivery().getDeliveryId())
                            .getResultList();
                    o.getDelivery().setAssignments(assignments);
                }
            }

            return orders;
        } catch (Exception e) {
            logger.error("✗ Failed to get orders ready for shipping for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to get orders ready for shipping: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * DTO cho thông tin đơn hàng trả về JSON
     */
    public static class OrderInfoDTO {
        private int orderId;
        private String orderNumber;
        private String status;
        private double totalPrice;
        private double shippingFee;
        private String orderDate;
        private CustomerInfoDTO customer;
        private AddressInfoDTO address;

        // Constructor and getters/setters
        public OrderInfoDTO(Order order) {
            this.orderId = order.getOrderId();
            this.orderNumber = order.getOrderNumber();
            this.status = order.getStatus();
            this.totalPrice = order.getTotalPrice();
            this.shippingFee = order.getShippingFee();
            this.orderDate = order.getOrderDate() != null ? order.getOrderDate().toString() : null;

            // Initialize customer info with null-safe approach
            if (order.getCustomer() != null) {
                this.customer = new CustomerInfoDTO(order.getCustomer());
            } else {
                // Provide empty customer info if not available
                this.customer = new CustomerInfoDTO();
            }

            // Initialize address info with null-safe approach
            if (order.getAddress() != null) {
                this.address = new AddressInfoDTO(order.getAddress());
            } else {
                // Provide empty address info if not available
                this.address = new AddressInfoDTO();
            }
        }

        // Getters and setters
        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public double getShippingFee() {
            return shippingFee;
        }

        public void setShippingFee(double shippingFee) {
            this.shippingFee = shippingFee;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public CustomerInfoDTO getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerInfoDTO customer) {
            this.customer = customer;
        }

        public AddressInfoDTO getAddress() {
            return address;
        }

        public void setAddress(AddressInfoDTO address) {
            this.address = address;
        }
    }

    /**
     * DTO cho thông tin khách hàng
     */
    public static class CustomerInfoDTO {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;

        // Default constructor for null-safe initialization
        public CustomerInfoDTO() {
            this.firstName = "N/A";
            this.lastName = "";
            this.email = "N/A";
            this.phone = "N/A";
        }

        public CustomerInfoDTO(viettech.entity.user.Customer customer) {
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
            this.email = customer.getEmail();
            this.phone = customer.getPhone();
        }

        // Getters and setters
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    /**
     * DTO cho thông tin địa chỉ
     */
    public static class AddressInfoDTO {
        private String street;
        private String district;
        private String city;
        private String ward;

        // Default constructor for null-safe initialization
        public AddressInfoDTO() {
            this.street = "N/A";
            this.district = "N/A";
            this.city = "N/A";
            this.ward = "";
        }

        public AddressInfoDTO(viettech.entity.Address address) {
            this.street = address.getStreet();
            this.district = address.getDistrict();
            this.city = address.getCity();
            this.ward = address.getWard();
        }

        // Getters and setters
        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getWard() {
            return ward;
        }

        public void setWard(String ward) {
            this.ward = ward;
        }
    }

    /**
     * DTO cho thông tin sản phẩm trong đơn hàng
     */
    public static class OrderItemDTO {
        private String productName;
        private int quantity;
        private double price;

        public OrderItemDTO(OrderDetail detail) {
            this.productName = detail.getProductName();
            this.quantity = detail.getQuantity();
            this.price = detail.getUnitPrice();
        }

        // Getters and setters
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTotal() {
            return quantity * price;
        }
    }

    /**
     * Lấy danh sách shipper có sẵn
     */
    public List<Shipper> getAvailableShippers() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT s FROM Shipper s WHERE s.isAvailable = true";
            TypedQuery<Shipper> query = em.createQuery(jpql, Shipper.class);
            List<Shipper> shippers = query.getResultList();
            logger.info("✓ Found {} available shippers", shippers.size());
            return shippers;
        } catch (Exception e) {
            logger.error("✗ Failed to get available shippers", e);
            throw new RuntimeException("Failed to get available shippers", e);
        } finally {
            em.close();
        }
    }
}
