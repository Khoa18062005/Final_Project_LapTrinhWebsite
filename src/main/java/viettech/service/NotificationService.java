package viettech.service;

import viettech.dao.NotificationDAO;
import viettech.entity.Notification;

import java.util.List;

/**
 * Service xử lý logic thông báo
 */
public class NotificationService {
    private final NotificationDAO notificationDAO = new NotificationDAO();
    
    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationDAO.findByUserId(userId);
    }
    
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        return notificationDAO.findUnreadByUserId(userId);
    }
    
    public int getUnreadCountByUserId(int userId) {
        return (int) notificationDAO.countUnreadByUserId(userId);
    }
    
    public void markAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }
    
    public void markAllAsReadByUserId(int userId) {
        notificationDAO.markAllAsReadByUserId(userId);
    }
    
    public boolean deleteNotification(int notificationId) {
        try {
            notificationDAO.delete(notificationId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}