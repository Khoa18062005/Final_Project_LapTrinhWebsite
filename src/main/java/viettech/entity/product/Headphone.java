package viettech.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "headphones")
@PrimaryKeyJoinColumn(name = "product_id")
public class Headphone extends Product {

    @Column(nullable = false)
    private String type;              // in-ear, on-ear, over-ear

    @Column(name = "form_factor", nullable = false)
    private String formFactor;        // true wireless, neckband, wired

    @Column(name = "battery_life")
    private int batteryLife;          // hours

    @Column(name = "charging_time")
    private int chargingTime;         // minutes

    @Column(name = "charging_port")
    private String chargingPort;      // USB-C, Lightning

    @Column(name = "noise_cancellation", nullable = false)
    private boolean noiseCancellation;

    @Column(name = "noise_cancellation_type")
    private String noiseCancellationType; // ANC, ENC, Hybrid

    @Column(name = "ambient_mode", nullable = false)
    private boolean ambientMode;

    @Column(name = "driver_size")
    private int driverSize;           // mm

    @Column(name = "driver_type")
    private String driverType;        // dynamic, planar, balanced armature

    @Column(name = "frequency_response")
    private String frequencyResponse;

    private int impedance;            // ohm
    private int sensitivity;          // dB

    @Column(nullable = false)
    private boolean microphone;

    @Column(name = "mic_type")
    private String micType;

    @Column(name = "waterproof_rating")
    private String waterproofRating;

    @Column(nullable = false)
    private String connectivity;      // wired, bluetooth

    @Column(name = "bluetooth_version")
    private String bluetoothVersion;

    @Column(name = "bluetooth_codecs")
    private String bluetoothCodecs;   // SBC, AAC, aptX, LDAC

    @Column(name = "wired_connection")
    private String wiredConnection;   // 3.5mm, USB-C

    @Column(nullable = false)
    private boolean multipoint;

    @Column(name = "sound_profile")
    private String soundProfile;      // bass boost, balanced

    @Column(name = "app_control", nullable = false)
    private boolean appControl;

    @Column(name = "custom_eq", nullable = false)
    private boolean customEQ;

    @Column(name = "surround_sound", nullable = false)
    private boolean surroundSound;

    @Column(nullable = false)
    private boolean foldable;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Headphone() {
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

    // Constructor dùng khi tạo Headphone mới
    public Headphone(int vendorId,
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

                     String type,
                     String formFactor,
                     int batteryLife,
                     int chargingTime,
                     String chargingPort,
                     boolean noiseCancellation,
                     String noiseCancellationType,
                     boolean ambientMode,
                     int driverSize,
                     String driverType,
                     String frequencyResponse,
                     int impedance,
                     int sensitivity,
                     boolean microphone,
                     String micType,
                     String waterproofRating,
                     String connectivity,
                     String bluetoothVersion,
                     String bluetoothCodecs,
                     String wiredConnection,
                     boolean multipoint,
                     String soundProfile,
                     boolean appControl,
                     boolean customEQ,
                     boolean surroundSound,
                     boolean foldable) {

        super(vendorId, categoryId, name, slug, basePrice,
              description, brand, specifications, status,
              condition, weight, dimensions, isFeatured);

        this.type = type != null ? type : "";
        this.formFactor = formFactor != null ? formFactor : "";
        this.batteryLife = batteryLife;
        this.chargingTime = chargingTime;
        this.chargingPort = chargingPort != null ? chargingPort : "";
        this.noiseCancellation = noiseCancellation;
        this.noiseCancellationType = noiseCancellationType != null ? noiseCancellationType : "";
        this.ambientMode = ambientMode;
        this.driverSize = driverSize;
        this.driverType = driverType != null ? driverType : "";
        this.frequencyResponse = frequencyResponse != null ? frequencyResponse : "";
        this.impedance = impedance;
        this.sensitivity = sensitivity;
        this.microphone = microphone;
        this.micType = micType != null ? micType : "";
        this.waterproofRating = waterproofRating != null ? waterproofRating : "";
        this.connectivity = connectivity != null ? connectivity : "";
        this.bluetoothVersion = bluetoothVersion != null ? bluetoothVersion : "";
        this.bluetoothCodecs = bluetoothCodecs != null ? bluetoothCodecs : "";
        this.wiredConnection = wiredConnection != null ? wiredConnection : "";
        this.multipoint = multipoint;
        this.soundProfile = soundProfile != null ? soundProfile : "";
        this.appControl = appControl;
        this.customEQ = customEQ;
        this.surroundSound = surroundSound;
        this.foldable = foldable;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor != null ? formFactor : "";
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
        this.chargingPort = chargingPort != null ? chargingPort : "";
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
        this.noiseCancellationType = noiseCancellationType != null ? noiseCancellationType : "";
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
        this.driverType = driverType != null ? driverType : "";
    }

    public String getFrequencyResponse() {
        return frequencyResponse;
    }

    public void setFrequencyResponse(String frequencyResponse) {
        this.frequencyResponse = frequencyResponse != null ? frequencyResponse : "";
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
        this.micType = micType != null ? micType : "";
    }

    public String getWaterproofRating() {
        return waterproofRating;
    }

    public void setWaterproofRating(String waterproofRating) {
        this.waterproofRating = waterproofRating != null ? waterproofRating : "";
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity != null ? connectivity : "";
    }

    public String getBluetoothVersion() {
        return bluetoothVersion;
    }

    public void setBluetoothVersion(String bluetoothVersion) {
        this.bluetoothVersion = bluetoothVersion != null ? bluetoothVersion : "";
    }

    public String getBluetoothCodecs() {
        return bluetoothCodecs;
    }

    public void setBluetoothCodecs(String bluetoothCodecs) {
        this.bluetoothCodecs = bluetoothCodecs != null ? bluetoothCodecs : "";
    }

    public String getWiredConnection() {
        return wiredConnection;
    }

    public void setWiredConnection(String wiredConnection) {
        this.wiredConnection = wiredConnection != null ? wiredConnection : "";
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
        this.soundProfile = soundProfile != null ? soundProfile : "";
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
