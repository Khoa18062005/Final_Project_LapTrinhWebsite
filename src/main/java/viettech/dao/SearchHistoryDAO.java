package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.search.SearchHistory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * SearchHistory DAO - Data Access Object for SearchHistory entity
 * @author VietTech Team
 */
public class SearchHistoryDAO {

    private static final Logger logger = LoggerFactory.getLogger(SearchHistoryDAO.class);

    // CREATE
    public void insert(SearchHistory searchHistory) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(searchHistory);
            trans.commit();
            logger.info("✓ Inserted new search history for customer ID: {}", searchHistory.getCustomerId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert search history", e);
            throw new RuntimeException("Failed to insert search history", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public SearchHistory findById(int searchId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            SearchHistory search = em.find(SearchHistory.class, searchId);
            if (search != null) {
                logger.debug("✓ Found search history by ID: {}", searchId);
            } else {
                logger.warn("✗ Search history not found with ID: {}", searchId);
            }
            return search;
        } catch (Exception e) {
            logger.error("✗ Error finding search history by ID: {}", searchId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public List<SearchHistory> findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT sh FROM SearchHistory sh WHERE sh.customerId = :customerId ORDER BY sh.searchDate DESC";
            TypedQuery<SearchHistory> query = em.createQuery(jpql, SearchHistory.class);
            query.setParameter("customerId", customerId);
            List<SearchHistory> searches = query.getResultList();
            logger.debug("✓ Found {} search(es) for customer ID: {}", searches.size(), customerId);
            return searches;
        } catch (Exception e) {
            logger.error("✗ Error finding searches by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find search history by customer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Keyword
    public List<SearchHistory> findByKeyword(String keyword) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT sh FROM SearchHistory sh WHERE sh.keyword LIKE :keyword ORDER BY sh.searchDate DESC";
            TypedQuery<SearchHistory> query = em.createQuery(jpql, SearchHistory.class);
            query.setParameter("keyword", "%" + keyword + "%");
            List<SearchHistory> searches = query.getResultList();
            logger.debug("✓ Found {} search(es) with keyword: {}", searches.size(), keyword);
            return searches;
        } catch (Exception e) {
            logger.error("✗ Error finding searches by keyword: {}", keyword, e);
            throw new RuntimeException("Failed to find search history by keyword", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy recent searches của customer
    public List<SearchHistory> findRecentByCustomerId(int customerId, int limit) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT sh FROM SearchHistory sh WHERE sh.customerId = :customerId ORDER BY sh.searchDate DESC";
            TypedQuery<SearchHistory> query = em.createQuery(jpql, SearchHistory.class);
            query.setParameter("customerId", customerId);
            query.setMaxResults(limit);
            List<SearchHistory> searches = query.getResultList();
            logger.debug("✓ Found {} recent search(es) for customer ID: {}", searches.size(), customerId);
            return searches;
        } catch (Exception e) {
            logger.error("✗ Error finding recent searches for customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find recent searches", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<SearchHistory> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<SearchHistory> searches = em.createQuery("SELECT sh FROM SearchHistory sh ORDER BY sh.searchDate DESC", SearchHistory.class).getResultList();
            logger.debug("✓ Retrieved {} search history(ies)", searches.size());
            return searches;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all search histories", e);
            throw new RuntimeException("Failed to retrieve search histories", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(SearchHistory searchHistory) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(searchHistory);
            trans.commit();
            logger.info("✓ Updated search history ID: {}", searchHistory.getSearchId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update search history ID: {}", searchHistory.getSearchId(), e);
            throw new RuntimeException("Failed to update search history", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int searchId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            SearchHistory search = em.find(SearchHistory.class, searchId);
            if (search != null) {
                em.remove(search);
                logger.info("✓ Deleted search history ID: {}", searchId);
            } else {
                logger.warn("✗ Cannot delete - search history not found with ID: {}", searchId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete search history ID: {}", searchId, e);
            throw new RuntimeException("Failed to delete search history", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa tất cả search history của customer
    public void deleteByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM SearchHistory sh WHERE sh.customerId = :customerId";
            int deleted = em.createQuery(jpql).setParameter("customerId", customerId).executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} search(es) for customer ID: {}", deleted, customerId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete searches for customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to delete search history", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(sh) FROM SearchHistory sh", Long.class).getSingleResult();
            logger.debug("✓ Total search histories count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting search histories", e);
            throw new RuntimeException("Failed to count search histories", e);
        } finally {
            em.close();
        }
    }
}

