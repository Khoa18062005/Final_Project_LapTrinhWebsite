package viettech.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database Configuration
 * Lưu trữ các thông tin cấu hình database từ environment variables
 *
 * @author VietTech Team
 */
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private final String jdbcUrl;
    private final String username;
    private final String password;

    // Singleton instance
    private static DatabaseConfig instance;

    /**
     * Private constructor - đọc config từ environment variables
     */
    private DatabaseConfig() {
        this.jdbcUrl = System.getenv("DB_URL");
        this.username = System.getenv("DB_USER");
        this.password = System.getenv("DB_PASSWORD");

        validateConfig();
    }

    /**
     * Lấy singleton instance
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * Validate config - throw exception nếu thiếu
     */
    private void validateConfig() {
        if (jdbcUrl == null || username == null || password == null) {
            String errorMsg = "Missing required DB environment variables:\n" +
                    "  - DB_URL: " + (jdbcUrl != null ? "✓" : "✗") + "\n" +
                    "  - DB_USER: " + (username != null ? "✓" : "✗") + "\n" +
                    "  - DB_PASSWORD: " + (password != null ? "✓" : "✗");

            logger.error("❌ {}", errorMsg);
            throw new RuntimeException("Missing DB environment variables. Please check your configuration.");
        }
    }

    // ============ GETTERS ============

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Lấy JDBC URL đã mask (ẩn sensitive params)
     */
    public String getMaskedJdbcUrl() {
        if (jdbcUrl == null) return "null";

        int paramIndex = jdbcUrl.indexOf('?');
        if (paramIndex > 0) {
            return jdbcUrl.substring(0, paramIndex) + "?[params_hidden]";
        }
        return jdbcUrl;
    }

    /**
     * Log config info (safe for logging)
     */
    public void logConfigInfo() {
        logger.info("✓ Database Configuration loaded:");
        logger.info("  JDBC URL: {}", getMaskedJdbcUrl());
        logger.info("  Username: {}", username);
        logger.debug("  Password: [hidden]");
    }
}