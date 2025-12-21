package viettech.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String type;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phone;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String ward;

    private double latitude;
    private double longitude;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Address() {
        this.userId = "";
        this.type = "";
        this.recipientName = "";
        this.phone = "";
        this.addressLine = "";
        this.province = "";
        this.district = "";
        this.ward = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.isDefault = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Constructor dùng khi tạo Address mới
    public Address(String userId,
                   String type,
                   String recipientName,
                   String phone,
                   String addressLine,
                   String province,
                   String district,
                   String ward,
                   double latitude,
                   double longitude,
                   boolean isDefault) {

        this.userId = userId != null ? userId : "";
        this.type = type != null ? type : "";
        this.recipientName = recipientName != null ? recipientName : "";
        this.phone = phone != null ? phone : "";
        this.addressLine = addressLine != null ? addressLine : "";
        this.province = province != null ? province : "";
        this.district = district != null ? district : "";
        this.ward = ward != null ? ward : "";
        this.latitude = latitude;
        this.longitude = longitude;
        this.isDefault = isDefault;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public Long getAddressId() {
        return addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId != null ? userId : "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName != null ? recipientName : "";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone : "";
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine != null ? addressLine : "";
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province != null ? province : "";
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district != null ? district : "";
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward != null ? ward : "";
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt != null ? createdAt : new Date();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt != null ? updatedAt : new Date();
    }
}
