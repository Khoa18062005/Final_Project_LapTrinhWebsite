package viettech.controller;

import com.google.gson.Gson;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.service.AddressService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/profile/address/set-default")
public class SetDefaultAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        System.out.println("🎯 ===== SET DEFAULT ADDRESS DEBUG =====");

        Map<String, Object> result = new HashMap<>();

        try {
            // Check login
            User user = (User) SessionUtil.getAttribute(request, "user");
            if (user == null || !(user instanceof Customer)) {
                System.out.println("❌ User not logged in or not a customer");
                result.put("success", false);
                result.put("message", "Vui lòng đăng nhập!");
                out.print(gson.toJson(result));
                return;
            }

            Customer customer = (Customer) user;

            // Lấy parameters
            String addressIdStr = request.getParameter("addressId");
            String customerIdStr = request.getParameter("customerId");

            System.out.println("📝 Parameters:");
            System.out.println("  - addressId: " + addressIdStr);
            System.out.println("  - customerId: " + customerIdStr);

            // Validate parameters
            if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
                System.out.println("❌ Missing addressId");
                result.put("success", false);
                result.put("message", "Thiếu thông tin địa chỉ!");
                out.print(gson.toJson(result));
                return;
            }

            int addressId = Integer.parseInt(addressIdStr);
            int customerId = customer.getUserId();

            System.out.println("✅ Parsed values:");
            System.out.println("  - addressId: " + addressId);
            System.out.println("  - customerId: " + customerId);

            // Gọi service để set default
            boolean success = addressService.setDefaultAddress(addressId, customerId);

            if (success) {
                System.out.println("✅ Set default address successfully!");
                result.put("success", true);
                result.put("message", "Đã đặt làm địa chỉ mặc định!");
            } else {
                System.out.println("❌ Set default address failed");
                result.put("success", false);
                result.put("message", "Không thể đặt làm địa chỉ mặc định!");
            }

        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid number format: " + e.getMessage());
            result.put("success", false);
            result.put("message", "Thông tin địa chỉ không hợp lệ!");

        } catch (Exception e) {
            System.err.println("❌ ERROR in SetDefaultAddressServlet:");
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Có lỗi xảy ra: " + e.getMessage());
        }

        System.out.println("🎯 ===== END DEBUG =====");
        out.print(gson.toJson(result));
        out.flush();
    }
}