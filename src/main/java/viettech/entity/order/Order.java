package viettech.entity.order;

import viettech.entity.Address;
import viettech.entity.user.Customer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "vendor_id", nullable = false)
    private int vendorId;

    @Column(name = "address_id", nullable = false)
    private int addressId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private double subtotal;

    @Column(name = "shipping_fee", nullable = false)
    private double shippingFee;

    @Column(nullable = false)
    private double discount;

    @Column(nullable = false)
    private double tax;

    @Column(name = "voucher_discount", nullable = false)
    private double voucherDiscount;

    @Column(name = "loyalty_points_used", nullable = false)
    private int loyaltyPointsUsed;

    @Column(name = "loyalty_points_discount", nullable = false)
    private double loyaltyPointsDiscount;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(length = 500)
    private String notes;

    @Column(name = "cancel_reason", length = 255)
    private String cancelReason;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cancelled_at")
    private Date cancelledAt;

    @Column(name = "cancelled_by", length = 100)
    private String cancelledBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "estimated_delivery")
    private Date estimatedDelivery;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completed_at")
    private Date completedAt;

    /* =========================
       MAPPING
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    private Address address;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Order() {
        this.orderNumber = "";
        this.status = "";
        this.subtotal = 0.0;
        this.shippingFee = 0.0;
        this.discount = 0.0;
        this.tax = 0.0;
        this.voucherDiscount = 0.0;
        this.loyaltyPointsUsed = 0;
        this.loyaltyPointsDiscount = 0.0;
        this.totalPrice = 0.0;
        this.notes = "";
        this.orderDate = new Date();
    }

    // Constructor tạo Order mới (KHÔNG có orderId)
    public Order(String orderNumber,
                 int customerId,
                 int vendorId,
                 int addressId,
                 String status,
                 double subtotal,
                 double shippingFee,
                 double discount,
                 double tax,
                 double voucherDiscount,
                 int loyaltyPointsUsed,
                 double loyaltyPointsDiscount,
                 double totalPrice,
                 String notes,
                 Date estimatedDelivery) {

        this.orderNumber = orderNumber != null ? orderNumber : "";
        this.customerId = customerId;
        this.vendorId = vendorId;
        this.addressId = addressId;
        this.status = status != null ? status : "";
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discount = discount;
        this.tax = tax;
        this.voucherDiscount = voucherDiscount;
        this.loyaltyPointsUsed = loyaltyPointsUsed;
        this.loyaltyPointsDiscount = loyaltyPointsDiscount;
        this.totalPrice = totalPrice;
        this.notes = notes;
        this.orderDate = new Date();
        this.estimatedDelivery = estimatedDelivery;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber != null ? orderNumber : "";
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(double voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public int getLoyaltyPointsUsed() {
        return loyaltyPointsUsed;
    }

    public void setLoyaltyPointsUsed(int loyaltyPointsUsed) {
        this.loyaltyPointsUsed = loyaltyPointsUsed;
    }

    public double getLoyaltyPointsDiscount() {
        return loyaltyPointsDiscount;
    }

    public void setLoyaltyPointsDiscount(double loyaltyPointsDiscount) {
        this.loyaltyPointsDiscount = loyaltyPointsDiscount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Date getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Date cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Date getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(Date estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}
