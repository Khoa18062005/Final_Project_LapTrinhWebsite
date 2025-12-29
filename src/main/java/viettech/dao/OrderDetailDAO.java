package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.order.OrderDetail;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDetail DAO - Data Access Object for OrderDetail entity
 * @author VietTech Team
 */
public class OrderDetailDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrderDetailDAO.class);

    // ==========================================
    // BASIC CRUD OPERATIONS
    // ==========================================

    // CREATE
    public void insert(OrderDetail orderDetail) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(orderDetail);
            trans.commit();
            logger.info("✓ Inserted order detail for order ID: {}", orderDetail.getOrderId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert order detail", e);
            throw new RuntimeException("Failed to insert order detail", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public OrderDetail findById(int orderDetailId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            OrderDetail detail = em.find(OrderDetail.class, orderDetailId);
            if (detail != null) {
                logger.debug("✓ Found order detail by ID: {}", orderDetailId);
            } else {
                logger.warn("✗ Order detail not found with ID: {}", orderDetailId);
            }
            return detail;
        } catch (Exception e) {
            logger.error("✗ Error finding order detail by ID: {}", orderDetailId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Order ID
    public List<OrderDetail> findByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT od FROM OrderDetail od WHERE od.orderId = :orderId";
            TypedQuery<OrderDetail> query = em.createQuery(jpql, OrderDetail.class);
            query.setParameter("orderId", orderId);
            List<OrderDetail> details = query.getResultList();
            logger.debug("✓ Found {} detail(s) for order ID: {}", details.size(), orderId);
            return details;
        } catch (Exception e) {
            logger.error("✗ Error finding details by order ID: {}", orderId, e);
            throw new RuntimeException("Failed to find order details", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<OrderDetail> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<OrderDetail> details = em.createQuery("SELECT od FROM OrderDetail od", OrderDetail.class).getResultList();
            logger.debug("✓ Retrieved {} order detail(s)", details.size());
            return details;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all order details", e);
            throw new RuntimeException("Failed to retrieve order details", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(OrderDetail orderDetail) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(orderDetail);
            trans.commit();
            logger.info("✓ Updated order detail ID: {}", orderDetail.getOrderDetailId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update order detail", e);
            throw new RuntimeException("Failed to update order detail", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int orderDetailId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            OrderDetail detail = em.find(OrderDetail.class, orderDetailId);
            if (detail != null) {
                em.remove(detail);
                logger.info("✓ Deleted order detail ID: {}", orderDetailId);
            } else {
                logger.warn("✗ Cannot delete - order detail not found with ID: {}", orderDetailId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete order detail ID: {}", orderDetailId, e);
            throw new RuntimeException("Failed to delete order detail", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa tất cả details của order
    public void deleteByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM OrderDetail od WHERE od.orderId = :orderId";
            int deleted = em.createQuery(jpql).setParameter("orderId", orderId).executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} detail(s) for order ID: {}", deleted, orderId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete details for order ID: {}", orderId, e);
            throw new RuntimeException("Failed to delete order details", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(od) FROM OrderDetail od", Long.class).getSingleResult();
            logger.debug("✓ Total order details: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting order details", e);
            throw new RuntimeException("Failed to count order details", e);
        } finally {
            em.close();
        }
    }

    // ==========================================
    // STATISTICS METHODS (ADDED TO FIX LAZY LOADING)
    // ==========================================

    /**
     * Thống kê số lượng bán theo danh mục (Category)
     * Query: OrderDetail -> Variant -> Product -> Category
     * @return List<Object[]> : [String categoryName, Long totalQuantity]
     */
    public List<Object[]> getCategoryStatistics() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            // SQL Gốc của bạn:
            // SELECT c.name, SUM(od.quantity) FROM order_details od ...

            // JPQL Tương ứng (Lưu ý Case Sensitive tên Class và tên Biến):
            String jpql = "SELECT c.name, SUM(od.quantity) " +
                    "FROM OrderDetail od " +      // Class OrderDetail
                    "JOIN od.variant v " +        // Biến variant trong OrderDetail
                    "JOIN v.product p " +         // Biến product trong Variant
                    "JOIN p.category c " +        // Biến category trong Product
                    "GROUP BY c.name";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            List<Object[]> results = query.getResultList();

            logger.info("✓ Retrieved category statistics. Rows: {}", results.size());
            return results;
        } catch (Exception e) {
            logger.error("✗ Error calculating category statistics", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Thống kê Top sản phẩm bán chạy nhất
     * Query: OrderDetail -> Variant -> Product
     * @param limit Số lượng sản phẩm muốn lấy (ví dụ: Top 5)
     * @return List<Object[]> : [String productName, Long totalQuantity, Double totalRevenue]
     */
    public List<Object[]> getTopSellingProducts(int limit) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            // Lưu ý: od.unitPrice phải đúng tên biến trong Entity OrderDetail (thường là unitPrice chứ không phải unit_price)
            String jpql = "SELECT p.name, SUM(od.quantity), SUM(od.quantity * od.unitPrice) " +
                    "FROM OrderDetail od " +
                    "JOIN od.variant v " +
                    "JOIN v.product p " +
                    "GROUP BY p.name " +
                    "ORDER BY SUM(od.quantity) DESC";

            return em.createQuery(jpql, Object[].class)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            logger.error("✗ Error calculating top products", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}