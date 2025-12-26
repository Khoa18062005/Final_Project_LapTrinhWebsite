package viettech.dao;
}
    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to count wishlists", e);
            logger.error("✗ Error counting wishlists", e);
        } catch (Exception e) {
            return count;
            logger.debug("✓ Total wishlists count: {}", count);
            Long count = em.createQuery("SELECT COUNT(w) FROM Wishlist w", Long.class).getSingleResult();
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public long count() {
    // COUNT

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to delete wishlist", e);
            logger.error("✗ Failed to delete wishlist ID: {}", wishlistId, e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            trans.commit();
            }
                logger.warn("✗ Cannot delete - wishlist not found with ID: {}", wishlistId);
            } else {
                logger.info("✓ Deleted wishlist ID: {} ({})", wishlistId, wishlist.getName());
                em.remove(wishlist);
            if (wishlist != null) {
            Wishlist wishlist = em.find(Wishlist.class, wishlistId);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void delete(int wishlistId) {
    // DELETE

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to update wishlist", e);
            logger.error("✗ Failed to update wishlist: {}", wishlist.getName(), e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            logger.info("✓ Updated wishlist: {}", wishlist.getName());
            trans.commit();
            em.merge(wishlist);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void update(Wishlist wishlist) {
    // UPDATE

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to retrieve wishlists", e);
            logger.error("✗ Error retrieving all wishlists", e);
        } catch (Exception e) {
            return wishlists;
            logger.debug("✓ Retrieved {} wishlist(s)", wishlists.size());
            List<Wishlist> wishlists = em.createQuery("SELECT w FROM Wishlist w ORDER BY w.createdAt DESC", Wishlist.class).getResultList();
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<Wishlist> findAll() {
    // READ - Lấy tất cả

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find public wishlists", e);
            logger.error("✗ Error finding public wishlists", e);
        } catch (Exception e) {
            return wishlists;
            logger.debug("✓ Found {} public wishlist(s)", wishlists.size());
            List<Wishlist> wishlists = em.createQuery(jpql, Wishlist.class).getResultList();
            String jpql = "SELECT w FROM Wishlist w WHERE w.isPublic = true ORDER BY w.createdAt DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<Wishlist> findPublic() {
    // READ - Tìm public wishlists

    }
        }
            em.close();
        } finally {
            return null;
            logger.error("✗ Error finding default wishlist for customer ID: {}", customerId, e);
        } catch (Exception e) {
            return null;
            logger.debug("✗ Default wishlist not found for customer ID: {}", customerId);
        } catch (NoResultException e) {
            return wishlist;
            logger.debug("✓ Found default wishlist for customer ID: {}", customerId);
            Wishlist wishlist = query.getSingleResult();
            query.setParameter("customerId", customerId);
            TypedQuery<Wishlist> query = em.createQuery(jpql, Wishlist.class);
            String jpql = "SELECT w FROM Wishlist w WHERE w.customerId = :customerId AND w.isDefault = true";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public Wishlist findDefaultByCustomerId(int customerId) {
    // READ - Tìm default wishlist của customer

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find wishlists by customer", e);
            logger.error("✗ Error finding wishlists by customer ID: {}", customerId, e);
        } catch (Exception e) {
            return wishlists;
            logger.debug("✓ Found {} wishlist(s) for customer ID: {}", wishlists.size(), customerId);
            List<Wishlist> wishlists = query.getResultList();
            query.setParameter("customerId", customerId);
            TypedQuery<Wishlist> query = em.createQuery(jpql, Wishlist.class);
            String jpql = "SELECT w FROM Wishlist w WHERE w.customerId = :customerId ORDER BY w.createdAt DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<Wishlist> findByCustomerId(int customerId) {
    // READ - Tìm theo Customer ID

    }
        }
            em.close();
        } finally {
            return null;
            logger.error("✗ Error finding wishlist by ID: {}", wishlistId, e);
        } catch (Exception e) {
            return wishlist;
            }
                logger.warn("✗ Wishlist not found with ID: {}", wishlistId);
            } else {
                logger.debug("✓ Found wishlist by ID: {}", wishlistId);
            if (wishlist != null) {
            Wishlist wishlist = em.find(Wishlist.class, wishlistId);
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public Wishlist findById(int wishlistId) {
    // READ - Tìm theo ID

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to insert wishlist", e);
            logger.error("✗ Failed to insert wishlist: {}", wishlist.getName(), e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            logger.info("✓ Inserted new wishlist: {}", wishlist.getName());
            trans.commit();
            em.persist(wishlist);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void insert(Wishlist wishlist) {
    // CREATE

    private static final Logger logger = LoggerFactory.getLogger(WishlistDAO.class);

public class WishlistDAO {
 */
 * @author VietTech Team
 * Wishlist DAO - Data Access Object for Wishlist entity
/**

import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;

import viettech.entity.wishlist.Wishlist;
import viettech.config.JPAConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


