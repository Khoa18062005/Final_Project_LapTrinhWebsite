package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.review.ReviewVote;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * ReviewVote DAO - Data Access Object for ReviewVote entity
 * @author VietTech Team
 */
public class ReviewVoteDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReviewVoteDAO.class);

    // CREATE
    public void insert(ReviewVote vote) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(vote);
            trans.commit();
            logger.info("✓ Inserted review vote for review ID: {}", vote.getReviewId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert review vote", e);
            throw new RuntimeException("Failed to insert review vote", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public ReviewVote findById(int voteId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            ReviewVote vote = em.find(ReviewVote.class, voteId);
            if (vote != null) {
                logger.debug("✓ Found review vote by ID: {}", voteId);
            } else {
                logger.warn("✗ Review vote not found with ID: {}", voteId);
            }
            return vote;
        } catch (Exception e) {
            logger.error("✗ Error finding review vote by ID: {}", voteId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Review ID và User ID
    public ReviewVote findByReviewIdAndUserId(int reviewId, int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT rv FROM ReviewVote rv WHERE rv.reviewId = :reviewId AND rv.userId = :userId";
            TypedQuery<ReviewVote> query = em.createQuery(jpql, ReviewVote.class);
            query.setParameter("reviewId", reviewId);
            query.setParameter("userId", userId);
            ReviewVote vote = query.getSingleResult();
            logger.debug("✓ Found vote for review {} by user {}", reviewId, userId);
            return vote;
        } catch (NoResultException e) {
            logger.debug("✗ Vote not found for review {} by user {}", reviewId, userId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding vote", e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Review ID
    public List<ReviewVote> findByReviewId(int reviewId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT rv FROM ReviewVote rv WHERE rv.reviewId = :reviewId";
            TypedQuery<ReviewVote> query = em.createQuery(jpql, ReviewVote.class);
            query.setParameter("reviewId", reviewId);
            List<ReviewVote> votes = query.getResultList();
            logger.debug("✓ Found {} vote(s) for review ID: {}", votes.size(), reviewId);
            return votes;
        } catch (Exception e) {
            logger.error("✗ Error finding votes by review ID: {}", reviewId, e);
            throw new RuntimeException("Failed to find review votes", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<ReviewVote> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<ReviewVote> votes = em.createQuery("SELECT rv FROM ReviewVote rv", ReviewVote.class).getResultList();
            logger.debug("✓ Retrieved {} review vote(s)", votes.size());
            return votes;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all review votes", e);
            throw new RuntimeException("Failed to retrieve review votes", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(ReviewVote vote) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(vote);
            trans.commit();
            logger.info("✓ Updated review vote ID: {}", vote.getVoteId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update review vote", e);
            throw new RuntimeException("Failed to update review vote", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int voteId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            ReviewVote vote = em.find(ReviewVote.class, voteId);
            if (vote != null) {
                em.remove(vote);
                logger.info("✓ Deleted review vote ID: {}", voteId);
            } else {
                logger.warn("✗ Cannot delete - review vote not found with ID: {}", voteId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete review vote ID: {}", voteId, e);
            throw new RuntimeException("Failed to delete review vote", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm helpful votes cho review
    public long countHelpfulByReviewId(int reviewId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(rv) FROM ReviewVote rv WHERE rv.reviewId = :reviewId AND rv.voteType = 'helpful'";
            Long count = em.createQuery(jpql, Long.class).setParameter("reviewId", reviewId).getSingleResult();
            logger.debug("✓ Helpful votes for review {}: {}", reviewId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting helpful votes for review ID: {}", reviewId, e);
            throw new RuntimeException("Failed to count helpful votes", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(rv) FROM ReviewVote rv", Long.class).getSingleResult();
            logger.debug("✓ Total review votes: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting review votes", e);
            throw new RuntimeException("Failed to count review votes", e);
        } finally {
            em.close();
        }
    }
}

