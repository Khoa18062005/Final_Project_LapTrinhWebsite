package viettech.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "tablets")
@PrimaryKeyJoinColumn(name = "product_id")
public class Tablet extends Product {

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

    @Column(name = "sim_support", nullable = false)
    private boolean simSupport;

    @Column(name = "network_support", nullable = false)
    private String networkSupport;

    @Column(nullable = false)
    private String connectivity;

    @Column(name = "stylus_support", nullable = false)
    private boolean stylusSupport;

    @Column(name = "stylus_included", nullable = false)
    private boolean stylusIncluded;

    @Column(name = "keyboard_support", nullable = false)
    private boolean keyboardSupport;

    private String speakers;

    @Column(name = "audio_jack", nullable = false)
    private boolean audioJack;

    @Column(name = "waterproof_rating")
    private String waterproofRating;

    @Column(name = "face_recognition", nullable = false)
    private boolean faceRecognition;

    @Column(name = "fingerprint_sensor", nullable = false)
    private boolean fingerprintSensor;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Tablet() {
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

    // Constructor dùng khi tạo Tablet mới
    public Tablet(int vendorId,
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
                  int quantity,

                  double screenSize,
                  String screenResolution,
                  String screenType,
                  int refreshRate,
                  int batteryCapacity,
                  String processor,
                  String gpu,
                  String rearCamera,
                  String frontCamera,
                  String videoRecording,
                  String os,
                  String osVersion,
                  boolean simSupport,
                  String networkSupport,
                  String connectivity,
                  boolean stylusSupport,
                  boolean stylusIncluded,
                  boolean keyboardSupport,
                  String speakers,
                  boolean audioJack,
                  String waterproofRating,
                  boolean faceRecognition,
                  boolean fingerprintSensor) {

        super(vendorId, categoryId, name, slug, basePrice,
                description, brand, specifications, status,
                condition, weight, dimensions, isFeatured, quantity);

        this.screenSize = screenSize;
        this.screenResolution = screenResolution != null ? screenResolution : "";
        this.screenType = screenType != null ? screenType : "";
        this.refreshRate = refreshRate;
        this.batteryCapacity = batteryCapacity;
        this.processor = processor != null ? processor : "";
        this.gpu = gpu != null ? gpu : "";
        this.rearCamera = rearCamera != null ? rearCamera : "";
        this.frontCamera = frontCamera != null ? frontCamera : "";
        this.videoRecording = videoRecording != null ? videoRecording : "";
        this.os = os != null ? os : "";
        this.osVersion = osVersion != null ? osVersion : "";
        this.simSupport = simSupport;
        this.networkSupport = networkSupport != null ? networkSupport : "";
        this.connectivity = connectivity != null ? connectivity : "";
        this.stylusSupport = stylusSupport;
        this.stylusIncluded = stylusIncluded;
        this.keyboardSupport = keyboardSupport;
        this.speakers = speakers != null ? speakers : "";
        this.audioJack = audioJack;
        this.waterproofRating = waterproofRating != null ? waterproofRating : "";
        this.faceRecognition = faceRecognition;
        this.fingerprintSensor = fingerprintSensor;
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
        this.networkSupport = networkSupport != null ? networkSupport : "";
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity != null ? connectivity : "";
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
        this.speakers = speakers != null ? speakers : "";
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
        this.waterproofRating = waterproofRating != null ? waterproofRating : "";
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
