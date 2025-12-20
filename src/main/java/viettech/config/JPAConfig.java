package viettech.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAConfig {

    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT_NAME = "VietTech"; // ✅ Khớp với persistence.xml

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            initializeEntityManagerFactory();
        }
        return emf;
    }
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

            // ✅ Set DataSource (HikariCP) - Override persistence.xml
            properties.put("javax.persistence.nonJtaDataSource", HikariCPConfig.getDataSource());

            // ✅ Hibernate Dialect
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

            // ✅ SQL Logging (Development)
            properties.put("hibernate.show_sql", "false");  // Set true để xem SQL
            properties.put("hibernate.format_sql", "true");
            properties.put("hibernate.use_sql_comments", "true");

            // ✅ Schema Management (QUAN TRỌNG!)
            properties.put("hibernate.hbm2ddl.auto", "update"); // validate/update/create/none

            // ✅ Performance Settings
            properties.put("hibernate.jdbc.batch_size", "20");
            properties.put("hibernate.order_inserts", "true");
            properties.put("hibernate.order_updates", "true");
            properties.put("hibernate.jdbc.batch_versioned_data", "true");

            // ✅ Connection Pool Validation
            properties.put("hibernate.connection.provider_disables_autocommit", "true");

            // ✅ Naming Strategy (tùy chọn)
            // Convert camelCase → snake_case
            properties.put("hibernate.physical_naming_strategy",
                    "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

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

    public static synchronized void closeEntityManagerFactory() {
        System.out.println("🛑 Closing EntityManagerFactory...");

        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("✓ EntityManagerFactory closed");
            emf = null;
        }

        HikariCPConfig.closeDataSource();
    }

    public static boolean isOpen() {
        return emf != null && emf.isOpen();
    }

    public static String getInfo() {
        StringBuilder info = new StringBuilder();

        info.append("=".repeat(60)).append("\n");
        info.append("JPA & Database Configuration Info\n");
        info.append("=".repeat(60)).append("\n");

        info.append("JPA Status:\n");
        info.append("  EntityManagerFactory: ").append(isOpen() ? "OPEN ✓" : "CLOSED ✗").append("\n");
        info.append("  Persistence Unit: ").append(PERSISTENCE_UNIT_NAME).append("\n");
        info.append("\n");

        DatabaseConfig dbConfig = DatabaseConfig.getInstance();
        info.append("Database Configuration:\n");
        info.append("  JDBC URL: ").append(dbConfig.getMaskedJdbcUrl()).append("\n");
        info.append("  Username: ").append(dbConfig.getUsername()).append("\n");
        info.append("\n");

        if (HikariCPConfig.isInitialized()) {
            info.append(HikariCPConfig.getPoolInfo()).append("\n");
        } else {
            info.append("HikariCP Pool: Not initialized\n");
        }

        info.append("=".repeat(60));

        return info.toString();
    }

    private JPAConfig() {
        throw new AssertionError("Cannot instantiate JPAConfig - use static methods only");
    }
}