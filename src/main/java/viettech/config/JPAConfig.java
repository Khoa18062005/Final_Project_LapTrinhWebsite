package viettech.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA Configuration
 * Quản lý EntityManagerFactory
 * 
 * @author VietTech Team
 */
public class JPAConfig {
    
    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "VietTech";
    
    /**
     * Lấy EntityManagerFactory singleton instance
     * Tự động khởi tạo nếu chưa có
     * 
     * @return EntityManagerFactory instance
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            initializeEntityManagerFactory();
        }
        return emf;
    }
    
    /**
     * Khởi tạo EntityManagerFactory với HikariCP DataSource
     */
    private static void initializeEntityManagerFactory() {
        System.out.println("\n🔧 Initializing JPA EntityManagerFactory...");
        
        try {
            // 1. Load database config
            DatabaseConfig dbConfig = DatabaseConfig.getInstance();
            dbConfig.logConfigInfo();
            
            // 2. Create HikariCP DataSource
            HikariCPConfig.createDataSource(dbConfig);
            // 3. Configure JPA properties
            Map<String, Object> properties = new HashMap<>();
            
            // Set DataSource (HikariCP)
            properties.put("javax.persistence.nonJtaDataSource", HikariCPConfig.getDataSource());
            
            // Hibernate properties
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            properties.put("hibernate.show_sql", "false");        // Set true for development
            properties.put("hibernate.format_sql", "true");
            properties.put("hibernate.use_sql_comments", "true");
            properties.put("hibernate.hbm2ddl.auto", "update");   // validate/update/create/create-drop
            
            // Performance settings
            properties.put("hibernate.jdbc.batch_size", "20");
            properties.put("hibernate.order_inserts", "true");
            properties.put("hibernate.order_updates", "true");
            properties.put("hibernate.jdbc.batch_versioned_data", "true");
            
            // Second-level cache (optional)
            // properties.put("hibernate.cache.use_second_level_cache", "true");
            // properties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
            
            System.out.println("✓ JPA properties configured");
            System.out.println("  Persistence Unit: " + PERSISTENCE_UNIT_NAME);
            System.out.println("  Hibernate Dialect: MySQL8Dialect");
            System.out.println("  DDL Auto: update");
            
            // 4. Create EntityManagerFactory
            System.out.println("🔄 Creating EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
            
            System.out.println("✅ EntityManagerFactory initialized successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize EntityManagerFactory");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize JPA", e);
        }
    }
    
    /**
     * Đóng EntityManagerFactory
     * Nên gọi khi application shutdown
     */
    public static synchronized void closeEntityManagerFactory() {
        System.out.println("🛑 Closing EntityManagerFactory...");
        
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✓ EntityManagerFactory closed");
            emf = null;
        }
        
        // Đóng HikariCP DataSource
        HikariCPConfig.closeDataSource();
    }
    
    /**
     * Kiểm tra EntityManagerFactory có đang mở không
     * 
     * @return true nếu EMF đang mở
     */
    public static boolean isOpen() {
        return emf != null && emf.isOpen();
    }
    
    /**
     * Lấy thông tin về JPA và Connection Pool
     */
    public static String getInfo() {
        StringBuilder info = new StringBuilder();
        
        info.append("=".repeat(60)).append("\n");
        info.append("JPA & Database Configuration Info\n");
        info.append("=".repeat(60)).append("\n");
        
        // JPA status
        info.append("JPA Status:\n");
        info.append("  EntityManagerFactory: ").append(isOpen() ? "OPEN ✓" : "CLOSED ✗").append("\n");
        info.append("  Persistence Unit: ").append(PERSISTENCE_UNIT_NAME).append("\n");
        info.append("\n");
        
        // Database config
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();
        info.append("Database Configuration:\n");
        info.append("  JDBC URL: ").append(dbConfig.getMaskedJdbcUrl()).append("\n");
        info.append("  Username: ").append(dbConfig.getUsername()).append("\n");
        info.append("\n");
        
        // HikariCP pool info
        if (HikariCPConfig.isInitialized()) {
            info.append(HikariCPConfig.getPoolInfo()).append("\n");
        } else {
            info.append("HikariCP Pool: Not initialized\n");
        }
        
        info.append("=".repeat(60));
        
        return info.toString();
    }
    
    /**
     * Private constructor để prevent instantiation
     */
    private JPAConfig() {
        throw new AssertionError("Cannot instantiate JPAConfig - use static methods only");
    }
}