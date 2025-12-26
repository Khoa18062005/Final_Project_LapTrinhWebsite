package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.review.ReviewResponse;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * ReviewResponse DAO - Data Access Object for ReviewResponse entity
 * @author VietTech Team
 */
public class ReviewResponseDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReviewResponseDAO.class);

    // CREATE
    public void insert(ReviewResponse response) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(response);
            trans.commit();
            logger.info("✓ Inserted review response for review ID: {}", response.getReviewId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert review response", e);
            throw new RuntimeException("Failed to insert review response", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public ReviewResponse findById(int responseId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            ReviewResponse response = em.find(ReviewResponse.class, responseId);
            if (response != null) {
                logger.debug("✓ Found review response by ID: {}", responseId);
            } else {
                logger.warn("✗ Review response not found with ID: {}", responseId);
            }
            return response;
        } catch (Exception e) {
            logger.error("✗ Error finding review response by ID: {}", responseId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Review ID
    public List<ReviewResponse> findByReviewId(int reviewId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT rr FROM ReviewResponse rr WHERE rr.reviewId = :reviewId ORDER BY rr.responseDate DESC";
            TypedQuery<ReviewResponse> query = em.createQuery(jpql, ReviewResponse.class);
            query.setParameter("reviewId", reviewId);
            List<ReviewResponse> responses = query.getResultList();
            logger.debug("✓ Found {} response(s) for review ID: {}", responses.size(), reviewId);
            return responses;
        } catch (Exception e) {
            logger.error("✗ Error finding responses by review ID: {}", reviewId, e);
            throw new RuntimeException("Failed to find review responses", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<ReviewResponse> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<ReviewResponse> responses = em.createQuery("SELECT rr FROM ReviewResponse rr ORDER BY rr.responseDate DESC", ReviewResponse.class).getResultList();
            logger.debug("✓ Retrieved {} review response(s)", responses.size());
            return responses;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all review responses", e);
            throw new RuntimeException("Failed to retrieve review responses", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(ReviewResponse response) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(response);
            trans.commit();
            logger.info("✓ Updated review response ID: {}", response.getResponseId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update review response", e);
            throw new RuntimeException("Failed to update review response", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int responseId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            ReviewResponse response = em.find(ReviewResponse.class, responseId);
            if (response != null) {
                em.remove(response);
                logger.info("✓ Deleted review response ID: {}", responseId);
            } else {
                logger.warn("✗ Cannot delete - review response not found with ID: {}", responseId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete review response ID: {}", responseId, e);
            throw new RuntimeException("Failed to delete review response", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(rr) FROM ReviewResponse rr", Long.class).getSingleResult();
            logger.debug("✓ Total review responses: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting review responses", e);
            throw new RuntimeException("Failed to count review responses", e);
        } finally {
            em.close();
        }
    }
}

