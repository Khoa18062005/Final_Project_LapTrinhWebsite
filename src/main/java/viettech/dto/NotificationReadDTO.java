package viettech.dto;

import java.io.Serializable;

public class NotificationReadDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int notificationId;
    private boolean markAll;
    private int userId; // Thêm userId để kiểm tra trong service

    public NotificationReadDTO() {
    }

    public NotificationReadDTO(int notificationId, boolean markAll, int userId) {
        this.notificationId = notificationId;
        this.markAll = markAll;
        this.userId = userId;
    }

    // Getters và Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public boolean isMarkAll() {
        return markAll;
    }

    public void setMarkAll(boolean markAll) {
        this.markAll = markAll;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}