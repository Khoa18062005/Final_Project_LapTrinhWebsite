package viettech.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "phones")
@PrimaryKeyJoinColumn(name = "product_id")
public class Phone extends Product {

    @Column(name = "screen_size", nullable = false)
    private double screenSize;

    @Column(name = "screen_resolution", nullable = false)
    private String screenResolution;

    @Column(name = "screen_type", nullable = false)
    private String screenType;

    @Column(name = "refresh_rate", nullable = false)
    private int refreshRate;

    @Column(name = "battery_capacity", nullable = false)
    private int batteryCapacity;

    @Column(name = "charger_type", nullable = false)
    private String chargerType;

    @Column(name = "charging_speed", nullable = false)
    private int chargingSpeed;

    @Column(nullable = false)
    private String processor;

    @Column(nullable = false)
    private String gpu;

    @Column(name = "rear_camera", columnDefinition = "TEXT")
    private String rearCamera;

    @Column(name = "front_camera", columnDefinition = "TEXT")
    private String frontCamera;

    @Column(name = "video_recording", columnDefinition = "TEXT")
    private String videoRecording;

    @Column(nullable = false)
    private String os;

    @Column(name = "os_version", nullable = false)
    private String osVersion;

    @Column(name = "sim_type", nullable = false)
    private String simType;

    @Column(name = "network_support", nullable = false)
    private String networkSupport;

    @Column(nullable = false)
    private String connectivity;

    @Column(nullable = false)
    private boolean nfc;

    @Column(name = "waterproof_rating")
    private String waterproofRating;

    @Column(name = "dustproof_rating")
    private String dustproofRating;

    @Column(name = "fingerprint_sensor", nullable = false)
    private boolean fingerprintSensor;

    @Column(name = "face_recognition", nullable = false)
    private boolean faceRecognition;

    @Column(name = "wireless_charging", nullable = false)
    private boolean wirelessCharging;

    @Column(name = "reverse_charging", nullable = false)
    private boolean reverseCharging;

    @Column(name = "audio_jack", nullable = false)
    private boolean audioJack;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Phone() {
        super();

        this.screenSize = 0.0;
        this.screenResolution = "";
        this.screenType = "";
        this.refreshRate = 0;
        this.batteryCapacity = 0;
        this.chargerType = "";
        this.chargingSpeed = 0;
        this.processor = "";
        this.gpu = "";
        this.rearCamera = "";
        this.frontCamera = "";
        this.videoRecording = "";
        this.os = "";
        this.osVersion = "";
        this.simType = "";
        this.networkSupport = "";
        this.connectivity = "";
        this.nfc = false;
        this.waterproofRating = "";
        this.dustproofRating = "";
        this.fingerprintSensor = false;
        this.faceRecognition = false;
        this.wirelessCharging = false;
        this.reverseCharging = false;
        this.audioJack = false;
    }

    // Constructor dùng khi tạo Phone mới
    public Phone(String vendorId,
                 String categoryId,
                 String name,
                 String slug,
                 double basePrice,
                 String description,
                 String brand,
                 String specifications,
                 String status,
                 String condition,
                 double weight,
                 String dimensions,
                 boolean isFeatured,

                 double screenSize,
                 String screenResolution,
                 String screenType,
                 int refreshRate,
                 int batteryCapacity,
                 String chargerType,
                 int chargingSpeed,
                 String processor,
                 String gpu,
                 String rearCamera,
                 String frontCamera,
                 String videoRecording,
                 String os,
                 String osVersion,
                 String simType,
                 String networkSupport,
                 String connectivity,
                 boolean nfc,
                 String waterproofRating,
                 String dustproofRating,
                 boolean fingerprintSensor,
                 boolean faceRecognition,
                 boolean wirelessCharging,
                 boolean reverseCharging,
                 boolean audioJack) {

        super(vendorId, categoryId, name, slug, basePrice,
                description, brand, specifications, status,
                condition, weight, dimensions, isFeatured);

        this.screenSize = screenSize;
        this.screenResolution = screenResolution != null ? screenResolution : "";
        this.screenType = screenType != null ? screenType : "";
        this.refreshRate = refreshRate;
        this.batteryCapacity = batteryCapacity;
        this.chargerType = chargerType != null ? chargerType : "";
        this.chargingSpeed = chargingSpeed;
        this.processor = processor != null ? processor : "";
        this.gpu = gpu != null ? gpu : "";
        this.rearCamera = rearCamera != null ? rearCamera : "";
        this.frontCamera = frontCamera != null ? frontCamera : "";
        this.videoRecording = videoRecording != null ? videoRecording : "";
        this.os = os != null ? os : "";
        this.osVersion = osVersion != null ? osVersion : "";
        this.simType = simType != null ? simType : "";
        this.networkSupport = networkSupport != null ? networkSupport : "";
        this.connectivity = connectivity != null ? connectivity : "";
        this.nfc = nfc;
        this.waterproofRating = waterproofRating != null ? waterproofRating : "";
        this.dustproofRating = dustproofRating != null ? dustproofRating : "";
        this.fingerprintSensor = fingerprintSensor;
        this.faceRecognition = faceRecognition;
        this.wirelessCharging = wirelessCharging;
        this.reverseCharging = reverseCharging;
        this.audioJack = audioJack;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution != null ? screenResolution : "";
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType != null ? screenType : "";
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public String getChargerType() {
        return chargerType;
    }

    public void setChargerType(String chargerType) {
        this.chargerType = chargerType != null ? chargerType : "";
    }

    public int getChargingSpeed() {
        return chargingSpeed;
    }

    public void setChargingSpeed(int chargingSpeed) {
        this.chargingSpeed = chargingSpeed;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor != null ? processor : "";
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu != null ? gpu : "";
    }

    public String getRearCamera() {
        return rearCamera;
    }

    public void setRearCamera(String rearCamera) {
        this.rearCamera = rearCamera != null ? rearCamera : "";
    }

    public String getFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(String frontCamera) {
        this.frontCamera = frontCamera != null ? frontCamera : "";
    }

    public String getVideoRecording() {
        return videoRecording;
    }

    public void setVideoRecording(String videoRecording) {
        this.videoRecording = videoRecording != null ? videoRecording : "";
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os != null ? os : "";
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion != null ? osVersion : "";
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType != null ? simType : "";
    }

    public String getNetworkSupport() {
        return networkSupport;
    }

    public void setNetworkSupport(String networkSupport) {
        this.networkSupport = networkSupport != null ? networkSupport : "";
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity != null ? connectivity : "";
    }

    public boolean isNfc() {
        return nfc;
    }

    public void setNfc(boolean nfc) {
        this.nfc = nfc;
    }

    public String getWaterproofRating() {
        return waterproofRating;
    }

    public void setWaterproofRating(String waterproofRating) {
        this.waterproofRating = waterproofRating != null ? waterproofRating : "";
    }

    public String getDustproofRating() {
        return dustproofRating;
    }

    public void setDustproofRating(String dustproofRating) {
        this.dustproofRating = dustproofRating != null ? dustproofRating : "";
    }

    public boolean isFingerprintSensor() {
        return fingerprintSensor;
    }

    public void setFingerprintSensor(boolean fingerprintSensor) {
        this.fingerprintSensor = fingerprintSensor;
    }

    public boolean isFaceRecognition() {
        return faceRecognition;
    }

    public void setFaceRecognition(boolean faceRecognition) {
        this.faceRecognition = faceRecognition;
    }

    public boolean isWirelessCharging() {
        return wirelessCharging;
    }

    public void setWirelessCharging(boolean wirelessCharging) {
        this.wirelessCharging = wirelessCharging;
    }

    public boolean isReverseCharging() {
        return reverseCharging;
    }

    public void setReverseCharging(boolean reverseCharging) {
        this.reverseCharging = reverseCharging;
    }

    public boolean isAudioJack() {
        return audioJack;
    }

    public void setAudioJack(boolean audioJack) {
        this.audioJack = audioJack;
    }
}
