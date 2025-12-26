package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.search.ProductView;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * ProductView DAO - Data Access Object for ProductView entity
 * @author VietTech Team
 */
public class ProductViewDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductViewDAO.class);

    // CREATE
    public void insert(ProductView productView) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(productView);
            trans.commit();
            logger.info("✓ Inserted new product view for product ID: {}", productView.getProductId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert product view", e);
            throw new RuntimeException("Failed to insert product view", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public ProductView findById(int viewId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            ProductView view = em.find(ProductView.class, viewId);
            if (view != null) {
                logger.debug("✓ Found product view by ID: {}", viewId);
            } else {
                logger.warn("✗ Product view not found with ID: {}", viewId);
            }
            return view;
        } catch (Exception e) {
            logger.error("✗ Error finding product view by ID: {}", viewId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Product ID
    public List<ProductView> findByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT pv FROM ProductView pv WHERE pv.productId = :productId ORDER BY pv.viewDate DESC";
            TypedQuery<ProductView> query = em.createQuery(jpql, ProductView.class);
            query.setParameter("productId", productId);
            List<ProductView> views = query.getResultList();
            logger.debug("✓ Found {} view(s) for product ID: {}", views.size(), productId);
            return views;
        } catch (Exception e) {
            logger.error("✗ Error finding views by product ID: {}", productId, e);
            throw new RuntimeException("Failed to find views by product", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public List<ProductView> findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT pv FROM ProductView pv WHERE pv.customerId = :customerId ORDER BY pv.viewDate DESC";
            TypedQuery<ProductView> query = em.createQuery(jpql, ProductView.class);
            query.setParameter("customerId", customerId);
            List<ProductView> views = query.getResultList();
            logger.debug("✓ Found {} view(s) for customer ID: {}", views.size(), customerId);
            return views;
        } catch (Exception e) {
            logger.error("✗ Error finding views by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find views by customer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng thời gian
    public List<ProductView> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT pv FROM ProductView pv WHERE pv.viewDate BETWEEN :startDate AND :endDate ORDER BY pv.viewDate DESC";
            TypedQuery<ProductView> query = em.createQuery(jpql, ProductView.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<ProductView> views = query.getResultList();
            logger.debug("✓ Found {} view(s) in date range", views.size());
            return views;
        } catch (Exception e) {
            logger.error("✗ Error finding views by date range", e);
            throw new RuntimeException("Failed to find views by date range", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<ProductView> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<ProductView> views = em.createQuery("SELECT pv FROM ProductView pv ORDER BY pv.viewDate DESC", ProductView.class).getResultList();
            logger.debug("✓ Retrieved {} product view(s)", views.size());
            return views;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all product views", e);
            throw new RuntimeException("Failed to retrieve product views", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(ProductView productView) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(productView);
            trans.commit();
            logger.info("✓ Updated product view ID: {}", productView.getViewId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update product view ID: {}", productView.getViewId(), e);
            throw new RuntimeException("Failed to update product view", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int viewId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            ProductView view = em.find(ProductView.class, viewId);
            if (view != null) {
                em.remove(view);
                logger.info("✓ Deleted product view ID: {}", viewId);
            } else {
                logger.warn("✗ Cannot delete - product view not found with ID: {}", viewId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete product view ID: {}", viewId, e);
            throw new RuntimeException("Failed to delete product view", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm views theo product
    public long countByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(pv) FROM ProductView pv WHERE pv.productId = :productId";
            Long count = em.createQuery(jpql, Long.class).setParameter("productId", productId).getSingleResult();
            logger.debug("✓ Product ID {} has {} view(s)", productId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting views for product ID: {}", productId, e);
            throw new RuntimeException("Failed to count product views", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(pv) FROM ProductView pv", Long.class).getSingleResult();
            logger.debug("✓ Total product views count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting product views", e);
            throw new RuntimeException("Failed to count product views", e);
        } finally {
            em.close();
        }
    }
}

