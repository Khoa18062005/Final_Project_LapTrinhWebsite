package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.order.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Order DAO - Data Access Object for Order entity
 * @author VietTech Team
 */
public class OrderDAO {

    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);

    // CREATE
    public void insert(Order order) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(order);
            trans.commit();
            logger.info("✓ Inserted order: {}", order.getOrderNumber());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert order", e);
            throw new RuntimeException("Failed to insert order", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Order findById(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Order order = em.find(Order.class, orderId);
            if (order != null) {
                logger.debug("✓ Found order by ID: {}", orderId);
            } else {
                logger.warn("✗ Order not found with ID: {}", orderId);
            }
            return order;
        } catch (Exception e) {
            logger.error("✗ Error finding order by ID: {}", orderId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Order Number
    public Order findByOrderNumber(String orderNumber) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.orderNumber = :orderNumber";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("orderNumber", orderNumber);
            Order order = query.getSingleResult();
            logger.debug("✓ Found order by number: {}", orderNumber);
            return order;
        } catch (NoResultException e) {
            logger.debug("✗ Order not found with number: {}", orderNumber);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding order by number: {}", orderNumber, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public List<Order> findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.customerId = :customerId ORDER BY o.orderDate DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("customerId", customerId);
            List<Order> orders = query.getResultList();
            logger.debug("✓ Found {} order(s) for customer ID: {}", orders.size(), customerId);
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error finding orders by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find orders by customer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID
    public List<Order> findByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.vendorId = :vendorId ORDER BY o.orderDate DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("vendorId", vendorId);
            List<Order> orders = query.getResultList();
            logger.debug("✓ Found {} order(s) for vendor ID: {}", orders.size(), vendorId);
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error finding orders by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to find orders by vendor", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Status
    public List<Order> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.status = :status ORDER BY o.orderDate DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("status", status);
            List<Order> orders = query.getResultList();
            logger.debug("✓ Found {} order(s) with status: {}", orders.size(), status);
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error finding orders by status: {}", status, e);
            throw new RuntimeException("Failed to find orders by status", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng thời gian
    public List<Order> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Order> orders = query.getResultList();
            logger.debug("✓ Found {} order(s) in date range", orders.size());
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error finding orders by date range", e);
            throw new RuntimeException("Failed to find orders by date range", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Order> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Order> orders = em.createQuery("SELECT o FROM Order o ORDER BY o.orderDate DESC", Order.class).getResultList();
            logger.debug("✓ Retrieved {} order(s)", orders.size());
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all orders", e);
            throw new RuntimeException("Failed to retrieve orders", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Order order) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(order);
            trans.commit();
            logger.info("✓ Updated order: {}", order.getOrderNumber());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update order: {}", order.getOrderNumber(), e);
            throw new RuntimeException("Failed to update order", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Order order = em.find(Order.class, orderId);
            if (order != null) {
                em.remove(order);
                logger.info("✓ Deleted order ID: {} ({})", orderId, order.getOrderNumber());
            } else {
                logger.warn("✗ Cannot delete - order not found with ID: {}", orderId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete order ID: {}", orderId, e);
            throw new RuntimeException("Failed to delete order", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
            logger.debug("✓ Total orders: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting orders", e);
            throw new RuntimeException("Failed to count orders", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm theo status
    public long countByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(o) FROM Order o WHERE o.status = :status";
            Long count = em.createQuery(jpql, Long.class).setParameter("status", status).getSingleResult();
            logger.debug("✓ Orders with status '{}': {}", status, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting orders by status: {}", status, e);
            throw new RuntimeException("Failed to count orders by status", e);
        } finally {
            em.close();
        }
    }
}

