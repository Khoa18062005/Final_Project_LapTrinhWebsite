package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.transaction.TransactionHistory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * TransactionHistory DAO - Data Access Object for TransactionHistory entity
 * @author VietTech Team
 */
public class TransactionHistoryDAO {

    private static final Logger logger = LoggerFactory.getLogger(TransactionHistoryDAO.class);

    // CREATE
    public void insert(TransactionHistory history) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(history);
            trans.commit();
            logger.info("✓ Inserted new transaction history: {}", history.getAction());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert transaction history", e);
            throw new RuntimeException("Failed to insert transaction history", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public TransactionHistory findById(int historyId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            TransactionHistory history = em.find(TransactionHistory.class, historyId);
            if (history != null) {
                logger.debug("✓ Found transaction history by ID: {}", historyId);
            } else {
                logger.warn("✗ Transaction history not found with ID: {}", historyId);
            }
            return history;
        } catch (Exception e) {
            logger.error("✗ Error finding transaction history by ID: {}", historyId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Entity Type và Entity ID
    public List<TransactionHistory> findByEntity(String entityType, String entityId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT th FROM TransactionHistory th WHERE th.entityType = :entityType AND th.entityId = :entityId ORDER BY th.timestamp DESC";
            TypedQuery<TransactionHistory> query = em.createQuery(jpql, TransactionHistory.class);
            query.setParameter("entityType", entityType);
            query.setParameter("entityId", entityId);
            List<TransactionHistory> histories = query.getResultList();
            logger.debug("✓ Found {} history(ies) for {} {}", histories.size(), entityType, entityId);
            return histories;
        } catch (Exception e) {
            logger.error("✗ Error finding histories by entity", e);
            throw new RuntimeException("Failed to find transaction histories by entity", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Action
    public List<TransactionHistory> findByAction(String action) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT th FROM TransactionHistory th WHERE th.action = :action ORDER BY th.timestamp DESC";
            TypedQuery<TransactionHistory> query = em.createQuery(jpql, TransactionHistory.class);
            query.setParameter("action", action);
            List<TransactionHistory> histories = query.getResultList();
            logger.debug("✓ Found {} history(ies) with action: {}", histories.size(), action);
            return histories;
        } catch (Exception e) {
            logger.error("✗ Error finding histories by action: {}", action, e);
            throw new RuntimeException("Failed to find transaction histories by action", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Performed By
    public List<TransactionHistory> findByPerformedBy(String performedBy) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT th FROM TransactionHistory th WHERE th.performedBy = :performedBy ORDER BY th.timestamp DESC";
            TypedQuery<TransactionHistory> query = em.createQuery(jpql, TransactionHistory.class);
            query.setParameter("performedBy", performedBy);
            List<TransactionHistory> histories = query.getResultList();
            logger.debug("✓ Found {} history(ies) performed by: {}", histories.size(), performedBy);
            return histories;
        } catch (Exception e) {
            logger.error("✗ Error finding histories by performer: {}", performedBy, e);
            throw new RuntimeException("Failed to find transaction histories by performer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng thời gian
    public List<TransactionHistory> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT th FROM TransactionHistory th WHERE th.timestamp BETWEEN :startDate AND :endDate ORDER BY th.timestamp DESC";
            TypedQuery<TransactionHistory> query = em.createQuery(jpql, TransactionHistory.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<TransactionHistory> histories = query.getResultList();
            logger.debug("✓ Found {} history(ies) in date range", histories.size());
            return histories;
        } catch (Exception e) {
            logger.error("✗ Error finding histories by date range", e);
            throw new RuntimeException("Failed to find transaction histories by date range", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<TransactionHistory> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<TransactionHistory> histories = em.createQuery("SELECT th FROM TransactionHistory th ORDER BY th.timestamp DESC", TransactionHistory.class).getResultList();
            logger.debug("✓ Retrieved {} transaction history(ies)", histories.size());
            return histories;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all transaction histories", e);
            throw new RuntimeException("Failed to retrieve transaction histories", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(TransactionHistory history) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(history);
            trans.commit();
            logger.info("✓ Updated transaction history ID: {}", history.getHistoryId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update transaction history ID: {}", history.getHistoryId(), e);
            throw new RuntimeException("Failed to update transaction history", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int historyId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            TransactionHistory history = em.find(TransactionHistory.class, historyId);
            if (history != null) {
                em.remove(history);
                logger.info("✓ Deleted transaction history ID: {}", historyId);
            } else {
                logger.warn("✗ Cannot delete - transaction history not found with ID: {}", historyId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete transaction history ID: {}", historyId, e);
            throw new RuntimeException("Failed to delete transaction history", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(th) FROM TransactionHistory th", Long.class).getSingleResult();
            logger.debug("✓ Total transaction histories count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting transaction histories", e);
            throw new RuntimeException("Failed to count transaction histories", e);
        } finally {
            em.close();
        }
    }
}

