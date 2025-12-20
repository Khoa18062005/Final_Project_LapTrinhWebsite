package viettech.listener;

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
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Context Initialization
     * Được gọi khi web application khởi động
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🚀 VietTech Application Starting...");
        System.out.println("   Time: " + timestamp);
        System.out.println("=".repeat(70) + "\n");
        
        try {
            // Warm-up JPA EntityManagerFactory & Database Connection Pool
            System.out.println("📦 Initializing Database & JPA...");
            JPAConfig.getEntityManagerFactory();
            
            // Show detailed info
            System.out.println("\n" + JPAConfig.getInfo() + "\n");
            
            // TODO: Initialize other resources
            // - Load application cache
            // - Initialize third-party services (Email, Payment...)
            // - Schedule background jobs
            // - Load configuration
            
            System.out.println("=".repeat(70));
            System.out.println("✅ VietTech Application Started Successfully!");
            System.out.println("   Ready to accept requests");
            System.out.println("   Access: http://localhost:8080/viettech");
            System.out.println("=".repeat(70) + "\n");
            
        } catch (Exception e) {
            System.err.println("\n" + "=".repeat(70));
            System.err.println("❌ Application Startup Failed!");
            System.err.println("=".repeat(70));
            e.printStackTrace();
            
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
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🛑 VietTech Application Shutting Down...");
        System.out.println("   Time: " + timestamp);
        System.out.println("=".repeat(70) + "\n");
        
        try {
            // Cleanup database connections & JPA
            System.out.println("📦 Closing Database & JPA...");
            JPAConfig.closeEntityManagerFactory();
            
            // TODO: Cleanup other resources
            // - Clear caches
            // - Close third-party connections
            // - Stop background jobs
            // - Save application state
            
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✅ VietTech Application Stopped Successfully!");
            System.out.println("   All resources cleaned up");
            System.out.println("=".repeat(70) + "\n");
            
        } catch (Exception e) {
            System.err.println("\n" + "=".repeat(70));
            System.err.println("⚠️ Error during application shutdown");
            System.err.println("=".repeat(70));
            e.printStackTrace();
        }
    }
}