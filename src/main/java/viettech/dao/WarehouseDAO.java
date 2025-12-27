package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.storage.Warehouse;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Warehouse DAO - Data Access Object for Warehouse entity
 * @author VietTech Team
 */
public class WarehouseDAO {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseDAO.class);

    // CREATE
    public void insert(Warehouse warehouse) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(warehouse);
            trans.commit();
            logger.info("✓ Inserted new warehouse: {}", warehouse.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert warehouse: {}", warehouse.getName(), e);
            throw new RuntimeException("Failed to insert warehouse", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Warehouse findById(int warehouseId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Warehouse warehouse = em.find(Warehouse.class, warehouseId);
            if (warehouse != null) {
                logger.debug("✓ Found warehouse by ID: {}", warehouseId);
            } else {
                logger.warn("✗ Warehouse not found with ID: {}", warehouseId);
            }
            return warehouse;
        } catch (Exception e) {
            logger.error("✗ Error finding warehouse by ID: {}", warehouseId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Code
    public Warehouse findByCode(String code) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT w FROM Warehouse w WHERE w.code = :code";
            TypedQuery<Warehouse> query = em.createQuery(jpql, Warehouse.class);
            query.setParameter("code", code);
            Warehouse warehouse = query.getSingleResult();
            logger.debug("✓ Found warehouse by code: {}", code);
            return warehouse;
        } catch (NoResultException e) {
            logger.debug("✗ Warehouse not found with code: {}", code);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding warehouse by code: {}", code, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID
    public List<Warehouse> findByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT w FROM Warehouse w WHERE w.vendor.userId = :vendorId";
            TypedQuery<Warehouse> query = em.createQuery(jpql, Warehouse.class);
            query.setParameter("vendorId", vendorId);
            List<Warehouse> warehouses = query.getResultList();
            logger.debug("✓ Found {} warehouse(s) for vendor ID: {}", warehouses.size(), vendorId);
            return warehouses;
        } catch (Exception e) {
            logger.error("✗ Error finding warehouses by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to find warehouses by vendor", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Province
    public List<Warehouse> findByProvince(String province) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT w FROM Warehouse w WHERE w.province = :province";
            TypedQuery<Warehouse> query = em.createQuery(jpql, Warehouse.class);
            query.setParameter("province", province);
            List<Warehouse> warehouses = query.getResultList();
            logger.debug("✓ Found {} warehouse(s) in province: {}", warehouses.size(), province);
            return warehouses;
        } catch (Exception e) {
            logger.error("✗ Error finding warehouses by province: {}", province, e);
            throw new RuntimeException("Failed to find warehouses by province", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả active warehouses
    public List<Warehouse> findAllActive() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT w FROM Warehouse w WHERE w.isActive = true";
            List<Warehouse> warehouses = em.createQuery(jpql, Warehouse.class).getResultList();
            logger.debug("✓ Retrieved {} active warehouse(s)", warehouses.size());
            return warehouses;
        } catch (Exception e) {
            logger.error("✗ Error retrieving active warehouses", e);
            throw new RuntimeException("Failed to retrieve active warehouses", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Warehouse> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Warehouse> warehouses = em.createQuery("SELECT w FROM Warehouse w", Warehouse.class).getResultList();
            logger.debug("✓ Retrieved {} warehouse(s)", warehouses.size());
            return warehouses;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all warehouses", e);
            throw new RuntimeException("Failed to retrieve warehouses", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Warehouse warehouse) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(warehouse);
            trans.commit();
            logger.info("✓ Updated warehouse: {}", warehouse.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update warehouse: {}", warehouse.getName(), e);
            throw new RuntimeException("Failed to update warehouse", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int warehouseId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Warehouse warehouse = em.find(Warehouse.class, warehouseId);
            if (warehouse != null) {
                em.remove(warehouse);
                logger.info("✓ Deleted warehouse ID: {} ({})", warehouseId, warehouse.getName());
            } else {
                logger.warn("✗ Cannot delete - warehouse not found with ID: {}", warehouseId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete warehouse ID: {}", warehouseId, e);
            throw new RuntimeException("Failed to delete warehouse", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(w) FROM Warehouse w", Long.class).getSingleResult();
            logger.debug("✓ Total warehouses count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting warehouses", e);
            throw new RuntimeException("Failed to count warehouses", e);
        } finally {
            em.close();
        }
    }
}

