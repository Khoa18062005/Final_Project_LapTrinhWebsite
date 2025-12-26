package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.voucher.VoucherUsage;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * VoucherUsage DAO - Data Access Object for VoucherUsage entity
 * @author VietTech Team
 */
public class VoucherUsageDAO {

    private static final Logger logger = LoggerFactory.getLogger(VoucherUsageDAO.class);

    // CREATE
    public void insert(VoucherUsage usage) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(usage);
            trans.commit();
            logger.info("✓ Inserted new voucher usage for voucher ID: {}", usage.getVoucherId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert voucher usage", e);
            throw new RuntimeException("Failed to insert voucher usage", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public VoucherUsage findById(int usageId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            VoucherUsage usage = em.find(VoucherUsage.class, usageId);
            if (usage != null) {
                logger.debug("✓ Found voucher usage by ID: {}", usageId);
            } else {
                logger.warn("✗ Voucher usage not found with ID: {}", usageId);
            }
            return usage;
        } catch (Exception e) {
            logger.error("✗ Error finding voucher usage by ID: {}", usageId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Voucher ID
    public List<VoucherUsage> findByVoucherId(int voucherId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT vu FROM VoucherUsage vu WHERE vu.voucherId = :voucherId ORDER BY vu.usedDate DESC";
            TypedQuery<VoucherUsage> query = em.createQuery(jpql, VoucherUsage.class);
            query.setParameter("voucherId", voucherId);
            List<VoucherUsage> usages = query.getResultList();
            logger.debug("✓ Found {} usage(s) for voucher ID: {}", usages.size(), voucherId);
            return usages;
        } catch (Exception e) {
            logger.error("✗ Error finding usages by voucher ID: {}", voucherId, e);
            throw new RuntimeException("Failed to find voucher usages by voucher", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public List<VoucherUsage> findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT vu FROM VoucherUsage vu WHERE vu.customerId = :customerId ORDER BY vu.usedDate DESC";
            TypedQuery<VoucherUsage> query = em.createQuery(jpql, VoucherUsage.class);
            query.setParameter("customerId", customerId);
            List<VoucherUsage> usages = query.getResultList();
            logger.debug("✓ Found {} usage(s) for customer ID: {}", usages.size(), customerId);
            return usages;
        } catch (Exception e) {
            logger.error("✗ Error finding usages by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find voucher usages by customer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Order ID
    public VoucherUsage findByOrderId(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT vu FROM VoucherUsage vu WHERE vu.orderId = :orderId";
            TypedQuery<VoucherUsage> query = em.createQuery(jpql, VoucherUsage.class);
            query.setParameter("orderId", orderId);
            VoucherUsage usage = query.getSingleResult();
            logger.debug("✓ Found voucher usage for order ID: {}", orderId);
            return usage;
        } catch (NoResultException e) {
            logger.debug("✗ Voucher usage not found for order ID: {}", orderId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding usage by order ID: {}", orderId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Kiểm tra customer đã dùng voucher chưa
    public boolean hasCustomerUsedVoucher(int customerId, int voucherId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(vu) FROM VoucherUsage vu WHERE vu.customerId = :customerId AND vu.voucherId = :voucherId";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("customerId", customerId)
                    .setParameter("voucherId", voucherId)
                    .getSingleResult();
            boolean used = count > 0;
            logger.debug("✓ Customer {} {} voucher {}", customerId, used ? "has used" : "has not used", voucherId);
            return used;
        } catch (Exception e) {
            logger.error("✗ Error checking voucher usage", e);
            throw new RuntimeException("Failed to check voucher usage", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<VoucherUsage> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<VoucherUsage> usages = em.createQuery("SELECT vu FROM VoucherUsage vu ORDER BY vu.usedDate DESC", VoucherUsage.class).getResultList();
            logger.debug("✓ Retrieved {} voucher usage(s)", usages.size());
            return usages;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all voucher usages", e);
            throw new RuntimeException("Failed to retrieve voucher usages", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(VoucherUsage usage) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(usage);
            trans.commit();
            logger.info("✓ Updated voucher usage ID: {}", usage.getUsageId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update voucher usage ID: {}", usage.getUsageId(), e);
            throw new RuntimeException("Failed to update voucher usage", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int usageId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            VoucherUsage usage = em.find(VoucherUsage.class, usageId);
            if (usage != null) {
                em.remove(usage);
                logger.info("✓ Deleted voucher usage ID: {}", usageId);
            } else {
                logger.warn("✗ Cannot delete - voucher usage not found with ID: {}", usageId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete voucher usage ID: {}", usageId, e);
            throw new RuntimeException("Failed to delete voucher usage", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm số lần voucher được sử dụng
    public long countByVoucherId(int voucherId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(vu) FROM VoucherUsage vu WHERE vu.voucherId = :voucherId";
            Long count = em.createQuery(jpql, Long.class).setParameter("voucherId", voucherId).getSingleResult();
            logger.debug("✓ Voucher ID {} has been used {} time(s)", voucherId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting voucher usages", e);
            throw new RuntimeException("Failed to count voucher usages", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(vu) FROM VoucherUsage vu", Long.class).getSingleResult();
            logger.debug("✓ Total voucher usages count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting voucher usages", e);
            throw new RuntimeException("Failed to count voucher usages", e);
        } finally {
            em.close();
        }
    }
}

