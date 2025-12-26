package viettech.entity.voucher;

import viettech.entity.order.Order;
import viettech.entity.user.Customer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "voucher_usages")
public class VoucherUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private int usageId;

    @Column(name = "voucher_id", nullable = false)
    private int voucherId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "used_date", nullable = false)
    private Date usedDate;

    @Column(name = "order_value", precision = 12, scale = 2, nullable = false)
    private double orderValue;

    @Column(name = "discount_applied", precision = 12, scale = 2, nullable = false)
    private double discountApplied;

    /* =========================
       MAPPING
       ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", insertable = false, updatable = false)
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public VoucherUsage() {
        this.voucherId = 0;
        this.customerId = 0;
        this.orderId = 0;
        this.orderValue = 0.0;
        this.discountApplied = 0.0;

        this.usedDate = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có usageId)
    public VoucherUsage(int voucherId,
                        int customerId,
                        int orderId,
                        Date usedDate,
                        double orderValue,
                        double discountApplied) {

        this.voucherId = voucherId;
        this.customerId = customerId;
        this.orderId = orderId;
        this.usedDate = usedDate;
        this.orderValue = orderValue;
        this.discountApplied = discountApplied;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getUsageId() {
        return usageId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(Date usedDate) {
        this.usedDate = usedDate;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public double getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(double discountApplied) {
        this.discountApplied = discountApplied;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}