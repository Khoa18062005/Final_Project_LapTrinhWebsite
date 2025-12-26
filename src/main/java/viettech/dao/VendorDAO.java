package viettech.dao;

import viettech.config.JPAConfig;
import viettech.entity.user.Vendor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class VendorDAO {

    public void insert(Vendor vendor) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(vendor);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Vendor findById(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Vendor.class, vendorId);
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
            return query.getSingleResult();
        } catch (Exception e) {
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
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Vendor> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT v FROM Vendor v", Vendor.class).getResultList();
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
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
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