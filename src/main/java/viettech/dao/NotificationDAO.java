package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.Notification;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Notification DAO - Data Access Object for Notification entity
 * @author VietTech Team
 */
public class NotificationDAO {

    private static final Logger logger = LoggerFactory.getLogger(NotificationDAO.class);

    // CREATE
    public void insert(Notification notification) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(notification);
            trans.commit();
            logger.info("✓ Inserted new notification for user ID: {}", notification.getUserId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert notification", e);
            throw new RuntimeException("Failed to insert notification", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Notification findById(int notificationId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Notification notification = em.find(Notification.class, notificationId);
            if (notification != null) {
                logger.debug("✓ Found notification by ID: {}", notificationId);
            } else {
                logger.warn("✗ Notification not found with ID: {}", notificationId);
            }
            return notification;
        } catch (Exception e) {
            logger.error("✗ Error finding notification by ID: {}", notificationId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo User ID
    public List<Notification> findByUserId(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.createdAt DESC";
            TypedQuery<Notification> query = em.createQuery(jpql, Notification.class);
            query.setParameter("userId", userId);
            List<Notification> notifications = query.getResultList();
            logger.debug("✓ Found {} notification(s) for user ID: {}", notifications.size(), userId);
            return notifications;
        } catch (Exception e) {
            logger.error("✗ Error finding notifications by user ID: {}", userId, e);
            throw new RuntimeException("Failed to find notifications by user", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm unread notifications của user
    public List<Notification> findUnreadByUserId(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT n FROM Notification n WHERE n.userId = :userId AND n.isRead = false ORDER BY n.createdAt DESC";
            TypedQuery<Notification> query = em.createQuery(jpql, Notification.class);
            query.setParameter("userId", userId);
            List<Notification> notifications = query.getResultList();
            logger.debug("✓ Found {} unread notification(s) for user ID: {}", notifications.size(), userId);
            return notifications;
        } catch (Exception e) {
            logger.error("✗ Error finding unread notifications for user ID: {}", userId, e);
            throw new RuntimeException("Failed to find unread notifications", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Type
    public List<Notification> findByType(String type) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT n FROM Notification n WHERE n.type = :type ORDER BY n.createdAt DESC";
            TypedQuery<Notification> query = em.createQuery(jpql, Notification.class);
            query.setParameter("type", type);
            List<Notification> notifications = query.getResultList();
            logger.debug("✓ Found {} notification(s) with type: {}", notifications.size(), type);
            return notifications;
        } catch (Exception e) {
            logger.error("✗ Error finding notifications by type: {}", type, e);
            throw new RuntimeException("Failed to find notifications by type", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Notification> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Notification> notifications = em.createQuery("SELECT n FROM Notification n ORDER BY n.createdAt DESC", Notification.class).getResultList();
            logger.debug("✓ Retrieved {} notification(s)", notifications.size());
            return notifications;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all notifications", e);
            throw new RuntimeException("Failed to retrieve notifications", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Notification notification) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(notification);
            trans.commit();
            logger.info("✓ Updated notification ID: {}", notification.getNotificationId());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update notification ID: {}", notification.getNotificationId(), e);
            throw new RuntimeException("Failed to update notification", e);
        } finally {
            em.close();
        }
    }

    // UPDATE - Mark as read
    public void markAsRead(int notificationId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Notification notification = em.find(Notification.class, notificationId);
            if (notification != null) {
                notification.setRead(true);
                notification.setReadAt(new java.util.Date());
                em.merge(notification);
                logger.info("✓ Marked notification ID {} as read", notificationId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to mark notification as read", e);
            throw new RuntimeException("Failed to mark notification as read", e);
        } finally {
            em.close();
        }
    }

    // UPDATE - Mark all as read for user
    public void markAllAsReadByUserId(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.userId = :userId AND n.isRead = false";
            int updated = em.createQuery(jpql)
                    .setParameter("readAt", new java.util.Date())
                    .setParameter("userId", userId)
                    .executeUpdate();
            trans.commit();
            logger.info("✓ Marked {} notification(s) as read for user ID: {}", updated, userId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to mark all notifications as read for user ID: {}", userId, e);
            throw new RuntimeException("Failed to mark all notifications as read", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int notificationId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Notification notification = em.find(Notification.class, notificationId);
            if (notification != null) {
                em.remove(notification);
                logger.info("✓ Deleted notification ID: {}", notificationId);
            } else {
                logger.warn("✗ Cannot delete - notification not found with ID: {}", notificationId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete notification ID: {}", notificationId, e);
            throw new RuntimeException("Failed to delete notification", e);
        } finally {
            em.close();
        }
    }

    // DELETE - Xóa tất cả notifications của user
    public void deleteByUserId(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM Notification n WHERE n.userId = :userId";
            int deleted = em.createQuery(jpql).setParameter("userId", userId).executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} notification(s) for user ID: {}", deleted, userId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete notifications for user ID: {}", userId, e);
            throw new RuntimeException("Failed to delete notifications", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm unread notifications của user
    public long countUnreadByUserId(int userId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.isRead = false";
            Long count = em.createQuery(jpql, Long.class).setParameter("userId", userId).getSingleResult();
            logger.debug("✓ User ID {} has {} unread notification(s)", userId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting unread notifications for user ID: {}", userId, e);
            throw new RuntimeException("Failed to count unread notifications", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(n) FROM Notification n", Long.class).getSingleResult();
            logger.debug("✓ Total notifications count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting notifications", e);
            throw new RuntimeException("Failed to count notifications", e);
        } finally {
            em.close();
        }
    }

    /**
     * Bulk delete notifications by (type, actionUrl).
     * Used for broadcasting READY-delivery notifications to many shippers and
     * cleaning them up once one shipper accepted.
     */
    public int deleteByTypeAndActionUrl(String type, String actionUrl) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM Notification n WHERE n.type = :type AND n.actionUrl = :actionUrl";
            int deleted = em.createQuery(jpql)
                    .setParameter("type", type)
                    .setParameter("actionUrl", actionUrl)
                    .executeUpdate();
            trans.commit();
            logger.info("✓ Deleted {} notification(s) by type={} actionUrl={}", deleted, type, actionUrl);
            return deleted;
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete notifications by type/actionUrl", e);
            throw new RuntimeException("Failed to delete notifications", e);
        } finally {
            em.close();
        }
    }
}
