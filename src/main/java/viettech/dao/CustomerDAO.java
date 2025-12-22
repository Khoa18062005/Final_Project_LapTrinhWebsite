package viettech.dao;

import viettech.config.JPAConfig;
import viettech.entity.user.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerDAO {

    // CREATE - Thêm mới khách hàng
    public void insert(Customer customer) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            em.persist(customer);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Customer findById(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Customer.class, userId);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Email (Vì email nằm ở lớp User)
    public Customer findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Customer c WHERE c.email = :email";
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả khách hàng
    public List<Customer> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE - Cập nhật thông tin khách hàng
    public void update(Customer customer) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            em.merge(customer);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa khách hàng theo ID
    public void delete(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            Customer customer = em.find(Customer.class, userId);
            if (customer != null) {
                em.remove(customer);
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