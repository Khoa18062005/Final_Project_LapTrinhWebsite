package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.voucher.Voucher;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Voucher DAO - Data Access Object for Voucher entity
 * @author VietTech Team
 */
public class VoucherDAO {

    private static final Logger logger = LoggerFactory.getLogger(VoucherDAO.class);

    // CREATE
    public void insert(Voucher voucher) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(voucher);
            trans.commit();
            logger.info("✓ Inserted new voucher: {}", voucher.getCode());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert voucher: {}", voucher.getCode(), e);
            throw new RuntimeException("Failed to insert voucher", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Voucher findById(int voucherId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Voucher voucher = em.find(Voucher.class, voucherId);
            if (voucher != null) {
                logger.debug("✓ Found voucher by ID: {}", voucherId);
            } else {
                logger.warn("✗ Voucher not found with ID: {}", voucherId);
            }
            return voucher;
        } catch (Exception e) {
            logger.error("✗ Error finding voucher by ID: {}", voucherId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Code
    public Voucher findByCode(String code) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Voucher v WHERE v.code = :code";
            TypedQuery<Voucher> query = em.createQuery(jpql, Voucher.class);
            query.setParameter("code", code);
            Voucher voucher = query.getSingleResult();
            logger.debug("✓ Found voucher by code: {}", code);
            return voucher;
        } catch (NoResultException e) {
            logger.debug("✗ Voucher not found with code: {}", code);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding voucher by code: {}", code, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm voucher đang active và valid
    public List<Voucher> findActiveAndValid() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Date now = new Date();
            String jpql = "SELECT v FROM Voucher v WHERE v.isActive = true AND v.startDate <= :now AND v.expiryDate >= :now AND v.usageCount < v.usageLimit";
            TypedQuery<Voucher> query = em.createQuery(jpql, Voucher.class);
            query.setParameter("now", now);
            List<Voucher> vouchers = query.getResultList();
            logger.debug("✓ Found {} active and valid voucher(s)", vouchers.size());
            return vouchers;
        } catch (Exception e) {
            logger.error("✗ Error finding active vouchers", e);
            throw new RuntimeException("Failed to find active vouchers", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Type
    public List<Voucher> findByType(String type) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Voucher v WHERE v.type = :type ORDER BY v.startDate DESC";
            TypedQuery<Voucher> query = em.createQuery(jpql, Voucher.class);
            query.setParameter("type", type);
            List<Voucher> vouchers = query.getResultList();
            logger.debug("✓ Found {} voucher(s) with type: {}", vouchers.size(), type);
            return vouchers;
        } catch (Exception e) {
            logger.error("✗ Error finding vouchers by type: {}", type, e);
            throw new RuntimeException("Failed to find vouchers by type", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Voucher> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Voucher> vouchers = em.createQuery("SELECT v FROM Voucher v ORDER BY v.startDate DESC", Voucher.class).getResultList();
            logger.debug("✓ Retrieved {} voucher(s)", vouchers.size());
            return vouchers;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all vouchers", e);
            throw new RuntimeException("Failed to retrieve vouchers", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Voucher voucher) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(voucher);
            trans.commit();
            logger.info("✓ Updated voucher: {}", voucher.getCode());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update voucher: {}", voucher.getCode(), e);
            throw new RuntimeException("Failed to update voucher", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int voucherId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Voucher voucher = em.find(Voucher.class, voucherId);
            if (voucher != null) {
                em.remove(voucher);
                logger.info("✓ Deleted voucher ID: {} ({})", voucherId, voucher.getCode());
            } else {
                logger.warn("✗ Cannot delete - voucher not found with ID: {}", voucherId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete voucher ID: {}", voucherId, e);
            throw new RuntimeException("Failed to delete voucher", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(v) FROM Voucher v", Long.class).getSingleResult();
            logger.debug("✓ Total vouchers count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting vouchers", e);
            throw new RuntimeException("Failed to count vouchers", e);
        } finally {
            em.close();
        }
    }

    /**
     * Đếm số lần user đã sử dụng voucher
     */
    public long countUserUsage(int voucherId, int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(vu) FROM VoucherUsage vu WHERE vu.voucherId = :voucherId AND vu.customerId = :customerId";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("voucherId", voucherId);
            query.setParameter("customerId", customerId);
            Long count = query.getSingleResult();
            logger.debug("✓ User {} has used voucher {} : {} times", customerId, voucherId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting user usage for voucher: {} and user: {}", voucherId, customerId, e);
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Kiểm tra xem user còn được dùng voucher này không
     */
    public boolean canUserUseVoucher(int voucherId, int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            // Lấy thông tin voucher
            Voucher voucher = em.find(Voucher.class, voucherId);
            if (voucher == null || !voucher.isActive()) {
                logger.debug("✗ Voucher {} is not active or not found", voucherId);
                return false;
            }

            // Kiểm tra thời gian
            Date now = new Date();
            if (voucher.getStartDate().after(now) || voucher.getExpiryDate().before(now)) {
                logger.debug("✗ Voucher {} is expired or not started yet", voucherId);
                return false;
            }

            // Kiểm tra usage limit toàn server
            if (voucher.getUsageCount() >= voucher.getUsageLimit()) {
                logger.debug("✗ Voucher {} has reached server-wide usage limit", voucherId);
                return false;
            }

            // Kiểm tra usage limit per user
            long userUsageCount = countUserUsage(voucherId, customerId);
            if (userUsageCount >= voucher.getUsageLimitPerUser()) {
                logger.debug("✗ User {} has reached usage limit for voucher {}", customerId, voucherId);
                return false;
            }

            logger.debug("✓ User {} can use voucher {}", customerId, voucherId);
            return true;

        } catch (Exception e) {
            logger.error("✗ Error checking if user can use voucher: {} for user: {}", voucherId, customerId, e);
            return false;
        } finally {
            em.close();
        }
    }
}

