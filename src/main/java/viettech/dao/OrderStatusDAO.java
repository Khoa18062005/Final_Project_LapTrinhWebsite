package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.order.OrderStatus;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * OrderStatus DAO - Data Access Object for OrderStatus entity
 * @author VietTech Team
 */
public class OrderStatusDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusDAO.class);

    // CREATE
    public void insert(OrderStatus orderStatus) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(orderStatus);
            trans.commit();
            logger.info("✓ Inserted order status for order ID: {}", orderStatus.getOrderId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert order status", e);
            throw new RuntimeException("Failed to insert order status", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public OrderStatus findById(int statusId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            OrderStatus status = em.find(OrderStatus.class, statusId);
            if (status != null) {
                logger.debug("✓ Found order status by ID: {}", statusId);
            } else {
                logger.warn("✗ Order status not found with ID: {}", statusId);
            }
            return status;
        } catch (Exception e) {
            logger.error("✗ Error finding order status by ID: {}", statusId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Order ID
    public List<OrderStatus> findByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT os FROM OrderStatus os WHERE os.orderId = :orderId ORDER BY os.changedAt DESC";
            TypedQuery<OrderStatus> query = em.createQuery(jpql, OrderStatus.class);
            query.setParameter("orderId", orderId);
            List<OrderStatus> statuses = query.getResultList();
            logger.debug("✓ Found {} status(es) for order ID: {}", statuses.size(), orderId);
            return statuses;
        } catch (Exception e) {
            logger.error("✗ Error finding statuses by order ID: {}", orderId, e);
            throw new RuntimeException("Failed to find order statuses", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy status mới nhất của order
    public OrderStatus findLatestByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT os FROM OrderStatus os WHERE os.orderId = :orderId ORDER BY os.changedAt DESC";
            TypedQuery<OrderStatus> query = em.createQuery(jpql, OrderStatus.class);
            query.setParameter("orderId", orderId);
            query.setMaxResults(1);
            List<OrderStatus> statuses = query.getResultList();
            if (!statuses.isEmpty()) {
                logger.debug("✓ Found latest status for order ID: {}", orderId);
                return statuses.get(0);
            }
            logger.debug("✗ No status found for order ID: {}", orderId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding latest status for order ID: {}", orderId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<OrderStatus> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<OrderStatus> statuses = em.createQuery("SELECT os FROM OrderStatus os ORDER BY os.changedAt DESC", OrderStatus.class).getResultList();
            logger.debug("✓ Retrieved {} order status(es)", statuses.size());
            return statuses;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all order statuses", e);
            throw new RuntimeException("Failed to retrieve order statuses", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(OrderStatus orderStatus) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(orderStatus);
            trans.commit();
            logger.info("✓ Updated order status ID: {}", orderStatus.getStatusId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update order status", e);
            throw new RuntimeException("Failed to update order status", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int statusId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            OrderStatus status = em.find(OrderStatus.class, statusId);
            if (status != null) {
                em.remove(status);
                logger.info("✓ Deleted order status ID: {}", statusId);
            } else {
                logger.warn("✗ Cannot delete - order status not found with ID: {}", statusId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete order status ID: {}", statusId, e);
            throw new RuntimeException("Failed to delete order status", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(os) FROM OrderStatus os", Long.class).getSingleResult();
            logger.debug("✓ Total order statuses: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting order statuses", e);
            throw new RuntimeException("Failed to count order statuses", e);
        } finally {
            em.close();
        }
    }
}

