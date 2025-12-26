package viettech.entity.product;

import viettech.entity.order.OrderDetail;
import viettech.entity.storage.Inventory;
import viettech.entity.voucher.FlashSale;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "variants")
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private int variantId;

    /* =========================
       RELATIONSHIPS
       ========================= */

    // Variant * -- 1 Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Variant 1 -- 0..* Inventory
    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<Inventory> inventories;

    // Variant 1 -- 0..* ProductImage
    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<ProductImage> images;

    // Variant 1 -- 1..* VariantAttribute
    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<VariantAttribute> attributes;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<FlashSale> flashSales;

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

    public Variant() {
        this.sku = "";
        this.basePrice = 0.0;
        this.finalPrice = 0.0;
        this.weight = 0.0;
        this.isActive = true;
        this.createdAt = new Date();
    }

    public Variant(Product product,
                   String sku,
                   double basePrice,
                   double weight) {

        this.product = product;
        this.sku = sku != null ? sku : "";
        this.basePrice = basePrice;
        this.finalPrice = basePrice;
        this.weight = weight;
        this.isActive = true;
        this.createdAt = new Date();
    }

    /* =========================
       GETTERS
       ========================= */

    public int getVariantId() {
        return variantId;
    }

    public Product getProduct() {
        return product;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public List<VariantAttribute> getAttributes() {
        return attributes;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setAttributes(List<VariantAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<FlashSale> getFlashSales() {
        return flashSales;
    }

    public void setFlashSales(List<FlashSale> flashSales) {
        this.flashSales = flashSales;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
