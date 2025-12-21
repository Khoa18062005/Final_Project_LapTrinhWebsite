package viettech.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Application Lifecycle Listener
 * Khởi tạo và cleanup resources khi web app start/stop
 *
 * @author VietTech Team
 */
@WebListener
public class StartupListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Context Initialization
     * Được gọi khi web application khởi động
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String timestamp = LocalDateTime.now().format(formatter);

        logger.info("======================================================================");
        logger.info("🚀 VietTech Application Starting...");
        logger.info("   Time: {}", timestamp);
        logger.info("======================================================================");

        try {
            // Warm-up JPA EntityManagerFactory & Database Connection Pool
            logger.info("📦 Initializing Database & JPA...");
            JPAConfig.getEntityManagerFactory();

            // Show detailed info
            String jpaInfo = JPAConfig.getInfo();
            logger.info("\n{}", jpaInfo);

            // TODO: Initialize other resources
            logger.debug("TODO: Initialize application cache");
            logger.debug("TODO: Initialize third-party services (Email, Payment...)");
            logger.debug("TODO: Schedule background jobs");
            logger.debug("TODO: Load configuration");

            logger.info("======================================================================");
            logger.info("✅ VietTech Application Started Successfully!");
            logger.info("   Ready to accept requests");
            logger.info("   Access: http://localhost:8080/viettech");
            logger.info("======================================================================");

        } catch (Exception e) {
            logger.error("======================================================================");
            logger.error("❌ Application Startup Failed!");
            logger.error("======================================================================");
            logger.error("Error details:", e);

            // Throw exception để prevent app khởi động nếu database fail
            throw new RuntimeException("Failed to initialize VietTech application", e);
        }
    }

    /**
     * Context Destruction
     * Được gọi khi web application shutdown
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        String timestamp = LocalDateTime.now().format(formatter);

        logger.info("======================================================================");
        logger.info("🛑 VietTech Application Shutting Down...");
        logger.info("   Time: {}", timestamp);
        logger.info("======================================================================");

        try {
            // Cleanup database connections & JPA
            logger.info("📦 Closing Database & JPA...");
            JPAConfig.closeEntityManagerFactory();

            // TODO: Cleanup other resources
            logger.debug("TODO: Clear caches");
            logger.debug("TODO: Close third-party connections");
            logger.debug("TODO: Stop background jobs");
            logger.debug("TODO: Save application state");

            logger.info("======================================================================");
            logger.info("✅ VietTech Application Stopped Successfully!");
            logger.info("   All resources cleaned up");
            logger.info("======================================================================");

        } catch (Exception e) {
            logger.warn("======================================================================");
            logger.warn("⚠️ Error during application shutdown");
            logger.warn("======================================================================");
            logger.error("Shutdown error details:", e);
        }
    }
}