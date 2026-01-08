package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.review.Review;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Review DAO - Data Access Object for Review entity
 * @author VietTech Team
 */
public class ReviewDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReviewDAO.class);

    // CREATE
    public void insert(Review review) {
        logger.info(">>> DAO insert called for productId: {}, customerId: {}", review.getProductId(), review.getCustomerId());
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            logger.info(">>> Transaction started");
            em.persist(review);
            logger.info(">>> Persist called");
            trans.commit();
            logger.info(">>> Transaction committed");
            logger.info("✓ Inserted review for product ID: {}", review.getProductId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert review - Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert review", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Review findById(int reviewId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Review review = em.find(Review.class, reviewId);
            if (review != null) {
                logger.debug("✓ Found review by ID: {}", reviewId);
            } else {
                logger.warn("✗ Review not found with ID: {}", reviewId);
            }
            return review;
        } catch (Exception e) {
            logger.error("✗ Error finding review by ID: {}", reviewId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Product ID
    public List<Review> findByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT r FROM Review r WHERE r.productId = :productId ORDER BY r.reviewDate DESC";
            TypedQuery<Review> query = em.createQuery(jpql, Review.class);
            query.setParameter("productId", productId);
            List<Review> reviews = query.getResultList();
            logger.debug("✓ Found {} review(s) for product ID: {}", reviews.size(), productId);
            return reviews;
        } catch (Exception e) {
            logger.error("✗ Error finding reviews by product ID: {}", productId, e);
            throw new RuntimeException("Failed to find reviews", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public List<Review> findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT r FROM Review r WHERE r.customerId = :customerId ORDER BY r.reviewDate DESC";
            TypedQuery<Review> query = em.createQuery(jpql, Review.class);
            query.setParameter("customerId", customerId);
            List<Review> reviews = query.getResultList();
            logger.debug("✓ Found {} review(s) for customer ID: {}", reviews.size(), customerId);
            return reviews;
        } catch (Exception e) {
            logger.error("✗ Error finding reviews by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find reviews by customer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Rating
    public List<Review> findByRating(int rating) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT r FROM Review r WHERE r.rating = :rating ORDER BY r.reviewDate DESC";
            TypedQuery<Review> query = em.createQuery(jpql, Review.class);
            query.setParameter("rating", rating);
            List<Review> reviews = query.getResultList();
            logger.debug("✓ Found {} review(s) with rating: {}", reviews.size(), rating);
            return reviews;
        } catch (Exception e) {
            logger.error("✗ Error finding reviews by rating: {}", rating, e);
            throw new RuntimeException("Failed to find reviews by rating", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Review> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Review> reviews = em.createQuery("SELECT r FROM Review r ORDER BY r.reviewDate DESC", Review.class).getResultList();
            logger.debug("✓ Retrieved {} review(s)", reviews.size());
            return reviews;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all reviews", e);
            throw new RuntimeException("Failed to retrieve reviews", e);
        } finally {
            em.close();
        }
    }

    // READ - Tính trung bình rating theo Product ID
    public Double getAverageRatingByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId";
            Double avgRating = em.createQuery(jpql, Double.class).setParameter("productId", productId).getSingleResult();
            logger.debug("✓ Average rating for product {}: {}", productId, avgRating);
            return avgRating != null ? avgRating : 0.0;
        } catch (Exception e) {
            logger.error("✗ Error calculating average rating for product ID: {}", productId, e);
            return 0.0;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Review review) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(review);
            trans.commit();
            logger.info("✓ Updated review ID: {}", review.getReviewId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update review", e);
            throw new RuntimeException("Failed to update review", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int reviewId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Review review = em.find(Review.class, reviewId);
            if (review != null) {
                em.remove(review);
                logger.info("✓ Deleted review ID: {}", reviewId);
            } else {
                logger.warn("✗ Cannot delete - review not found with ID: {}", reviewId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete review ID: {}", reviewId, e);
            throw new RuntimeException("Failed to delete review", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(r) FROM Review r", Long.class).getSingleResult();
            logger.debug("✓ Total reviews: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting reviews", e);
            throw new RuntimeException("Failed to count reviews", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm reviews theo product
    public long countByProductId(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Review r WHERE r.productId = :productId";
            Long count = em.createQuery(jpql, Long.class).setParameter("productId", productId).getSingleResult();
            logger.debug("✓ Reviews for product {}: {}", productId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting reviews for product ID: {}", productId, e);
            throw new RuntimeException("Failed to count reviews", e);
        } finally {
            em.close();
        }
    }
}

