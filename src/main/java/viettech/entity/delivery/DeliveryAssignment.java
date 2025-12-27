package viettech.entity.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import viettech.entity.user.Shipper;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "delivery_assignments")
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private int assignmentId;

    @Column(name = "delivery_id", nullable = false)
    private int deliveryId;

    @Column(name = "shipper_id", nullable = false)
    private int shipperId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assigned_at")
    private Date assignedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "accepted_at")
    private Date acceptedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started_at")
    private Date startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completed_at")
    private Date completedAt;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "earnings", precision = 12, scale = 2)
    private double earnings;

    @Column(name = "rating")
    private int rating;

    @Column(name = "feedback")
    private String feedback;

    /* =========================
       MAPPING
       ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipper_id", insertable = false, updatable = false)
    private Shipper shipper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", insertable = false, updatable = false)
    private Delivery delivery;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public DeliveryAssignment() {
        this.deliveryId = 0;
        this.shipperId = 0;
        this.earnings = 0.0;
        this.rating = 0;

        this.status = "";
        this.feedback = "";

        this.assignedAt = new Date();
        this.acceptedAt = new Date();
        this.startedAt = new Date();
        this.completedAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có assignmentId)
    public DeliveryAssignment(int deliveryId,
                              int shipperId,
                              Date assignedAt,
                              Date acceptedAt,
                              Date startedAt,
                              Date completedAt,
                              String status,
                              double earnings,
                              int rating,
                              String feedback) {

        this.deliveryId = deliveryId;
        this.shipperId = shipperId;
        this.assignedAt = assignedAt;
        this.acceptedAt = acceptedAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.status = status != null ? status : "";
        this.earnings = earnings;
        this.rating = rating;
        this.feedback = feedback != null ? feedback : "";
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getAssignmentId() {
        return assignmentId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public Date getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Date assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Date getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Date acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback != null ? feedback : "";
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}