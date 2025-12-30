package viettech.controller;

import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet xử lý trang VTX (VietTech Xu - Loyalty Points)
 * URL: /profile/vtx
 */
@WebServlet("/profile/vtx")
public class VTXServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("🪙 ===== VTX PAGE DEBUG =====");

        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            System.out.println("❌ User not logged in");
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check if user is Customer
        if (!(user instanceof Customer)) {
            System.out.println("❌ User is not a Customer");
            SessionUtil.setErrorMessage(request, "Chỉ khách hàng mới có thể xem VTX!");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Customer customer = (Customer) user;
        
        // Get loyalty points and membership info
        int loyaltyPoints = customer.getLoyaltyPoints();
        String membershipTier = customer.getMembershipTier();
        double totalSpent = customer.getTotalSpent();

        System.out.println("👤 Customer: " + customer.getEmail());
        System.out.println("🪙 Loyalty Points: " + loyaltyPoints);
        System.out.println("🏆 Membership Tier: " + membershipTier);
        System.out.println("💰 Total Spent: " + totalSpent);

        // Calculate tier progress (Bronze -> Silver -> Gold -> Platinum)
        int nextTierThreshold = calculateNextTierThreshold(membershipTier);
        int tierProgress = calculateTierProgress(totalSpent, membershipTier);

        System.out.println("🎯 Next Tier Threshold: " + nextTierThreshold);
        System.out.println("📊 Tier Progress: " + tierProgress + "%");
        System.out.println("🪙 ===== END DEBUG =====");

        // Set attributes
        request.setAttribute("user", customer);
        request.setAttribute("activePage", "vtx");
        request.setAttribute("loyaltyPoints", loyaltyPoints);
        request.setAttribute("membershipTier", membershipTier);
        request.setAttribute("totalSpent", totalSpent);
        request.setAttribute("nextTierThreshold", nextTierThreshold);
        request.setAttribute("tierProgress", tierProgress);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/profile/vtx.jsp")
                .forward(request, response);
    }

    /**
     * Tính ngưỡng chi tiêu cần đạt để lên hạng tiếp theo
     */
    private int calculateNextTierThreshold(String currentTier) {
        switch (currentTier.toLowerCase()) {
            case "bronze":
                return 5000000; // 5 triệu để lên Silver
            case "silver":
                return 15000000; // 15 triệu để lên Gold
            case "gold":
                return 50000000; // 50 triệu để lên Platinum
            case "platinum":
                return 0; // Đã max tier
            default:
                return 5000000;
        }
    }

    /**
     * Tính % tiến độ lên hạng tiếp theo
     */
    private int calculateTierProgress(double totalSpent, String currentTier) {
        int threshold = calculateNextTierThreshold(currentTier);
        
        if (threshold == 0) {
            return 100; // Platinum = max tier
        }

        int previousThreshold = 0;
        switch (currentTier.toLowerCase()) {
            case "silver":
                previousThreshold = 5000000; // Bronze threshold
                break;
            case "gold":
                previousThreshold = 15000000; // Silver threshold
                break;
            case "platinum":
                return 100;
        }

        double progress = ((totalSpent - previousThreshold) / (threshold - previousThreshold)) * 100;
        return Math.min(100, Math.max(0, (int) progress));
    }
}