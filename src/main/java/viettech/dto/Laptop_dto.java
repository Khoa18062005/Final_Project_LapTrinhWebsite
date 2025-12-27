package viettech.dto;

/**
 * DTO for Laptop extending Product_dto
 * Chuẩn bị dữ liệu laptop để thêm vào LaptopDAO
 */
public class Laptop_dto extends Product_dto {
    private static final long serialVersionUID = 1L;

    // ===== Laptop Specific Properties =====
    private String cpu;
    private String cpuGeneration;
    private String cpuSpeed;
    private String gpu;
    private String gpuMemory;
    private String ram;
    private String ramType;
    private String maxRam;
    private String storage;
    private String storageType;
    private String additionalSlot;
    private String screenSize;
    private String screenResolution;
    private String screenType;
    private String refreshRate;
    private String colorGamut;
    private String batteryCapacity;
    private String batteryLife;
    private String ports;
    private String os;
    private String keyboardType;
    private String keyboardBacklight;
    private String webcam;
    private String speakers;
    private String microphone;
    private String touchScreen;
    private String fingerprintSensor;
    private String discreteGpu;
    private String thunderbolt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    public Laptop_dto() {
        super();
    }

    public Laptop_dto(String vendorId, String categoryId, String name, String slug,
                      String basePrice, String description, String brand, String specifications,
                      String status, String conditions, String weight, String dimensions,
                      String isFeatured, String cpu, String cpuGeneration, String cpuSpeed,
                      String gpu, String gpuMemory, String ram, String ramType, String maxRam,
                      String storage, String storageType, String additionalSlot, String screenSize,
                      String screenResolution, String screenType, String refreshRate,
                      String colorGamut, String batteryCapacity, String batteryLife,
                      String ports, String os, String keyboardType, String keyboardBacklight,
                      String webcam, String speakers, String microphone, String touchScreen,
                      String fingerprintSensor, String discreteGpu, String thunderbolt) {
        super(vendorId, categoryId, name, slug, basePrice, description, brand,
                specifications, status, conditions, weight, dimensions, isFeatured);
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

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getCpu() { return cpu; }
    public void setCpu(String cpu) { this.cpu = cpu; }

    public String getCpuGeneration() { return cpuGeneration; }
    public void setCpuGeneration(String cpuGeneration) { this.cpuGeneration = cpuGeneration; }

    public String getCpuSpeed() { return cpuSpeed; }
    public void setCpuSpeed(String cpuSpeed) { this.cpuSpeed = cpuSpeed; }

    public String getGpu() { return gpu; }
    public void setGpu(String gpu) { this.gpu = gpu; }

    public String getGpuMemory() { return gpuMemory; }
    public void setGpuMemory(String gpuMemory) { this.gpuMemory = gpuMemory; }

    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }

    public String getRamType() { return ramType; }
    public void setRamType(String ramType) { this.ramType = ramType; }

    public String getMaxRam() { return maxRam; }
    public void setMaxRam(String maxRam) { this.maxRam = maxRam; }

    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }

    public String getStorageType() { return storageType; }
    public void setStorageType(String storageType) { this.storageType = storageType; }

    public String getAdditionalSlot() { return additionalSlot; }
    public void setAdditionalSlot(String additionalSlot) { this.additionalSlot = additionalSlot; }

    public String getScreenSize() { return screenSize; }
    public void setScreenSize(String screenSize) { this.screenSize = screenSize; }

    public String getScreenResolution() { return screenResolution; }
    public void setScreenResolution(String screenResolution) { this.screenResolution = screenResolution; }

    public String getScreenType() { return screenType; }
    public void setScreenType(String screenType) { this.screenType = screenType; }

    public String getRefreshRate() { return refreshRate; }
    public void setRefreshRate(String refreshRate) { this.refreshRate = refreshRate; }

    public String getColorGamut() { return colorGamut; }
    public void setColorGamut(String colorGamut) { this.colorGamut = colorGamut; }

    public String getBatteryCapacity() { return batteryCapacity; }
    public void setBatteryCapacity(String batteryCapacity) { this.batteryCapacity = batteryCapacity; }

    public String getBatteryLife() { return batteryLife; }
    public void setBatteryLife(String batteryLife) { this.batteryLife = batteryLife; }

    public String getPorts() { return ports; }
    public void setPorts(String ports) { this.ports = ports; }

    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }

    public String getKeyboardType() { return keyboardType; }
    public void setKeyboardType(String keyboardType) { this.keyboardType = keyboardType; }

    public String getKeyboardBacklight() { return keyboardBacklight; }
    public void setKeyboardBacklight(String keyboardBacklight) { this.keyboardBacklight = keyboardBacklight; }

    public String getWebcam() { return webcam; }
    public void setWebcam(String webcam) { this.webcam = webcam; }

    public String getSpeakers() { return speakers; }
    public void setSpeakers(String speakers) { this.speakers = speakers; }

    public String getMicrophone() { return microphone; }
    public void setMicrophone(String microphone) { this.microphone = microphone; }

    public String getTouchScreen() { return touchScreen; }
    public void setTouchScreen(String touchScreen) { this.touchScreen = touchScreen; }

    public String getFingerprintSensor() { return fingerprintSensor; }
    public void setFingerprintSensor(String fingerprintSensor) { this.fingerprintSensor = fingerprintSensor; }

    public String getDiscreteGpu() { return discreteGpu; }
    public void setDiscreteGpu(String discreteGpu) { this.discreteGpu = discreteGpu; }

    public String getThunderbolt() { return thunderbolt; }
    public void setThunderbolt(String thunderbolt) { this.thunderbolt = thunderbolt; }

    /* =========================
       CONVERSION METHODS FOR DAO
       ========================= */

    public int getVendorIdAsInt() {
        return parseIntOrDefault(vendorId, 0);
    }

    public int getCategoryIdAsInt() {
        return parseIntOrDefault(categoryId, 0);
    }

    public double getBasePriceAsDouble() {
        return parseDoubleOrDefault(basePrice, 0.0);
    }

    public double getWeightAsDouble() {
        return parseDoubleOrDefault(weight, 0.0);
    }

    public boolean getIsFeaturedAsBoolean() {
        return parseBoolean(isFeatured);
    }

    public double getCpuSpeedAsDouble() {
        return parseDoubleOrDefault(cpuSpeed, 0.0);
    }

    public int getGpuMemoryAsInt() {
        return parseIntOrDefault(gpuMemory, 0);
    }

    public int getRamAsInt() {
        return parseIntOrDefault(ram, 0);
    }

    public int getMaxRamAsInt() {
        return parseIntOrDefault(maxRam, 0);
    }

    public int getStorageAsInt() {
        return parseIntOrDefault(storage, 0);
    }

    public boolean getAdditionalSlotAsBoolean() {
        return parseBoolean(additionalSlot);
    }

    public double getScreenSizeAsDouble() {
        return parseDoubleOrDefault(screenSize, 0.0);
    }

    public int getRefreshRateAsInt() {
        return parseIntOrDefault(refreshRate, 60);
    }

    public int getBatteryCapacityAsInt() {
        return parseIntOrDefault(batteryCapacity, 0);
    }

    public int getBatteryLifeAsInt() {
        return parseIntOrDefault(batteryLife, 0);
    }

    public boolean getKeyboardBacklightAsBoolean() {
        return parseBoolean(keyboardBacklight);
    }

    public boolean getTouchScreenAsBoolean() {
        return parseBoolean(touchScreen);
    }

    public boolean getFingerprintSensorAsBoolean() {
        return parseBoolean(fingerprintSensor);
    }

    public boolean getDiscreteGpuAsBoolean() {
        return parseBoolean(discreteGpu);
    }

    public boolean getThunderboltAsBoolean() {
        return parseBoolean(thunderbolt);
    }

    /**
     * Validate all required fields for Laptop
     */
    public boolean isValid() {
        return isValidString(name) &&
               isValidString(slug) &&
               isValidString(basePrice) &&
               isValidString(cpu) &&
               isValidString(ram) &&
               isValidString(storage);
    }

    @Override
    public String toString() {
        return "Laptop_dto{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", basePrice='" + basePrice + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", storage='" + storage + '\'' +
                '}';
    }
}

