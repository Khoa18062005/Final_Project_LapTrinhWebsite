package viettech.entity.user;

import jakarta.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected Long userId;

    @Column(name = "first_name", nullable = false)
    protected String firstName;

    @Column(name = "last_name", nullable = false)
    protected String lastName;

    @Column(name = "username", nullable = false, unique = true)
    protected String username;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @Column(name = "phone", nullable = false)
    protected String phone;

    @Column(name = "avatar", nullable = false)
    protected String avatar;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    protected Date dateOfBirth;

    @Column(name = "gender", nullable = false)
    protected String gender;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    protected Date updatedAt;

    @Column(name = "is_active", nullable = false)
    protected boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_at")
    protected Date lastLoginAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định: KHÔNG NULL
    public User() {
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.password = "";
        this.email = "";
        this.phone = "";
        this.avatar = "";
        this.gender = "";

        this.dateOfBirth = null;      // DOB có thể null là hợp lý
        this.lastLoginAt = null;

        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.isActive = true;
    }

    // Constructor truyền đầy đủ
    public User(
            String firstName,
            String lastName,
            String username,
            String password,
            String email,
            String phone,
            String avatar,
            Date dateOfBirth,
            String gender,
            boolean isActive
    ) {
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.username = username != null ? username : "";
        this.password = password != null ? password : "";
        this.email = email != null ? email : "";
        this.phone = phone != null ? phone : "";
        this.avatar = avatar != null ? avatar : "";
        this.gender = gender != null ? gender : "";

        this.dateOfBirth = dateOfBirth;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.isActive = isActive;
        this.lastLoginAt = null;
    }
    //Constructor cho class con
    public User(String firstName,
                String lastName,
                String username,
                String password,
                String email,
                String phone,
                String avatar,
                String gender) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.gender = gender;

        this.dateOfBirth = null;
        this.isActive = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.lastLoginAt = null;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public Long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName : "";
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName : "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username : "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password != null ? password : "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone : "";
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar != null ? avatar : "";
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender != null ? gender : "";
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt != null ? updatedAt : new Date();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
