package viettech.dao;
}
    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to count product views", e);
            logger.error("✗ Error counting product views", e);
        } catch (Exception e) {
            return count;
            logger.debug("✓ Total product views count: {}", count);
            Long count = em.createQuery("SELECT COUNT(pv) FROM ProductView pv", Long.class).getSingleResult();
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public long count() {
    // COUNT

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to count product views", e);
            logger.error("✗ Error counting views for product ID: {}", productId, e);
        } catch (Exception e) {
            return count;
            logger.debug("✓ Product ID {} has {} view(s)", productId, count);
            Long count = em.createQuery(jpql, Long.class).setParameter("productId", productId).getSingleResult();
            String jpql = "SELECT COUNT(pv) FROM ProductView pv WHERE pv.productId = :productId";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public long countByProductId(int productId) {
    // COUNT - Đếm view theo product

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to delete product view", e);
            logger.error("✗ Failed to delete product view ID: {}", viewId, e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            trans.commit();
            }
                logger.warn("✗ Cannot delete - product view not found with ID: {}", viewId);
            } else {
                logger.info("✓ Deleted product view ID: {}", viewId);
                em.remove(view);
            if (view != null) {
            ProductView view = em.find(ProductView.class, viewId);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void delete(int viewId) {
    // DELETE

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to update product view", e);
            logger.error("✗ Failed to update product view ID: {}", productView.getViewId(), e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            logger.info("✓ Updated product view ID: {}", productView.getViewId());
            trans.commit();
            em.merge(productView);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void update(ProductView productView) {
    // UPDATE

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to retrieve product views", e);
            logger.error("✗ Error retrieving all product views", e);
        } catch (Exception e) {
            return views;
            logger.debug("✓ Retrieved {} product view(s)", views.size());
            List<ProductView> views = em.createQuery("SELECT pv FROM ProductView pv ORDER BY pv.viewDate DESC", ProductView.class).getResultList();
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<ProductView> findAll() {
    // READ - Lấy tất cả

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find views by date range", e);
            logger.error("✗ Error finding views by date range", e);
        } catch (Exception e) {
            return views;
            logger.debug("✓ Found {} view(s) in date range", views.size());
            List<ProductView> views = query.getResultList();
            query.setParameter("endDate", endDate);
            query.setParameter("startDate", startDate);
            TypedQuery<ProductView> query = em.createQuery(jpql, ProductView.class);
            String jpql = "SELECT pv FROM ProductView pv WHERE pv.viewDate BETWEEN :startDate AND :endDate ORDER BY pv.viewDate DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<ProductView> findByDateRange(Date startDate, Date endDate) {
    // READ - Tìm theo khoảng thời gian

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find views by customer", e);
            logger.error("✗ Error finding views by customer ID: {}", customerId, e);
        } catch (Exception e) {
            return views;
            logger.debug("✓ Found {} view(s) for customer ID: {}", views.size(), customerId);
            List<ProductView> views = query.getResultList();
            query.setParameter("customerId", customerId);
            TypedQuery<ProductView> query = em.createQuery(jpql, ProductView.class);
            String jpql = "SELECT pv FROM ProductView pv WHERE pv.customerId = :customerId ORDER BY pv.viewDate DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<ProductView> findByCustomerId(int customerId) {
    // READ - Tìm theo Customer ID

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to find views by product", e);
            logger.error("✗ Error finding views by product ID: {}", productId, e);
        } catch (Exception e) {
            return views;
            logger.debug("✓ Found {} view(s) for product ID: {}", views.size(), productId);
            List<ProductView> views = query.getResultList();
            query.setParameter("productId", productId);
            TypedQuery<ProductView> query = em.createQuery(jpql, ProductView.class);
            String jpql = "SELECT pv FROM ProductView pv WHERE pv.productId = :productId ORDER BY pv.viewDate DESC";
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public List<ProductView> findByProductId(int productId) {
    // READ - Tìm theo Product ID

    }
        }
            em.close();
        } finally {
            return null;
            logger.error("✗ Error finding product view by ID: {}", viewId, e);
        } catch (Exception e) {
            return view;
            }
                logger.warn("✗ Product view not found with ID: {}", viewId);
            } else {
                logger.debug("✓ Found product view by ID: {}", viewId);
            if (view != null) {
            ProductView view = em.find(ProductView.class, viewId);
        try {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public ProductView findById(int viewId) {
    // READ - Tìm theo ID

    }
        }
            em.close();
        } finally {
            throw new RuntimeException("Failed to insert product view", e);
            logger.error("✗ Failed to insert product view", e);
            if (trans.isActive()) trans.rollback();
        } catch (Exception e) {
            logger.info("✓ Inserted new product view for product ID: {}", productView.getProductId());
            trans.commit();
            em.persist(productView);
            trans.begin();
        try {
        EntityTransaction trans = em.getTransaction();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
    public void insert(ProductView productView) {
    // CREATE

    private static final Logger logger = LoggerFactory.getLogger(ProductViewDAO.class);

public class ProductViewDAO {
 */
 * @author VietTech Team
 * ProductView DAO - Data Access Object for ProductView entity
/**

import java.util.List;
import java.util.Date;
import javax.persistence.TypedQuery;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;

import viettech.entity.search.ProductView;
import viettech.config.JPAConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


