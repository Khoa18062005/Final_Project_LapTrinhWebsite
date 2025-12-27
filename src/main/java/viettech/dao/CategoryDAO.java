package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Category DAO - Data Access Object for Category entity
 * @author VietTech Team
 */
public class CategoryDAO {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);

    // CREATE
    public void insert(Category category) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(category);
            trans.commit();
            logger.info("✓ Inserted category: {}", category.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert category: {}", category.getName(), e);
            throw new RuntimeException("Failed to insert category", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Category findById(int categoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Category category = em.find(Category.class, categoryId);
            if (category != null) {
                logger.debug("✓ Found category by ID: {}", categoryId);
            } else {
                logger.warn("✗ Category not found with ID: {}", categoryId);
            }
            return category;
        } catch (Exception e) {
            logger.error("✗ Error finding category by ID: {}", categoryId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Slug
    public Category findBySlug(String slug) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Category c WHERE c.slug = :slug";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("slug", slug);
            Category category = query.getSingleResult();
            logger.debug("✓ Found category by slug: {}", slug);
            return category;
        } catch (NoResultException e) {
            logger.debug("✗ Category not found with slug: {}", slug);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding category by slug: {}", slug, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Name
    public Category findByName(String name) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Category c WHERE c.name = :name";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("name", name);
            Category category = query.getSingleResult();
            logger.debug("✓ Found category by name: {}", name);
            return category;
        } catch (NoResultException e) {
            logger.debug("✗ Category not found with name: {}", name);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding category by name: {}", name, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Category> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Category> categories = em.createQuery("SELECT c FROM Category c ORDER BY c.sortOrder", Category.class).getResultList();
            logger.debug("✓ Retrieved {} category(s)", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all categories", e);
            throw new RuntimeException("Failed to retrieve categories", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả active
    public List<Category> findAllActive() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Category c WHERE c.isActive = true ORDER BY c.sortOrder";
            List<Category> categories = em.createQuery(jpql, Category.class).getResultList();
            logger.debug("✓ Retrieved {} active category(s)", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("✗ Error retrieving active categories", e);
            throw new RuntimeException("Failed to retrieve active categories", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy root categories (không có parent)
    public List<Category> findRootCategories() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Category c WHERE c.parentCategoryId IS NULL ORDER BY c.sortOrder";
            List<Category> categories = em.createQuery(jpql, Category.class).getResultList();
            logger.debug("✓ Retrieved {} root category(s)", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("✗ Error retrieving root categories", e);
            throw new RuntimeException("Failed to retrieve root categories", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy sub categories theo parent ID
    public List<Category> findByParentId(int parentCategoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Category c WHERE c.parentCategoryId = :parentId ORDER BY c.sortOrder";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("parentId", parentCategoryId);
            List<Category> categories = query.getResultList();
            logger.debug("✓ Found {} sub-category(s) for parent ID: {}", categories.size(), parentCategoryId);
            return categories;
        } catch (Exception e) {
            logger.error("✗ Error finding sub-categories for parent ID: {}", parentCategoryId, e);
            throw new RuntimeException("Failed to find sub-categories", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Category category) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(category);
            trans.commit();
            logger.info("✓ Updated category: {}", category.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update category: {}", category.getName(), e);
            throw new RuntimeException("Failed to update category", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int categoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Category category = em.find(Category.class, categoryId);
            if (category != null) {
                em.remove(category);
                logger.info("✓ Deleted category ID: {} ({})", categoryId, category.getName());
            } else {
                logger.warn("✗ Cannot delete - category not found with ID: {}", categoryId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete category ID: {}", categoryId, e);
            throw new RuntimeException("Failed to delete category", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(c) FROM Category c", Long.class).getSingleResult();
            logger.debug("✓ Total categories: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting categories", e);
            throw new RuntimeException("Failed to count categories", e);
        } finally {
            em.close();
        }
    }
}

