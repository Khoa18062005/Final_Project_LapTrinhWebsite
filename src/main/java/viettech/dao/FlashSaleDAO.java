package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.voucher.FlashSale;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * FlashSale DAO - Data Access Object for FlashSale entity
 * @author VietTech Team
 */
public class FlashSaleDAO {

    private static final Logger logger = LoggerFactory.getLogger(FlashSaleDAO.class);

    // CREATE
    public void insert(FlashSale flashSale) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(flashSale);
            trans.commit();
            logger.info("✓ Inserted new flash sale: {}", flashSale.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert flash sale: {}", flashSale.getName(), e);
            throw new RuntimeException("Failed to insert flash sale", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public FlashSale findById(int saleId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            FlashSale flashSale = em.find(FlashSale.class, saleId);
            if (flashSale != null) {
                logger.debug("✓ Found flash sale by ID: {}", saleId);
            } else {
                logger.warn("✗ Flash sale not found with ID: {}", saleId);
            }
            return flashSale;
        } catch (Exception e) {
            logger.error("✗ Error finding flash sale by ID: {}", saleId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Product ID
    public List<FlashSale> findByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT fs FROM FlashSale fs WHERE fs.productId = :productId ORDER BY fs.startTime DESC";
            TypedQuery<FlashSale> query = em.createQuery(jpql, FlashSale.class);
            query.setParameter("productId", productId);
            List<FlashSale> sales = query.getResultList();
            logger.debug("✓ Found {} flash sale(s) for product ID: {}", sales.size(), productId);
            return sales;
        } catch (Exception e) {
            logger.error("✗ Error finding flash sales by product ID: {}", productId, e);
            throw new RuntimeException("Failed to find flash sales by product", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm flash sale đang active
    public List<FlashSale> findActive() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Date now = new Date();
            String jpql = "SELECT fs FROM FlashSale fs WHERE fs.isActive = true AND fs.startTime <= :now AND fs.endTime >= :now";
            TypedQuery<FlashSale> query = em.createQuery(jpql, FlashSale.class);
            query.setParameter("now", now);
            List<FlashSale> sales = query.getResultList();
            logger.debug("✓ Found {} active flash sale(s)", sales.size());
            return sales;
        } catch (Exception e) {
            logger.error("✗ Error finding active flash sales", e);
            throw new RuntimeException("Failed to find active flash sales", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm flash sale sắp diễn ra
    public List<FlashSale> findUpcoming() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Date now = new Date();
            String jpql = "SELECT fs FROM FlashSale fs WHERE fs.isActive = true AND fs.startTime > :now ORDER BY fs.startTime";
            TypedQuery<FlashSale> query = em.createQuery(jpql, FlashSale.class);
            query.setParameter("now", now);
            List<FlashSale> sales = query.getResultList();
            logger.debug("✓ Found {} upcoming flash sale(s)", sales.size());
            return sales;
        } catch (Exception e) {
            logger.error("✗ Error finding upcoming flash sales", e);
            throw new RuntimeException("Failed to find upcoming flash sales", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<FlashSale> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<FlashSale> sales = em.createQuery("SELECT fs FROM FlashSale fs ORDER BY fs.startTime DESC", FlashSale.class).getResultList();
            logger.debug("✓ Retrieved {} flash sale(s)", sales.size());
            return sales;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all flash sales", e);
            throw new RuntimeException("Failed to retrieve flash sales", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(FlashSale flashSale) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(flashSale);
            trans.commit();
            logger.info("✓ Updated flash sale: {}", flashSale.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update flash sale: {}", flashSale.getName(), e);
            throw new RuntimeException("Failed to update flash sale", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int saleId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            FlashSale flashSale = em.find(FlashSale.class, saleId);
            if (flashSale != null) {
                em.remove(flashSale);
                logger.info("✓ Deleted flash sale ID: {} ({})", saleId, flashSale.getName());
            } else {
                logger.warn("✗ Cannot delete - flash sale not found with ID: {}", saleId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete flash sale ID: {}", saleId, e);
            throw new RuntimeException("Failed to delete flash sale", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(fs) FROM FlashSale fs", Long.class).getSingleResult();
            logger.debug("✓ Total flash sales count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting flash sales", e);
            throw new RuntimeException("Failed to count flash sales", e);
        } finally {
            em.close();
        }
    }
}

