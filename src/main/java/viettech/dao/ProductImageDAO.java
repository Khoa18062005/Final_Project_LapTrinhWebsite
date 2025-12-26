package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.ProductImage;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * ProductImage DAO - Data Access Object for ProductImage entity
 * @author VietTech Team
 */
public class ProductImageDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductImageDAO.class);

    // CREATE
    public void insert(ProductImage image) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(image);
            trans.commit();
            logger.info("✓ Inserted product image for product ID: {}", image.getProductId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert product image", e);
            throw new RuntimeException("Failed to insert product image", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public ProductImage findById(int imageId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            ProductImage image = em.find(ProductImage.class, imageId);
            if (image != null) {
                logger.debug("✓ Found product image by ID: {}", imageId);
            } else {
                logger.warn("✗ Product image not found with ID: {}", imageId);
            }
            return image;
        } catch (Exception e) {
            logger.error("✗ Error finding product image by ID: {}", imageId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Product ID
    public List<ProductImage> findByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT pi FROM ProductImage pi WHERE pi.productId = :productId ORDER BY pi.sortOrder";
            TypedQuery<ProductImage> query = em.createQuery(jpql, ProductImage.class);
            query.setParameter("productId", productId);
            List<ProductImage> images = query.getResultList();
            logger.debug("✓ Found {} image(s) for product ID: {}", images.size(), productId);
            return images;
        } catch (Exception e) {
            logger.error("✗ Error finding images by product ID: {}", productId, e);
            throw new RuntimeException("Failed to find product images", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm ảnh chính của product
    public ProductImage findPrimaryByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT pi FROM ProductImage pi WHERE pi.productId = :productId AND pi.isPrimary = true";
            TypedQuery<ProductImage> query = em.createQuery(jpql, ProductImage.class);
            query.setParameter("productId", productId);
            ProductImage image = query.getSingleResult();
            logger.debug("✓ Found primary image for product ID: {}", productId);
            return image;
        } catch (NoResultException e) {
            logger.debug("✗ Primary image not found for product ID: {}", productId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding primary image for product ID: {}", productId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Variant ID
    public List<ProductImage> findByVariantId(int variantId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT pi FROM ProductImage pi WHERE pi.variantId = :variantId ORDER BY pi.sortOrder";
            TypedQuery<ProductImage> query = em.createQuery(jpql, ProductImage.class);
            query.setParameter("variantId", variantId);
            List<ProductImage> images = query.getResultList();
            logger.debug("✓ Found {} image(s) for variant ID: {}", images.size(), variantId);
            return images;
        } catch (Exception e) {
            logger.error("✗ Error finding images by variant ID: {}", variantId, e);
            throw new RuntimeException("Failed to find variant images", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<ProductImage> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<ProductImage> images = em.createQuery("SELECT pi FROM ProductImage pi", ProductImage.class).getResultList();
            logger.debug("✓ Retrieved {} product image(s)", images.size());
            return images;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all product images", e);
            throw new RuntimeException("Failed to retrieve product images", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(ProductImage image) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(image);
            trans.commit();
            logger.info("✓ Updated product image ID: {}", image.getImageId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update product image", e);
            throw new RuntimeException("Failed to update product image", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int imageId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            ProductImage image = em.find(ProductImage.class, imageId);
            if (image != null) {
                em.remove(image);
                logger.info("✓ Deleted product image ID: {}", imageId);
            } else {
                logger.warn("✗ Cannot delete - product image not found with ID: {}", imageId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete product image ID: {}", imageId, e);
            throw new RuntimeException("Failed to delete product image", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa tất cả ảnh của product
    public void deleteByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM ProductImage pi WHERE pi.productId = :productId";
            int deleted = em.createQuery(jpql).setParameter("productId", productId).executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} image(s) for product ID: {}", deleted, productId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete images for product ID: {}", productId, e);
            throw new RuntimeException("Failed to delete product images", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(pi) FROM ProductImage pi", Long.class).getSingleResult();
            logger.debug("✓ Total product images: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting product images", e);
            throw new RuntimeException("Failed to count product images", e);
        } finally {
            em.close();
        }
    }
}

