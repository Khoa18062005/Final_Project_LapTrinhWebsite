package viettech.controller;

import viettech.dao.VoucherDAO;
import viettech.entity.user.User;
import viettech.entity.voucher.Voucher;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Servlet xử lý các trang menu profile
 * URL pattern: /profile/xxx
 */
@WebServlet("/profile/vouchers")
public class VoucherServlet extends HttpServlet {

    private final VoucherDAO voucherDAO = new VoucherDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            SessionUtil.setErrorMessage(request, "Vui lòng đăng nhập!");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get current page from URL
        String path = request.getServletPath();
        String page = path.substring(path.lastIndexOf('/') + 1);

        System.out.println("🔍 ProfileMenuServlet - Page: " + page);

        // XỬ LÝ ĐẶC BIỆT CHO VOUCHERS
        if ("vouchers".equals(page)) {
            handleVouchersPage(request, response, user);
            return;
        }

        // Set attributes cho các trang khác
        request.setAttribute("user", user);
        request.setAttribute("activePage", page);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/profile/" + page + ".jsp")
                .forward(request, response);
    }

    /**
     * Xử lý trang vouchers với logic đặc biệt
     */
    private void handleVouchersPage(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            System.out.println("🎫 ===== VOUCHER PAGE DEBUG =====");

            // Lấy tất cả voucher
            List<Voucher> allVouchers = voucherDAO.findAll();
            System.out.println("📊 Total vouchers from DB: " + allVouchers.size());

            // Debug từng voucher
            for (Voucher v : allVouchers) {
                System.out.println("  - Voucher: " + v.getCode() + " | isPublic: " + v.isPublic() + " | isActive: " + v.isActive());
            }

            // Lọc chỉ lấy voucher public
            List<Voucher> publicVouchers = allVouchers.stream()
                    .filter(Voucher::isPublic)
                    .toList();
            System.out.println("📊 Public vouchers: " + publicVouchers.size());

            // Phân loại voucher
            Date now = new Date();
            System.out.println("📅 Current time: " + now);

            // Voucher có thể dùng
            List<Voucher> activeVouchers = publicVouchers.stream()
                    .filter(v -> {
                        boolean isActive = v.isActive();
                        boolean startOk = v.getStartDate().before(now);
                        boolean expiryOk = v.getExpiryDate().after(now);
                        boolean usageOk = v.getUsageCount() < v.getUsageLimit();

                        System.out.println("  🔍 " + v.getCode() +
                                " | active:" + isActive +
                                " | start:" + startOk +
                                " | expiry:" + expiryOk +
                                " | usage:" + usageOk);

                        return isActive && startOk && expiryOk && usageOk;
                    })
                    .toList();
            System.out.println("✅ Active vouchers: " + activeVouchers.size());

            // Voucher hết hạn
            List<Voucher> expiredVouchers = publicVouchers.stream()
                    .filter(v -> v.getExpiryDate().before(now))
                    .toList();
            System.out.println("❌ Expired vouchers: " + expiredVouchers.size());

            System.out.println("🎫 ===== END DEBUG =====");

            // Set attributes
            request.setAttribute("user", user);
            request.setAttribute("activePage", "vouchers");
            request.setAttribute("allVouchers", publicVouchers);
            request.setAttribute("activeVouchers", activeVouchers);
            request.setAttribute("expiredVouchers", expiredVouchers);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/profile/vouchers.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ ERROR in handleVouchersPage:");
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi tải danh sách voucher!");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}