package viettech.entity.order;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private int orderDetailId;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "variant_id", nullable = false)
    private int variantId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "variant_info", length = 255)
    private String variantInfo;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private double discount;

    @Column(nullable = false)
    private double subtotal;

    @Column(nullable = false, length = 50)
    private String status;

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
    public OrderDetail() {
        this.productName = "";
        this.variantInfo = "";
        this.quantity = 0;
        this.unitPrice = 0.0;
        this.discount = 0.0;
        this.subtotal = 0.0;
        this.status = "";
    }

    // Constructor đầy đủ tham số (KHÔNG có orderDetailId)
    public OrderDetail(int orderId,
                       int productId,
                       int variantId,
                       String productName,
                       String variantInfo,
                       int quantity,
                       double unitPrice,
                       double discount,
                       double subtotal,
                       String status) {

        this.orderId = orderId;
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName != null ? productName : "";
        this.variantInfo = variantInfo != null ? variantInfo : "";
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.subtotal = subtotal;
        this.status = status != null ? status : "";
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName != null ? productName : "";
    }

    public String getVariantInfo() {
        return variantInfo;
    }

    public void setVariantInfo(String variantInfo) {
        this.variantInfo = variantInfo != null ? variantInfo : "";
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
