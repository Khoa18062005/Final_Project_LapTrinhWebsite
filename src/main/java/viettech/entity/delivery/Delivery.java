package viettech.entity.delivery;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private int deliveryId;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "tracking_number", unique = true, length = 100)
    private String trackingNumber;

    @Column(name = "shipping_method", length = 50)
    private String shippingMethod;

    @Column(length = 100)
    private String carrier;

    @Column(name = "warehouse_id", nullable = false)
    private int warehouseId;

    @Column(name = "address_id", nullable = false)
    private int addressId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "estimated_pickup")
    private Date estimatedPickup;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "actual_pickup")
    private Date actualPickup;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "estimated_delivery")
    private Date estimatedDelivery;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "actual_delivery")
    private Date actualDelivery;

    @Column(name = "shipping_fee", nullable = false)
    private double shippingFee;

    @Column(name = "insurance_fee", nullable = false)
    private double insuranceFee;

    @Column(name = "current_location", length = 255)
    private String currentLocation;

    @Column(name = "current_latitude")
    private double currentLatitude;

    @Column(name = "current_longitude")
    private double currentLongitude;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(length = 255)
    private String signature;

    @Column(name = "proof_of_delivery", length = 500)
    private String proofOfDelivery;

    @Column(length = 500)
    private String notes;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Delivery() {
        this.trackingNumber = "";
        this.shippingMethod = "";
        this.carrier = "";
        this.currentLocation = "";
        this.status = "";
        this.shippingFee = 0.0;
        this.insuranceFee = 0.0;
        this.attemptCount = 0;
    }

    // Constructor đầy đủ tham số (KHÔNG có deliveryId)
    public Delivery(int orderId,
                    int warehouseId,
                    int addressId,
                    String trackingNumber,
                    String shippingMethod,
                    String carrier,
                    Date estimatedPickup,
                    Date estimatedDelivery,
                    double shippingFee,
                    double insuranceFee,
                    String status) {

        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.addressId = addressId;
        this.trackingNumber = trackingNumber;
        this.shippingMethod = shippingMethod;
        this.carrier = carrier;
        this.estimatedPickup = estimatedPickup;
        this.estimatedDelivery = estimatedDelivery;
        this.shippingFee = shippingFee;
        this.insuranceFee = insuranceFee;
        this.status = status != null ? status : "";
        this.attemptCount = 0;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getDeliveryId() {
        return deliveryId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Date getEstimatedPickup() {
        return estimatedPickup;
    }

    public void setEstimatedPickup(Date estimatedPickup) {
        this.estimatedPickup = estimatedPickup;
    }

    public Date getActualPickup() {
        return actualPickup;
    }

    public void setActualPickup(Date actualPickup) {
        this.actualPickup = actualPickup;
    }

    public Date getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(Date estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public Date getActualDelivery() {
        return actualDelivery;
    }

    public void setActualDelivery(Date actualDelivery) {
        this.actualDelivery = actualDelivery;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getProofOfDelivery() {
        return proofOfDelivery;
    }

    public void setProofOfDelivery(String proofOfDelivery) {
        this.proofOfDelivery = proofOfDelivery;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
