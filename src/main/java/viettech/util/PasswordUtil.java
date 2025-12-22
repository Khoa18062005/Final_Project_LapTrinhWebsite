package viettech.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class để xử lý mật khẩu an toàn bằng BCrypt (jBCrypt)
 * 
 * - Pure static methods
 * - Không có dependency (chỉ dùng thư viện jBCrypt đã có trong project)
 * - Không thể instantiate
 */
public class PasswordUtil {

    /**
     * Hash mật khẩu bằng BCrypt
     * 
     * @param plainPassword mật khẩu dạng plain text
     * @return chuỗi hash BCrypt (đã bao gồm salt và version)
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        // gensalt(12) là mức strength phổ biến: cân bằng giữa bảo mật và performance
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Kiểm tra mật khẩu người dùng nhập có khớp với hash lưu trong DB không
     * 
     * @param plainPassword mật khẩu người dùng nhập (plain text)
     * @param hashedPassword mật khẩu đã hash lưu trong database
     * @return true nếu khớp, false nếu không khớp hoặc có lỗi
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            // IllegalArgumentException có thể xảy ra nếu hashedPassword format sai
            return false;
        }
    }

    /**
     * Private constructor để ngăn instantiate class utility
     */
    private PasswordUtil() {
        throw new AssertionError("Cannot instantiate utility class PasswordUtil");
    }
}