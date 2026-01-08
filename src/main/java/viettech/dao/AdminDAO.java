package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.user.Admin;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Admin DAO - Data Access Object for Admin entity
 * @author VietTech Team
 */
public class AdminDAO {

    private static final Logger logger = LoggerFactory.getLogger(AdminDAO.class);

    // CREATE - Thêm mới admin
    public void insert(Admin admin) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(admin);
            trans.commit();
            logger.info("✓ Inserted new admin: {}", admin.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert admin: {}", admin.getEmail(), e);
            throw new RuntimeException("Failed to insert admin", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Admin findById(int adminId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Admin admin = em.find(Admin.class, adminId);
            if (admin != null) {
                logger.debug("✓ Found admin by ID: {}", adminId);
            } else {
                logger.warn("✗ Admin not found with ID: {}", adminId);
            }
            return admin;
        } catch (Exception e) {
            logger.error("✗ Error finding admin by ID: {}", adminId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Email
    public Admin findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Admin a WHERE a.email = :email";
            TypedQuery<Admin> query = em.createQuery(jpql, Admin.class);
            query.setParameter("email", email);
            Admin admin = query.getSingleResult();
            logger.debug("✓ Found admin by email: {}", email);
            return admin;
        } catch (NoResultException e) {
            logger.debug("✗ Admin not found with email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding admin by email: {}", email, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm admin theo email và password (dùng cho đăng nhập)
    public Admin findByEmailAndPassword(String email, String password) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Admin a WHERE a.email = :email AND a.password = :password";
            TypedQuery<Admin> query = em.createQuery(jpql, Admin.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            Admin admin = query.getSingleResult();
            logger.info("✓ Admin login successful: {}", email);
            return admin;
        } catch (NoResultException e) {
            logger.warn("✗ Admin login failed - invalid credentials for email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error during admin login for email: {}", email, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả admin
    public List<Admin> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Admin> admins = em.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
            // Ensure roleID is set correctly
            for (Admin a : admins) {
                if (a.getRoleID() != 1) {
                    a.setRoleID(1);
                }
            }
            logger.debug("✓ Retrieved {} admin(s)", admins.size());
            return admins;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all admins", e);
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    // UPDATE - Cập nhật thông tin admin
    public void update(Admin admin) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(admin);
            trans.commit();
            logger.info("✓ Updated admin: {}", admin.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update admin: {}", admin.getEmail(), e);
            throw new RuntimeException("Failed to update admin", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa admin theo ID
    public void delete(int adminId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Admin admin = em.find(Admin.class, adminId);
            if (admin != null) {
                em.remove(admin);
                logger.info("✓ Deleted admin ID: {} ({})", adminId, admin.getEmail());
            } else {
                logger.warn("✗ Cannot delete - admin not found with ID: {}", adminId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete admin ID: {}", adminId, e);
            throw new RuntimeException("Failed to delete admin", e);
        } finally {
            em.close();
        }
    }

    /**
     * READ - Tìm tất cả admin đang active
     * @return List<Admin> danh sách admin active
     */
    public List<Admin> findAllActive() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Admin a WHERE a.isActive = true";
            TypedQuery<Admin> query = em.createQuery(jpql, Admin.class);
            List<Admin> admins = query.getResultList();

            logger.info("✓ Found {} active admin(s)", admins.size());
            return admins;

        } catch (Exception e) {
            logger.error("✗ Error finding active admins", e);
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * COUNT - Đếm số lượng admin đang active
     * @return long số lượng admin active
     */
    public long countActive() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(a) FROM Admin a WHERE a.isActive = true";
            Long count = em.createQuery(jpql, Long.class).getSingleResult();

            logger.debug("✓ Total active admins: {}", count);
            return count;

        } catch (Exception e) {
            logger.error("✗ Error counting active admins", e);
            return 0;
        } finally {
            em.close();
        }
    }
}