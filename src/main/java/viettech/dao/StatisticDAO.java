package viettech.dao;
}
    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to count statistics", e);
            logger.error("✗ Error counting statistics", e);
        } catch (Exception e) {
            return count;
            logger.debug("✓ Total statistics count: {}", count);
            Long count = em.createQuery("SELECT COUNT(s) FROM Statistic s", Long.class).getSingleResult();
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public long count() {
    // COUNT

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to delete statistic", e);
            logger.error("✗ Failed to delete statistic ID: {}", statId, e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            trans.commit();
            }
                logger.warn("✗ Cannot delete - statistic not found with ID: {}", statId);
            } else {
                logger.info("✓ Deleted statistic ID: {}", statId);
                em.remove(statistic);
            if (statistic != null) {
            Statistic statistic = em.find(Statistic.class, statId);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void delete(int statId) {
    // DELETE

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to update statistic", e);
            logger.error("✗ Failed to update statistic ID: {}", statistic.getStatId(), e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            logger.info("✓ Updated statistic ID: {}", statistic.getStatId());
            trans.commit();
            em.merge(statistic);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void update(Statistic statistic) {
    // UPDATE

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to retrieve statistics", e);
            logger.error("✗ Error retrieving all statistics", e);
        } catch (Exception e) {
            return statistics;
            logger.debug("✓ Retrieved {} statistic(s)", statistics.size());
            List<Statistic> statistics = em.createQuery("SELECT s FROM Statistic s ORDER BY s.startDate DESC", Statistic.class).getResultList();
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<Statistic> findAll() {
    // READ - Lấy tất cả

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find statistics by date range", e);
            logger.error("✗ Error finding statistics by date range", e);
        } catch (Exception e) {
            return statistics;
            logger.debug("✓ Found {} statistic(s) in date range", statistics.size());
            List<Statistic> statistics = query.getResultList();
            query.setParameter("endDate", endDate);
            query.setParameter("startDate", startDate);
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            String jpql = "SELECT s FROM Statistic s WHERE s.startDate >= :startDate AND s.endDate <= :endDate ORDER BY s.startDate DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<Statistic> findByDateRange(Date startDate, Date endDate) {
    // READ - Tìm theo khoảng thời gian

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find statistics by period", e);
            logger.error("✗ Error finding statistics by period: {}", period, e);
        } catch (Exception e) {
            return statistics;
            logger.debug("✓ Found {} statistic(s) for period: {}", statistics.size(), period);
            List<Statistic> statistics = query.getResultList();
            query.setParameter("period", period);
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            String jpql = "SELECT s FROM Statistic s WHERE s.period = :period ORDER BY s.startDate DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<Statistic> findByPeriod(String period) {
    // READ - Tìm theo Period

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find statistics by vendor", e);
            logger.error("✗ Error finding statistics by vendor ID: {}", vendorId, e);
        } catch (Exception e) {
            return statistics;
            logger.debug("✓ Found {} statistic(s) for vendor ID: {}", statistics.size(), vendorId);
            List<Statistic> statistics = query.getResultList();
            query.setParameter("vendorId", vendorId);
            TypedQuery<Statistic> query = em.createQuery(jpql, Statistic.class);
            String jpql = "SELECT s FROM Statistic s WHERE s.vendorId = :vendorId ORDER BY s.startDate DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<Statistic> findByVendorId(int vendorId) {
    // READ - Tìm theo Vendor ID

    }
        }
            em.close();
        } finally {
            return null;
            logger.error("✗ Error finding statistic by ID: {}", statId, e);
        } catch (Exception e) {
            return statistic;
            }
                logger.warn("✗ Statistic not found with ID: {}", statId);
            } else {
                logger.debug("✓ Found statistic by ID: {}", statId);
            if (statistic != null) {
            Statistic statistic = em.find(Statistic.class, statId);
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public Statistic findById(int statId) {
    // READ - Tìm theo ID

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to insert statistic", e);
            logger.error("✗ Failed to insert statistic", e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            logger.info("✓ Inserted new statistic for vendor ID: {}", statistic.getVendorId());
            trans.commit();
            em.persist(statistic);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void insert(Statistic statistic) {
    // CREATE

    private static final Logger logger = LoggerFactory.getLogger(StatisticDAO.class);

public class StatisticDAO {
 */
 * @author VietTech Team
 * Statistic DAO - Data Access Object for Statistic entity
/**

import java.util.List;
import java.util.Date;
import javax.persistence.TypedQuery;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;

import viettech.entity.transaction.Statistic;
import viettech.config.JPAConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


