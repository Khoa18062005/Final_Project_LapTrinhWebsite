package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.user.Vendor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Vendor DAO - Data Access Object for Vendor entity
 * @author VietTech Team
 */
public class VendorDAO {

    private static final Logger logger = LoggerFactory.getLogger(VendorDAO.class);

    public void insert(Vendor vendor) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(vendor);
            trans.commit();
            logger.info("✓ Inserted new vendor: {}", vendor.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert vendor: {}", vendor.getEmail(), e);
            throw new RuntimeException("Failed to insert vendor", e);
        } finally {
            em.close();
        }
    }

    public Vendor findById(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Vendor vendor = em.find(Vendor.class, vendorId);
            if (vendor != null) {
                logger.debug("✓ Found vendor by ID: {}", vendorId);
            } else {
                logger.warn("✗ Vendor not found with ID: {}", vendorId);
            }
            return vendor;
        } catch (Exception e) {
            logger.error("✗ Error finding vendor by ID: {}", vendorId, e);
            return null;
        } finally {
            em.close();
        }
    }

    public Vendor findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Vendor v WHERE v.email = :email";
            TypedQuery<Vendor> query = em.createQuery(jpql, Vendor.class);
            query.setParameter("email", email);
            Vendor vendor = query.getSingleResult();
            logger.debug("✓ Found vendor by email: {}", email);
            return vendor;
        } catch (NoResultException e) {
            logger.debug("✗ Vendor not found with email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding vendor by email: {}", email, e);
            return null;
        } finally {
            em.close();
        }
    }

    public Vendor findByEmailAndPassword(String email, String password) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT v FROM Vendor v WHERE v.email = :email AND v.password = :password";
            TypedQuery<Vendor> query = em.createQuery(jpql, Vendor.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            Vendor vendor = query.getSingleResult();
            logger.info("✓ Vendor login successful: {}", email);
            return vendor;
        } catch (NoResultException e) {
            logger.warn("✗ Vendor login failed - invalid credentials for email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error during vendor login for email: {}", email, e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<Vendor> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Vendor> vendors = em.createQuery("SELECT v FROM Vendor v", Vendor.class).getResultList();
            logger.debug("✓ Retrieved {} vendor(s)", vendors.size());
            return vendors;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all vendors", e);
            throw new RuntimeException("Failed to retrieve vendors", e);
        } finally {
            em.close();
        }
    }

    public void update(Vendor vendor) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(vendor);
            trans.commit();
            logger.info("✓ Updated vendor: {}", vendor.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update vendor: {}", vendor.getEmail(), e);
            throw new RuntimeException("Failed to update vendor", e);
        } finally {
            em.close();
        }
    }

    public void delete(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Vendor vendor = em.find(Vendor.class, vendorId);
            if (vendor != null) {
                em.remove(vendor);
                logger.info("✓ Deleted vendor ID: {} ({})", vendorId, vendor.getEmail());
            } else {
                logger.warn("✗ Cannot delete - vendor not found with ID: {}", vendorId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to delete vendor", e);
        } finally {
            em.close();
        }
    }
}