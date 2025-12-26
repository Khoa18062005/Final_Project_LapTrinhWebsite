    package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.Laptop;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Laptop DAO - Data Access Object for Laptop entity
 * @author VietTech Team
 */
public class LaptopDAO {

    private static final Logger logger = LoggerFactory.getLogger(LaptopDAO.class);

    // CREATE - Thêm mới laptop
    public void insert(Laptop laptop) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(laptop);
            trans.commit();
            logger.info("✓ Inserted new laptop: {}", laptop.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert laptop: {}", laptop.getName(), e);
            throw new RuntimeException("Failed to insert laptop", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Laptop findById(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Laptop laptop = em.find(Laptop.class, productId);
            if (laptop != null) {
                logger.debug("✓ Found laptop by ID: {}", productId);
            } else {
                logger.warn("✗ Laptop not found with ID: {}", productId);
            }
            return laptop;
        } catch (Exception e) {
            logger.error("✗ Error finding laptop by ID: {}", productId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Slug
    public Laptop findBySlug(String slug) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.slug = :slug";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            query.setParameter("slug", slug);
            Laptop laptop = query.getSingleResult();
            logger.debug("✓ Found laptop by slug: {}", slug);
            return laptop;
        } catch (NoResultException e) {
            logger.debug("✗ Laptop not found with slug: {}", slug);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding laptop by slug: {}", slug, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Brand
    public List<Laptop> findByBrand(String brand) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.brand = :brand";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            query.setParameter("brand", brand);
            List<Laptop> laptops = query.getResultList();
            logger.debug("✓ Found {} laptop(s) by brand: {}", laptops.size(), brand);
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error finding laptops by brand: {}", brand, e);
            throw new RuntimeException("Failed to find laptops by brand", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID
    public List<Laptop> findByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.vendorId = :vendorId";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            query.setParameter("vendorId", vendorId);
            List<Laptop> laptops = query.getResultList();
            logger.debug("✓ Found {} laptop(s) by vendor ID: {}", laptops.size(), vendorId);
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error finding laptops by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to find laptops by vendor", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả laptop
    public List<Laptop> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Laptop> laptops = em.createQuery("SELECT l FROM Laptop l", Laptop.class).getResultList();
            logger.debug("✓ Retrieved {} laptop(s)", laptops.size());
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all laptops", e);
            throw new RuntimeException("Failed to retrieve laptops", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng giá
    public List<Laptop> findByPriceRange(double minPrice, double maxPrice) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.basePrice BETWEEN :minPrice AND :maxPrice ORDER BY l.basePrice";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            List<Laptop> laptops = query.getResultList();
            logger.debug("✓ Found {} laptop(s) in price range {} - {}", laptops.size(), minPrice, maxPrice);
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error finding laptops by price range: {} - {}", minPrice, maxPrice, e);
            throw new RuntimeException("Failed to find laptops by price range", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo RAM
    public List<Laptop> findByRam(int ram) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.ram = :ram";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            query.setParameter("ram", ram);
            List<Laptop> laptops = query.getResultList();
            logger.debug("✓ Found {} laptop(s) with RAM: {}GB", laptops.size(), ram);
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error finding laptops by RAM: {}", ram, e);
            throw new RuntimeException("Failed to find laptops by RAM", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo CPU
    public List<Laptop> findByCpu(String cpu) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.cpu LIKE :cpu";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            query.setParameter("cpu", "%" + cpu + "%");
            List<Laptop> laptops = query.getResultList();
            logger.debug("✓ Found {} laptop(s) with CPU: {}", laptops.size(), cpu);
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error finding laptops by CPU: {}", cpu, e);
            throw new RuntimeException("Failed to find laptops by CPU", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm laptop nổi bật
    public List<Laptop> findFeatured() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.isFeatured = true ORDER BY l.totalSold DESC";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            List<Laptop> laptops = query.getResultList();
            logger.debug("✓ Found {} featured laptop(s)", laptops.size());
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error finding featured laptops", e);
            throw new RuntimeException("Failed to find featured laptops", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm laptop có GPU rời
    public List<Laptop> findWithDiscreteGpu() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT l FROM Laptop l WHERE l.discreteGpu = true";
            TypedQuery<Laptop> query = em.createQuery(jpql, Laptop.class);
            List<Laptop> laptops = query.getResultList();
            logger.debug("✓ Found {} laptop(s) with discrete GPU", laptops.size());
            return laptops;
        } catch (Exception e) {
            logger.error("✗ Error finding laptops with discrete GPU", e);
            throw new RuntimeException("Failed to find laptops with discrete GPU", e);
        } finally {
            em.close();
        }
    }

    // UPDATE - Cập nhật thông tin laptop
    public void update(Laptop laptop) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(laptop);
            trans.commit();
            logger.info("✓ Updated laptop: {}", laptop.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update laptop: {}", laptop.getName(), e);
            throw new RuntimeException("Failed to update laptop", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa laptop theo ID
    public void delete(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Laptop laptop = em.find(Laptop.class, productId);
            if (laptop != null) {
                em.remove(laptop);
                logger.info("✓ Deleted laptop ID: {} ({})", productId, laptop.getName());
            } else {
                logger.warn("✗ Cannot delete - laptop not found with ID: {}", productId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete laptop ID: {}", productId, e);
            throw new RuntimeException("Failed to delete laptop", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm số lượng laptop
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(l) FROM Laptop l", Long.class).getSingleResult();
            logger.debug("✓ Total laptops count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting laptops", e);
            throw new RuntimeException("Failed to count laptops", e);
        } finally {
            em.close();
        }
    }
}

