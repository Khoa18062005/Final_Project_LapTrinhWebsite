package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.delivery.DeliveryAssignment;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * DeliveryAssignment DAO - Data Access Object for DeliveryAssignment entity
 * @author VietTech Team
 */
public class DeliveryAssignmentDAO {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryAssignmentDAO.class);

    // CREATE
    public void insert(DeliveryAssignment assignment) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(assignment);
            trans.commit();
            logger.info("✓ Inserted delivery assignment for shipper ID: {}", assignment.getShipperId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert delivery assignment", e);
            throw new RuntimeException("Failed to insert delivery assignment", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public DeliveryAssignment findById(int assignmentId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            DeliveryAssignment assignment = em.find(DeliveryAssignment.class, assignmentId);
            if (assignment != null) {
                logger.debug("✓ Found delivery assignment by ID: {}", assignmentId);
            } else {
                logger.warn("✗ Delivery assignment not found with ID: {}", assignmentId);
            }
            return assignment;
        } catch (Exception e) {
            logger.error("✗ Error finding delivery assignment by ID: {}", assignmentId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Shipper ID
    public List<DeliveryAssignment> findByShipperId(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT da FROM DeliveryAssignment da WHERE da.shipperId = :shipperId ORDER BY da.assignedAt DESC";
            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("shipperId", shipperId);
            List<DeliveryAssignment> assignments = query.getResultList();
            logger.debug("✓ Found {} assignment(s) for shipper ID: {}", assignments.size(), shipperId);
            return assignments;
        } catch (Exception e) {
            logger.error("✗ Error finding assignments by shipper ID: {}", shipperId, e);
            throw new RuntimeException("Failed to find delivery assignments", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Delivery ID
    public List<DeliveryAssignment> findByDeliveryId(int deliveryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT da FROM DeliveryAssignment da WHERE da.deliveryId = :deliveryId ORDER BY da.assignedAt DESC";
            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("deliveryId", deliveryId);
            List<DeliveryAssignment> assignments = query.getResultList();
            logger.debug("✓ Found {} assignment(s) for delivery ID: {}", assignments.size(), deliveryId);
            return assignments;
        } catch (Exception e) {
            logger.error("✗ Error finding assignments by delivery ID: {}", deliveryId, e);
            throw new RuntimeException("Failed to find delivery assignments", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Status
    public List<DeliveryAssignment> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT da FROM DeliveryAssignment da WHERE da.status = :status ORDER BY da.assignedAt DESC";
            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("status", status);
            List<DeliveryAssignment> assignments = query.getResultList();
            logger.debug("✓ Found {} assignment(s) with status: {}", assignments.size(), status);
            return assignments;
        } catch (Exception e) {
            logger.error("✗ Error finding assignments by status: {}", status, e);
            throw new RuntimeException("Failed to find delivery assignments by status", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<DeliveryAssignment> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<DeliveryAssignment> assignments = em.createQuery("SELECT da FROM DeliveryAssignment da ORDER BY da.assignedAt DESC", DeliveryAssignment.class).getResultList();
            logger.debug("✓ Retrieved {} delivery assignment(s)", assignments.size());
            return assignments;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all delivery assignments", e);
            throw new RuntimeException("Failed to retrieve delivery assignments", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(DeliveryAssignment assignment) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(assignment);
            trans.commit();
            logger.info("✓ Updated delivery assignment ID: {}", assignment.getAssignmentId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update delivery assignment", e);
            throw new RuntimeException("Failed to update delivery assignment", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int assignmentId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            DeliveryAssignment assignment = em.find(DeliveryAssignment.class, assignmentId);
            if (assignment != null) {
                em.remove(assignment);
                logger.info("✓ Deleted delivery assignment ID: {}", assignmentId);
            } else {
                logger.warn("✗ Cannot delete - delivery assignment not found with ID: {}", assignmentId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete delivery assignment ID: {}", assignmentId, e);
            throw new RuntimeException("Failed to delete delivery assignment", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(da) FROM DeliveryAssignment da", Long.class).getSingleResult();
            logger.debug("✓ Total delivery assignments: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting delivery assignments", e);
            throw new RuntimeException("Failed to count delivery assignments", e);
        } finally {
            em.close();
        }
    }
}

