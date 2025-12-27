package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.delivery.Delivery;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Delivery DAO - Data Access Object for Delivery entity
 * @author VietTech Team
 */
public class DeliveryDAO {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryDAO.class);

    // CREATE
    public void insert(Delivery delivery) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(delivery);
            trans.commit();
            logger.info("✓ Inserted delivery: {}", delivery.getTrackingNumber());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert delivery", e);
            throw new RuntimeException("Failed to insert delivery", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Delivery findById(int deliveryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Delivery delivery = em.find(Delivery.class, deliveryId);
            if (delivery != null) {
                logger.debug("✓ Found delivery by ID: {}", deliveryId);
            } else {
                logger.warn("✗ Delivery not found with ID: {}", deliveryId);
            }
            return delivery;
        } catch (Exception e) {
            logger.error("✗ Error finding delivery by ID: {}", deliveryId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Tracking Number
    public Delivery findByTrackingNumber(String trackingNumber) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT d FROM Delivery d WHERE d.trackingNumber = :trackingNumber";
            TypedQuery<Delivery> query = em.createQuery(jpql, Delivery.class);
            query.setParameter("trackingNumber", trackingNumber);
            Delivery delivery = query.getSingleResult();
            logger.debug("✓ Found delivery by tracking number: {}", trackingNumber);
            return delivery;
        } catch (NoResultException e) {
            logger.debug("✗ Delivery not found with tracking number: {}", trackingNumber);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding delivery by tracking number: {}", trackingNumber, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Order ID
    public Delivery findByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT d FROM Delivery d WHERE d.orderId = :orderId";
            TypedQuery<Delivery> query = em.createQuery(jpql, Delivery.class);
            query.setParameter("orderId", orderId);
            Delivery delivery = query.getSingleResult();
            logger.debug("✓ Found delivery for order ID: {}", orderId);
            return delivery;
        } catch (NoResultException e) {
            logger.debug("✗ Delivery not found for order ID: {}", orderId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding delivery by order ID: {}", orderId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Status
    public List<Delivery> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT d FROM Delivery d WHERE d.status = :status ORDER BY d.estimatedDelivery DESC";
            TypedQuery<Delivery> query = em.createQuery(jpql, Delivery.class);
            query.setParameter("status", status);
            List<Delivery> deliveries = query.getResultList();
            logger.debug("✓ Found {} delivery(ies) with status: {}", deliveries.size(), status);
            return deliveries;
        } catch (Exception e) {
            logger.error("✗ Error finding deliveries by status: {}", status, e);
            throw new RuntimeException("Failed to find deliveries by status", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Delivery> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Delivery> deliveries = em.createQuery("SELECT d FROM Delivery d ORDER BY d.estimatedDelivery DESC", Delivery.class).getResultList();
            logger.debug("✓ Retrieved {} delivery(ies)", deliveries.size());
            return deliveries;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all deliveries", e);
            throw new RuntimeException("Failed to retrieve deliveries", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Delivery delivery) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(delivery);
            trans.commit();
            logger.info("✓ Updated delivery: {}", delivery.getTrackingNumber());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update delivery: {}", delivery.getTrackingNumber(), e);
            throw new RuntimeException("Failed to update delivery", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int deliveryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Delivery delivery = em.find(Delivery.class, deliveryId);
            if (delivery != null) {
                em.remove(delivery);
                logger.info("✓ Deleted delivery ID: {} ({})", deliveryId, delivery.getTrackingNumber());
            } else {
                logger.warn("✗ Cannot delete - delivery not found with ID: {}", deliveryId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete delivery ID: {}", deliveryId, e);
            throw new RuntimeException("Failed to delete delivery", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(d) FROM Delivery d", Long.class).getSingleResult();
            logger.debug("✓ Total deliveries: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting deliveries", e);
            throw new RuntimeException("Failed to count deliveries", e);
        } finally {
            em.close();
        }
    }
}

