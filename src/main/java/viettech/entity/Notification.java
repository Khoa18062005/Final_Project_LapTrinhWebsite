package viettech.entity;

import viettech.entity.user.Customer;
import viettech.entity.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "action_url", length = 500)
    private String actionUrl;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "read_at")
    private Date readAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private Date expiresAt;

    /* =========================
       MAPPING
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Customer customer;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Notification() {
        this.userId = 0;
        this.isRead = false;

        this.type = "";
        this.title = "";
        this.message = "";
        this.data = "";
        this.imageUrl = "";
        this.actionUrl = "";

        this.createdAt = new Date();
        this.readAt = new Date();
        this.expiresAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có notificationId)
    public Notification(int userId,
                        String type,
                        String title,
                        String message,
                        String data,
                        String imageUrl,
                        String actionUrl,
                        boolean isRead,
                        Date readAt,
                        Date createdAt,
                        Date expiresAt) {

        this.userId = userId;
        this.type = type != null ? type : "";
        this.title = title != null ? title : "";
        this.message = message != null ? message : "";
        this.data = data != null ? data : "";
        this.imageUrl = imageUrl != null ? imageUrl : "";
        this.actionUrl = actionUrl != null ? actionUrl : "";
        this.isRead = isRead;
        this.readAt = readAt;
        this.createdAt = createdAt != null ? createdAt : new Date();
        this.expiresAt = expiresAt;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getNotificationId() {
        return notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data != null ? data : "";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl != null ? imageUrl : "";
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl != null ? actionUrl : "";
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
        if (read && this.readAt == null) {
            this.readAt = new Date();
        }
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}