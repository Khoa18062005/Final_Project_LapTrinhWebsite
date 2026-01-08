package viettech.service;

// VendorService - Updated:  product CRUD operations without admin approval

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
import viettech.entity.storage.Inventory;
import viettech.entity.storage.Warehouse;
import viettech.entity.Notification;
import viettech.service.NotificationService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;

public class VendorService {

    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductApprovalDAO approvalDAO = new ProductApprovalDAO();
    private final DeliveryAssignmentDAO deliveryAssignmentDAO = new DeliveryAssignmentDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
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

    // ==================== VENDOR STATISTICS API ====================

    /**
     * Get vendor statistics for the stats API endpoint.
     * Returns data directly from database.
     *
     * @param vendorId The vendor's user ID
     * @param period   Filter period: "month", "3months", "year", "all"
     * @return Map containing statistics data
     */
    public Map<String, Object> getVendorStats(int vendorId, String period) {
        EntityManager em = JPAConfig.getEntityManager();
        Map<String, Object> stats = new HashMap<>();

        try {
            logger.info("Getting vendor stats for vendor {} with period {}", vendorId, period);

            // Calculate date range based on period
            Date startDate = calculateStartDate(period);

            // 1. Total sold quantity (from completed orders)
            String soldQuery = "SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od " +
                    "JOIN Order o ON od.orderId = o.orderId " +
                    "WHERE o.vendorId = :vendorId AND LOWER(o.status) = 'completed'";
            if (startDate != null) {
                soldQuery += " AND o.orderDate >= :startDate";
            }
            TypedQuery<Long> soldQ = em.createQuery(soldQuery, Long.class)
                    .setParameter("vendorId", vendorId);
            if (startDate != null) {
                soldQ.setParameter("startDate", startDate);
            }
            Long totalSoldQuantity = soldQ.getSingleResult();
            stats.put("totalSoldQuantity", totalSoldQuantity != null ? totalSoldQuantity : 0L);

            // 2. Total product quantity (sum of Product.quantity for vendor's products)
            String productQtyQuery = "SELECT COALESCE(SUM(p.quantity), 0) FROM Product p WHERE p.vendorId = :vendorId";
            Long totalProductQuantity = em.createQuery(productQtyQuery, Long.class)
                    .setParameter("vendorId", vendorId)
                    .getSingleResult();
            stats.put("totalProductQuantity", totalProductQuantity != null ? totalProductQuantity : 0L);

            // 3. Total stock quantity (from Inventory table if exists)
            Long totalStockQuantity = 0L;
            try {
                // Inventory links to Variant, and Variant links to Product. Avoid joining on non-existent fields.
                String stockQuery = "SELECT COALESCE(SUM(i.availableQuantity), 0) " +
                        "FROM Inventory i " +
                        "JOIN i.variant v " +
                        "JOIN v.product p " +
                        "WHERE p.vendorId = :vendorId";
                totalStockQuantity = em.createQuery(stockQuery, Long.class)
                        .setParameter("vendorId", vendorId)
                        .getSingleResult();
            } catch (Exception e) {
                logger.warn("Could not query inventory stock for vendor {}", vendorId, e);
            }
            stats.put("totalStockQuantity", totalStockQuantity != null ? totalStockQuantity : 0L);

            // 4. Low stock count (products with quantity < 10)
            String lowStockCountQuery = "SELECT COUNT(p) FROM Product p WHERE p.vendorId = :vendorId AND p.quantity < 10 AND p.quantity > 0";
            Long lowStockCount = em.createQuery(lowStockCountQuery, Long.class)
                    .setParameter("vendorId", vendorId)
                    .getSingleResult();
            stats.put("lowStockCount", lowStockCount != null ? lowStockCount : 0L);

            // 5. Products quantity list (for table display)
            String productsQtyQuery = "SELECT p.name, p.quantity, p.status FROM Product p WHERE p.vendorId = :vendorId ORDER BY p.quantity ASC";
            @SuppressWarnings("unchecked")
            List<Object[]> productsQtyRows = em.createQuery(productsQtyQuery)
                    .setParameter("vendorId", vendorId)
                    .setMaxResults(20)
                    .getResultList();
            List<Map<String, Object>> productsQuantity = new ArrayList<>();
            for (Object[] row : productsQtyRows) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", row[0] != null ? row[0].toString() : "N/A");
                item.put("quantity", row[1] != null ? ((Number) row[1]).intValue() : 0);
                item.put("status", row[2] != null ? row[2].toString() : "UNKNOWN");
                productsQuantity.add(item);
            }
            stats.put("productsQuantity", productsQuantity);

            // 6. Top sold products
            String topSoldQuery = "SELECT od.productName, SUM(od.quantity) as totalQty " +
                    "FROM OrderDetail od " +
                    "JOIN Order o ON od.orderId = o.orderId " +
                    "WHERE o.vendorId = :vendorId AND LOWER(o.status) = 'completed'";
            if (startDate != null) {
                topSoldQuery += " AND o.orderDate >= :startDate";
            }
            topSoldQuery += " GROUP BY od.productName ORDER BY totalQty DESC";

            Query topSoldQ = em.createQuery(topSoldQuery);
            topSoldQ.setParameter("vendorId", vendorId);
            if (startDate != null) {
                topSoldQ.setParameter("startDate", startDate);
            }
            topSoldQ.setMaxResults(10);

            @SuppressWarnings("unchecked")
            List<Object[]> topSoldRows = topSoldQ.getResultList();
            List<Map<String, Object>> topSoldProducts = new ArrayList<>();
            for (Object[] row : topSoldRows) {
                Map<String, Object> item = new HashMap<>();
                item.put("productName", row[0] != null ? row[0].toString() : "N/A");
                item.put("soldQuantity", row[1] != null ? ((Number) row[1]).longValue() : 0L);
                topSoldProducts.add(item);
            }
            stats.put("topSoldProducts", topSoldProducts);

            // 7. Low stock products (quantity < 10)
            String lowStockQuery = "SELECT p.name, p.quantity FROM Product p " +
                    "WHERE p.vendorId = :vendorId AND p.quantity < 10 AND p.quantity > 0 " +
                    "ORDER BY p.quantity ASC";
            @SuppressWarnings("unchecked")
            List<Object[]> lowStockRows = em.createQuery(lowStockQuery)
                    .setParameter("vendorId", vendorId)
                    .setMaxResults(10)
                    .getResultList();
            List<Map<String, Object>> lowStockProducts = new ArrayList<>();
            for (Object[] row : lowStockRows) {
                Map<String, Object> item = new HashMap<>();
                item.put("productName", row[0] != null ? row[0].toString() : "N/A");
                item.put("availableQuantity", row[1] != null ? ((Number) row[1]).intValue() : 0);
                item.put("threshold", 10); // Default threshold
                lowStockProducts.add(item);
            }
            stats.put("lowStockProducts", lowStockProducts);

            // 8. Products by status count
            String statusCountQuery = "SELECT p.status, COUNT(p) FROM Product p " +
                    "WHERE p.vendorId = :vendorId GROUP BY p.status";
            @SuppressWarnings("unchecked")
            List<Object[]> statusRows = em.createQuery(statusCountQuery)
                    .setParameter("vendorId", vendorId)
                    .getResultList();
            List<Map<String, Object>> statusCounts = new ArrayList<>();
            for (Object[] row : statusRows) {
                Map<String, Object> item = new HashMap<>();
                item.put("status", row[0] != null ? row[0].toString() : "UNKNOWN");
                item.put("count", row[1] != null ? ((Number) row[1]).longValue() : 0L);
                statusCounts.add(item);
            }
            stats.put("statusCounts", statusCounts);

            logger.info("✓ Successfully retrieved vendor stats for vendor {}", vendorId);
            return stats;

        } catch (Exception e) {
            logger.error("✗ Error getting vendor stats for vendor {}", vendorId, e);
            // Return empty stats on error
            stats.put("error", e.getMessage());
            stats.put("totalSoldQuantity", 0L);
            stats.put("totalProductQuantity", 0L);
            stats.put("totalStockQuantity", 0L);
            stats.put("lowStockCount", 0L);
            stats.put("productsQuantity", new ArrayList<>());
            stats.put("topSoldProducts", new ArrayList<>());
            stats.put("lowStockProducts", new ArrayList<>());
            stats.put("statusCounts", new ArrayList<>());
            return stats;
        } finally {
            em.close();
        }
    }

    /**
     * Calculate start date based on period filter
     */
    private Date calculateStartDate(String period) {
        if (period == null || "all".equalsIgnoreCase(period)) {
            return null; // No filter
        }

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);

        switch (period.toLowerCase()) {
            case "month":
                cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                break;
            case "3months":
                cal.add(java.util.Calendar.MONTH, -3);
                break;
            case "year":
                cal.set(java.util.Calendar.DAY_OF_YEAR, 1);
                break;
            default:
                return null;
        }

        return cal.getTime();
    }

    /**
     * Get revenue by month for the last N months
     */
    private List<Map<String, Object>> getRevenueByMonth(EntityManager em, int vendorId, int months) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            // Get revenue for each of the last N months
            java.util.Calendar cal = java.util.Calendar.getInstance();

            for (int i = months - 1; i >= 0; i--) {
                java.util.Calendar monthCal = (java.util.Calendar) cal.clone();
                monthCal.add(java.util.Calendar.MONTH, -i);
                monthCal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                monthCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                monthCal.set(java.util.Calendar.MINUTE, 0);
                monthCal.set(java.util.Calendar.SECOND, 0);
                Date startOfMonth = monthCal.getTime();

                monthCal.add(java.util.Calendar.MONTH, 1);
                Date endOfMonth = monthCal.getTime();

                String query = "SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o " +
                        "WHERE o.vendorId = :vendorId AND LOWER(o.status) = 'completed' " +
                        "AND o.orderDate >= :startDate AND o.orderDate < :endDate";

                Double monthRevenue = em.createQuery(query, Double.class)
                        .setParameter("vendorId", vendorId)
                        .setParameter("startDate", startOfMonth)
                        .setParameter("endDate", endOfMonth)
                        .getSingleResult();

                // Format month label
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/yyyy");
                String monthLabel = sdf.format(startOfMonth);

                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", monthLabel);
                monthData.put("revenue", monthRevenue != null ? monthRevenue : 0.0);
                result.add(monthData);
            }
        } catch (Exception e) {
            logger.warn("Error getting revenue by month for vendor {}", vendorId, e);
        }

        return result;
    }

    /**
     * Get top selling products for vendor
     */
    private List<Map<String, Object>> getTopSellingProducts(EntityManager em, int vendorId, Date startDate, int limit) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            String query = "SELECT od.productName, SUM(od.quantity) as totalQty, SUM(od.quantity * od.unitPrice) as totalRev " +
                    "FROM OrderDetail od " +
                    "JOIN Order o ON od.orderId = o.orderId " +
                    "WHERE o.vendorId = :vendorId AND LOWER(o.status) = 'completed'";
            if (startDate != null) {
                query += " AND o.orderDate >= :startDate";
            }
            query += " GROUP BY od.productName ORDER BY totalQty DESC";

            Query q = em.createQuery(query);
            q.setParameter("vendorId", vendorId);
            if (startDate != null) {
                q.setParameter("startDate", startDate);
            }
            q.setMaxResults(limit);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();

            for (Object[] row : rows) {
                Map<String, Object> product = new HashMap<>();
                product.put("productName", row[0] != null ? row[0].toString() : "Unknown");
                product.put("quantitySold", row[1] != null ? ((Number) row[1]).longValue() : 0L);
                product.put("revenue", row[2] != null ? ((Number) row[2]).doubleValue() : 0.0);
                result.add(product);
            }
        } catch (Exception e) {
            logger.warn("Error getting top selling products for vendor {}", vendorId, e);
        }

        return result;
    }

    // ==================== PRODUCT MANAGEMENT ====================

    /**
     * Thêm sản phẩm trực tiếp vào database (không cần admin phê duyệt)
     */
    public Product addProduct(Map<String, Object> productData, int vendorId) {
        try {
            // Xác định loại sản phẩm dựa trên categoryId
            int categoryId = Integer.parseInt(String.valueOf(productData.getOrDefault("categoryId", "1")));
            Product product = createProductByCategory(categoryId);

            // Set common fields
            product.setVendorId(vendorId);
            product.setCategoryId(categoryId);
            product.setName(String.valueOf(productData.getOrDefault("name", "")));
            product.setBrand(String.valueOf(productData.getOrDefault("brand", "")));
            product.setDescription(String.valueOf(productData.getOrDefault("description", "")));
            product.setBasePrice(Double.parseDouble(String.valueOf(productData.getOrDefault("basePrice", "0"))));
            product.setStatus(String.valueOf(productData.getOrDefault("status", "AVAILABLE")));
            product.setConditions(String.valueOf(productData.getOrDefault("conditions", "New")));
            product.setSlug(generateSlug(product.getName()));
            product.setCreatedAt(new Date());
            product.setUpdatedAt(new Date());

            // Set optional fields
            if (productData.containsKey("weight")) {
                product.setWeight(Double.parseDouble(String.valueOf(productData.get("weight"))));
            }
            if (productData.containsKey("dimensions")) {
                product.setDimensions(String.valueOf(productData.get("dimensions")));
            }
            if (productData.containsKey("specifications")) {
                product.setSpecifications(String.valueOf(productData.get("specifications")));
            }

            productDAO.insert(product);
            logger.info("✓ Vendor {} added new product: {}", vendorId, product.getName());
            return product;
        } catch (Exception e) {
            logger.error("✗ Failed to add product for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to add product: " + e.getMessage(), e);
        }
    }

    /**
     * Cập nhật sản phẩm trực tiếp (không cần admin phê duyệt)
     */
    public Product updateProduct(int productId, Map<String, Object> productData, int vendorId) {
        try {
            Product product = productDAO.findById(productId);
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
            if (product.getVendorId() != vendorId) {
                throw new RuntimeException("Product does not belong to this vendor");
            }

            // Update fields
            if (productData.containsKey("name")) {
                product.setName(String.valueOf(productData.get("name")));
                product.setSlug(generateSlug(product.getName()));
            }
            if (productData.containsKey("brand")) {
                product.setBrand(String.valueOf(productData.get("brand")));
            }
            if (productData.containsKey("description")) {
                product.setDescription(String.valueOf(productData.get("description")));
            }
            if (productData.containsKey("basePrice")) {
                product.setBasePrice(Double.parseDouble(String.valueOf(productData.get("basePrice"))));
            }
            if (productData.containsKey("status")) {
                product.setStatus(String.valueOf(productData.get("status")));
            }
            if (productData.containsKey("conditions")) {
                product.setConditions(String.valueOf(productData.get("conditions")));
            }
            if (productData.containsKey("weight")) {
                product.setWeight(Double.parseDouble(String.valueOf(productData.get("weight"))));
            }
            if (productData.containsKey("dimensions")) {
                product.setDimensions(String.valueOf(productData.get("dimensions")));
            }
            if (productData.containsKey("specifications")) {
                product.setSpecifications(String.valueOf(productData.get("specifications")));
            }
            if (productData.containsKey("categoryId")) {
                product.setCategoryId(Integer.parseInt(String.valueOf(productData.get("categoryId"))));
            }
            product.setUpdatedAt(new Date());

            productDAO.update(product);
            logger.info("✓ Vendor {} updated product {}", vendorId, productId);
            return product;
        } catch (Exception e) {
            logger.error("✗ Failed to update product for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }

    /**
     * Xóa sản phẩm trực tiếp (không cần admin phê duyệt)
     */
    public void deleteProduct(int productId, int vendorId) {
        try {
            Product product = productDAO.findById(productId);
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
            if (product.getVendorId() != vendorId) {
                throw new RuntimeException("Product does not belong to this vendor");
            }

            productDAO.delete(productId);
            logger.info("✓ Vendor {} deleted product {}", vendorId, productId);
        } catch (Exception e) {
            logger.error("✗ Failed to delete product for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to delete product: " + e.getMessage(), e);
        }
    }

    /**
     * Tạo đối tượng Product dựa trên categoryId
     */
    private Product createProductByCategory(int categoryId) {
        switch (categoryId) {
            case 1: // Điện thoại
                return new viettech.entity.product.Phone();
            case 3: // Laptop
                return new viettech.entity.product.Laptop();
            case 4: // Máy tính bảng
                return new viettech.entity.product.Tablet();
            case 5: // Tai nghe
                return new viettech.entity.product.Headphone();
            default:
                return new viettech.entity.product.Phone();

        }
    }


    /**
     * Tạo slug từ tên sản phẩm
     */
    private String generateSlug(String name) {
        if (name == null) return "";
        String normalized = java.text.Normalizer.normalize(name, java.text.Normalizer.Form.NFD);
        String slug = normalized.replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
        return slug + "-" + System.currentTimeMillis();
    }


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
     * Flow: CONFIRMED → PROCESSING → READY → SHIPPING → COMPLETED
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

            final String oldStatusRaw = order.getStatus();
            final String oldStatus = oldStatusRaw == null ? "" : oldStatusRaw.trim().toUpperCase();
            final String targetStatus = newStatus == null ? "" : newStatus.trim().toUpperCase();

            if (targetStatus.isEmpty()) {
                throw new RuntimeException("Status is required");
            }

            // Define valid status transitions for vendor
            java.util.Map<String, java.util.Set<String>> validTransitions = new java.util.HashMap<>();
            validTransitions.put("CONFIRMED", java.util.Set.of("PROCESSING"));
            validTransitions.put("PROCESSING", java.util.Set.of("READY"));
            validTransitions.put("READY", java.util.Set.of()); // Vendor cannot change READY directly - shipper must accept
            validTransitions.put("SHIPPING", java.util.Set.of("COMPLETED"));

            // Check if transition is valid
            java.util.Set<String> allowedNext = validTransitions.getOrDefault(oldStatus, java.util.Set.of());

            if (!allowedNext.contains(targetStatus)) {
                if ("READY".equals(oldStatus) && "SHIPPING".equals(targetStatus)) {
                    throw new RuntimeException("Không thể chuyển trạng thái. Đơn hàng ở trạng thái READY phải đợi shipper nhận đơn.");
                }
                throw new RuntimeException("Invalid status transition: " + oldStatus + " → " + targetStatus);
            }

            // Cập nhật trạng thái
            order.setStatus(targetStatus);

            // Cập nhật các timestamp tương ứng
            if ("COMPLETED".equals(targetStatus)) {
                order.setCompletedAt(new Date());
            }

            orderDAO.update(order);

            // Send notifications based on new status
            // Notify customer about status change
            try {
                notifyCustomerOrderStatusChange(order, oldStatus, targetStatus);
            } catch (Exception ex) {
                logger.warn("Failed to notify customer for order {}", orderId, ex);
            }

            // If moved to READY -> broadcast to all available shippers
            if ("READY".equals(targetStatus) && !"READY".equals(oldStatus)) {
                try {
                    broadcastReadyToShippers(orderId);
                } catch (Exception ex) {
                    logger.warn("Failed to broadcast READY notifications for order {}", orderId, ex);
                }
            }

            // If moved to COMPLETED -> notify vendor and customer
            if ("COMPLETED".equals(targetStatus) && !"COMPLETED".equals(oldStatus)) {
                try {
                    sendOrderCompletedNotifications(order);
                } catch (Exception ex) {
                    logger.warn("Failed to send COMPLETED notifications for order {}", orderId, ex);
                }
            }

            logger.info("✓ Vendor {} updated order {} status from {} to {}",
                    vendorId, orderId, oldStatusRaw, targetStatus);
            return order;
        } catch (Exception e) {
            logger.error("✗ Failed to update order status for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    /**
     * Send notifications when order is COMPLETED.
     * Notifies both vendor and customer.
     */
    private void sendOrderCompletedNotifications(Order order) {
        NotificationService notificationService = new NotificationService();
        String orderNumber = order.getOrderNumber() != null ? order.getOrderNumber() : String.valueOf(order.getOrderId());

        // Notify VENDOR
        try {
            int vendorId = order.getVendorId();
            Notification vendorNotif = new Notification();
            vendorNotif.setUserId(vendorId);
            vendorNotif.setType("ORDER_COMPLETED");
            vendorNotif.setTitle("Đơn hàng đã hoàn thành");
            vendorNotif.setMessage("Đơn hàng #" + orderNumber + " đã được giao thành công.");
            vendorNotif.setActionUrl("/vendor?action=orders&status=COMPLETED");
            vendorNotif.setCreatedAt(new Date());
            notificationService.createNotification(vendorNotif);
            logger.info("✓ Sent COMPLETED notification to vendor {}", vendorId);
        } catch (Exception e) {
            logger.warn("Failed to send COMPLETED notification to vendor", e);
        }

        // Notify CUSTOMER
        try {
            int customerId = order.getCustomerId();
            Notification customerNotif = new Notification();
            customerNotif.setUserId(customerId);
            customerNotif.setType("ORDER_DELIVERED");
            customerNotif.setTitle("Đơn hàng đã giao thành công");
            customerNotif.setMessage("Đơn hàng #" + orderNumber + " đã được giao đến bạn. Cảm ơn bạn đã mua hàng!");
            customerNotif.setActionUrl("/profile?action=orders");
            customerNotif.setCreatedAt(new Date());
            notificationService.createNotification(customerNotif);
            logger.info("✓ Sent DELIVERED notification to customer {}", customerId);
        } catch (Exception e) {
            logger.warn("Failed to send DELIVERED notification to customer", e);
        }
    }

    /**
     * Notify all shippers when a new order is placed (called from OrderService or checkout flow)
     * This method should be called when order status changes to CONFIRMED
     */
    public void notifyNewOrder(int orderId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                logger.warn("Cannot notify new order - order {} not found", orderId);
                return;
            }

            String orderNumber = order.getOrderNumber() != null ? order.getOrderNumber() : String.valueOf(orderId);

            // Notify vendor about new order
            NotificationService notificationService = new NotificationService();
            try {
                Notification vendorNotif = new Notification();
                vendorNotif.setUserId(vendorId);
                vendorNotif.setType("NEW_ORDER");
                vendorNotif.setTitle("Đơn hàng mới");
                vendorNotif.setMessage("Bạn có đơn hàng mới #" + orderNumber + ". Vui lòng xử lý đơn hàng.");
                vendorNotif.setActionUrl("/vendor?action=orders");
                vendorNotif.setCreatedAt(new Date());
                notificationService.createNotification(vendorNotif);
                logger.info("✓ Sent NEW_ORDER notification to vendor {}", vendorId);
            } catch (Exception e) {
                logger.warn("Failed to send NEW_ORDER notification to vendor {}", vendorId, e);
            }
        } catch (Exception e) {
            logger.error("Error in notifyNewOrder for order {}", orderId, e);
        } finally {
            em.close();
        }
    }

    /**
     * Send notification to customer when order status changes
     */
    public void notifyCustomerOrderStatusChange(Order order, String oldStatus, String newStatus) {
        if (order == null) return;

        NotificationService notificationService = new NotificationService();
        String orderNumber = order.getOrderNumber() != null ? order.getOrderNumber() : String.valueOf(order.getOrderId());
        int customerId = order.getCustomerId();

        String title = "";
        String message = "";
        String type = "ORDER_UPDATE";

        switch (newStatus.toUpperCase()) {
            case "PROCESSING":
                title = "Đơn hàng đang được xử lý";
                message = "Đơn hàng #" + orderNumber + " đang được chuẩn bị.";
                type = "ORDER_PROCESSING";
                break;
            case "READY":
                title = "Đơn hàng sẵn sàng giao";
                message = "Đơn hàng #" + orderNumber + " đã sẵn sàng và đang chờ shipper nhận giao.";
                type = "ORDER_READY";
                break;
            case "SHIPPING":
                title = "Đơn hàng đang được giao";
                message = "Đơn hàng #" + orderNumber + " đang trên đường giao đến bạn.";
                type = "ORDER_SHIPPING";
                break;
            case "COMPLETED":
                title = "Đơn hàng đã giao thành công";
                message = "Đơn hàng #" + orderNumber + " đã được giao thành công. Cảm ơn bạn đã mua hàng!";
                type = "ORDER_COMPLETED";
                break;
            case "CANCELLED":
                title = "Đơn hàng đã bị hủy";
                message = "Đơn hàng #" + orderNumber + " đã bị hủy.";
                type = "ORDER_CANCELLED";
                break;
            default:
                return; // Don't send notification for unknown statuses
        }

        try {
            Notification notif = new Notification();
            notif.setUserId(customerId);
            notif.setType(type);
            notif.setTitle(title);
            notif.setMessage(message);
            notif.setActionUrl("/profile?action=orders");
            notif.setCreatedAt(new Date());
            notificationService.createNotification(notif);
            logger.info("✓ Sent {} notification to customer {} for order {}", type, customerId, order.getOrderId());
        } catch (Exception e) {
            logger.warn("Failed to send {} notification to customer {}", type, customerId, e);
        }
    }

    /**
     * Broadcast delivery request to all available shippers
     * Called when order status changes to READY
     */
    private void broadcastReadyToShippers(int orderId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Fetch order with address eagerly
            Order order = em.createQuery(
                    "SELECT o FROM Order o LEFT JOIN FETCH o.address WHERE o.orderId = :orderId",
                    Order.class)
                    .setParameter("orderId", orderId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            String orderNumber = (order != null && order.getOrderNumber() != null)
                                 ? order.getOrderNumber()
                                 : String.valueOf(orderId);

            // Get delivery address for context
            String addressInfo = "";
            if (order != null && order.getAddress() != null) {
                addressInfo = " - Giao đến: " + order.getAddress().getDistrict() + ", " + order.getAddress().getCity();
            }

            String actionUrl = "/shipper?focus=assignment&orderId=" + orderId;
            List<Shipper> shippers = getAvailableShippers();
            NotificationService notificationService = new NotificationService();

            logger.info("Broadcasting READY notification for order {} to {} available shippers", orderId, shippers.size());

            int sentCount = 0;
            for (Shipper s : shippers) {
                try {
                    Notification n = new Notification();
                    n.setUserId(s.getUserId());
                    n.setType("DELIVERY_READY");
                    n.setTitle("Có đơn hàng mới cần giao");
                    n.setMessage("Đơn hàng #" + orderNumber + " đang chờ shipper nhận" + addressInfo + ". Nhấn để xem chi tiết.");
                    n.setActionUrl(actionUrl);
                    n.setCreatedAt(new Date());
                    notificationService.createNotification(n);
                    sentCount++;
                } catch (Exception e) {
                    logger.warn("Failed to send notification to shipper {}", s.getUserId(), e);
                }
            }

            logger.info("✓ Broadcasted READY notification for order {} to {}/{} shippers", orderId, sentCount, shippers.size());
        } catch (Exception e) {
            logger.error("✗ Failed to broadcast READY notifications for order {}", orderId, e);
            throw new RuntimeException("Failed to broadcast notifications: " + e.getMessage(), e);
        } finally {
            em.close();
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
     * Lấy danh sách đơn hàng trong shipping flow với thông tin chi tiết
     * Includes: PROCESSING (waiting to be sent to shipper), READY (waiting shipper acceptance), SHIPPING (in transit)
     */
    public List<Order> getOrdersReadyForShipping(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {

            // Shipping management should list orders in the shipping flow:
            // - PROCESSING: waiting for vendor to send to shipper
            // - READY: sent to shippers, waiting for acceptance
            // - SHIPPING: shipper has accepted, in transit
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "LEFT JOIN FETCH o.delivery d " +
                    "WHERE o.vendorId = :vendorId " +
                    "AND (LOWER(o.status) = 'processing' OR LOWER(o.status) = 'ready' OR LOWER(o.status) = 'shipping') " +
                    "ORDER BY o.orderDate DESC";

            List<Order> orders = em.createQuery(jpql, Order.class)
                    .setParameter("vendorId", vendorId)
                    .getResultList();

            for (Order o : orders) {
                // Fill orderDetails (if mapped) - best-effort; ignore if model doesn't have setter.
                List<OrderDetail> details = em.createQuery(
                                "SELECT od FROM OrderDetail od WHERE od.orderId = :orderId",
                                OrderDetail.class)
                        .setParameter("orderId", o.getOrderId())
                        .getResultList();
                try {
                    o.setOrderDetails(details);
                } catch (Exception ignore) {
                    // Order entity might not expose setOrderDetails; JSP can still use details via separate API.
                }

                // Initialize shipper assignments (if delivery exists)
                try {
                    Integer deliveryId = em.createQuery(
                                    "SELECT d.deliveryId FROM viettech.entity.delivery.Delivery d WHERE d.orderId = :orderId",
                                    Integer.class)
                            .setParameter("orderId", o.getOrderId())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    if (deliveryId != null) {
                        List<DeliveryAssignment> assignments = em.createQuery(
                                        "SELECT da FROM viettech.entity.delivery.DeliveryAssignment da " +
                                                "LEFT JOIN FETCH da.shipper s " +
                                                "WHERE da.deliveryId = :deliveryId",
                                        DeliveryAssignment.class)
                                .setParameter("deliveryId", deliveryId)
                                .getResultList();
                        // best-effort attach to delivery if mapping exists
                        if (o.getDelivery() != null) {
                            try {
                                o.getDelivery().setAssignments(assignments);
                            } catch (Exception ignore) {
                                // no setter
                            }
                        }
                    }
                } catch (Exception ex) {
                    logger.warn("Could not initialize delivery assignments for order {}", o.getOrderId(), ex);
                }
            }

            logger.info("✓ Loaded {} orders (PROCESSING/READY/SHIPPING) for shipping for vendor {}", orders.size(), vendorId);
            return orders;
        } catch (Exception ex) {
            logger.error("✗ Failed to get orders (PROCESSING/READY/SHIPPING) for vendor {}", vendorId, ex);
            throw new RuntimeException("Failed to get orders ready for shipping: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    /**
     * Get shipping summary statistics for vendor.
     * Returns a Map with:
     * - processingOrders: Count of orders with status = PROCESSING (waiting to be sent to shipper)
     * - ordersToDeliver: Count of orders with status = READY (waiting shipper acceptance)
     * - shippingOrders: Count of orders with status = SHIPPING (in transit, shipper accepted)
     */
    public Map<String, Long> getShippingSummary(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        Map<String, Long> summary = new HashMap<>();

        try {
            // Count orders with status = PROCESSING (waiting to be sent to shipper)
            String processingCountQuery = "SELECT COUNT(o) FROM Order o WHERE o.vendorId = :vendorId AND LOWER(o.status) = 'processing'";
            Long processingOrders = em.createQuery(processingCountQuery, Long.class)
                    .setParameter("vendorId", vendorId)
                    .getSingleResult();
            summary.put("processingOrders", processingOrders != null ? processingOrders : 0L);

            // Count orders with status = READY (waiting for shipper to accept)
            String readyCountQuery = "SELECT COUNT(o) FROM Order o WHERE o.vendorId = :vendorId AND LOWER(o.status) = 'ready'";
            Long ordersToDeliver = em.createQuery(readyCountQuery, Long.class)
                    .setParameter("vendorId", vendorId)
                    .getSingleResult();
            summary.put("ordersToDeliver", ordersToDeliver != null ? ordersToDeliver : 0L);

            // Count orders with status = SHIPPING (shipper has accepted, in transit)
            String shippingCountQuery = "SELECT COUNT(o) FROM Order o WHERE o.vendorId = :vendorId AND LOWER(o.status) = 'shipping'";
            Long shippingOrders = em.createQuery(shippingCountQuery, Long.class)
                    .setParameter("vendorId", vendorId)
                    .getSingleResult();
            summary.put("shippingOrders", shippingOrders != null ? shippingOrders : 0L);

            // For backward compatibility
            summary.put("assignedShipper", shippingOrders != null ? shippingOrders : 0L);
            summary.put("unassignedShipper", ordersToDeliver != null ? ordersToDeliver : 0L);

            logger.info("✓ Shipping summary for vendor {}: processingOrders={}, ordersToDeliver={}, shippingOrders={}",
                    vendorId, processingOrders, ordersToDeliver, shippingOrders);

            return summary;
        } catch (Exception ex) {
            logger.error("✗ Failed to get shipping summary for vendor {}", vendorId, ex);
            // Return default values on error
            summary.put("processingOrders", 0L);
            summary.put("ordersToDeliver", 0L);
            summary.put("shippingOrders", 0L);
            summary.put("assignedShipper", 0L);
            summary.put("unassignedShipper", 0L);
            return summary;
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
            // Only shippers (roleID=3) that are currently available
            String jpql = "SELECT s FROM Shipper s WHERE s.roleID = 3 AND s.isAvailable = true";
            TypedQuery<Shipper> query = em.createQuery(jpql, Shipper.class);
            List<Shipper> shippers = query.getResultList();
            logger.info("✓ Found {} available shippers (roleID=3)", shippers.size());
            return shippers;
        } catch (Exception e) {
            logger.error("✗ Failed to get available shippers", e);
            throw new RuntimeException("Failed to get available shippers", e);
        } finally {
            em.close();
        }
    }

    // ==================== BROADCAST DELIVERY (Vendor -> Shippers) ====================

    /**
     * Vendor bấm nút "Gửi cho shipper" ở trang giao hàng.
     * - ONLY allowed when order status = PROCESSING or READY
     * - Updates order status to READY (if PROCESSING)
     * - Broadcasts notification to all available shippers
     */
    public void broadcastDeliveryRequest(int orderId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = null;
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                throw new RuntimeException("Đơn hàng không tồn tại");
            }
            if (order.getVendorId() != vendorId) {
                throw new RuntimeException("Đơn hàng không thuộc về vendor này");
            }

            String currentStatus = order.getStatus() != null ? order.getStatus().trim().toUpperCase() : "";
            logger.info("BroadcastDeliveryRequest: Order {} current status: {}", orderId, currentStatus);

            // Allow broadcasting if status is PROCESSING or READY
            if ("PROCESSING".equals(currentStatus)) {
                // Update order status to READY
                tx = em.getTransaction();
                tx.begin();
                order.setStatus("READY");
                em.merge(order);
                tx.commit();
                tx = null; // Reset to avoid double rollback
                logger.info("✓ Order {} status updated from PROCESSING to READY", orderId);
            } else if ("READY".equals(currentStatus)) {
                // Already READY, just broadcast again
                logger.info("✓ Order {} already READY, broadcasting again", orderId);
            } else {
                throw new RuntimeException("Chỉ có thể gửi thông báo cho shipper khi đơn hàng đang ở trạng thái PROCESSING hoặc READY. Trạng thái hiện tại: " + order.getStatus());
            }

            // Broadcast to all available shippers
            broadcastReadyToShippers(orderId);
            logger.info("✓ Broadcasted delivery request for order {} to all available shippers", orderId);
        } catch (Exception e) {
            logger.error("✗ Failed to broadcast delivery request for order {}", orderId, e);
            throw new RuntimeException("Không thể gửi yêu cầu giao hàng: " + e.getMessage(), e);
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            if (em.isOpen()) em.close();
        }
    }

    // ==================== SHIPPER ACCEPT (DB handles transaction/lock/validation) ====================

    /**
     * Shipper nhận đơn hàng.
     * - Database đã xử lý transaction/lock/validation.
     * - Service chỉ gọi DB và trả kết quả.
     * - KHÔNG kiểm tra status đơn hàng trong Service.
     * - Sends notification to vendor when shipper accepts
     *
     * @param assignmentId delivery_assignments.assignment_id
     * @param shipperId    shipper user_id
     * @return true nếu nhận thành công
     */
    public boolean assignOrder(int assignmentId, int shipperId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // DB should validate that shipper is available, assignment is valid, etc.
            int updated = em.createNativeQuery(
                            "UPDATE delivery_assignments SET status = 'Accepted', accepted_at = NOW() " +
                                    "WHERE assignment_id = ? AND shipper_id = ?")
                    .setParameter(1, assignmentId)
                    .setParameter(2, shipperId)
                    .executeUpdate();

            // Lookup orderId and vendorId to update order status + send notifications
            Integer orderId = null;
            Integer vendorId = null;
            String orderNumber = null;
            try {
                Object[] row = (Object[]) em.createNativeQuery(
                                "SELECT d.order_id, o.vendor_id, o.order_number FROM deliveries d " +
                                        "JOIN orders o ON o.order_id = d.order_id " +
                                        "JOIN delivery_assignments da ON da.delivery_id = d.delivery_id " +
                                        "WHERE da.assignment_id = ?")
                        .setParameter(1, assignmentId)
                        .setMaxResults(1)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if (row != null) {
                    orderId = row[0] != null ? ((Number) row[0]).intValue() : null;
                    vendorId = row[1] != null ? ((Number) row[1]).intValue() : null;
                    orderNumber = row[2] != null ? row[2].toString() : null;
                }
            } catch (Exception ignored) {
                logger.warn("Could not fetch order details for assignment {}", assignmentId);
            }

            if (updated > 0 && orderId != null) {
                // Change order status to SHIPPING when a shipper accepts
                try {
                    em.createNativeQuery("UPDATE orders SET status = 'SHIPPING' WHERE order_id = ?")
                            .setParameter(1, orderId)
                            .executeUpdate();
                } catch (Exception ignored) {
                }
            }

            tx.commit();

            if (updated <= 0) {
                return false;
            }

            // After commit: cleanup broadcast notifications so other shippers don't see the same order
            if (orderId != null) {
                try {
                    String actionUrl = "/shipper?focus=assignment&orderId=" + orderId;
                    new NotificationDAO().deleteByTypeAndActionUrl("DELIVERY_READY", actionUrl);
                } catch (Exception ignored) {
                }
            }

            // Get shipper info for notification
            String shipperName = "Shipper";
            try {
                Shipper shipper = em.find(Shipper.class, shipperId);
                if (shipper != null) {
                    shipperName = (shipper.getFirstName() != null ? shipper.getFirstName() : "") + " " +
                                  (shipper.getLastName() != null ? shipper.getLastName() : "");
                    shipperName = shipperName.trim();
                    if (shipperName.isEmpty()) shipperName = "Shipper #" + shipperId;
                }
            } catch (Exception ignored) {
            }

            NotificationService notificationService = new NotificationService();

            // Send notification to VENDOR: Shipper has accepted the order
            if (vendorId != null) {
                try {
                    Notification vendorNotif = new Notification();
                    vendorNotif.setUserId(vendorId);
                    vendorNotif.setType("ORDER_SHIPPING");
                    vendorNotif.setTitle("Shipper đã nhận đơn hàng");
                    vendorNotif.setMessage("Đơn hàng #" + (orderNumber != null ? orderNumber : orderId) +
                                          " đã được " + shipperName + " nhận. Đang giao hàng.");
                    vendorNotif.setActionUrl("/vendor?action=shipping");
                    vendorNotif.setCreatedAt(new Date());
                    notificationService.createNotification(vendorNotif);
                    logger.info("✓ Sent notification to vendor {} for order {}", vendorId, orderId);
                } catch (Exception e) {
                    logger.warn("Failed to send notification to vendor {}", vendorId, e);
                }
            }

            // Send confirmation notification to SHIPPER
            try {
                Notification shipperNotif = new Notification();
                shipperNotif.setUserId(shipperId);
                shipperNotif.setType("DELIVERY_ACCEPTED");
                shipperNotif.setTitle("Nhận đơn thành công");
                shipperNotif.setMessage("Bạn đã nhận đơn hàng #" + (orderNumber != null ? orderNumber : orderId) + ". Vui lòng giao hàng.");
                shipperNotif.setActionUrl("/shipper");
                shipperNotif.setCreatedAt(new Date());
                notificationService.createNotification(shipperNotif);
                logger.info("✓ Sent confirmation notification to shipper {}", shipperId);
            } catch (Exception e) {
                logger.warn("Failed to send confirmation to shipper {}", shipperId, e);
            }

            return true;
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * Status priority for vendor order management
     * Lower number = higher priority (shown first)
     */
    private static final Map<String, Integer> STATUS_PRIORITY = new HashMap<>();
    static {
        STATUS_PRIORITY.put("CONFIRMED", 1);
        STATUS_PRIORITY.put("PROCESSING", 2);
        STATUS_PRIORITY.put("READY", 3);
        STATUS_PRIORITY.put("SHIPPING", 4);
        STATUS_PRIORITY.put("COMPLETED", 5);
    }

    /**
     * Get status priority for sorting (COMPLETED always last)
     */
    private int getStatusPriority(String status) {
        if (status == null) return 99;
        String upper = status.trim().toUpperCase();
        return STATUS_PRIORITY.getOrDefault(upper, 99);
    }

    /**
     * Sort orders by status priority, then by date within same status
     */
    public List<Order> sortOrdersByStatusPriority(List<Order> orders) {
        if (orders == null) return new ArrayList<>();

        return orders.stream()
            .sorted(Comparator
                .comparingInt((Order o) -> getStatusPriority(o.getStatus()))
                .thenComparing((Order o) -> o.getOrderDate() != null ? o.getOrderDate() : new Date(0), Comparator.reverseOrder()))
            .toList();
    }

    /**
     * Get all orders for vendor sorted by status priority
     */
    public List<Order> getAllOrdersSortedByStatus(int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "WHERE o.vendorId = :vendorId";

            List<Order> orders = em.createQuery(jpql, Order.class)
                    .setParameter("vendorId", vendorId)
                    .getResultList();

            return sortOrdersByStatusPriority(orders);
        } catch (Exception e) {
            logger.error("Failed to get all orders for vendor {}", vendorId, e);
            throw new RuntimeException("Failed to get orders", e);
        } finally {
            em.close();
        }
    }

    /**
     * Get complete order details for modal view
     */
    public Map<String, Object> getCompleteOrderDetails(int orderId, int vendorId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Fetch order with customer and address
            Order order = em.createQuery(
                    "SELECT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "WHERE o.orderId = :orderId AND o.vendorId = :vendorId", Order.class)
                    .setParameter("orderId", orderId)
                    .setParameter("vendorId", vendorId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (order == null) {
                return null;
            }

            // Fetch order details with products
            List<OrderDetail> orderDetails = em.createQuery(
                    "SELECT od FROM OrderDetail od " +
                    "LEFT JOIN FETCH od.product p " +
                    "WHERE od.orderId = :orderId", OrderDetail.class)
                    .setParameter("orderId", orderId)
                    .getResultList();

            // Build response map
            Map<String, Object> result = new HashMap<>();

            // Order info
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("orderId", order.getOrderId());
            orderInfo.put("orderNumber", order.getOrderNumber());
            orderInfo.put("status", order.getStatus());
            orderInfo.put("totalPrice", order.getTotalPrice());
            orderInfo.put("shippingFee", order.getShippingFee());
            orderInfo.put("orderDate", order.getOrderDate());
            orderInfo.put("notes", order.getNotes());
            result.put("order", orderInfo);

            // Customer info
            Map<String, Object> customerInfo = new HashMap<>();
            if (order.getCustomer() != null) {
                customerInfo.put("firstName", order.getCustomer().getFirstName());
                customerInfo.put("lastName", order.getCustomer().getLastName());
                customerInfo.put("email", order.getCustomer().getEmail());
                customerInfo.put("phone", order.getCustomer().getPhone());
            }
            result.put("customer", customerInfo);

            // Address info
            Map<String, Object> addressInfo = new HashMap<>();
            if (order.getAddress() != null) {
                addressInfo.put("street", order.getAddress().getStreet());
                addressInfo.put("ward", order.getAddress().getWard());
                addressInfo.put("district", order.getAddress().getDistrict());
                addressInfo.put("city", order.getAddress().getCity());
            }
            result.put("address", addressInfo);

            // Order items
            List<Map<String, Object>> items = new ArrayList<>();
            for (OrderDetail detail : orderDetails) {
                Map<String, Object> item = new HashMap<>();
                item.put("productName", detail.getProductName());
                item.put("quantity", detail.getQuantity());
                item.put("unitPrice", detail.getUnitPrice());
                item.put("subtotal", detail.getQuantity() * detail.getUnitPrice());
                items.add(item);
            }
            result.put("orderItems", items);

            return result;
        } catch (Exception e) {
            logger.error("Failed to get complete order details for order {} vendor {}", orderId, vendorId, e);
            throw new RuntimeException("Failed to get order details", e);
        } finally {
            em.close();
        }
    }

    /**
     * Check if order has an assigned/accepted shipper
     */
    public boolean hasAssignedShipper(int orderId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Check if there's a delivery assignment with status ASSIGNED or Accepted
            String jpql = "SELECT COUNT(da) FROM viettech.entity.delivery.DeliveryAssignment da " +
                    "JOIN viettech.entity.delivery.Delivery d ON da.deliveryId = d.deliveryId " +
                    "WHERE d.orderId = :orderId " +
                    "AND (da.status = 'ASSIGNED' OR da.status = 'Accepted' OR da.status = 'ACCEPTED')";

            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();

            return count != null && count > 0;
        } catch (Exception e) {
            logger.warn("Error checking shipper assignment for order {}", orderId, e);
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Get shipper assignment status for an order
     * Returns: "ASSIGNED", "PENDING", "NONE"
     */
    public String getShipperAssignmentStatus(int orderId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT da.status FROM viettech.entity.delivery.DeliveryAssignment da " +
                    "JOIN viettech.entity.delivery.Delivery d ON da.deliveryId = d.deliveryId " +
                    "WHERE d.orderId = :orderId " +
                    "ORDER BY da.assignedAt DESC";

            List<String> statuses = em.createQuery(jpql, String.class)
                    .setParameter("orderId", orderId)
                    .setMaxResults(1)
                    .getResultList();

            if (statuses.isEmpty()) {
                return "NONE";
            }

            String status = statuses.get(0);
            if ("ASSIGNED".equalsIgnoreCase(status) || "Accepted".equalsIgnoreCase(status)) {
                return "ASSIGNED";
            } else if ("PENDING".equalsIgnoreCase(status) || "Broadcasted".equalsIgnoreCase(status)) {
                return "PENDING";
            }

            return "NONE";
        } catch (Exception e) {
            logger.warn("Error getting shipper assignment status for order {}", orderId, e);
            return "NONE";
        } finally {
            em.close();
        }
    }
}
