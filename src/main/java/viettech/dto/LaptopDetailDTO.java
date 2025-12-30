package viettech.dto;

import java.util.Date;

public class LaptopDetailDTO extends ProductDetailDTO {
    private String cpu;
    private String cpuGeneration;
    private double cpuSpeed;
    private String gpu;
    private int gpuMemory;
    private int ram;
    private String ramType;
    private int maxRam;
    private int storage;
    private String storageType;
    private boolean additionalSlot;
    private double screenSize;
    private String screenResolution;
    private String screenType;
    private int refreshRate;
    private String colorGamut;
    private int batteryCapacity;
    private int batteryLife;
    private String ports;
    private String os;
    private String keyboardType;
    private boolean keyboardBacklight;
    private String webcam;
    private String speakers;
    private String microphone;
    private boolean touchScreen;
    private boolean fingerprintSensor;
    private boolean discreteGpu;
    private boolean thunderbolt;
    private String imageUrl;

    // Constructor mặc định
    public LaptopDetailDTO() {
        super();
        this.cpu = "";
        this.cpuGeneration = "";
        this.cpuSpeed = 0.0;
        this.gpu = "";
        this.gpuMemory = 0;
        this.ram = 0;
        this.ramType = "";
        this.maxRam = 0;
        this.storage = 0;
        this.storageType = "";
        this.additionalSlot = false;
        this.screenSize = 0.0;
        this.screenResolution = "";
        this.screenType = "";
        this.refreshRate = 0;
        this.colorGamut = "";
        this.batteryCapacity = 0;
        this.batteryLife = 0;
        this.ports = "";
        this.os = "";
        this.keyboardType = "";
        this.keyboardBacklight = false;
        this.webcam = "";
        this.speakers = "";
        this.microphone = "";
        this.touchScreen = false;
        this.fingerprintSensor = false;
        this.discreteGpu = false;
        this.thunderbolt = false;
    }

    // Constructor all-arg
    public LaptopDetailDTO(int productId, int vendorId, int categoryId, String name, String slug,
                           double basePrice, String description, String brand, String specifications,
                           String status, String conditions, double weight, String dimensions,
                           Date createdAt, Date updatedAt, double averageRating, int totalReviews,
                           int totalSold, int viewCount, boolean isFeatured,
                           String cpu, String cpuGeneration, double cpuSpeed, String gpu, int gpuMemory,
                           int ram, String ramType, int maxRam, int storage, String storageType,
                           boolean additionalSlot, double screenSize, String screenResolution, String screenType,
                           int refreshRate, String colorGamut, int batteryCapacity, int batteryLife,
                           String ports, String os, String keyboardType, boolean keyboardBacklight,
                           String webcam, String speakers, String microphone, boolean touchScreen,
                           boolean fingerprintSensor, boolean discreteGpu, boolean thunderbolt, String imageUrl) {
        super(productId, vendorId, categoryId, name, slug, basePrice, description, brand, specifications,
                status, conditions, weight, dimensions, createdAt, updatedAt, averageRating, totalReviews,
                totalSold, viewCount, isFeatured, imageUrl);
        this.cpu = cpu;
        this.cpuGeneration = cpuGeneration;
        this.cpuSpeed = cpuSpeed;
        this.gpu = gpu;
        this.gpuMemory = gpuMemory;
        this.ram = ram;
        this.ramType = ramType;
        this.maxRam = maxRam;
        this.storage = storage;
        this.storageType = storageType;
        this.additionalSlot = additionalSlot;
        this.screenSize = screenSize;
        this.screenResolution = screenResolution;
        this.screenType = screenType;
        this.refreshRate = refreshRate;
        this.colorGamut = colorGamut;
        this.batteryCapacity = batteryCapacity;
        this.batteryLife = batteryLife;
        this.ports = ports;
        this.os = os;
        this.keyboardType = keyboardType;
        this.keyboardBacklight = keyboardBacklight;
        this.webcam = webcam;
        this.speakers = speakers;
        this.microphone = microphone;
        this.touchScreen = touchScreen;
        this.fingerprintSensor = fingerprintSensor;
        this.discreteGpu = discreteGpu;
        this.thunderbolt = thunderbolt;
    }

    // Getters và Setters
    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getCpuGeneration() {
        return cpuGeneration;
    }

    public void setCpuGeneration(String cpuGeneration) {
        this.cpuGeneration = cpuGeneration;
    }

    public double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public int getGpuMemory() {
        return gpuMemory;
    }

    public void setGpuMemory(int gpuMemory) {
        this.gpuMemory = gpuMemory;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }

    public int getMaxRam() {
        return maxRam;
    }

    public void setMaxRam(int maxRam) {
        this.maxRam = maxRam;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public boolean isAdditionalSlot() {
        return additionalSlot;
    }

    public void setAdditionalSlot(boolean additionalSlot) {
        this.additionalSlot = additionalSlot;
    }

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

    public String getColorGamut() {
        return colorGamut;
    }

    public void setColorGamut(String colorGamut) {
        this.colorGamut = colorGamut;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public int getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(int batteryLife) {
        this.batteryLife = batteryLife;
    }

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(String keyboardType) {
        this.keyboardType = keyboardType;
    }

    public boolean isKeyboardBacklight() {
        return keyboardBacklight;
    }

    public void setKeyboardBacklight(boolean keyboardBacklight) {
        this.keyboardBacklight = keyboardBacklight;
    }

    public String getWebcam() {
        return webcam;
    }

    public void setWebcam(String webcam) {
        this.webcam = webcam;
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }

    public String getMicrophone() {
        return microphone;
    }

    public void setMicrophone(String microphone) {
        this.microphone = microphone;
    }

    public boolean isTouchScreen() {
        return touchScreen;
    }

    public void setTouchScreen(boolean touchScreen) {
        this.touchScreen = touchScreen;
    }

    public boolean isFingerprintSensor() {
        return fingerprintSensor;
    }

    public void setFingerprintSensor(boolean fingerprintSensor) {
        this.fingerprintSensor = fingerprintSensor;
    }

    public boolean isDiscreteGpu() {
        return discreteGpu;
    }

    public void setDiscreteGpu(boolean discreteGpu) {
        this.discreteGpu = discreteGpu;
    }

    public boolean isThunderbolt() {
        return thunderbolt;
    }

    public void setThunderbolt(boolean thunderbolt) {
        this.thunderbolt = thunderbolt;
    }
}