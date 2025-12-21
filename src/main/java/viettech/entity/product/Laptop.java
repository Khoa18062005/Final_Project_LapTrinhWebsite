package viettech.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "laptops")
@PrimaryKeyJoinColumn(name = "product_id")
public class Laptop extends Product {

    @Column(nullable = false)
    private String cpu;

    @Column(name = "cpu_generation", nullable = false)
    private String cpuGeneration;

    @Column(name = "cpu_speed", nullable = false)
    private double cpuSpeed;

    @Column(nullable = false)
    private String gpu;

    @Column(name = "gpu_memory", nullable = false)
    private int gpuMemory;

    @Column(nullable = false)
    private int ram;

    @Column(name = "ram_type", nullable = false)
    private String ramType;

    @Column(name = "max_ram", nullable = false)
    private int maxRam;

    @Column(nullable = false)
    private int storage;

    @Column(name = "storage_type", nullable = false)
    private String storageType;

    @Column(name = "additional_slot", nullable = false)
    private boolean additionalSlot;

    @Column(name = "screen_size", nullable = false)
    private double screenSize;

    @Column(name = "screen_resolution", nullable = false)
    private String screenResolution;

    @Column(name = "screen_type", nullable = false)
    private String screenType;

    @Column(name = "refresh_rate", nullable = false)
    private int refreshRate;

    @Column(name = "color_gamut", nullable = false)
    private String colorGamut;

    @Column(name = "battery_capacity", nullable = false)
    private int batteryCapacity;

    @Column(name = "battery_life", nullable = false)
    private int batteryLife;

    @Column(columnDefinition = "TEXT")
    private String ports;

    @Column(nullable = false)
    private String os;

    @Column(name = "keyboard_type", nullable = false)
    private String keyboardType;

    @Column(name = "keyboard_backlight", nullable = false)
    private boolean keyboardBacklight;

    private String webcam;
    private String speakers;
    private String microphone;

    @Column(name = "touch_screen", nullable = false)
    private boolean touchScreen;

    @Column(name = "fingerprint_sensor", nullable = false)
    private boolean fingerprintSensor;

    @Column(name = "discrete_gpu", nullable = false)
    private boolean discreteGpu;

    @Column(nullable = false)
    private boolean thunderbolt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Laptop() {
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

    // Constructor dùng khi tạo Laptop mới
    public Laptop(int vendorId,
                  int categoryId,
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

                  String cpu,
                  String cpuGeneration,
                  double cpuSpeed,
                  String gpu,
                  int gpuMemory,
                  int ram,
                  String ramType,
                  int maxRam,
                  int storage,
                  String storageType,
                  boolean additionalSlot,
                  double screenSize,
                  String screenResolution,
                  String screenType,
                  int refreshRate,
                  String colorGamut,
                  int batteryCapacity,
                  int batteryLife,
                  String ports,
                  String os,
                  String keyboardType,
                  boolean keyboardBacklight,
                  String webcam,
                  String speakers,
                  String microphone,
                  boolean touchScreen,
                  boolean fingerprintSensor,
                  boolean discreteGpu,
                  boolean thunderbolt) {

        super(vendorId, categoryId, name, slug, basePrice,
                description, brand, specifications, status,
                condition, weight, dimensions, isFeatured);

        this.cpu = cpu != null ? cpu : "";
        this.cpuGeneration = cpuGeneration != null ? cpuGeneration : "";
        this.cpuSpeed = cpuSpeed;
        this.gpu = gpu != null ? gpu : "";
        this.gpuMemory = gpuMemory;
        this.ram = ram;
        this.ramType = ramType != null ? ramType : "";
        this.maxRam = maxRam;
        this.storage = storage;
        this.storageType = storageType != null ? storageType : "";
        this.additionalSlot = additionalSlot;
        this.screenSize = screenSize;
        this.screenResolution = screenResolution != null ? screenResolution : "";
        this.screenType = screenType != null ? screenType : "";
        this.refreshRate = refreshRate;
        this.colorGamut = colorGamut != null ? colorGamut : "";
        this.batteryCapacity = batteryCapacity;
        this.batteryLife = batteryLife;
        this.ports = ports != null ? ports : "";
        this.os = os != null ? os : "";
        this.keyboardType = keyboardType != null ? keyboardType : "";
        this.keyboardBacklight = keyboardBacklight;
        this.webcam = webcam != null ? webcam : "";
        this.speakers = speakers != null ? speakers : "";
        this.microphone = microphone != null ? microphone : "";
        this.touchScreen = touchScreen;
        this.fingerprintSensor = fingerprintSensor;
        this.discreteGpu = discreteGpu;
        this.thunderbolt = thunderbolt;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu != null ? cpu : "";
    }

    public String getCpuGeneration() {
        return cpuGeneration;
    }

    public void setCpuGeneration(String cpuGeneration) {
        this.cpuGeneration = cpuGeneration != null ? cpuGeneration : "";
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
        this.gpu = gpu != null ? gpu : "";
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
        this.ramType = ramType != null ? ramType : "";
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
        this.storageType = storageType != null ? storageType : "";
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

    public String getColorGamut() {
        return colorGamut;
    }

    public void setColorGamut(String colorGamut) {
        this.colorGamut = colorGamut != null ? colorGamut : "";
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
        this.ports = ports != null ? ports : "";
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os != null ? os : "";
    }

    public String getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(String keyboardType) {
        this.keyboardType = keyboardType != null ? keyboardType : "";
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
        this.webcam = webcam != null ? webcam : "";
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers != null ? speakers : "";
    }

    public String getMicrophone() {
        return microphone;
    }

    public void setMicrophone(String microphone) {
        this.microphone = microphone != null ? microphone : "";
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
