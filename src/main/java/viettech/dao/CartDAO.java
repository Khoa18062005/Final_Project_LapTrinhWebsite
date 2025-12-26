package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.cart.Cart;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Cart DAO - Data Access Object for Cart entity
 * @author VietTech Team
 */
public class CartDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartDAO.class);

    // CREATE
    public void insert(Cart cart) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(cart);
            trans.commit();
            logger.info("✓ Inserted cart for customer ID: {}", cart.getCustomerId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert cart", e);
            throw new RuntimeException("Failed to insert cart", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Cart findById(int cartId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Cart cart = em.find(Cart.class, cartId);
            if (cart != null) {
                logger.debug("✓ Found cart by ID: {}", cartId);
            } else {
                logger.warn("✗ Cart not found with ID: {}", cartId);
            }
            return cart;
        } catch (Exception e) {
            logger.error("✗ Error finding cart by ID: {}", cartId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public Cart findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Cart c WHERE c.customerId = :customerId";
            TypedQuery<Cart> query = em.createQuery(jpql, Cart.class);
            query.setParameter("customerId", customerId);
            Cart cart = query.getSingleResult();
            logger.debug("✓ Found cart for customer ID: {}", customerId);
            return cart;
        } catch (NoResultException e) {
            logger.debug("✗ Cart not found for customer ID: {}", customerId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding cart by customer ID: {}", customerId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Cart> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Cart> carts = em.createQuery("SELECT c FROM Cart c", Cart.class).getResultList();
            logger.debug("✓ Retrieved {} cart(s)", carts.size());
            return carts;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all carts", e);
            throw new RuntimeException("Failed to retrieve carts", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Cart cart) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(cart);
            trans.commit();
            logger.info("✓ Updated cart ID: {}", cart.getCartId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update cart", e);
            throw new RuntimeException("Failed to update cart", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int cartId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Cart cart = em.find(Cart.class, cartId);
            if (cart != null) {
                em.remove(cart);
                logger.info("✓ Deleted cart ID: {}", cartId);
            } else {
                logger.warn("✗ Cannot delete - cart not found with ID: {}", cartId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete cart ID: {}", cartId, e);
            throw new RuntimeException("Failed to delete cart", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(c) FROM Cart c", Long.class).getSingleResult();
            logger.debug("✓ Total carts: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting carts", e);
            throw new RuntimeException("Failed to count carts", e);
        } finally {
            em.close();
        }
    }
}

