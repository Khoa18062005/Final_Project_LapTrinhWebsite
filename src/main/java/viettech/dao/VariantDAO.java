package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.Variant;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Variant DAO - Data Access Object for Variant entity
 * @author VietTech Team
 */
public class VariantDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantDAO.class);

    // CREATE
    public void insert(Variant variant) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(variant);
            trans.commit();
            logger.info("✓ Inserted variant: {}", variant.getSku());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert variant: {}", variant.getSku(), e);
            throw new RuntimeException("Failed to insert variant", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Variant findById(int variantId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Variant variant = em.find(Variant.class, variantId);
            if (variant != null) {
                logger.debug("✓ Found variant by ID: {}", variantId);
            } else {
                logger.warn("✗ Variant not found with ID: {}", variantId);
            }
            return variant;
        } catch (Exception e) {
            logger.error("✗ Error finding variant by ID: {}", variantId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo SKU
    public Variant findBySku(String sku) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Variant v WHERE v.sku = :sku";
            TypedQuery<Variant> query = em.createQuery(jpql, Variant.class);
            query.setParameter("sku", sku);
            Variant variant = query.getSingleResult();
            logger.debug("✓ Found variant by SKU: {}", sku);
            return variant;
        } catch (NoResultException e) {
            logger.debug("✗ Variant not found with SKU: {}", sku);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding variant by SKU: {}", sku, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Product ID
    public List<Variant> findByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Variant v WHERE v.product.productId = :productId";
            TypedQuery<Variant> query = em.createQuery(jpql, Variant.class);
            query.setParameter("productId", productId);
            List<Variant> variants = query.getResultList();
            logger.debug("✓ Found {} variant(s) for product ID: {}", variants.size(), productId);
            return variants;
        } catch (Exception e) {
            logger.error("✗ Error finding variants by product ID: {}", productId, e);
            throw new RuntimeException("Failed to find variants by product", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm variant active theo Product ID
    public List<Variant> findActiveByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Variant v WHERE v.product.productId = :productId AND v.isActive = true";
            TypedQuery<Variant> query = em.createQuery(jpql, Variant.class);
            query.setParameter("productId", productId);
            List<Variant> variants = query.getResultList();
            logger.debug("✓ Found {} active variant(s) for product ID: {}", variants.size(), productId);
            return variants;
        } catch (Exception e) {
            logger.error("✗ Error finding active variants by product ID: {}", productId, e);
            throw new RuntimeException("Failed to find active variants", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Variant> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Variant> variants = em.createQuery("SELECT v FROM Variant v", Variant.class).getResultList();
            logger.debug("✓ Retrieved {} variant(s)", variants.size());
            return variants;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all variants", e);
            throw new RuntimeException("Failed to retrieve variants", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng giá
    public List<Variant> findByPriceRange(double minPrice, double maxPrice) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Variant v WHERE v.finalPrice BETWEEN :minPrice AND :maxPrice ORDER BY v.finalPrice";
            TypedQuery<Variant> query = em.createQuery(jpql, Variant.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            List<Variant> variants = query.getResultList();
            logger.debug("✓ Found {} variant(s) in price range {} - {}", variants.size(), minPrice, maxPrice);
            return variants;
        } catch (Exception e) {
            logger.error("✗ Error finding variants by price range", e);
            throw new RuntimeException("Failed to find variants by price range", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Variant variant) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(variant);
            trans.commit();
            logger.info("✓ Updated variant: {}", variant.getSku());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update variant: {}", variant.getSku(), e);
            throw new RuntimeException("Failed to update variant", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int variantId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Variant variant = em.find(Variant.class, variantId);
            if (variant != null) {
                em.remove(variant);
                logger.info("✓ Deleted variant ID: {} ({})", variantId, variant.getSku());
            } else {
                logger.warn("✗ Cannot delete - variant not found with ID: {}", variantId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete variant ID: {}", variantId, e);
            throw new RuntimeException("Failed to delete variant", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(v) FROM Variant v", Long.class).getSingleResult();
            logger.debug("✓ Total variants: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting variants", e);
            throw new RuntimeException("Failed to count variants", e);
        } finally {
            em.close();
        }
    }
}

