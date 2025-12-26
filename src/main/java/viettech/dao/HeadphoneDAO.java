package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.Headphone;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Headphone DAO - Data Access Object for Headphone entity
 * @author VietTech Team
 */
public class HeadphoneDAO {

    private static final Logger logger = LoggerFactory.getLogger(HeadphoneDAO.class);

    // CREATE
    public void insert(Headphone headphone) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(headphone);
            trans.commit();
            logger.info("✓ Inserted headphone: {}", headphone.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert headphone: {}", headphone.getName(), e);
            throw new RuntimeException("Failed to insert headphone", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Headphone findById(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Headphone headphone = em.find(Headphone.class, productId);
            if (headphone != null) {
                logger.debug("✓ Found headphone by ID: {}", productId);
            } else {
                logger.warn("✗ Headphone not found with ID: {}", productId);
            }
            return headphone;
        } catch (Exception e) {
            logger.error("✗ Error finding headphone by ID: {}", productId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Slug
    public Headphone findBySlug(String slug) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT h FROM Headphone h WHERE h.slug = :slug";
            TypedQuery<Headphone> query = em.createQuery(jpql, Headphone.class);
            query.setParameter("slug", slug);
            Headphone headphone = query.getSingleResult();
            logger.debug("✓ Found headphone by slug: {}", slug);
            return headphone;
        } catch (NoResultException e) {
            logger.debug("✗ Headphone not found with slug: {}", slug);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding headphone by slug: {}", slug, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Brand
    public List<Headphone> findByBrand(String brand) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT h FROM Headphone h WHERE h.brand = :brand";
            TypedQuery<Headphone> query = em.createQuery(jpql, Headphone.class);
            query.setParameter("brand", brand);
            List<Headphone> headphones = query.getResultList();
            logger.debug("✓ Found {} headphone(s) by brand: {}", headphones.size(), brand);
            return headphones;
        } catch (Exception e) {
            logger.error("✗ Error finding headphones by brand: {}", brand, e);
            throw new RuntimeException("Failed to find headphones by brand", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Type
    public List<Headphone> findByType(String type) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT h FROM Headphone h WHERE h.type = :type";
            TypedQuery<Headphone> query = em.createQuery(jpql, Headphone.class);
            query.setParameter("type", type);
            List<Headphone> headphones = query.getResultList();
            logger.debug("✓ Found {} headphone(s) by type: {}", headphones.size(), type);
            return headphones;
        } catch (Exception e) {
            logger.error("✗ Error finding headphones by type: {}", type, e);
            throw new RuntimeException("Failed to find headphones by type", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm có ANC
    public List<Headphone> findWithNoiseCancellation() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT h FROM Headphone h WHERE h.noiseCancellation = true";
            List<Headphone> headphones = em.createQuery(jpql, Headphone.class).getResultList();
            logger.debug("✓ Found {} headphone(s) with noise cancellation", headphones.size());
            return headphones;
        } catch (Exception e) {
            logger.error("✗ Error finding headphones with noise cancellation", e);
            throw new RuntimeException("Failed to find headphones with ANC", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Headphone> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Headphone> headphones = em.createQuery("SELECT h FROM Headphone h", Headphone.class).getResultList();
            logger.debug("✓ Retrieved {} headphone(s)", headphones.size());
            return headphones;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all headphones", e);
            throw new RuntimeException("Failed to retrieve headphones", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả active
    public List<Headphone> findAllActive() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT h FROM Headphone h WHERE h.status = 'active'";
            List<Headphone> headphones = em.createQuery(jpql, Headphone.class).getResultList();
            logger.debug("✓ Retrieved {} active headphone(s)", headphones.size());
            return headphones;
        } catch (Exception e) {
            logger.error("✗ Error retrieving active headphones", e);
            throw new RuntimeException("Failed to retrieve active headphones", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Headphone headphone) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(headphone);
            trans.commit();
            logger.info("✓ Updated headphone: {}", headphone.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update headphone: {}", headphone.getName(), e);
            throw new RuntimeException("Failed to update headphone", e);
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
            Headphone headphone = em.find(Headphone.class, productId);
            if (headphone != null) {
                em.remove(headphone);
                logger.info("✓ Deleted headphone ID: {} ({})", productId, headphone.getName());
            } else {
                logger.warn("✗ Cannot delete - headphone not found with ID: {}", productId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete headphone ID: {}", productId, e);
            throw new RuntimeException("Failed to delete headphone", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(h) FROM Headphone h", Long.class).getSingleResult();
            logger.debug("✓ Total headphones: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting headphones", e);
            throw new RuntimeException("Failed to count headphones", e);
        } finally {
            em.close();
        }
    }
}

