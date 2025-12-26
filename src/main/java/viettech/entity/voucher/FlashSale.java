package viettech.entity.voucher;

import viettech.entity.product.Product;
import viettech.entity.product.Variant;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "flash_sales")
public class FlashSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private int saleId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "variant_id")
    private int variantId;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "original_price", precision = 12, scale = 2, nullable = false)
    private double originalPrice;

    @Column(name = "sale_price", precision = 12, scale = 2, nullable = false)
    private double salePrice;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private double discountPercent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "sold_count", nullable = false)
    private int soldCount;

    @Column(name = "limit_per_customer", nullable = false)
    private int limitPerCustomer;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    /* =========================
       MAPPING
       ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", insertable = false, updatable = false)
    private Variant variant;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public FlashSale() {
        this.productId = 0;
        this.variantId = 0;
        this.originalPrice = 0.0;
        this.salePrice = 0.0;
        this.discountPercent = 0.0;
        this.totalQuantity = 0;
        this.soldCount = 0;
        this.limitPerCustomer = 0;
        this.isActive = false;

        this.name = "";
        this.description = "";

        this.startTime = new Date();
        this.endTime = new Date();
        this.createdAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có saleId)
    public FlashSale(int productId,
                     int variantId,
                     String name,
                     String description,
                     double originalPrice,
                     double salePrice,
                     double discountPercent,
                     Date startTime,
                     Date endTime,
                     int totalQuantity,
                     int soldCount,
                     int limitPerCustomer,
                     boolean isActive) {

        this.productId = productId;
        this.variantId = variantId;
        this.name = name != null ? name : "";
        this.description = description != null ? description : "";
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.discountPercent = discountPercent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalQuantity = totalQuantity;
        this.soldCount = soldCount;
        this.limitPerCustomer = limitPerCustomer;
        this.isActive = isActive;
        this.createdAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getSaleId() {
        return saleId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public int getLimitPerCustomer() {
        return limitPerCustomer;
    }

    public void setLimitPerCustomer(int limitPerCustomer) {
        this.limitPerCustomer = limitPerCustomer;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }
}