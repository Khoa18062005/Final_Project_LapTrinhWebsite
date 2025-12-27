package viettech.dto;

import java.util.Date;

public class HeadphoneDetailDTO extends ProductDetailDTO {
    private String type;
    private String formFactor;
    private int batteryLife;
    private int chargingTime;
    private String chargingPort;
    private boolean noiseCancellation;
    private String noiseCancellationType;
    private boolean ambientMode;
    private int driverSize;
    private String driverType;
    private String frequencyResponse;
    private int impedance;
    private int sensitivity;
    private boolean microphone;
    private String micType;
    private String waterproofRating;
    private String connectivity;
    private String bluetoothVersion;
    private String bluetoothCodecs;
    private String wiredConnection;
    private boolean multipoint;
    private String soundProfile;
    private boolean appControl;
    private boolean customEQ;
    private boolean surroundSound;
    private boolean foldable;

    // Constructor mặc định
    public HeadphoneDetailDTO() {
        super();
        this.type = "";
        this.formFactor = "";
        this.batteryLife = 0;
        this.chargingTime = 0;
        this.chargingPort = "";
        this.noiseCancellation = false;
        this.noiseCancellationType = "";
        this.ambientMode = false;
        this.driverSize = 0;
        this.driverType = "";
        this.frequencyResponse = "";
        this.impedance = 0;
        this.sensitivity = 0;
        this.microphone = false;
        this.micType = "";
        this.waterproofRating = "";
        this.connectivity = "";
        this.bluetoothVersion = "";
        this.bluetoothCodecs = "";
        this.wiredConnection = "";
        this.multipoint = false;
        this.soundProfile = "";
        this.appControl = false;
        this.customEQ = false;
        this.surroundSound = false;
        this.foldable = false;
    }

    // Constructor all-arg
    public HeadphoneDetailDTO(int productId, int vendorId, int categoryId, String name, String slug,
                              double basePrice, String description, String brand, String specifications,
                              String status, String conditions, double weight, String dimensions,
                              Date createdAt, Date updatedAt, double averageRating, int totalReviews,
                              int totalSold, int viewCount, boolean isFeatured,
                              String type, String formFactor, int batteryLife, int chargingTime,
                              String chargingPort, boolean noiseCancellation, String noiseCancellationType,
                              boolean ambientMode, int driverSize, String driverType, String frequencyResponse,
                              int impedance, int sensitivity, boolean microphone, String micType,
                              String waterproofRating, String connectivity, String bluetoothVersion,
                              String bluetoothCodecs, String wiredConnection, boolean multipoint,
                              String soundProfile, boolean appControl, boolean customEQ,
                              boolean surroundSound, boolean foldable) {
        super(productId, vendorId, categoryId, name, slug, basePrice, description, brand, specifications,
                status, conditions, weight, dimensions, createdAt, updatedAt, averageRating, totalReviews,
                totalSold, viewCount, isFeatured);
        this.type = type;
        this.formFactor = formFactor;
        this.batteryLife = batteryLife;
        this.chargingTime = chargingTime;
        this.chargingPort = chargingPort;
        this.noiseCancellation = noiseCancellation;
        this.noiseCancellationType = noiseCancellationType;
        this.ambientMode = ambientMode;
        this.driverSize = driverSize;
        this.driverType = driverType;
        this.frequencyResponse = frequencyResponse;
        this.impedance = impedance;
        this.sensitivity = sensitivity;
        this.microphone = microphone;
        this.micType = micType;
        this.waterproofRating = waterproofRating;
        this.connectivity = connectivity;
        this.bluetoothVersion = bluetoothVersion;
        this.bluetoothCodecs = bluetoothCodecs;
        this.wiredConnection = wiredConnection;
        this.multipoint = multipoint;
        this.soundProfile = soundProfile;
        this.appControl = appControl;
        this.customEQ = customEQ;
        this.surroundSound = surroundSound;
        this.foldable = foldable;
    }

    // Getters và Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public int getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(int batteryLife) {
        this.batteryLife = batteryLife;
    }

    public int getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(int chargingTime) {
        this.chargingTime = chargingTime;
    }

    public String getChargingPort() {
        return chargingPort;
    }

    public void setChargingPort(String chargingPort) {
        this.chargingPort = chargingPort;
    }

    public boolean isNoiseCancellation() {
        return noiseCancellation;
    }

    public void setNoiseCancellation(boolean noiseCancellation) {
        this.noiseCancellation = noiseCancellation;
    }

    public String getNoiseCancellationType() {
        return noiseCancellationType;
    }

    public void setNoiseCancellationType(String noiseCancellationType) {
        this.noiseCancellationType = noiseCancellationType;
    }

    public boolean isAmbientMode() {
        return ambientMode;
    }

    public void setAmbientMode(boolean ambientMode) {
        this.ambientMode = ambientMode;
    }

    public int getDriverSize() {
        return driverSize;
    }

    public void setDriverSize(int driverSize) {
        this.driverSize = driverSize;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getFrequencyResponse() {
        return frequencyResponse;
    }

    public void setFrequencyResponse(String frequencyResponse) {
        this.frequencyResponse = frequencyResponse;
    }

    public int getImpedance() {
        return impedance;
    }

    public void setImpedance(int impedance) {
        this.impedance = impedance;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public boolean isMicrophone() {
        return microphone;
    }

    public void setMicrophone(boolean microphone) {
        this.microphone = microphone;
    }

    public String getMicType() {
        return micType;
    }

    public void setMicType(String micType) {
        this.micType = micType;
    }

    public String getWaterproofRating() {
        return waterproofRating;
    }

    public void setWaterproofRating(String waterproofRating) {
        this.waterproofRating = waterproofRating;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public String getBluetoothVersion() {
        return bluetoothVersion;
    }

    public void setBluetoothVersion(String bluetoothVersion) {
        this.bluetoothVersion = bluetoothVersion;
    }

    public String getBluetoothCodecs() {
        return bluetoothCodecs;
    }

    public void setBluetoothCodecs(String bluetoothCodecs) {
        this.bluetoothCodecs = bluetoothCodecs;
    }

    public String getWiredConnection() {
        return wiredConnection;
    }

    public void setWiredConnection(String wiredConnection) {
        this.wiredConnection = wiredConnection;
    }

    public boolean isMultipoint() {
        return multipoint;
    }

    public void setMultipoint(boolean multipoint) {
        this.multipoint = multipoint;
    }

    public String getSoundProfile() {
        return soundProfile;
    }

    public void setSoundProfile(String soundProfile) {
        this.soundProfile = soundProfile;
    }

    public boolean isAppControl() {
        return appControl;
    }

    public void setAppControl(boolean appControl) {
        this.appControl = appControl;
    }

    public boolean isCustomEQ() {
        return customEQ;
    }

    public void setCustomEQ(boolean customEQ) {
        this.customEQ = customEQ;
    }

    public boolean isSurroundSound() {
        return surroundSound;
    }

    public void setSurroundSound(boolean surroundSound) {
        this.surroundSound = surroundSound;
    }

    public boolean isFoldable() {
        return foldable;
    }

    public void setFoldable(boolean foldable) {
        this.foldable = foldable;
    }
}