package viettech.entity.product;

import viettech.entity.order.OrderDetail;
import viettech.entity.storage.Inventory;

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
}
