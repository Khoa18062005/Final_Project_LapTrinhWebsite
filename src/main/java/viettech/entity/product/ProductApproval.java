package viettech.entity.product;

import javax.persistence.*;
import java.util.Date;

/**
 * ProductApproval Entity - Quản lý phê duyệt sản phẩm
 * Khi vendor thêm/sửa/xóa sản phẩm, cần admin phê duyệt
 */
@Entity
@Table(name = "product_approvals")
public class ProductApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_id")
    private int approvalId;

    @Column(name = "product_id")
    private Integer productId; // Null nếu là thêm mới

    @Column(name = "vendor_id", nullable = false)
    private int vendorId;

    @Column(name = "action_type", nullable = false, length = 20)
    private String actionType; // ADD, UPDATE, DELETE

    @Column(name = "status", nullable = false, length = 20)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(name = "product_data", columnDefinition = "TEXT")
    private String productData; // JSON data của sản phẩm

    @Column(name = "requested_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedAt;

    @Column(name = "reviewed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt;

    @Column(name = "reviewed_by")
    private Integer reviewedBy; // Admin ID

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "notes", length = 500)
    private String notes;

    // Constructors
    public ProductApproval() {
        this.requestedAt = new Date();
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(int approvalId) {
        this.approvalId = approvalId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductData() {
        return productData;
    }

    public void setProductData(String productData) {
        this.productData = productData;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

