package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.wishlist.Wishlist;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Wishlist DAO - Data Access Object for Wishlist entity
 * @author VietTech Team
 */
public class WishlistDAO {

    private static final Logger logger = LoggerFactory.getLogger(WishlistDAO.class);

    // CREATE
    public void insert(Wishlist wishlist) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(wishlist);
            trans.commit();
            logger.info("✓ Inserted wishlist: {} for customer ID: {}", wishlist.getName(), wishlist.getCustomerId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert wishlist", e);
            throw new RuntimeException("Failed to insert wishlist", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Wishlist findById(int wishlistId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Wishlist wishlist = em.find(Wishlist.class, wishlistId);
            if (wishlist != null) {
                logger.debug("✓ Found wishlist by ID: {}", wishlistId);
            } else {
                logger.warn("✗ Wishlist not found with ID: {}", wishlistId);
            }
            return wishlist;
        } catch (Exception e) {
            logger.error("✗ Error finding wishlist by ID: {}", wishlistId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public List<Wishlist> findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT w FROM Wishlist w WHERE w.customerId = :customerId ORDER BY w.createdAt DESC";
            TypedQuery<Wishlist> query = em.createQuery(jpql, Wishlist.class);
            query.setParameter("customerId", customerId);
            List<Wishlist> wishlists = query.getResultList();
            logger.debug("✓ Found {} wishlist(s) for customer ID: {}", wishlists.size(), customerId);
            return wishlists;
        } catch (Exception e) {
            logger.error("✗ Error finding wishlists by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find wishlists", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm default wishlist của customer
    public Wishlist findDefaultByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT w FROM Wishlist w WHERE w.customerId = :customerId AND w.isDefault = true";
            TypedQuery<Wishlist> query = em.createQuery(jpql, Wishlist.class);
            query.setParameter("customerId", customerId);
            Wishlist wishlist = query.getSingleResult();
            logger.debug("✓ Found default wishlist for customer ID: {}", customerId);
            return wishlist;
        } catch (NoResultException e) {
            logger.debug("✗ Default wishlist not found for customer ID: {}", customerId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding default wishlist", e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm public wishlists
    public List<Wishlist> findPublicWishlists() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT w FROM Wishlist w WHERE w.isPublic = true ORDER BY w.createdAt DESC";
            List<Wishlist> wishlists = em.createQuery(jpql, Wishlist.class).getResultList();
            logger.debug("✓ Found {} public wishlist(s)", wishlists.size());
            return wishlists;
        } catch (Exception e) {
            logger.error("✗ Error finding public wishlists", e);
            throw new RuntimeException("Failed to find public wishlists", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Wishlist> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Wishlist> wishlists = em.createQuery("SELECT w FROM Wishlist w ORDER BY w.createdAt DESC", Wishlist.class).getResultList();
            logger.debug("✓ Retrieved {} wishlist(s)", wishlists.size());
            return wishlists;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all wishlists", e);
            throw new RuntimeException("Failed to retrieve wishlists", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Wishlist wishlist) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(wishlist);
            trans.commit();
            logger.info("✓ Updated wishlist ID: {}", wishlist.getWishlistId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update wishlist", e);
            throw new RuntimeException("Failed to update wishlist", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int wishlistId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Wishlist wishlist = em.find(Wishlist.class, wishlistId);
            if (wishlist != null) {
                em.remove(wishlist);
                logger.info("✓ Deleted wishlist ID: {} ({})", wishlistId, wishlist.getName());
            } else {
                logger.warn("✗ Cannot delete - wishlist not found with ID: {}", wishlistId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete wishlist ID: {}", wishlistId, e);
            throw new RuntimeException("Failed to delete wishlist", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(w) FROM Wishlist w", Long.class).getSingleResult();
            logger.debug("✓ Total wishlists: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting wishlists", e);
            throw new RuntimeException("Failed to count wishlists", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm wishlists của customer
    public long countByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(w) FROM Wishlist w WHERE w.customerId = :customerId";
            Long count = em.createQuery(jpql, Long.class).setParameter("customerId", customerId).getSingleResult();
            logger.debug("✓ Wishlists for customer {}: {}", customerId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting wishlists for customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to count wishlists", e);
        } finally {
            em.close();
        }
    }
}

