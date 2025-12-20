package viettech.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * HikariCP Connection Pool Configuration
 * Quản lý HikariCP DataSource và connection pool
 * 
 * @author VietTech Team
 */
public class HikariCPConfig {
    
    private static HikariDataSource dataSource;
    
    /**
     * Khởi tạo HikariCP DataSource
     * 
     * @param dbConfig Database configuration
     * @return HikariDataSource instance
     */
    public static synchronized HikariDataSource createDataSource(DatabaseConfig dbConfig) {
        if (dataSource == null) {
            System.out.println("🔧 Initializing HikariCP Connection Pool...");
            
            // Tạo HikariConfig
            HikariConfig config = new HikariConfig();
            
            // Database connection
            config.setJdbcUrl(dbConfig.getJdbcUrl());
            config.setUsername(dbConfig.getUsername());
            config.setPassword(dbConfig.getPassword());
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            
            // Pool settings
            config.setMaximumPoolSize(10);           // Max connections trong pool
            config.setMinimumIdle(5);                // Min idle connections
            config.setConnectionTimeout(30000);      // 30 giây
            config.setIdleTimeout(600000);           // 10 phút (không dùng connection thì đóng)
            config.setMaxLifetime(1800000);          // 30 phút (max tuổi của 1 connection)
            
            // Connection test
            config.setConnectionTestQuery("SELECT 1");
            
            // Pool name (dễ debug trong logs)
            config.setPoolName("VietTech-HikariCP");
            
            // Leak detection (tìm connection leak trong dev)
            config.setLeakDetectionThreshold(60000); // 60 giây - cảnh báo nếu connection không được close
            
            // Auto commit (mặc định true)
            config.setAutoCommit(false); // Set false để tự quản lý transaction
            
            // Validation timeout
            config.setValidationTimeout(5000);       // 5 giây
            
            // Cache
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            
            System.out.println("✓ HikariCP configuration loaded:");
            System.out.println("  Pool Name: " + config.getPoolName());
            System.out.println("  Max Pool Size: " + config.getMaximumPoolSize());
            System.out.println("  Min Idle: " + config.getMinimumIdle());
            System.out.println("  Connection Timeout: " + config.getConnectionTimeout() + "ms");
            System.out.println("  Max Lifetime: " + config.getMaxLifetime() + "ms");
            
            // Tạo DataSource
            dataSource = new HikariDataSource(config);
            
            System.out.println("✅ HikariCP DataSource created successfully!");
        }
        
        return dataSource;
    }
    
    /**
     * Lấy DataSource hiện tại (nếu đã khởi tạo)
     */
    public static HikariDataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized. Call createDataSource() first.");
        }
        return dataSource;
    }
    
    /**
     * Đóng DataSource
     */
    public static synchronized void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            System.out.println("🛑 Closing HikariCP DataSource...");
            dataSource.close();
            System.out.println("✓ HikariCP DataSource closed");
            dataSource = null;
        }
    }
    
    /**
     * Kiểm tra DataSource có đang hoạt động không
     */
    public static boolean isInitialized() {
        return dataSource != null && !dataSource.isClosed();
    }
    
    /**
     * Lấy thông tin về connection pool (for monitoring)
     */
    public static String getPoolInfo() {
        if (dataSource == null || dataSource.isClosed()) {
            return "HikariCP Pool: Not initialized";
        }
        
        try {
            return String.format(
                "HikariCP Pool Status:\n" +
                "  Pool Name: %s\n" +
                "  Active Connections: %d\n" +
                "  Idle Connections: %d\n" +
                "  Total Connections: %d\n" +
                "  Threads Awaiting: %d\n" +
                "  Max Pool Size: %d",
                dataSource.getPoolName(),
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getTotalConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection(),
                dataSource.getMaximumPoolSize()
            );
        } catch (Exception e) {
            return "HikariCP Pool: Error getting pool info - " + e.getMessage();
        }
    }
    
    /**
     * Private constructor để prevent instantiation
     */
    private HikariCPConfig() {
        throw new AssertionError("Cannot instantiate HikariCPConfig - use static methods only");
    }
}