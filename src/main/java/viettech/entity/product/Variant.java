package viettech.entity.product;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "variants")
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private Long variantId;

    /* =========================
       RELATIONSHIPS
       ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /* =========================
       FIELDS
       ========================= */

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @Column(name = "final_price", nullable = false)
    private double finalPrice;

    @Column(nullable = false)
    private double weight;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Variant() {
        this.sku = "";
        this.basePrice = 0.0;
        this.finalPrice = 0.0;
        this.weight = 0.0;
        this.isActive = true;
        this.createdAt = new Date();
    }

    // Constructor tạo Variant mới
    public Variant(Product product,
                   String sku,
                   double basePrice,
                   double weight) {

        this.product = product;
        this.sku = sku != null ? sku : "";
        this.basePrice = basePrice;
        this.finalPrice = basePrice; // mặc định = basePrice
        this.weight = weight;
        this.isActive = true;
        this.createdAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public Long getVariantId() {
        return variantId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku != null ? sku : "";
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
}
