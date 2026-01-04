package viettech.dto;

import java.io.Serializable;

public class NotificationDeleteDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int notificationId;
    private int userId;
    
    public NotificationDeleteDTO() {
    }
    
    public NotificationDeleteDTO(int notificationId, int userId) {
        this.notificationId = notificationId;
        this.userId = userId;
    }
    
    // Getters và Setters
    public int getNotificationId() {
        return notificationId;
    }
    
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
}