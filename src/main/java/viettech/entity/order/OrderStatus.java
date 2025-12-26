package viettech.entity.order;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_statuses")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private int statusId;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(nullable = false, length = 50)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    @Column(length = 255)
    private String note;

    @Column(length = 255)
    private String location;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(length = 500)
    private String images;

    /* =========================
       MAPPING
       ========================= */

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public OrderStatus() {
        this.status = "";
        this.note = "";
        this.location = "";
        this.updatedBy = "";
        this.images = "";
        this.timestamp = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có statusId)
    public OrderStatus(int orderId,
                       String status,
                       String note,
                       String location,
                       String updatedBy,
                       String images) {

        this.orderId = orderId;
        this.status = status != null ? status : "";
        this.note = note;
        this.location = location;
        this.updatedBy = updatedBy;
        this.images = images;
        this.timestamp = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getStatusId() {
        return statusId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
