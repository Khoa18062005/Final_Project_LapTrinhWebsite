package viettech.dao;

import viettech.config.JPAConfig;
import viettech.entity.user.Shipper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class ShipperDAO {

    public void insert(Shipper shipper) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(shipper);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Shipper findById(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Shipper.class, shipperId);
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
            return query.getSingleResult();
        } catch (Exception e) {
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
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Shipper> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Shipper s", Shipper.class).getResultList();
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
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
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