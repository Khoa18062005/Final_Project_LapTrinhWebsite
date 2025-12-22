package viettech.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "shippers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Shipper extends User {

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(name = "vehicle_plate", nullable = false)
    private String vehiclePlate;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @Column(name = "current_latitude", nullable = false)
    private double currentLatitude;

    @Column(name = "current_longitude", nullable = false)
    private double currentLongitude;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "total_deliveries", nullable = false)
    private int totalDeliveries;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Shipper() {
        super();

        this.licenseNumber = "";
        this.vehicleType = "";
        this.vehiclePlate = "";
        this.isAvailable = false;

        this.currentLatitude = 0.0;
        this.currentLongitude = 0.0;

        this.rating = 0.0;
        this.totalDeliveries = 0;
        this.roleID = 3;
    }

    // Constructor dùng khi đăng ký shipper
    public Shipper(String firstName,
                   String lastName,
                   String username,
                   String password,
                   String email,
                   String phone,
                   String avatar,
                   String gender,
                   String licenseNumber,
                   String vehicleType,
                   String vehiclePlate) {

        super(firstName, lastName, username, password, email, phone, avatar, gender);

        this.roleID = 3;
        this.licenseNumber = licenseNumber != null ? licenseNumber : "";
        this.vehicleType = vehicleType != null ? vehicleType : "";
        this.vehiclePlate = vehiclePlate != null ? vehiclePlate : "";

        this.isAvailable = false;     // chưa nhận đơn
        this.currentLatitude = 0.0;   // chưa có vị trí
        this.currentLongitude = 0.0;

        this.rating = 0.0;            // chưa có đánh giá
        this.totalDeliveries = 0;     // chưa giao đơn nào
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber != null ? licenseNumber : "";
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType != null ? vehicleType : "";
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate != null ? vehiclePlate : "";
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalDeliveries() {
        return totalDeliveries;
    }

    public void setTotalDeliveries(int totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }
}
