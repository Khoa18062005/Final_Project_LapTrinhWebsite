package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.VariantAttribute;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * VariantAttribute DAO - Data Access Object for VariantAttribute entity
 * @author VietTech Team
 */
public class VariantAttributeDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantAttributeDAO.class);

    // CREATE
    public void insert(VariantAttribute attribute) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(attribute);
            trans.commit();
            logger.info("✓ Inserted variant attribute: {} = {}", attribute.getAttributeName(), attribute.getAttributeValue());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert variant attribute", e);
            throw new RuntimeException("Failed to insert variant attribute", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public VariantAttribute findById(int attributeId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            VariantAttribute attribute = em.find(VariantAttribute.class, attributeId);
            if (attribute != null) {
                logger.debug("✓ Found variant attribute by ID: {}", attributeId);
            } else {
                logger.warn("✗ Variant attribute not found with ID: {}", attributeId);
            }
            return attribute;
        } catch (Exception e) {
            logger.error("✗ Error finding variant attribute by ID: {}", attributeId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Variant ID
    public List<VariantAttribute> findByVariantId(int variantId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT va FROM VariantAttribute va WHERE va.variantId = :variantId ORDER BY va.sortOrder";
            TypedQuery<VariantAttribute> query = em.createQuery(jpql, VariantAttribute.class);
            query.setParameter("variantId", variantId);
            List<VariantAttribute> attributes = query.getResultList();
            logger.debug("✓ Found {} attribute(s) for variant ID: {}", attributes.size(), variantId);
            return attributes;
        } catch (Exception e) {
            logger.error("✗ Error finding attributes by variant ID: {}", variantId, e);
            throw new RuntimeException("Failed to find variant attributes", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo attribute name
    public List<VariantAttribute> findByAttributeName(String attributeName) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT va FROM VariantAttribute va WHERE va.attributeName = :attributeName";
            TypedQuery<VariantAttribute> query = em.createQuery(jpql, VariantAttribute.class);
            query.setParameter("attributeName", attributeName);
            List<VariantAttribute> attributes = query.getResultList();
            logger.debug("✓ Found {} attribute(s) with name: {}", attributes.size(), attributeName);
            return attributes;
        } catch (Exception e) {
            logger.error("✗ Error finding attributes by name: {}", attributeName, e);
            throw new RuntimeException("Failed to find variant attributes by name", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<VariantAttribute> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<VariantAttribute> attributes = em.createQuery("SELECT va FROM VariantAttribute va", VariantAttribute.class).getResultList();
            logger.debug("✓ Retrieved {} variant attribute(s)", attributes.size());
            return attributes;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all variant attributes", e);
            throw new RuntimeException("Failed to retrieve variant attributes", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(VariantAttribute attribute) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(attribute);
            trans.commit();
            logger.info("✓ Updated variant attribute ID: {}", attribute.getAttributeId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update variant attribute", e);
            throw new RuntimeException("Failed to update variant attribute", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int attributeId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            VariantAttribute attribute = em.find(VariantAttribute.class, attributeId);
            if (attribute != null) {
                em.remove(attribute);
                logger.info("✓ Deleted variant attribute ID: {}", attributeId);
            } else {
                logger.warn("✗ Cannot delete - variant attribute not found with ID: {}", attributeId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete variant attribute ID: {}", attributeId, e);
            throw new RuntimeException("Failed to delete variant attribute", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa tất cả attributes của variant
    public void deleteByVariantId(int variantId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM VariantAttribute va WHERE va.variantId = :variantId";
            int deleted = em.createQuery(jpql).setParameter("variantId", variantId).executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} attribute(s) for variant ID: {}", deleted, variantId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete attributes for variant ID: {}", variantId, e);
            throw new RuntimeException("Failed to delete variant attributes", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(va) FROM VariantAttribute va", Long.class).getSingleResult();
            logger.debug("✓ Total variant attributes: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting variant attributes", e);
            throw new RuntimeException("Failed to count variant attributes", e);
        } finally {
            em.close();
        }
    }
}

