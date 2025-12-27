package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.payment.Payment;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Payment DAO - Data Access Object for Payment entity
 * @author VietTech Team
 */
public class PaymentDAO {

    private static final Logger logger = LoggerFactory.getLogger(PaymentDAO.class);

    // CREATE
    public void insert(Payment payment) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(payment);
            trans.commit();
            logger.info("✓ Inserted payment: {}", payment.getTransactionId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert payment", e);
            throw new RuntimeException("Failed to insert payment", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Payment findById(int paymentId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Payment payment = em.find(Payment.class, paymentId);
            if (payment != null) {
                logger.debug("✓ Found payment by ID: {}", paymentId);
            } else {
                logger.warn("✗ Payment not found with ID: {}", paymentId);
            }
            return payment;
        } catch (Exception e) {
            logger.error("✗ Error finding payment by ID: {}", paymentId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Transaction ID
    public Payment findByTransactionId(String transactionId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.transactionId = :transactionId";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("transactionId", transactionId);
            Payment payment = query.getSingleResult();
            logger.debug("✓ Found payment by transaction ID: {}", transactionId);
            return payment;
        } catch (NoResultException e) {
            logger.debug("✗ Payment not found with transaction ID: {}", transactionId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding payment by transaction ID: {}", transactionId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Order ID
    public Payment findByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.orderId = :orderId";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("orderId", orderId);
            Payment payment = query.getSingleResult();
            logger.debug("✓ Found payment for order ID: {}", orderId);
            return payment;
        } catch (NoResultException e) {
            logger.debug("✗ Payment not found for order ID: {}", orderId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding payment by order ID: {}", orderId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Status
    public List<Payment> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.status = :status ORDER BY p.paymentDate DESC";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("status", status);
            List<Payment> payments = query.getResultList();
            logger.debug("✓ Found {} payment(s) with status: {}", payments.size(), status);
            return payments;
        } catch (Exception e) {
            logger.error("✗ Error finding payments by status: {}", status, e);
            throw new RuntimeException("Failed to find payments by status", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Payment> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Payment> payments = em.createQuery("SELECT p FROM Payment p ORDER BY p.paymentDate DESC", Payment.class).getResultList();
            logger.debug("✓ Retrieved {} payment(s)", payments.size());
            return payments;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all payments", e);
            throw new RuntimeException("Failed to retrieve payments", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Payment payment) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(payment);
            trans.commit();
            logger.info("✓ Updated payment: {}", payment.getTransactionId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update payment: {}", payment.getTransactionId(), e);
            throw new RuntimeException("Failed to update payment", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int paymentId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Payment payment = em.find(Payment.class, paymentId);
            if (payment != null) {
                em.remove(payment);
                logger.info("✓ Deleted payment ID: {}", paymentId);
            } else {
                logger.warn("✗ Cannot delete - payment not found with ID: {}", paymentId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete payment ID: {}", paymentId, e);
            throw new RuntimeException("Failed to delete payment", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Payment p", Long.class).getSingleResult();
            logger.debug("✓ Total payments: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting payments", e);
            throw new RuntimeException("Failed to count payments", e);
        } finally {
            em.close();
        }
    }
}

