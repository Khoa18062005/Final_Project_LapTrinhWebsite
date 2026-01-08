package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * Shipper DAO - Data Access Object for Shipper entity
 * @author VietTech Team
 */
public class ShipperDAO {

    private static final Logger logger = LoggerFactory.getLogger(ShipperDAO.class);

    // ==================================================================
    // CÁC PHƯƠNG THỨC CƠ BẢN (CRUD SHIPPER) - GIỮ NGUYÊN
    // ==================================================================

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
            for (Shipper s : shippers) {
                if (s.getRoleID() != 3) {
                    s.setRoleID(3);
                }
            }
            logger.debug("✓ Retrieved {} shipper(s)", shippers.size());
            return shippers;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all shippers", e);
            return new java.util.ArrayList<>();
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

    // ==================================================================
    // CÁC PHƯƠNG THỨC MỚI (TÁCH TỪ SERVICE SANG)
    // ==================================================================

    /**
     * Lấy danh sách tất cả shipper đang hoạt động (role = 3)
     * Dùng để gửi thông báo khi có đơn hàng mới
     */
    public List<Shipper> findAllActiveShippers() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT s FROM Shipper s WHERE s.roleID = 3";
            TypedQuery<Shipper> query = em.createQuery(jpql, Shipper.class);
            List<Shipper> shippers = query.getResultList();
            logger.debug("✓ Found {} active shipper(s)", shippers.size());
            return shippers;
        } catch (Exception e) {
            logger.error("✗ Error finding active shippers", e);
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy danh sách nhiệm vụ giao hàng của Shipper (Kèm Fetch dữ liệu liên quan)
     */
    public List<DeliveryAssignment> getAssignmentsByShipperId(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT da FROM DeliveryAssignment da " +
                    "LEFT JOIN FETCH da.delivery d " +
                    "LEFT JOIN FETCH d.order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "LEFT JOIN FETCH d.warehouse w " +
                    "WHERE da.shipperId = :sid ORDER BY da.assignedAt DESC";

            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("sid", shipperId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy dữ liệu thống kê thu nhập (Sử dụng Native SQL)
     */
    public List<Object[]> getIncomeStatistics(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String sqlStats = "SELECT DATE(completed_at), SUM(earnings), COUNT(assignment_id) " +
                    "FROM delivery_assignments " +
                    "WHERE shipper_id = ? AND (status = 'Completed' OR status = 'Delivered') " +
                    "AND completed_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
                    "GROUP BY DATE(completed_at)";

            Query query = em.createNativeQuery(sqlStats);
            query.setParameter(1, shipperId);

            return query.getResultList();
        } catch (Exception e) {
            logger.error("✗ Error fetching statistics for shipper ID {}", shipperId, e);
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Tìm Assignment theo ID (Để update trạng thái)
     */
    /**
     * Tìm Assignment theo ID (Kèm FETCH dữ liệu Delivery/Order để tránh lỗi Lazy)
     */
    public DeliveryAssignment findAssignmentById(int id) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            // Dùng JOIN FETCH để ép Hibernate load hết dữ liệu 1 lần
            String jpql = "SELECT da FROM DeliveryAssignment da " +
                    "LEFT JOIN FETCH da.delivery d " +
                    "LEFT JOIN FETCH d.order o " +
                    "WHERE da.assignmentId = :id";

            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("id", id);

            return query.getSingleResult(); // Trả về kết quả duy nhất
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Cập nhật trạng thái Assignment
     */
    public void updateAssignment(DeliveryAssignment da) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(da);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    /**
     * Cập nhật thông tin phương tiện và bằng lái (Sử dụng Native SQL Update)
     */
    public boolean updateVehicleInfo(int userId, String vehiclePlate, String licenseNumber) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String sql = "UPDATE shippers SET vehicle_plate = ?, license_number = ? WHERE user_id = ?";
            Query query = em.createNativeQuery(sql);

            query.setParameter(1, vehiclePlate);
            query.setParameter(2, licenseNumber);
            query.setParameter(3, userId);

            int rows = query.executeUpdate();
            trans.commit();
            logger.info("✓ Updated vehicle info for shipper ID {}. Rows affected: {}", userId, rows);
            return true;
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update vehicle info for shipper ID {}", userId, e);
            return false;
        } finally {
            em.close();
        }
    }

    public void updateCurrentLocation(int shipperId, double lat, double lon) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            // Dùng Native Query vì bảng shippers tách biệt trong DB
            String sql = "UPDATE shippers SET current_latitude = ?, current_longitude = ? WHERE user_id = ?";
            Query query = em.createNativeQuery(sql);

            query.setParameter(1, lat);
            query.setParameter(2, lon);
            query.setParameter(3, shipperId);

            query.executeUpdate();
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy danh sách đơn hàng CHƯA CÓ SHIPPER (Pool đơn hàng tự do)
     */
    public List<DeliveryAssignment> getAvailableAssignments() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT da FROM DeliveryAssignment da " +
                    "LEFT JOIN FETCH da.delivery d " +
                    "LEFT JOIN FETCH d.order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "LEFT JOIN FETCH d.warehouse w " +
                    "WHERE da.status = 'READY' " +
                    "ORDER BY da.assignedAt DESC";

            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ==================================================================
    // PHƯƠNG THỨC LẤY ĐƠN HÀNG TỪ BẢNG ORDERS
    // ==================================================================

    /**
     * Lấy danh sách đơn hàng có status = Ready (Chờ shipper nhận)
     */
    public List<viettech.entity.order.Order> getOrdersByStatusReady() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "WHERE LOWER(o.status) = 'ready' " +
                    "ORDER BY o.orderDate DESC";

            TypedQuery<viettech.entity.order.Order> query = em.createQuery(jpql, viettech.entity.order.Order.class);
            List<viettech.entity.order.Order> orders = query.getResultList();
            logger.debug("✓ Found {} Ready orders", orders.size());
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error getting Ready orders", e);
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy danh sách đơn hàng có status = Shipping (Đang giao - của shipper cụ thể)
     * Lưu ý: Cần thêm trường shipper_id vào bảng orders hoặc dùng bảng deliveries
     */
    public List<viettech.entity.order.Order> getOrdersByStatusShipping(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            // Lấy từ bảng orders với status = Shipping
            // Nếu có trường shipper_id trong orders thì WHERE o.shipperId = :shipperId
            // Nếu không có, lấy qua delivery_assignments
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "LEFT JOIN o.delivery d " +
                    "LEFT JOIN DeliveryAssignment da ON da.delivery.deliveryId = d.deliveryId " +
                    "WHERE LOWER(o.status) = 'shipping' AND da.shipperId = :shipperId " +
                    "ORDER BY o.orderDate DESC";

            TypedQuery<viettech.entity.order.Order> query = em.createQuery(jpql, viettech.entity.order.Order.class);
            query.setParameter("shipperId", shipperId);
            List<viettech.entity.order.Order> orders = query.getResultList();
            logger.debug("✓ Found {} Shipping orders for shipper {}", orders.size(), shipperId);
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error getting Shipping orders for shipper {}", shipperId, e);
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy danh sách đơn hàng có status = Completed (Đã giao - của shipper cụ thể)
     * Bao gồm cả thông tin đánh giá từ delivery_assignments
     */
    public List<viettech.entity.order.Order> getOrdersByStatusCompleted(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "LEFT JOIN FETCH o.delivery d " +
                    "LEFT JOIN FETCH d.assignments da " +
                    "WHERE LOWER(o.status) = 'completed' AND da.shipperId = :shipperId " +
                    "ORDER BY o.completedAt DESC";

            TypedQuery<viettech.entity.order.Order> query = em.createQuery(jpql, viettech.entity.order.Order.class);
            query.setParameter("shipperId", shipperId);
            List<viettech.entity.order.Order> orders = query.getResultList();
            logger.debug("✓ Found {} Completed orders for shipper {}", orders.size(), shipperId);
            return orders;
        } catch (Exception e) {
            logger.error("✗ Error getting Completed orders for shipper {}", shipperId, e);
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Cập nhật status của Order
     */
    public void updateOrderStatus(int orderId, String newStatus) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            viettech.entity.order.Order order = em.find(viettech.entity.order.Order.class, orderId);
            if (order != null) {
                order.setStatus(newStatus);
                if ("Completed".equalsIgnoreCase(newStatus)) {
                    order.setCompletedAt(new java.util.Date());
                }
                em.merge(order);
                logger.info("✓ Updated order {} status to {}", orderId, newStatus);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Error updating order status", e);
        } finally {
            em.close();
        }
    }

    /**
     * Tìm Order theo ID
     */
    public viettech.entity.order.Order findOrderById(int orderId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(viettech.entity.order.Order.class, orderId);
        } finally {
            em.close();
        }
    }
}