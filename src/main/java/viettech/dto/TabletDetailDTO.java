package viettech.dto;

import java.util.Date;

public class TabletDetailDTO extends ProductDetailDTO {
    private double screenSize;
    private String screenResolution;
    private String screenType;
    private int refreshRate;
    private int batteryCapacity;
    private String processor;
    private String gpu;
    private String rearCamera;
    private String frontCamera;
    private String videoRecording;
    private String os;
    private String osVersion;
    private boolean simSupport;
    private String networkSupport;
    private String connectivity;
    private boolean stylusSupport;
    private boolean stylusIncluded;
    private boolean keyboardSupport;
    private String speakers;
    private boolean audioJack;
    private String waterproofRating;
    private boolean faceRecognition;
    private boolean fingerprintSensor;

    // Constructor mặc định
    public TabletDetailDTO() {
        super();
        this.screenSize = 0.0;
        this.screenResolution = "";
        this.screenType = "";
        this.refreshRate = 0;
        this.batteryCapacity = 0;
        this.processor = "";
        this.gpu = "";
        this.rearCamera = "";
        this.frontCamera = "";
        this.videoRecording = "";
        this.os = "";
        this.osVersion = "";
        this.simSupport = false;
        this.networkSupport = "";
        this.connectivity = "";
        this.stylusSupport = false;
        this.stylusIncluded = false;
        this.keyboardSupport = false;
        this.speakers = "";
        this.audioJack = false;
        this.waterproofRating = "";
        this.faceRecognition = false;
        this.fingerprintSensor = false;
    }

    // Constructor all-arg
    public TabletDetailDTO(int productId, int vendorId, int categoryId, String name, String slug,
                           double basePrice, String description, String brand, String specifications,
                           String status, String conditions, double weight, String dimensions,
                           Date createdAt, Date updatedAt, double averageRating, int totalReviews,
                           int totalSold, int viewCount, boolean isFeatured,
                           double screenSize, String screenResolution, String screenType, int refreshRate,
                           int batteryCapacity, String processor, String gpu, String rearCamera,
                           String frontCamera, String videoRecording, String os, String osVersion,
                           boolean simSupport, String networkSupport, String connectivity,
                           boolean stylusSupport, boolean stylusIncluded, boolean keyboardSupport,
                           String speakers, boolean audioJack, String waterproofRating,
                           boolean faceRecognition, boolean fingerprintSensor) {
        super(productId, vendorId, categoryId, name, slug, basePrice, description, brand, specifications,
                status, conditions, weight, dimensions, createdAt, updatedAt, averageRating, totalReviews,
                totalSold, viewCount, isFeatured);
        this.screenSize = screenSize;
        this.screenResolution = screenResolution;
        this.screenType = screenType;
        this.refreshRate = refreshRate;
        this.batteryCapacity = batteryCapacity;
        this.processor = processor;
        this.gpu = gpu;
        this.rearCamera = rearCamera;
        this.frontCamera = frontCamera;
        this.videoRecording = videoRecording;
        this.os = os;
        this.osVersion = osVersion;
        this.simSupport = simSupport;
        this.networkSupport = networkSupport;
        this.connectivity = connectivity;
        this.stylusSupport = stylusSupport;
        this.stylusIncluded = stylusIncluded;
        this.keyboardSupport = keyboardSupport;
        this.speakers = speakers;
        this.audioJack = audioJack;
        this.waterproofRating = waterproofRating;
        this.faceRecognition = faceRecognition;
        this.fingerprintSensor = fingerprintSensor;
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

    public boolean isSimSupport() {
        return simSupport;
    }

    public void setSimSupport(boolean simSupport) {
        this.simSupport = simSupport;
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

    public boolean isStylusSupport() {
        return stylusSupport;
    }

    public void setStylusSupport(boolean stylusSupport) {
        this.stylusSupport = stylusSupport;
    }

    public boolean isStylusIncluded() {
        return stylusIncluded;
    }

    public void setStylusIncluded(boolean stylusIncluded) {
        this.stylusIncluded = stylusIncluded;
    }

    public boolean isKeyboardSupport() {
        return keyboardSupport;
    }

    public void setKeyboardSupport(boolean keyboardSupport) {
        this.keyboardSupport = keyboardSupport;
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }

    public boolean isAudioJack() {
        return audioJack;
    }

    public void setAudioJack(boolean audioJack) {
        this.audioJack = audioJack;
    }

    public String getWaterproofRating() {
        return waterproofRating;
    }

    public void setWaterproofRating(String waterproofRating) {
        this.waterproofRating = waterproofRating;
    }

    public boolean isFaceRecognition() {
        return faceRecognition;
    }

    public void setFaceRecognition(boolean faceRecognition) {
        this.faceRecognition = faceRecognition;
    }

    public boolean isFingerprintSensor() {
        return fingerprintSensor;
    }

    public void setFingerprintSensor(boolean fingerprintSensor) {
        this.fingerprintSensor = fingerprintSensor;
    }
}