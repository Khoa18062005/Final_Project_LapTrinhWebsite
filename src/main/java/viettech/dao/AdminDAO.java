package viettech.dao;

import viettech.config.JPAConfig;
import viettech.entity.user.Admin;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class AdminDAO {

    // CREATE - Thêm mới admin
    public void insert(Admin admin) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(admin);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Admin findById(int adminId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Admin.class, adminId);
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
            return query.getSingleResult();
        } catch (Exception e) {
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
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả admin
    public List<Admin> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
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
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
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
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}