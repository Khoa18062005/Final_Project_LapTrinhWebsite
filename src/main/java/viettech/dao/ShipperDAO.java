package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.user.Shipper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Shipper DAO - Data Access Object for Shipper entity
 * @author VietTech Team
 */
public class ShipperDAO {

    private static final Logger logger = LoggerFactory.getLogger(ShipperDAO.class);

    public void insert(Shipper shipper) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(shipper);
            trans.commit();
            logger.info("✓ Inserted new shipper: {}", shipper.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert shipper: {}", shipper.getEmail(), e);
            throw new RuntimeException("Failed to insert shipper", e);
        } finally {
            em.close();
        }
    }

    public Shipper findById(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Shipper shipper = em.find(Shipper.class, shipperId);
            if (shipper != null) {
                logger.debug("✓ Found shipper by ID: {}", shipperId);
            } else {
                logger.warn("✗ Shipper not found with ID: {}", shipperId);
            }
            return shipper;
        } catch (Exception e) {
            logger.error("✗ Error finding shipper by ID: {}", shipperId, e);
            return null;
        } finally {
            em.close();
        }
    }

    public Shipper findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Shipper s WHERE s.email = :email";
            TypedQuery<Shipper> query = em.createQuery(jpql, Shipper.class);
            query.setParameter("email", email);
            Shipper shipper = query.getSingleResult();
            logger.debug("✓ Found shipper by email: {}", email);
            return shipper;
        } catch (NoResultException e) {
            logger.debug("✗ Shipper not found with email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding shipper by email: {}", email, e);
            return null;
        } finally {
            em.close();
        }
    }

    public Shipper findByEmailAndPassword(String email, String password) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Shipper s WHERE s.email = :email AND s.password = :password";
            TypedQuery<Shipper> query = em.createQuery(jpql, Shipper.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            Shipper shipper = query.getSingleResult();
            logger.info("✓ Shipper login successful: {}", email);
            return shipper;
        } catch (NoResultException e) {
            logger.warn("✗ Shipper login failed - invalid credentials for email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error during shipper login for email: {}", email, e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<Shipper> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Shipper> shippers = em.createQuery("SELECT s FROM Shipper s", Shipper.class).getResultList();
            logger.debug("✓ Retrieved {} shipper(s)", shippers.size());
            return shippers;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all shippers", e);
            throw new RuntimeException("Failed to retrieve shippers", e);
        } finally {
            em.close();
        }
    }

    public void update(Shipper shipper) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(shipper);
            trans.commit();
            logger.info("✓ Updated shipper: {}", shipper.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update shipper: {}", shipper.getEmail(), e);
            throw new RuntimeException("Failed to update shipper", e);
        } finally {
            em.close();
        }
    }

    public void delete(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Shipper shipper = em.find(Shipper.class, shipperId);
            if (shipper != null) {
                em.remove(shipper);
                logger.info("✓ Deleted shipper ID: {} ({})", shipperId, shipper.getEmail());
            } else {
                logger.warn("✗ Cannot delete - shipper not found with ID: {}", shipperId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete shipper ID: {}", shipperId, e);
            throw new RuntimeException("Failed to delete shipper", e);
        } finally {
            em.close();
        }
    }
}