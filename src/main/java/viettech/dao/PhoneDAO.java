package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.Phone;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Phone DAO - Data Access Object for Phone entity
 * @author VietTech Team
 */
public class PhoneDAO {

    private static final Logger logger = LoggerFactory.getLogger(PhoneDAO.class);

    // CREATE - Thêm mới điện thoại
    public void insert(Phone phone) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(phone);
            trans.commit();
            logger.info("✓ Inserted new phone: {}", phone.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert phone: {}", phone.getName(), e);
            throw new RuntimeException("Failed to insert phone", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Phone findById(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Phone phone = em.find(Phone.class, productId);
            if (phone != null) {
                logger.debug("✓ Found phone by ID: {}", productId);
            } else {
                logger.warn("✗ Phone not found with ID: {}", productId);
            }
            return phone;
        } catch (Exception e) {
            logger.error("✗ Error finding phone by ID: {}", productId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Slug
    public Phone findBySlug(String slug) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Phone p WHERE p.slug = :slug";
            TypedQuery<Phone> query = em.createQuery(jpql, Phone.class);
            query.setParameter("slug", slug);
            Phone phone = query.getSingleResult();
            logger.debug("✓ Found phone by slug: {}", slug);
            return phone;
        } catch (NoResultException e) {
            logger.debug("✗ Phone not found with slug: {}", slug);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding phone by slug: {}", slug, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Brand
    public List<Phone> findByBrand(String brand) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Phone p WHERE p.brand = :brand";
            TypedQuery<Phone> query = em.createQuery(jpql, Phone.class);
            query.setParameter("brand", brand);
            List<Phone> phones = query.getResultList();
            logger.debug("✓ Found {} phone(s) by brand: {}", phones.size(), brand);
            return phones;
        } catch (Exception e) {
            logger.error("✗ Error finding phones by brand: {}", brand, e);
            throw new RuntimeException("Failed to find phones by brand", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID
    public List<Phone> findByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Phone p WHERE p.vendorId = :vendorId";
            TypedQuery<Phone> query = em.createQuery(jpql, Phone.class);
            query.setParameter("vendorId", vendorId);
            List<Phone> phones = query.getResultList();
            logger.debug("✓ Found {} phone(s) by vendor ID: {}", phones.size(), vendorId);
            return phones;
        } catch (Exception e) {
            logger.error("✗ Error finding phones by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to find phones by vendor", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả điện thoại
    public List<Phone> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Phone> phones = em.createQuery("SELECT p FROM Phone p", Phone.class).getResultList();
            logger.debug("✓ Retrieved {} phone(s)", phones.size());
            return phones;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all phones", e);
            throw new RuntimeException("Failed to retrieve phones", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng giá
    public List<Phone> findByPriceRange(double minPrice, double maxPrice) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Phone p WHERE p.basePrice BETWEEN :minPrice AND :maxPrice ORDER BY p.basePrice";
            TypedQuery<Phone> query = em.createQuery(jpql, Phone.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            List<Phone> phones = query.getResultList();
            logger.debug("✓ Found {} phone(s) in price range {} - {}", phones.size(), minPrice, maxPrice);
            return phones;
        } catch (Exception e) {
            logger.error("✗ Error finding phones by price range: {} - {}", minPrice, maxPrice, e);
            throw new RuntimeException("Failed to find phones by price range", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm điện thoại nổi bật
    public List<Phone> findFeatured() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Phone p WHERE p.isFeatured = true ORDER BY p.totalSold DESC";
            TypedQuery<Phone> query = em.createQuery(jpql, Phone.class);
            List<Phone> phones = query.getResultList();
            logger.debug("✓ Found {} featured phone(s)", phones.size());
            return phones;
        } catch (Exception e) {
            logger.error("✗ Error finding featured phones", e);
            throw new RuntimeException("Failed to find featured phones", e);
        } finally {
            em.close();
        }
    }

    // UPDATE - Cập nhật thông tin điện thoại
    public void update(Phone phone) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(phone);
            trans.commit();
            logger.info("✓ Updated phone: {}", phone.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update phone: {}", phone.getName(), e);
            throw new RuntimeException("Failed to update phone", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa điện thoại theo ID
    public void delete(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Phone phone = em.find(Phone.class, productId);
            if (phone != null) {
                em.remove(phone);
                logger.info("✓ Deleted phone ID: {} ({})", productId, phone.getName());
            } else {
                logger.warn("✗ Cannot delete - phone not found with ID: {}", productId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete phone ID: {}", productId, e);
            throw new RuntimeException("Failed to delete phone", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm số lượng điện thoại
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Phone p", Long.class).getSingleResult();
            logger.debug("✓ Total phones count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting phones", e);
            throw new RuntimeException("Failed to count phones", e);
        } finally {
            em.close();
        }
    }
}
