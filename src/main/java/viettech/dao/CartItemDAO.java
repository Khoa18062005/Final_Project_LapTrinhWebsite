package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.cart.CartItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * CartItem DAO - Data Access Object for CartItem entity
 * @author VietTech Team
 */
public class CartItemDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartItemDAO.class);

    // CREATE
    public void insert(CartItem cartItem) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(cartItem);
            trans.commit();
            logger.info("✓ Inserted cart item for cart ID: {}", cartItem.getCartId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert cart item", e);
            throw new RuntimeException("Failed to insert cart item", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public CartItem findById(int cartItemId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            CartItem item = em.find(CartItem.class, cartItemId);
            if (item != null) {
                logger.debug("✓ Found cart item by ID: {}", cartItemId);
            } else {
                logger.warn("✗ Cart item not found with ID: {}", cartItemId);
            }
            return item;
        } catch (Exception e) {
            logger.error("✗ Error finding cart item by ID: {}", cartItemId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Cart ID
    public List<CartItem> findByCartId(int cartId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT ci FROM CartItem ci WHERE ci.cartId = :cartId ORDER BY ci.addedAt DESC";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("cartId", cartId);
            List<CartItem> items = query.getResultList();
            logger.debug("✓ Found {} item(s) for cart ID: {}", items.size(), cartId);
            return items;
        } catch (Exception e) {
            logger.error("✗ Error finding items by cart ID: {}", cartId, e);
            throw new RuntimeException("Failed to find cart items", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Cart ID và Variant ID và ProductId
    public CartItem findByCartIdAndVariantIdandProductId(int cartId, int variantId, int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT ci FROM CartItem ci WHERE ci.cartId = :cartId AND ci.variantId = :variantId AND ci.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("cartId", cartId);
            query.setParameter("variantId", variantId);
            query.setParameter("productId", productId);
            CartItem item = query.getSingleResult();
            logger.debug("✓ Found cart item for cart {} variant {} productId {}", cartId, variantId, productId);
            return item;
        } catch (NoResultException e) {
            logger.debug("✗ Cart item not found for cart {} variant {} productid {}", cartId, variantId, productId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding cart item", e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<CartItem> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<CartItem> items = em.createQuery("SELECT ci FROM CartItem ci", CartItem.class).getResultList();
            logger.debug("✓ Retrieved {} cart item(s)", items.size());
            return items;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all cart items", e);
            throw new RuntimeException("Failed to retrieve cart items", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(CartItem cartItem) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(cartItem);
            trans.commit();
            logger.info("✓ Updated cart item ID: {}", cartItem.getCartItemId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update cart item", e);
            throw new RuntimeException("Failed to update cart item", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int cartItemId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            CartItem item = em.find(CartItem.class, cartItemId);
            if (item != null) {
                em.remove(item);
                logger.info("✓ Deleted cart item ID: {}", cartItemId);
            } else {
                logger.warn("✗ Cannot delete - cart item not found with ID: {}", cartItemId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete cart item ID: {}", cartItemId, e);
            throw new RuntimeException("Failed to delete cart item", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa tất cả items của cart
    public void deleteByCartId(int cartId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM CartItem ci WHERE ci.cartId = :cartId";
            int deleted = em.createQuery(jpql).setParameter("cartId", cartId).executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} item(s) from cart ID: {}", deleted, cartId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete items from cart ID: {}", cartId, e);
            throw new RuntimeException("Failed to delete cart items", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm items trong cart
    public long countByCartId(int cartId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(ci) FROM CartItem ci WHERE ci.cartId = :cartId";
            Long count = em.createQuery(jpql, Long.class).setParameter("cartId", cartId).getSingleResult();
            logger.debug("✓ Cart {} has {} item(s)", cartId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting cart items", e);
            throw new RuntimeException("Failed to count cart items", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(ci) FROM CartItem ci", Long.class).getSingleResult();
            logger.debug("✓ Total cart items: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting cart items", e);
            throw new RuntimeException("Failed to count cart items", e);
        } finally {
            em.close();
        }
    }
}

