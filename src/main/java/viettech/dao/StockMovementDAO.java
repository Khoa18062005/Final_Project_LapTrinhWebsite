package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.storage.StockMovement;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * StockMovement DAO - Data Access Object for StockMovement entity
 * @author VietTech Team
 */
public class StockMovementDAO {

    private static final Logger logger = LoggerFactory.getLogger(StockMovementDAO.class);

    // CREATE
    public void insert(StockMovement movement) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(movement);
            trans.commit();
            logger.info("✓ Inserted new stock movement: {}", movement.getType());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert stock movement", e);
            throw new RuntimeException("Failed to insert stock movement", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public StockMovement findById(int movementId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            StockMovement movement = em.find(StockMovement.class, movementId);
            if (movement != null) {
                logger.debug("✓ Found stock movement by ID: {}", movementId);
            } else {
                logger.warn("✗ Stock movement not found with ID: {}", movementId);
            }
            return movement;
        } catch (Exception e) {
            logger.error("✗ Error finding stock movement by ID: {}", movementId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Inventory ID
    public List<StockMovement> findByInventoryId(int inventoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT sm FROM StockMovement sm WHERE sm.inventoryId = :inventoryId ORDER BY sm.timestamp DESC";
            TypedQuery<StockMovement> query = em.createQuery(jpql, StockMovement.class);
            query.setParameter("inventoryId", inventoryId);
            List<StockMovement> movements = query.getResultList();
            logger.debug("✓ Found {} movement(s) for inventory ID: {}", movements.size(), inventoryId);
            return movements;
        } catch (Exception e) {
            logger.error("✗ Error finding movements by inventory ID: {}", inventoryId, e);
            throw new RuntimeException("Failed to find stock movements by inventory", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Type
    public List<StockMovement> findByType(String type) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT sm FROM StockMovement sm WHERE sm.type = :type ORDER BY sm.timestamp DESC";
            TypedQuery<StockMovement> query = em.createQuery(jpql, StockMovement.class);
            query.setParameter("type", type);
            List<StockMovement> movements = query.getResultList();
            logger.debug("✓ Found {} movement(s) with type: {}", movements.size(), type);
            return movements;
        } catch (Exception e) {
            logger.error("✗ Error finding movements by type: {}", type, e);
            throw new RuntimeException("Failed to find stock movements by type", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng thời gian
    public List<StockMovement> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT sm FROM StockMovement sm WHERE sm.timestamp BETWEEN :startDate AND :endDate ORDER BY sm.timestamp DESC";
            TypedQuery<StockMovement> query = em.createQuery(jpql, StockMovement.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<StockMovement> movements = query.getResultList();
            logger.debug("✓ Found {} movement(s) in date range", movements.size());
            return movements;
        } catch (Exception e) {
            logger.error("✗ Error finding movements by date range", e);
            throw new RuntimeException("Failed to find stock movements by date range", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<StockMovement> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<StockMovement> movements = em.createQuery("SELECT sm FROM StockMovement sm ORDER BY sm.timestamp DESC", StockMovement.class).getResultList();
            logger.debug("✓ Retrieved {} stock movement(s)", movements.size());
            return movements;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all stock movements", e);
            throw new RuntimeException("Failed to retrieve stock movements", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(StockMovement movement) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(movement);
            trans.commit();
            logger.info("✓ Updated stock movement ID: {}", movement.getMovementId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update stock movement ID: {}", movement.getMovementId(), e);
            throw new RuntimeException("Failed to update stock movement", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int movementId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            StockMovement movement = em.find(StockMovement.class, movementId);
            if (movement != null) {
                em.remove(movement);
                logger.info("✓ Deleted stock movement ID: {}", movementId);
            } else {
                logger.warn("✗ Cannot delete - stock movement not found with ID: {}", movementId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete stock movement ID: {}", movementId, e);
            throw new RuntimeException("Failed to delete stock movement", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(sm) FROM StockMovement sm", Long.class).getSingleResult();
            logger.debug("✓ Total stock movements count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting stock movements", e);
            throw new RuntimeException("Failed to count stock movements", e);
        } finally {
            em.close();
        }
    }
}

