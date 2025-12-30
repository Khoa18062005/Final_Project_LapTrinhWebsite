package viettech.dto;

import java.io.Serializable;

/**
 * DTO dùng để hứng dữ liệu từ Form thêm/sửa địa chỉ
 */
public class Address_dto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String receiverName;
    private String phone;
    private String street;
    private String ward;
    private String district;
    private String city;
    private boolean isDefault;

    // 1. Constructor không đối số (Bắt buộc)
    public Address_dto() {
    }

    // 2. Constructor đầy đủ đối số
    public Address_dto(String receiverName, String phone, String street,
                     String ward, String district, String city, boolean isDefault) {
        this.receiverName = receiverName;
        this.phone = phone;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
    }

    // 3. Getters và Setters
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    // 4. Validate method
    public boolean isValid() {
        return receiverName != null && !receiverName.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty() &&
               street != null && !street.trim().isEmpty() &&
               ward != null && !ward.trim().isEmpty() &&
               district != null && !district.trim().isEmpty() &&
               city != null && !city.trim().isEmpty();
    }

    // 5. To String for debugging
    @Override
    public String toString() {
        return "AddressDTO [receiverName=" + receiverName + ", phone=" + phone + 
               ", street=" + street + ", ward=" + ward + 
               ", district=" + district + ", city=" + city + 
               ", isDefault=" + isDefault + "]";
    }

    // Thêm phương thức mới để xử lý checkbox
    public void setDefault(String isDefaultValue) {
        this.isDefault = "on".equals(isDefaultValue) || "true".equalsIgnoreCase(isDefaultValue);
    }


}