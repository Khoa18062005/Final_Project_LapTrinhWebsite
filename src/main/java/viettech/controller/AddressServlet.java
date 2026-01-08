package viettech.controller;

import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.service.AddressService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import viettech.entity.Address;

/**
 * Servlet xử lý trang Address
 * URL: /profile/address
 */
@WebServlet("/profile/address")
public class AddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📍 ===== ADDRESS PAGE DEBUG =====");

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
            SessionUtil.setErrorMessage(request, "Chỉ khách hàng mới có thể xem địa chỉ!");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Customer customer = (Customer) user;
        int customerId = customer.getUserId();

        System.out.println("👤 Customer ID: " + customerId);
        System.out.println("📧 Customer Email: " + customer.getEmail());

        try {
            // Lấy danh sách địa chỉ của customer qua Service
            List<Address> addresses = addressService.getAddressesByCustomerId(customerId);
            System.out.println("📍 Total addresses found: " + addresses.size());

            // ===== SẮP XẾP: ĐỊA CHỈ MẶC ĐỊNH LÊN ĐẦU =====
            List<Address> sortedAddresses = addresses.stream()
                    .sorted(Comparator.comparing(Address::isDefault).reversed())
                    .collect(Collectors.toList());

            System.out.println("🔄 Addresses sorted: Default first");

            // Debug từng địa chỉ
            for (int i = 0; i < sortedAddresses.size(); i++) {
                Address addr = sortedAddresses.get(i);
                System.out.println("  [" + (i + 1) + "] Address ID: " + addr.getAddressId());
                System.out.println("      Receiver: " + addr.getReceiverName());
                System.out.println("      Is Default: " + addr.isDefault());
                System.out.println("      ---");
            }

            // Tìm địa chỉ mặc định qua Service
            var defaultAddress = addressService.getDefaultAddress(customerId);
            if (defaultAddress != null) {
                System.out.println("✅ Default address ID: " + defaultAddress.getAddressId());
            } else {
                System.out.println("⚠️ No default address found");
            }

            System.out.println("📍 ===== END DEBUG =====");

            // Set attributes với danh sách đã sắp xếp
            request.setAttribute("user", customer);
            request.setAttribute("activePage", "address");
            request.setAttribute("addresses", sortedAddresses); // ← ĐÃ SẮP XẾP
            request.setAttribute("defaultAddress", defaultAddress);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/profile/address.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ ERROR in AddressServlet:");
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi tải danh sách địa chỉ!");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}