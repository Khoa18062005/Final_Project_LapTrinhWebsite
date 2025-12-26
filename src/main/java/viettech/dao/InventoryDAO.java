package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.storage.Inventory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Inventory DAO - Data Access Object for Inventory entity
 * @author VietTech Team
 */
public class InventoryDAO {

    private static final Logger logger = LoggerFactory.getLogger(InventoryDAO.class);

    // CREATE
    public void insert(Inventory inventory) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(inventory);
            trans.commit();
            logger.info("✓ Inserted new inventory record");
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert inventory", e);
            throw new RuntimeException("Failed to insert inventory", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Inventory findById(int inventoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Inventory inventory = em.find(Inventory.class, inventoryId);
            if (inventory != null) {
                logger.debug("✓ Found inventory by ID: {}", inventoryId);
            } else {
                logger.warn("✗ Inventory not found with ID: {}", inventoryId);
            }
            return inventory;
        } catch (Exception e) {
            logger.error("✗ Error finding inventory by ID: {}", inventoryId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Warehouse ID và Variant ID
    public Inventory findByWarehouseAndVariant(int warehouseId, int variantId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT i FROM Inventory i WHERE i.warehouse.warehouseId = :warehouseId AND i.variant.variantId = :variantId";
            TypedQuery<Inventory> query = em.createQuery(jpql, Inventory.class);
            query.setParameter("warehouseId", warehouseId);
            query.setParameter("variantId", variantId);
            Inventory inventory = query.getSingleResult();
            logger.debug("✓ Found inventory for warehouse {} and variant {}", warehouseId, variantId);
            return inventory;
        } catch (NoResultException e) {
            logger.debug("✗ Inventory not found for warehouse {} and variant {}", warehouseId, variantId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding inventory", e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Warehouse ID
    public List<Inventory> findByWarehouseId(int warehouseId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT i FROM Inventory i WHERE i.warehouse.warehouseId = :warehouseId";
            TypedQuery<Inventory> query = em.createQuery(jpql, Inventory.class);
            query.setParameter("warehouseId", warehouseId);
            List<Inventory> inventories = query.getResultList();
            logger.debug("✓ Found {} inventory(ies) for warehouse ID: {}", inventories.size(), warehouseId);
            return inventories;
        } catch (Exception e) {
            logger.error("✗ Error finding inventories by warehouse ID: {}", warehouseId, e);
            throw new RuntimeException("Failed to find inventories by warehouse", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Variant ID
    public List<Inventory> findByVariantId(int variantId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT i FROM Inventory i WHERE i.variant.variantId = :variantId";
            TypedQuery<Inventory> query = em.createQuery(jpql, Inventory.class);
            query.setParameter("variantId", variantId);
            List<Inventory> inventories = query.getResultList();
            logger.debug("✓ Found {} inventory(ies) for variant ID: {}", inventories.size(), variantId);
            return inventories;
        } catch (Exception e) {
            logger.error("✗ Error finding inventories by variant ID: {}", variantId, e);
            throw new RuntimeException("Failed to find inventories by variant", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm inventory có stock thấp
    public List<Inventory> findLowStock(int threshold) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT i FROM Inventory i WHERE i.availableQuantity <= :threshold";
            TypedQuery<Inventory> query = em.createQuery(jpql, Inventory.class);
            query.setParameter("threshold", threshold);
            List<Inventory> inventories = query.getResultList();
            logger.debug("✓ Found {} inventory(ies) with low stock (threshold: {})", inventories.size(), threshold);
            return inventories;
        } catch (Exception e) {
            logger.error("✗ Error finding low stock inventories", e);
            throw new RuntimeException("Failed to find low stock inventories", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Inventory> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Inventory> inventories = em.createQuery("SELECT i FROM Inventory i", Inventory.class).getResultList();
            logger.debug("✓ Retrieved {} inventory(ies)", inventories.size());
            return inventories;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all inventories", e);
            throw new RuntimeException("Failed to retrieve inventories", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Inventory inventory) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(inventory);
            trans.commit();
            logger.info("✓ Updated inventory ID: {}", inventory.getInventoryId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update inventory ID: {}", inventory.getInventoryId(), e);
            throw new RuntimeException("Failed to update inventory", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int inventoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Inventory inventory = em.find(Inventory.class, inventoryId);
            if (inventory != null) {
                em.remove(inventory);
                logger.info("✓ Deleted inventory ID: {}", inventoryId);
            } else {
                logger.warn("✗ Cannot delete - inventory not found with ID: {}", inventoryId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete inventory ID: {}", inventoryId, e);
            throw new RuntimeException("Failed to delete inventory", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(i) FROM Inventory i", Long.class).getSingleResult();
            logger.debug("✓ Total inventories count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting inventories", e);
            throw new RuntimeException("Failed to count inventories", e);
        } finally {
            em.close();
        }
    }
}

