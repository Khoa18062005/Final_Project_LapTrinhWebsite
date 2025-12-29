package viettech.dto;

import java.util.Date;

public class PhoneDetailDTO extends ProductDetailDTO {
    private double screenSize;
    private String screenResolution;
    private String screenType;
    private int refreshRate;
    private int batteryCapacity;
    private String chargerType;
    private int chargingSpeed;
    private String processor;
    private String gpu;
    private String rearCamera;
    private String frontCamera;
    private String videoRecording;
    private String os;
    private String osVersion;
    private String simType;
    private String networkSupport;
    private String connectivity;
    private boolean nfc;
    private String waterproofRating;
    private String dustproofRating;
    private boolean fingerprintSensor;
    private boolean faceRecognition;
    private boolean wirelessCharging;
    private boolean reverseCharging;
    private boolean audioJack;

    // Constructor mặc định
    public PhoneDetailDTO() {
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

    // Constructor all-arg (kết hợp với super all-arg)
    public PhoneDetailDTO(int productId, int vendorId, int categoryId, String name, String slug,
                          double basePrice, String description, String brand, String specifications,
                          String status, String conditions, double weight, String dimensions,
                          Date createdAt, Date updatedAt, double averageRating, int totalReviews,
                          int totalSold, int viewCount, boolean isFeatured,
                          double screenSize, String screenResolution, String screenType, int refreshRate,
                          int batteryCapacity, String chargerType, int chargingSpeed, String processor,
                          String gpu, String rearCamera, String frontCamera, String videoRecording,
                          String os, String osVersion, String simType, String networkSupport,
                          String connectivity, boolean nfc, String waterproofRating, String dustproofRating,
                          boolean fingerprintSensor, boolean faceRecognition, boolean wirelessCharging,
                          boolean reverseCharging, boolean audioJack, String imageUrl) {
        super(productId, vendorId, categoryId, name, slug, basePrice, description, brand, specifications,
                status, conditions, weight, dimensions, createdAt, updatedAt, averageRating, totalReviews,
                totalSold, viewCount, isFeatured, imageUrl);
        this.screenSize = screenSize;
        this.screenResolution = screenResolution;
        this.screenType = screenType;
        this.refreshRate = refreshRate;
        this.batteryCapacity = batteryCapacity;
        this.chargerType = chargerType;
        this.chargingSpeed = chargingSpeed;
        this.processor = processor;
        this.gpu = gpu;
        this.rearCamera = rearCamera;
        this.frontCamera = frontCamera;
        this.videoRecording = videoRecording;
        this.os = os;
        this.osVersion = osVersion;
        this.simType = simType;
        this.networkSupport = networkSupport;
        this.connectivity = connectivity;
        this.nfc = nfc;
        this.waterproofRating = waterproofRating;
        this.dustproofRating = dustproofRating;
        this.fingerprintSensor = fingerprintSensor;
        this.faceRecognition = faceRecognition;
        this.wirelessCharging = wirelessCharging;
        this.reverseCharging = reverseCharging;
        this.audioJack = audioJack;
    }

    // Getters và Setters
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
        this.screenResolution = screenResolution;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
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
        this.chargerType = chargerType;
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
        this.processor = processor;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getRearCamera() {
        return rearCamera;
    }

    public void setRearCamera(String rearCamera) {
        this.rearCamera = rearCamera;
    }

    public String getFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(String frontCamera) {
        this.frontCamera = frontCamera;
    }

    public String getVideoRecording() {
        return videoRecording;
    }

    public void setVideoRecording(String videoRecording) {
        this.videoRecording = videoRecording;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public String getNetworkSupport() {
        return networkSupport;
    }

    public void setNetworkSupport(String networkSupport) {
        this.networkSupport = networkSupport;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
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
        this.waterproofRating = waterproofRating;
    }

    public String getDustproofRating() {
        return dustproofRating;
    }

    public void setDustproofRating(String dustproofRating) {
        this.dustproofRating = dustproofRating;
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