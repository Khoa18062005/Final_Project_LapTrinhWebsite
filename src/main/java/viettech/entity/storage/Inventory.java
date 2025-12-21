package viettech.entity.storage;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private int inventoryId;

    @Column(name = "warehouse_id", nullable = false)
    private int warehouseId;

    @Column(name = "variant_id", nullable = false)
    private int variantId;

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

    // Constructor mặc định (BẮT BUỘC cho JPA)
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

    // Constructor đầy đủ tham số (KHÔNG có inventoryId)
    public Inventory(int warehouseId,
                     int variantId,
                     int stockQuantity,
                     int reservedQuantity,
                     int availableQuantity,
                     int damagedQuantity,
                     int reorderLevel,
                     int reorderQuantity,
                     String location) {

        this.warehouseId = warehouseId;
        this.variantId = variantId;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
        this.damagedQuantity = damagedQuantity;
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

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
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
