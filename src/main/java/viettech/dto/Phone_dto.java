package viettech.dto;

/**
 * DTO for Phone extending Product_dto
 * Chuẩn bị dữ liệu điện thoại để thêm vào PhoneDAO
 */
public class Phone_dto extends Product_dto {
    private static final long serialVersionUID = 1L;

    // ===== Phone Specific Properties =====
    private String screenSize;
    private String screenResolution;
    private String screenType;
    private String refreshRate;
    private String batteryCapacity;
    private String chargerType;
    private String chargingSpeed;
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
    private String nfc;
    private String waterproofRating;
    private String dustproofRating;
    private String fingerprintSensor;
    private String faceRecognition;
    private String wirelessCharging;
    private String reverseCharging;
    private String audioJack;

    /* =========================
       CONSTRUCTORS
       ========================= */

    public Phone_dto() {
        super();
    }

    public Phone_dto(String vendorId, String categoryId, String name, String slug,
                     String basePrice, String description, String brand, String specifications,
                     String status, String conditions, String weight, String dimensions,
                     String isFeatured, String screenSize, String screenResolution,
                     String screenType, String refreshRate, String batteryCapacity,
                     String chargerType, String chargingSpeed, String processor, String gpu,
                     String rearCamera, String frontCamera, String videoRecording,
                     String os, String osVersion, String simType, String networkSupport,
                     String connectivity, String nfc, String waterproofRating,
                     String dustproofRating, String fingerprintSensor, String faceRecognition,
                     String wirelessCharging, String reverseCharging, String audioJack) {
        super(vendorId, categoryId, name, slug, basePrice, description, brand,
                specifications, status, conditions, weight, dimensions, isFeatured);
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

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getScreenSize() { return screenSize; }
    public void setScreenSize(String screenSize) { this.screenSize = screenSize; }

    public String getScreenResolution() { return screenResolution; }
    public void setScreenResolution(String screenResolution) { this.screenResolution = screenResolution; }

    public String getScreenType() { return screenType; }
    public void setScreenType(String screenType) { this.screenType = screenType; }

    public String getRefreshRate() { return refreshRate; }
    public void setRefreshRate(String refreshRate) { this.refreshRate = refreshRate; }

    public String getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(String batteryCapacity) { this.batteryCapacity = batteryCapacity; }

    public String getChargerType() { return chargerType; }
    public void setChargerType(String chargerType) { this.chargerType = chargerType; }

    public String getChargingSpeed() { return chargingSpeed; }
    public void setChargingSpeed(String chargingSpeed) { this.chargingSpeed = chargingSpeed; }

    public String getProcessor() { return processor; }
    public void setProcessor(String processor) { this.processor = processor; }

    public String getGpu() { return gpu; }
    public void setGpu(String gpu) { this.gpu = gpu; }

    public String getRearCamera() { return rearCamera; }
    public void setRearCamera(String rearCamera) { this.rearCamera = rearCamera; }

    public String getFrontCamera() { return frontCamera; }
    public void setFrontCamera(String frontCamera) { this.frontCamera = frontCamera; }

    public String getVideoRecording() { return videoRecording; }
    public void setVideoRecording(String videoRecording) { this.videoRecording = videoRecording; }

    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }

    public String getOsVersion() { return osVersion; }
    public void setOsVersion(String osVersion) { this.osVersion = osVersion; }

    public String getSimType() { return simType; }
    public void setSimType(String simType) { this.simType = simType; }

    public String getNetworkSupport() { return networkSupport; }
    public void setNetworkSupport(String networkSupport) { this.networkSupport = networkSupport; }

    public String getConnectivity() { return connectivity; }
    public void setConnectivity(String connectivity) { this.connectivity = connectivity; }

    public String getNfc() { return nfc; }
    public void setNfc(String nfc) { this.nfc = nfc; }

    public String getWaterproofRating() { return waterproofRating; }
    public void setWaterproofRating(String waterproofRating) { this.waterproofRating = waterproofRating; }

    public String getDustproofRating() { return dustproofRating; }
    public void setDustproofRating(String dustproofRating) { this.dustproofRating = dustproofRating; }

    public String getFingerprintSensor() { return fingerprintSensor; }
    public void setFingerprintSensor(String fingerprintSensor) { this.fingerprintSensor = fingerprintSensor; }

    public String getFaceRecognition() { return faceRecognition; }
    public void setFaceRecognition(String faceRecognition) { this.faceRecognition = faceRecognition; }

    public String getWirelessCharging() { return wirelessCharging; }
    public void setWirelessCharging(String wirelessCharging) { this.wirelessCharging = wirelessCharging; }

    public String getReverseCharging() { return reverseCharging; }
    public void setReverseCharging(String reverseCharging) { this.reverseCharging = reverseCharging; }

    public String getAudioJack() { return audioJack; }
    public void setAudioJack(String audioJack) { this.audioJack = audioJack; }

    /* =========================
       CONVERSION METHODS FOR DAO
       ========================= */

    /**
     * Get parsed vendorId as int
     */
    public int getVendorIdAsInt() {
        return parseIntOrDefault(vendorId, 0);
    }

    /**
     * Get parsed categoryId as int
     */
    public int getCategoryIdAsInt() {
        return parseIntOrDefault(categoryId, 0);
    }

    /**
     * Get parsed basePrice as double
     */
    public double getBasePriceAsDouble() {
        return parseDoubleOrDefault(basePrice, 0.0);
    }

    /**
     * Get parsed weight as double
     */
    public double getWeightAsDouble() {
        return parseDoubleOrDefault(weight, 0.0);
    }

    /**
     * Get parsed isFeatured as boolean
     */
    public boolean getIsFeaturedAsBoolean() {
        return parseBoolean(isFeatured);
    }

    /**
     * Get parsed screenSize as double
     */
    public double getScreenSizeAsDouble() {
        return parseDoubleOrDefault(screenSize, 0.0);
    }

    /**
     * Get parsed refreshRate as int
     */
    public int getRefreshRateAsInt() {
        return parseIntOrDefault(refreshRate, 60);
    }

    /**
     * Get parsed batteryCapacity as int
     */
    public int getBatteryCapacityAsInt() {
        return parseIntOrDefault(batteryCapacity, 0);
    }

    /**
     * Get parsed chargingSpeed as int
     */
    public int getChargingSpeedAsInt() {
        return parseIntOrDefault(chargingSpeed, 0);
    }

    /**
     * Get parsed nfc as boolean
     */
    public boolean getNfcAsBoolean() {
        return parseBoolean(nfc);
    }

    /**
     * Get parsed fingerprintSensor as boolean
     */
    public boolean getFingerprintSensorAsBoolean() {
        return parseBoolean(fingerprintSensor);
    }

    /**
     * Get parsed faceRecognition as boolean
     */
    public boolean getFaceRecognitionAsBoolean() {
        return parseBoolean(faceRecognition);
    }

    /**
     * Get parsed wirelessCharging as boolean
     */
    public boolean getWirelessChargingAsBoolean() {
        return parseBoolean(wirelessCharging);
    }

    /**
     * Get parsed reverseCharging as boolean
     */
    public boolean getReverseChargingAsBoolean() {
        return parseBoolean(reverseCharging);
    }

    /**
     * Get parsed audioJack as boolean
     */
    public boolean getAudioJackAsBoolean() {
        return parseBoolean(audioJack);
    }

    /**
     * Validate all required fields for Phone
     */
    public boolean isValid() {
        return isValidString(name) &&
               isValidString(slug) &&
               isValidString(basePrice) &&
               isValidString(processor) &&
               isValidString(screenSize) &&
               isValidString(batteryCapacity);
    }

    @Override
    public String toString() {
        return "Phone_dto{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", processor='" + processor + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", batteryCapacity='" + batteryCapacity + '\'' +
                '}';
    }
}
