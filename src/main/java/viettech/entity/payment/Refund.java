package viettech.entity.payment;

import viettech.entity.order.Order;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private int refundId;

    @Column(name = "payment_id", nullable = false)
    private int paymentId;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "reason")
    private String reason;

    @Column(name = "amount", precision = 12, scale = 2)
    private double amount;

    @Column(name = "status", length = 50)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_date")
    private Date requestDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approved_date")
    private Date approvedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "processed_date")
    private Date processedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completed_date")
    private Date completedDate;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "processed_by", length = 100)
    private String processedBy;

    @Column(name = "notes")
    private String notes;

    @Column(name = "customer_notes")
    private String customerNotes;

    @Column(name = "admin_notes")
    private String adminNotes;

    @Column(name = "refund_method", length = 50)
    private String refundMethod;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    /* =========================
       MAPPING
       ========================= */

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "payment_id", insertable = false, updatable = false)
    private Payment payment;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Refund() {
        this.paymentId = 0;
        this.orderId = 0;
        this.customerId = 0;
        this.amount = 0.0;

        this.type = "";
        this.reason = "";
        this.status = "";
        this.approvedBy = "";
        this.processedBy = "";
        this.notes = "";
        this.customerNotes = "";
        this.adminNotes = "";
        this.refundMethod = "";
        this.transactionId = "";

        this.requestDate = new Date();
        this.approvedDate = new Date();
        this.processedDate = new Date();
        this.completedDate = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có refundId)
    public Refund(int paymentId,
                  int orderId,
                  int customerId,
                  String type,
                  String reason,
                  double amount,
                  String status,
                  Date requestDate,
                  Date approvedDate,
                  Date processedDate,
                  Date completedDate,
                  String approvedBy,
                  String processedBy,
                  String notes,
                  String customerNotes,
                  String adminNotes,
                  String refundMethod,
                  String transactionId) {

        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.type = type != null ? type : "";
        this.reason = reason != null ? reason : "";
        this.amount = amount;
        this.status = status != null ? status : "";
        this.requestDate = requestDate;
        this.approvedDate = approvedDate;
        this.processedDate = processedDate;
        this.completedDate = completedDate;
        this.approvedBy = approvedBy != null ? approvedBy : "";
        this.processedBy = processedBy != null ? processedBy : "";
        this.notes = notes != null ? notes : "";
        this.customerNotes = customerNotes != null ? customerNotes : "";
        this.adminNotes = adminNotes != null ? adminNotes : "";
        this.refundMethod = refundMethod != null ? refundMethod : "";
        this.transactionId = transactionId != null ? transactionId : "";
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getRefundId() {
        return refundId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason != null ? reason : "";
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy != null ? approvedBy : "";
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy != null ? processedBy : "";
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes != null ? customerNotes : "";
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes != null ? adminNotes : "";
    }

    public String getRefundMethod() {
        return refundMethod;
    }

    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod != null ? refundMethod : "";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId != null ? transactionId : "";
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}