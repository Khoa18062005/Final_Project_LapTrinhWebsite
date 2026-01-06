package viettech.dto;

import java.io.Serializable;

/**
 * DTO để hứng dữ liệu từ Form đổi mật khẩu
 */
public class ChangePassword_dto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    // Constructor không tham số (bắt buộc)
    public ChangePassword_dto() {
    }

    // Constructor đầy đủ
    public ChangePassword_dto(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    // Getters & Setters
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Validation methods
    public boolean isValid() {
        return currentPassword != null && !currentPassword.trim().isEmpty() &&
               newPassword != null && !newPassword.trim().isEmpty() &&
               confirmPassword != null && !confirmPassword.trim().isEmpty();
    }

    public boolean passwordsMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }

    public boolean isNewPasswordStrong() {
        if (newPassword == null || newPassword.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : newPassword.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(c) >= 0) hasSpecial = true;
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    @Override
    public String toString() {
        return "ChangePasswordDTO [currentPasswordLength=" + 
               (currentPassword != null ? currentPassword.length() : 0) + 
               ", newPasswordLength=" + 
               (newPassword != null ? newPassword.length() : 0) + "]";
    }
}