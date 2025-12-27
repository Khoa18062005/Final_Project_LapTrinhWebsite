package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.Tablet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Tablet DAO - Data Access Object for Tablet entity
 * @author VietTech Team
 */
public class TabletDAO {

    private static final Logger logger = LoggerFactory.getLogger(TabletDAO.class);

    // CREATE
    public void insert(Tablet tablet) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(tablet);
            trans.commit();
            logger.info("✓ Inserted tablet: {}", tablet.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert tablet: {}", tablet.getName(), e);
            throw new RuntimeException("Failed to insert tablet", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Tablet findById(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Tablet tablet = em.find(Tablet.class, productId);
            if (tablet != null) {
                logger.debug("✓ Found tablet by ID: {}", productId);
            } else {
                logger.warn("✗ Tablet not found with ID: {}", productId);
            }
            return tablet;
        } catch (Exception e) {
            logger.error("✗ Error finding tablet by ID: {}", productId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Slug
    public Tablet findBySlug(String slug) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT t FROM Tablet t WHERE t.slug = :slug";
            TypedQuery<Tablet> query = em.createQuery(jpql, Tablet.class);
            query.setParameter("slug", slug);
            Tablet tablet = query.getSingleResult();
            logger.debug("✓ Found tablet by slug: {}", slug);
            return tablet;
        } catch (NoResultException e) {
            logger.debug("✗ Tablet not found with slug: {}", slug);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding tablet by slug: {}", slug, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Brand
    public List<Tablet> findByBrand(String brand) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT t FROM Tablet t WHERE t.brand = :brand";
            TypedQuery<Tablet> query = em.createQuery(jpql, Tablet.class);
            query.setParameter("brand", brand);
            List<Tablet> tablets = query.getResultList();
            logger.debug("✓ Found {} tablet(s) by brand: {}", tablets.size(), brand);
            return tablets;
        } catch (Exception e) {
            logger.error("✗ Error finding tablets by brand: {}", brand, e);
            throw new RuntimeException("Failed to find tablets by brand", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm có SIM
    public List<Tablet> findWithSimSupport() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT t FROM Tablet t WHERE t.simSupport = true";
            List<Tablet> tablets = em.createQuery(jpql, Tablet.class).getResultList();
            logger.debug("✓ Found {} tablet(s) with SIM support", tablets.size());
            return tablets;
        } catch (Exception e) {
            logger.error("✗ Error finding tablets with SIM support", e);
            throw new RuntimeException("Failed to find tablets with SIM", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm có stylus
    public List<Tablet> findWithStylusSupport() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT t FROM Tablet t WHERE t.stylusSupport = true";
            List<Tablet> tablets = em.createQuery(jpql, Tablet.class).getResultList();
            logger.debug("✓ Found {} tablet(s) with stylus support", tablets.size());
            return tablets;
        } catch (Exception e) {
            logger.error("✗ Error finding tablets with stylus support", e);
            throw new RuntimeException("Failed to find tablets with stylus", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Tablet> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Tablet> tablets = em.createQuery("SELECT t FROM Tablet t", Tablet.class).getResultList();
            logger.debug("✓ Retrieved {} tablet(s)", tablets.size());
            return tablets;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all tablets", e);
            throw new RuntimeException("Failed to retrieve tablets", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả active
    public List<Tablet> findAllActive() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT t FROM Tablet t WHERE t.status = 'active'";
            List<Tablet> tablets = em.createQuery(jpql, Tablet.class).getResultList();
            logger.debug("✓ Retrieved {} active tablet(s)", tablets.size());
            return tablets;
        } catch (Exception e) {
            logger.error("✗ Error retrieving active tablets", e);
            throw new RuntimeException("Failed to retrieve active tablets", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Tablet tablet) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(tablet);
            trans.commit();
            logger.info("✓ Updated tablet: {}", tablet.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update tablet: {}", tablet.getName(), e);
            throw new RuntimeException("Failed to update tablet", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Tablet tablet = em.find(Tablet.class, productId);
            if (tablet != null) {
                em.remove(tablet);
                logger.info("✓ Deleted tablet ID: {} ({})", productId, tablet.getName());
            } else {
                logger.warn("✗ Cannot delete - tablet not found with ID: {}", productId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete tablet ID: {}", productId, e);
            throw new RuntimeException("Failed to delete tablet", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(t) FROM Tablet t", Long.class).getSingleResult();
            logger.debug("✓ Total tablets: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting tablets", e);
            throw new RuntimeException("Failed to count tablets", e);
        } finally {
            em.close();
        }
    }
}

