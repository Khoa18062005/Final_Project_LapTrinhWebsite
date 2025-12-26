package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.user.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Role DAO - Data Access Object for Role entity
 * @author VietTech Team
 */
public class RoleDAO {

    private static final Logger logger = LoggerFactory.getLogger(RoleDAO.class);

    // CREATE
    public void insert(Role role) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(role);
            trans.commit();
            logger.info("✓ Inserted role: {}", role.getRoleName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert role: {}", role.getRoleName(), e);
            throw new RuntimeException("Failed to insert role", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Role findById(int roleId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Role role = em.find(Role.class, roleId);
            if (role != null) {
                logger.debug("✓ Found role by ID: {}", roleId);
            } else {
                logger.warn("✗ Role not found with ID: {}", roleId);
            }
            return role;
        } catch (Exception e) {
            logger.error("✗ Error finding role by ID: {}", roleId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Role Name
    public Role findByRoleName(String roleName) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT r FROM Role r WHERE r.roleName = :roleName";
            TypedQuery<Role> query = em.createQuery(jpql, Role.class);
            query.setParameter("roleName", roleName);
            Role role = query.getSingleResult();
            logger.debug("✓ Found role by name: {}", roleName);
            return role;
        } catch (NoResultException e) {
            logger.debug("✗ Role not found with name: {}", roleName);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding role by name: {}", roleName, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Role> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Role> roles = em.createQuery("SELECT r FROM Role r ORDER BY r.roleName", Role.class).getResultList();
            logger.debug("✓ Retrieved {} role(s)", roles.size());
            return roles;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all roles", e);
            throw new RuntimeException("Failed to retrieve roles", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Role role) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(role);
            trans.commit();
            logger.info("✓ Updated role: {}", role.getRoleName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update role: {}", role.getRoleName(), e);
            throw new RuntimeException("Failed to update role", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int roleId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Role role = em.find(Role.class, roleId);
            if (role != null) {
                em.remove(role);
                logger.info("✓ Deleted role ID: {} ({})", roleId, role.getRoleName());
            } else {
                logger.warn("✗ Cannot delete - role not found with ID: {}", roleId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete role ID: {}", roleId, e);
            throw new RuntimeException("Failed to delete role", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(r) FROM Role r", Long.class).getSingleResult();
            logger.debug("✓ Total roles: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting roles", e);
            throw new RuntimeException("Failed to count roles", e);
        } finally {
            em.close();
        }
    }
}

