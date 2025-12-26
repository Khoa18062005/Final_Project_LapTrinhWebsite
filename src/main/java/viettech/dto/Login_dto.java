package viettech.dto;

import java.io.Serializable;

/**
 * DTO dùng để hứng dữ liệu từ Form đăng nhập
 * Chỉ cần email và password
 */
public class Login_dto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;

    // 1. Constructor không đối số (bắt buộc cho Serializable và framework)
    public Login_dto() {
    }

    // 2. Constructor đầy đủ đối số (tiện lợi khi khởi tạo nhanh hoặc test)
    public Login_dto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 3. Getters và Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 4. Override toString để debug dễ dàng
    @Override
    public String toString() {
        return "Login_dto [email=" + email + ", password=" + (password != null ? "*****" : "null") + "]";
    }
}