package viettech.dto;

import java.io.Serializable;

/**
 * DTO for Profile Update
 * @author VietTech Team
 */
public class Profile_dto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private String day;
    private String month;
    private String year;
    private String emailOtp;  // OTP cho email mới

    // Constructor không đối số
    public Profile_dto() {
    }

    // Constructor đầy đủ
    public Profile_dto(String firstName, String lastName, String email, String phone,
                      String gender, String day, String month, String year) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // Getters & Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getEmailOtp() { return emailOtp; }
    public void setEmailOtp(String emailOtp) { this.emailOtp = emailOtp; }

    @Override
    public String toString() {
        return "ProfileDTO [firstName=" + firstName + ", lastName=" + lastName +
                ", email=" + email + ", phone=" + phone + "]";
    }
}