package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.ProductApproval;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * ProductApproval DAO - Data Access Object for ProductApproval entity
 * @author VietTech Team
 */
public class ProductApprovalDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductApprovalDAO.class);

    // CREATE
    public void insert(ProductApproval approval) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(approval);
            trans.commit();
            logger.info("✓ Inserted product approval request: {} for vendor ID: {}",
                approval.getActionType(), approval.getVendorId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert product approval", e);
            throw new RuntimeException("Failed to insert product approval", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public ProductApproval findById(int approvalId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            ProductApproval approval = em.find(ProductApproval.class, approvalId);
            if (approval != null) {
                logger.debug("✓ Found product approval by ID: {}", approvalId);
            } else {
                logger.warn("✗ Product approval not found with ID: {}", approvalId);
            }
            return approval;
        } catch (Exception e) {
            logger.error("✗ Error finding product approval by ID: {}", approvalId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID
    public List<ProductApproval> findByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT pa FROM ProductApproval pa WHERE pa.vendorId = :vendorId ORDER BY pa.requestedAt DESC";
            TypedQuery<ProductApproval> query = em.createQuery(jpql, ProductApproval.class);
            query.setParameter("vendorId", vendorId);
            List<ProductApproval> approvals = query.getResultList();
            logger.debug("✓ Found {} approval(s) for vendor ID: {}", approvals.size(), vendorId);
            return approvals;
        } catch (Exception e) {
            logger.error("✗ Error finding approvals by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to find product approvals", e);
        } finally {
            em.close();
        }
    }

    // READ - Đếm pending approvals theo Vendor ID
    public long countPendingByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(pa) FROM ProductApproval pa WHERE pa.vendorId = :vendorId AND pa.status = 'PENDING'";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("vendorId", vendorId);
            Long count = query.getSingleResult();
            logger.debug("✓ Counted {} pending approval(s) for vendor ID: {}", count, vendorId);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting pending approvals by vendor ID: {}", vendorId, e);
            return 0;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<ProductApproval> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<ProductApproval> approvals = em.createQuery(
                "SELECT pa FROM ProductApproval pa ORDER BY pa.requestedAt DESC",
                ProductApproval.class
            ).getResultList();
            logger.debug("✓ Retrieved {} product approval(s)", approvals.size());
            return approvals;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all product approvals", e);
            throw new RuntimeException("Failed to retrieve product approvals", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(ProductApproval approval) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(approval);
            trans.commit();
            logger.info("✓ Updated product approval ID: {} to status: {}",
                approval.getApprovalId(), approval.getStatus());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update product approval ID: {}", approval.getApprovalId(), e);
            throw new RuntimeException("Failed to update product approval", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int approvalId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            ProductApproval approval = em.find(ProductApproval.class, approvalId);
            if (approval != null) {
                em.remove(approval);
                logger.info("✓ Deleted product approval ID: {}", approvalId);
            } else {
                logger.warn("✗ Cannot delete - product approval not found with ID: {}", approvalId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete product approval ID: {}", approvalId, e);
            throw new RuntimeException("Failed to delete product approval", e);
        } finally {
            em.close();
        }
    }
}

