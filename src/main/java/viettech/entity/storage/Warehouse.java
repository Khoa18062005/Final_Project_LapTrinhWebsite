package viettech.entity.storage;

import viettech.entity.user.Vendor;
import viettech.entity.delivery.Delivery;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private int warehouseId;

    /* =========================
       RELATIONSHIPS
       ========================= */

    // Warehouse "*" -- "1" Vendor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    // Warehouse "1" -- "0..*" Delivery
    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
    private List<Delivery> deliveries;

    /* =========================
       FIELDS
       ========================= */

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "address_line", nullable = false, length = 255)
    private String addressLine;

    @Column(length = 100)
    private String ward;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String province;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "used_capacity", nullable = false)
    private int usedCapacity;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(length = 20)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(name = "operating_hours", length = 255)
    private String operatingHours;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    public Warehouse() {
        this.name = "";
        this.code = "";
        this.addressLine = "";
        this.latitude = 0;
        this.longitude = 0;
        this.capacity = 0;
        this.usedCapacity = 0;
        this.isActive = true;
        this.createdAt = new Date();
    }

    public Warehouse(Vendor vendor,
                     String name,
                     String code,
                     String addressLine,
                     double latitude,
                     double longitude,
                     int capacity) {

        this.vendor = vendor;
        this.name = name;
        this.code = code;
        this.addressLine = addressLine;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
        this.usedCapacity = 0;
        this.isActive = true;
        this.createdAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getWarehouseId() {
        return warehouseId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code : "";
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine != null ? addressLine : "";
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(int usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
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
