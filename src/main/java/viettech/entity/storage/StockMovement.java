package viettech.entity.storage;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private int movementId;

    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "from_warehouse_id")
    private Integer fromWarehouseId;

    @Column(name = "to_warehouse_id")
    private Integer toWarehouseId;

    @Column(length = 255)
    private String reason;

    @Column(name = "performed_by", length = 100)
    private String performedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    @Column(name = "reference_id", length = 100)
    private String referenceId;

    /* =========================
       MAPPING
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", insertable = false, updatable = false)
    private Inventory inventory;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public StockMovement() {
        this.type = "";
        this.quantity = 0;
        this.reason = "";
        this.performedBy = "";
        this.referenceId = "";
        this.timestamp = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có movementId)
    public StockMovement(int inventoryId,
                         String type,
                         int quantity,
                         Integer fromWarehouseId,
                         Integer toWarehouseId,
                         String reason,
                         String performedBy,
                         String referenceId) {

        this.inventoryId = inventoryId;
        this.type = type != null ? type : "";
        this.quantity = quantity;
        this.fromWarehouseId = fromWarehouseId;
        this.toWarehouseId = toWarehouseId;
        this.reason = reason;
        this.performedBy = performedBy;
        this.referenceId = referenceId;
        this.timestamp = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getMovementId() {
        return movementId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getFromWarehouseId() {
        return fromWarehouseId;
    }

    public void setFromWarehouseId(Integer fromWarehouseId) {
        this.fromWarehouseId = fromWarehouseId;
    }

    public Integer getToWarehouseId() {
        return toWarehouseId;
    }

    public void setToWarehouseId(Integer toWarehouseId) {
        this.toWarehouseId = toWarehouseId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
