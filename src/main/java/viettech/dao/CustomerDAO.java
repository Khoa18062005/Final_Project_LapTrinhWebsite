package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.user.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Customer DAO - Data Access Object for Customer entity
 * @author VietTech Team
 */
public class CustomerDAO {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);

    // CREATE - Thêm mới khách hàng
    public void insert(Customer customer) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(customer);
            trans.commit();
            logger.info("✓ Inserted new customer: {}", customer.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert customer: {}", customer.getEmail(), e);
            throw new RuntimeException("Failed to insert customer", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Customer findById(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Customer customer = em.find(Customer.class, userId);
            if (customer != null) {
                logger.debug("✓ Found customer by ID: {}", userId);
            } else {
                logger.warn("✗ Customer not found with ID: {}", userId);
            }
            return customer;
        } catch (Exception e) {
            logger.error("✗ Error finding customer by ID: {}", userId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Email
    public Customer findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Customer c WHERE c.email = :email";
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setParameter("email", email);
            Customer customer = query.getSingleResult();
            logger.debug("✓ Found customer by email: {}", email);
            return customer;
        } catch (NoResultException e) {
            logger.debug("✗ Customer not found with email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding customer by email: {}", email, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả khách hàng
    public List<Customer> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Customer> customers = em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
            logger.debug("✓ Retrieved {} customer(s)", customers.size());
            return customers;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all customers", e);
            throw new RuntimeException("Failed to retrieve customers", e);
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
            logger.info("✓ Updated customer: {}", customer.getEmail());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update customer: {}", customer.getEmail(), e);
            throw new RuntimeException("Failed to update customer", e);
        } finally {
            em.close();
        }
    }
    public Integer count() {
        try {
            return this.findAll().size();
        } catch (Exception e) {
            throw new RuntimeException("Failed to count customer", e);
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
                logger.info("✓ Deleted customer ID: {} ({})", userId, customer.getEmail());
            } else {
                logger.warn("✗ Cannot delete - customer not found with ID: {}", userId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete customer ID: {}", userId, e);
            throw new RuntimeException("Failed to delete customer", e);
        } finally {
            em.close();
        }
    }

    public Customer findByUsername(String username) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Customer c WHERE c.username = :username";
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setParameter("username", username);
            Customer customer = query.getSingleResult();
            logger.debug("✓ Found customer by username: {}", username);
            return customer;
        } catch (NoResultException e) {
            logger.debug("✗ Customer not found with username: {}", username);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding customer by username: {}", username, e);
            return null;
        } finally {
            em.close();
        }
    }
}