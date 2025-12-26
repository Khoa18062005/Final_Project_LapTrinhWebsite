package viettech.entity.payment;

import viettech.entity.order.Order;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "method", length = 50)
    private String method;

    @Column(name = "provider", length = 100)
    private String provider;

    @Column(name = "amount", precision = 12, scale = 2)
    private double amount;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date")
    private Date paymentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "paid_at")
    private Date paidAt;

    @Column(name = "gateway", length = 100)
    private String gateway;

    @Column(name = "bank_code", length = 50)
    private String bankCode;

    @Column(name = "response_code", length = 50)
    private String responseCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "metadata")
    private String metadata;

    /* =========================
       MAPPING
       ========================= */

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<Refund> refunds;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Payment() {
        this.orderId = 0;
        this.amount = 0.0;

        this.method = "";
        this.provider = "";
        this.status = "";
        this.transactionId = "";
        this.gateway = "";
        this.bankCode = "";
        this.responseCode = "";
        this.errorMessage = "";
        this.ipAddress = "";
        this.metadata = "";

        this.paymentDate = new Date();
        this.paidAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có paymentId)
    public Payment(int orderId,
                   String method,
                   String provider,
                   double amount,
                   String status,
                   String transactionId,
                   Date paymentDate,
                   Date paidAt,
                   String gateway,
                   String bankCode,
                   String responseCode,
                   String errorMessage,
                   String ipAddress,
                   String metadata) {

        this.orderId = orderId;
        this.method = method != null ? method : "";
        this.provider = provider != null ? provider : "";
        this.amount = amount;
        this.status = status != null ? status : "";
        this.transactionId = transactionId != null ? transactionId : "";
        this.paymentDate = paymentDate;
        this.paidAt = paidAt;
        this.gateway = gateway != null ? gateway : "";
        this.bankCode = bankCode != null ? bankCode : "";
        this.responseCode = responseCode != null ? responseCode : "";
        this.errorMessage = errorMessage != null ? errorMessage : "";
        this.ipAddress = ipAddress != null ? ipAddress : "";
        this.metadata = metadata != null ? metadata : "";
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getPaymentId() {
        return paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method != null ? method : "";
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider != null ? provider : "";
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId != null ? transactionId : "";
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway != null ? gateway : "";
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode != null ? bankCode : "";
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode != null ? responseCode : "";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage != null ? errorMessage : "";
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress != null ? ipAddress : "";
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata != null ? metadata : "";
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}