package viettech.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "vendors")
@PrimaryKeyJoinColumn(name = "user_id")
public class Vendor extends User {

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "tax_id", nullable = false, unique = true)
    private String taxId;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "commission", nullable = false)
    private double commission;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Column(name = "bank_account", nullable = false)
    private String bankAccount;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Vendor() {
        super(); // User constructor

        this.businessName = "";
        this.taxId = "";
        this.rating = 0.0;
        this.commission = 0.0;
        this.isVerified = false;
        this.bankAccount = "";
    }

    // Constructor dùng khi đăng ký Vendor
    public Vendor(String firstName,
                  String lastName,
                  String username,
                  String password,
                  String email,
                  String phone,
                  String avatar,
                  String gender,
                  String businessName,
                  String taxId,
                  String bankAccount) {

        super(firstName, lastName, username, password, email, phone, avatar, gender);

        this.businessName = businessName != null ? businessName : "";
        this.taxId = taxId != null ? taxId : "";
        this.bankAccount = bankAccount != null ? bankAccount : "";

        this.rating = 0.0;        // chưa có đánh giá
        this.commission = 0.0;    // admin set sau
        this.isVerified = false;  // chờ admin duyệt
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName != null ? businessName : "";
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId != null ? taxId : "";
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount != null ? bankAccount : "";
    }
}