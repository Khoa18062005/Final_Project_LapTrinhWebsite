package viettech.entity.storage;

import viettech.entity.product.Variant;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "inventories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"warehouse_id", "variant_id"})
        }
)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private int inventoryId;

    /* =========================
       RELATIONSHIPS
       ========================= */

    // Inventory "*" -- "1" Warehouse
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // Inventory "*" -- "1" Variant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;

    // Inventory "1" -- "0..*" StockMovement
    @OneToMany(
            mappedBy = "inventory",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StockMovement> stockMovements;

    /* =========================
       FIELDS
       ========================= */

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "damaged_quantity", nullable = false)
    private int damagedQuantity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    private Date lastUpdated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_restocked_at")
    private Date lastRestockedAt;

    @Column(name = "reorder_level", nullable = false)
    private int reorderLevel;

    @Column(name = "reorder_quantity", nullable = false)
    private int reorderQuantity;

    @Column(length = 100)
    private String location;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // BẮT BUỘC cho JPA
    public Inventory() {
        this.stockQuantity = 0;
        this.reservedQuantity = 0;
        this.availableQuantity = 0;
        this.damagedQuantity = 0;
        this.reorderLevel = 0;
        this.reorderQuantity = 0;
        this.location = "";
        this.lastUpdated = new Date();
    }

    // Constructor tạo Inventory mới
    public Inventory(Warehouse warehouse,
                     Variant variant,
                     int stockQuantity,
                     int reorderLevel,
                     int reorderQuantity,
                     String location) {

        this.warehouse = warehouse;
        this.variant = variant;
        this.stockQuantity = stockQuantity;
        this.availableQuantity = stockQuantity;
        this.reservedQuantity = 0;
        this.damagedQuantity = 0;
        this.reorderLevel = reorderLevel;
        this.reorderQuantity = reorderQuantity;
        this.location = location != null ? location : "";
        this.lastUpdated = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getInventoryId() {
        return inventoryId;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public List<StockMovement> getStockMovements() {
        return stockMovements;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        this.lastUpdated = new Date();
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
        this.lastUpdated = new Date();
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.lastUpdated = new Date();
    }

    public int getDamagedQuantity() {
        return damagedQuantity;
    }

    public void setDamagedQuantity(int damagedQuantity) {
        this.damagedQuantity = damagedQuantity;
        this.lastUpdated = new Date();
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Date getLastRestockedAt() {
        return lastRestockedAt;
    }

    public void setLastRestockedAt(Date lastRestockedAt) {
        this.lastRestockedAt = lastRestockedAt;
        this.lastUpdated = new Date();
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(int reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location != null ? location : "";
    }
}
