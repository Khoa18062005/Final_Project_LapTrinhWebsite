package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.transaction.Statistic;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Statistic DAO - Data Access Object for Statistic entity
 * @author VietTech Team
 */
public class StatisticDAO {

    private static final Logger logger = LoggerFactory.getLogger(StatisticDAO.class);

    // CREATE
    public void insert(Statistic statistic) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(statistic);
            trans.commit();
            logger.info("✓ Inserted statistic for vendor ID: {} period: {}", statistic.getVendorId(), statistic.getPeriod());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert statistic", e);
            throw new RuntimeException("Failed to insert statistic", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Statistic findById(int statId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Statistic statistic = em.find(Statistic.class, statId);
            if (statistic != null) {
                logger.debug("✓ Found statistic by ID: {}", statId);
            } else {
                logger.warn("✗ Statistic not found with ID: {}", statId);
            }
            return statistic;
        } catch (Exception e) {
            logger.error("✗ Error finding statistic by ID: {}", statId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID
    public List<Statistic> findByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Statistic s WHERE s.vendorId = :vendorId ORDER BY s.generatedAt DESC";
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            query.setParameter("vendorId", vendorId);
            List<Statistic> statistics = query.getResultList();
            logger.debug("✓ Found {} statistic(s) for vendor ID: {}", statistics.size(), vendorId);
            return statistics;
        } catch (Exception e) {
            logger.error("✗ Error finding statistics by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to find statistics", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Period
    public List<Statistic> findByPeriod(String period) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Statistic s WHERE s.period = :period ORDER BY s.generatedAt DESC";
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            query.setParameter("period", period);
            List<Statistic> statistics = query.getResultList();
            logger.debug("✓ Found {} statistic(s) for period: {}", statistics.size(), period);
            return statistics;
        } catch (Exception e) {
            logger.error("✗ Error finding statistics by period: {}", period, e);
            throw new RuntimeException("Failed to find statistics by period", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID và Period
    public List<Statistic> findByVendorIdAndPeriod(int vendorId, String period) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Statistic s WHERE s.vendorId = :vendorId AND s.period = :period ORDER BY s.startDate DESC";
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            query.setParameter("vendorId", vendorId);
            query.setParameter("period", period);
            List<Statistic> statistics = query.getResultList();
            logger.debug("✓ Found {} statistic(s) for vendor {} period {}", statistics.size(), vendorId, period);
            return statistics;
        } catch (Exception e) {
            logger.error("✗ Error finding statistics", e);
            throw new RuntimeException("Failed to find statistics", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng thời gian
    public List<Statistic> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Statistic s WHERE s.startDate >= :startDate AND s.endDate <= :endDate ORDER BY s.startDate DESC";
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Statistic> statistics = query.getResultList();
            logger.debug("✓ Found {} statistic(s) in date range", statistics.size());
            return statistics;
        } catch (Exception e) {
            logger.error("✗ Error finding statistics by date range", e);
            throw new RuntimeException("Failed to find statistics by date range", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Statistic> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Statistic> statistics = em.createQuery("SELECT s FROM Statistic s ORDER BY s.generatedAt DESC", Statistic.class).getResultList();
            logger.debug("✓ Retrieved {} statistic(s)", statistics.size());
            return statistics;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all statistics", e);
            throw new RuntimeException("Failed to retrieve statistics", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy statistic mới nhất của vendor
    public Statistic findLatestByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Statistic s WHERE s.vendorId = :vendorId ORDER BY s.generatedAt DESC";
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            query.setParameter("vendorId", vendorId);
            query.setMaxResults(1);
            List<Statistic> statistics = query.getResultList();
            if (!statistics.isEmpty()) {
                logger.debug("✓ Found latest statistic for vendor ID: {}", vendorId);
                return statistics.get(0);
            }
            logger.debug("✗ No statistic found for vendor ID: {}", vendorId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding latest statistic for vendor ID: {}", vendorId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Statistic statistic) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(statistic);
            trans.commit();
            logger.info("✓ Updated statistic ID: {}", statistic.getStatId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update statistic", e);
            throw new RuntimeException("Failed to update statistic", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int statId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Statistic statistic = em.find(Statistic.class, statId);
            if (statistic != null) {
                em.remove(statistic);
                logger.info("✓ Deleted statistic ID: {}", statId);
            } else {
                logger.warn("✗ Cannot delete - statistic not found with ID: {}", statId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete statistic ID: {}", statId, e);
            throw new RuntimeException("Failed to delete statistic", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(s) FROM Statistic s", Long.class).getSingleResult();
            logger.debug("✓ Total statistics: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting statistics", e);
            throw new RuntimeException("Failed to count statistics", e);
        } finally {
            em.close();
        }
    }

    // SUM - Tổng doanh thu theo vendor
    public Double getTotalRevenueByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT SUM(s.revenue) FROM Statistic s WHERE s.vendorId = :vendorId";
            Double total = em.createQuery(jpql, Double.class).setParameter("vendorId", vendorId).getSingleResult();
            logger.debug("✓ Total revenue for vendor {}: {}", vendorId, total);
            return total != null ? total : 0.0;
        } catch (Exception e) {
            logger.error("✗ Error calculating total revenue for vendor ID: {}", vendorId, e);
            return 0.0;
        } finally {
            em.close();
        }
    }
}

