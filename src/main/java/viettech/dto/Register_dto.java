package viettech.dto;

import java.io.Serializable;

/**
 * DTO dùng để hứng dữ liệu từ Form đăng ký
 * Tên class nên tuân thủ PascalCase: RegisterDTO
 */
public class Register_dto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String dateOfBirth; // Để String để dễ dàng validate từ form trước khi convert
    private String gender;

    // 1. Constructor không đối số (Bắt buộc cho Serializable/Framework)
    public Register_dto() {
    }

    // 2. Constructor đầy đủ đối số (Tiện lợi khi khởi tạo nhanh)
    public Register_dto(String firstName, String lastName, String email, String password,
                        String phone, String dateOfBirth, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    // 3. Getters và Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // 4. (Tùy chọn) Override toString để debug dễ dàng hơn
    @Override
    public String toString() {
        return "RegisterDTO [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
    }
}