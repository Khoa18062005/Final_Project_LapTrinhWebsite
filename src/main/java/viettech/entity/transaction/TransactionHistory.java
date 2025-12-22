package viettech.entity.transaction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction_histories")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private int historyId;

    @Column(name = "entity_type", length = 100, nullable = false)
    private String entityType;

    @Column(name = "entity_id", length = 50, nullable = false)
    private String entityId;

    @Column(name = "action", length = 100, nullable = false)
    private String action;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "performed_by", length = 100)
    private String performedBy;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "before_data", columnDefinition = "TEXT")
    private String beforeData;

    @Column(name = "after_data", columnDefinition = "TEXT")
    private String afterData;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public TransactionHistory() {
        this.entityType = "";
        this.entityId = "";
        this.action = "";
        this.performedBy = "";
        this.ipAddress = "";
        this.userAgent = "";
        this.beforeData = "";
        this.afterData = "";
        this.details = "";

        this.timestamp = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có historyId)
    public TransactionHistory(String entityType,
                              String entityId,
                              String action,
                              String performedBy,
                              String ipAddress,
                              String userAgent,
                              String beforeData,
                              String afterData,
                              String details) {

        this.entityType = entityType != null ? entityType : "";
        this.entityId = entityId != null ? entityId : "";
        this.action = action != null ? action : "";
        this.performedBy = performedBy != null ? performedBy : "";
        this.ipAddress = ipAddress != null ? ipAddress : "";
        this.userAgent = userAgent != null ? userAgent : "";
        this.beforeData = beforeData != null ? beforeData : "";
        this.afterData = afterData != null ? afterData : "";
        this.details = details != null ? details : "";
        this.timestamp = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getHistoryId() {
        return historyId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType != null ? entityType : "";
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId != null ? entityId : "";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action != null ? action : "";
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy != null ? performedBy : "";
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress != null ? ipAddress : "";
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent != null ? userAgent : "";
    }

    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData != null ? beforeData : "";
    }

    public String getAfterData() {
        return afterData;
    }

    public void setAfterData(String afterData) {
        this.afterData = afterData != null ? afterData : "";
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details != null ? details : "";
    }
}