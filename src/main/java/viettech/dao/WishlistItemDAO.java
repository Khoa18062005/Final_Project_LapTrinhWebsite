package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.wishlist.WishlistItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * WishlistItem DAO - Data Access Object for WishlistItem entity
 * @author VietTech Team
 */
public class WishlistItemDAO {

    private static final Logger logger = LoggerFactory.getLogger(WishlistItemDAO.class);

    // CREATE
    public void insert(WishlistItem item) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(item);
            trans.commit();
            logger.info("✓ Inserted new wishlist item for wishlist ID: {}", item.getWishlistId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert wishlist item", e);
            throw new RuntimeException("Failed to insert wishlist item", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public WishlistItem findById(int itemId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            WishlistItem item = em.find(WishlistItem.class, itemId);
            if (item != null) {
                logger.debug("✓ Found wishlist item by ID: {}", itemId);
            } else {
                logger.warn("✗ Wishlist item not found with ID: {}", itemId);
            }
            return item;
        } catch (Exception e) {
            logger.error("✗ Error finding wishlist item by ID: {}", itemId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Wishlist ID
    public List<WishlistItem> findByWishlistId(int wishlistId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT wi FROM WishlistItem wi WHERE wi.wishlistId = :wishlistId ORDER BY wi.priority DESC, wi.addedDate DESC";
            TypedQuery<WishlistItem> query = em.createQuery(jpql, WishlistItem.class);
            query.setParameter("wishlistId", wishlistId);
            List<WishlistItem> items = query.getResultList();
            logger.debug("✓ Found {} item(s) for wishlist ID: {}", items.size(), wishlistId);
            return items;
        } catch (Exception e) {
            logger.error("✗ Error finding items by wishlist ID: {}", wishlistId, e);
            throw new RuntimeException("Failed to find wishlist items", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Product ID
    public List<WishlistItem> findByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT wi FROM WishlistItem wi WHERE wi.productId = :productId";
            TypedQuery<WishlistItem> query = em.createQuery(jpql, WishlistItem.class);
            query.setParameter("productId", productId);
            List<WishlistItem> items = query.getResultList();
            logger.debug("✓ Found {} wishlist item(s) for product ID: {}", items.size(), productId);
            return items;
        } catch (Exception e) {
            logger.error("✗ Error finding items by product ID: {}", productId, e);
            throw new RuntimeException("Failed to find wishlist items by product", e);
        } finally {
            em.close();
        }
    }

    // READ - Kiểm tra product có trong wishlist không
    public WishlistItem findByWishlistIdAndProductId(int wishlistId, int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT wi FROM WishlistItem wi WHERE wi.wishlistId = :wishlistId AND wi.productId = :productId";
            TypedQuery<WishlistItem> query = em.createQuery(jpql, WishlistItem.class);
            query.setParameter("wishlistId", wishlistId);
            query.setParameter("productId", productId);
            WishlistItem item = query.getSingleResult();
            logger.debug("✓ Found wishlist item for wishlist {} and product {}", wishlistId, productId);
            return item;
        } catch (NoResultException e) {
            logger.debug("✗ Wishlist item not found for wishlist {} and product {}", wishlistId, productId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding wishlist item", e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm items cần notify on discount
    public List<WishlistItem> findItemsToNotifyOnDiscount() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT wi FROM WishlistItem wi WHERE wi.notifyOnDiscount = true";
            List<WishlistItem> items = em.createQuery(jpql, WishlistItem.class).getResultList();
            logger.debug("✓ Found {} item(s) to notify on discount", items.size());
            return items;
        } catch (Exception e) {
            logger.error("✗ Error finding items to notify on discount", e);
            throw new RuntimeException("Failed to find items to notify", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<WishlistItem> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<WishlistItem> items = em.createQuery("SELECT wi FROM WishlistItem wi", WishlistItem.class).getResultList();
            logger.debug("✓ Retrieved {} wishlist item(s)", items.size());
            return items;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all wishlist items", e);
            throw new RuntimeException("Failed to retrieve wishlist items", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(WishlistItem item) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(item);
            trans.commit();
            logger.info("✓ Updated wishlist item ID: {}", item.getItemId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update wishlist item ID: {}", item.getItemId(), e);
            throw new RuntimeException("Failed to update wishlist item", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int itemId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            WishlistItem item = em.find(WishlistItem.class, itemId);
            if (item != null) {
                em.remove(item);
                logger.info("✓ Deleted wishlist item ID: {}", itemId);
            } else {
                logger.warn("✗ Cannot delete - wishlist item not found with ID: {}", itemId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete wishlist item ID: {}", itemId, e);
            throw new RuntimeException("Failed to delete wishlist item", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa tất cả items của wishlist
    public void deleteByWishlistId(int wishlistId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM WishlistItem wi WHERE wi.wishlistId = :wishlistId";
            int deleted = em.createQuery(jpql).setParameter("wishlistId", wishlistId).executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} item(s) for wishlist ID: {}", deleted, wishlistId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete items for wishlist ID: {}", wishlistId, e);
            throw new RuntimeException("Failed to delete wishlist items", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm số items trong wishlist
    public long countByWishlistId(int wishlistId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(wi) FROM WishlistItem wi WHERE wi.wishlistId = :wishlistId";
            Long count = em.createQuery(jpql, Long.class).setParameter("wishlistId", wishlistId).getSingleResult();
            logger.debug("✓ Wishlist ID {} has {} item(s)", wishlistId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting items for wishlist ID: {}", wishlistId, e);
            throw new RuntimeException("Failed to count wishlist items", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(wi) FROM WishlistItem wi", Long.class).getSingleResult();
            logger.debug("✓ Total wishlist items count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting wishlist items", e);
            throw new RuntimeException("Failed to count wishlist items", e);
        } finally {
            em.close();
        }
    }
}

