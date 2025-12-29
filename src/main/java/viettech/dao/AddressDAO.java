package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.Address;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Address DAO - Data Access Object for Address entity
 * @author VietTech Team
 */
public class AddressDAO {

    private static final Logger logger = LoggerFactory.getLogger(AddressDAO.class);

    // CREATE
    public void insert(Address address) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            System.out.println("📍 DAO: Starting to insert address");
            trans.begin();
            em.persist(address);
            trans.commit();
            System.out.println("✅ DAO: Inserted new address for customer ID: " + address.getCustomer().getUserId());
            logger.info("✓ Inserted new address for customer");
        } catch (Exception e) {
            System.out.println("❌ DAO Exception: " + e.getMessage());
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert address", e);
            throw new RuntimeException("Failed to insert address", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Address findById(int addressId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Address address = em.find(Address.class, addressId);
            if (address != null) {
                logger.debug("✓ Found address by ID: {}", addressId);
            } else {
                logger.warn("✗ Address not found with ID: {}", addressId);
            }
            return address;
        } catch (Exception e) {
            logger.error("✗ Error finding address by ID: {}", addressId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Customer ID
    public List<Address> findByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Address a WHERE a.customer.userId = :customerId";
            TypedQuery<Address> query = em.createQuery(jpql, Address.class);
            query.setParameter("customerId", customerId);
            List<Address> addresses = query.getResultList();
            logger.debug("✓ Found {} address(es) for customer ID: {}", addresses.size(), customerId);
            return addresses;
        } catch (Exception e) {
            logger.error("✗ Error finding addresses by customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to find addresses by customer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm default address của customer
    public Address findDefaultByCustomerId(int customerId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Address a WHERE a.customer.userId = :customerId AND a.isDefault = true";
            TypedQuery<Address> query = em.createQuery(jpql, Address.class);
            query.setParameter("customerId", customerId);
            List<Address> addresses = query.getResultList();
            if (!addresses.isEmpty()) {
                logger.debug("✓ Found default address for customer ID: {}", customerId);
                return addresses.get(0);
            }
            logger.debug("✗ Default address not found for customer ID: {}", customerId);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding default address for customer ID: {}", customerId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Address> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Address> addresses = em.createQuery("SELECT a FROM Address a", Address.class).getResultList();
            logger.debug("✓ Retrieved {} address(es)", addresses.size());
            return addresses;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all addresses", e);
            throw new RuntimeException("Failed to retrieve addresses", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Address address) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(address);
            trans.commit();
            logger.info("✓ Updated address ID: {}", address.getAddressId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update address ID: {}", address.getAddressId(), e);
            throw new RuntimeException("Failed to update address", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int addressId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Address address = em.find(Address.class, addressId);
            if (address != null) {
                em.remove(address);
                logger.info("✓ Deleted address ID: {}", addressId);
            } else {
                logger.warn("✗ Cannot delete - address not found with ID: {}", addressId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete address ID: {}", addressId, e);
            throw new RuntimeException("Failed to delete address", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(a) FROM Address a", Long.class).getSingleResult();
            logger.debug("✓ Total addresses count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting addresses", e);
            throw new RuntimeException("Failed to count addresses", e);
        } finally {
            em.close();
        }
    }
}

