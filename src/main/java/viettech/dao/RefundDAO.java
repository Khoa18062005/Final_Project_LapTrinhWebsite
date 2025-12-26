package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.payment.Refund;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Refund DAO - Data Access Object for Refund entity
 * @author VietTech Team
 */
public class RefundDAO {

    private static final Logger logger = LoggerFactory.getLogger(RefundDAO.class);

    // CREATE
    public void insert(Refund refund) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(refund);
            trans.commit();
            logger.info("✓ Inserted refund for order ID: {}", refund.getOrderId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert refund", e);
            throw new RuntimeException("Failed to insert refund", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Refund findById(int refundId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Refund refund = em.find(Refund.class, refundId);
            if (refund != null) {
                logger.debug("✓ Found refund by ID: {}", refundId);
            } else {
                logger.warn("✗ Refund not found with ID: {}", refundId);
            }
            return refund;
        } catch (Exception e) {
            logger.error("✗ Error finding refund by ID: {}", refundId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Order ID
    public List<Refund> findByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT r FROM Refund r WHERE r.orderId = :orderId ORDER BY r.requestedAt DESC";
            TypedQuery<Refund> query = em.createQuery(jpql, Refund.class);
            query.setParameter("orderId", orderId);
            List<Refund> refunds = query.getResultList();
            logger.debug("✓ Found {} refund(s) for order ID: {}", refunds.size(), orderId);
            return refunds;
        } catch (Exception e) {
            logger.error("✗ Error finding refunds by order ID: {}", orderId, e);
            throw new RuntimeException("Failed to find refunds", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Status
    public List<Refund> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT r FROM Refund r WHERE r.status = :status ORDER BY r.requestedAt DESC";
            TypedQuery<Refund> query = em.createQuery(jpql, Refund.class);
            query.setParameter("status", status);
            List<Refund> refunds = query.getResultList();
            logger.debug("✓ Found {} refund(s) with status: {}", refunds.size(), status);
            return refunds;
        } catch (Exception e) {
            logger.error("✗ Error finding refunds by status: {}", status, e);
            throw new RuntimeException("Failed to find refunds by status", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Refund> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Refund> refunds = em.createQuery("SELECT r FROM Refund r ORDER BY r.requestedAt DESC", Refund.class).getResultList();
            logger.debug("✓ Retrieved {} refund(s)", refunds.size());
            return refunds;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all refunds", e);
            throw new RuntimeException("Failed to retrieve refunds", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Refund refund) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(refund);
            trans.commit();
            logger.info("✓ Updated refund ID: {}", refund.getRefundId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update refund", e);
            throw new RuntimeException("Failed to update refund", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int refundId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Refund refund = em.find(Refund.class, refundId);
            if (refund != null) {
                em.remove(refund);
                logger.info("✓ Deleted refund ID: {}", refundId);
            } else {
                logger.warn("✗ Cannot delete - refund not found with ID: {}", refundId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete refund ID: {}", refundId, e);
            throw new RuntimeException("Failed to delete refund", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(r) FROM Refund r", Long.class).getSingleResult();
            logger.debug("✓ Total refunds: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting refunds", e);
            throw new RuntimeException("Failed to count refunds", e);
        } finally {
            em.close();
        }
    }
}

