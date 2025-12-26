package viettech.service;

import viettech.dao.AdminDAO;
import viettech.dao.CustomerDAO;
import viettech.dao.ShipperDAO;
import viettech.dao.VendorDAO;
import viettech.util.PasswordUtil;

public class LoginService {

    private final AdminDAO adminDAO = new AdminDAO();
    private final VendorDAO vendorDAO = new VendorDAO();
    private final ShipperDAO shipperDAO = new ShipperDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    /**
     * Xác thực người dùng theo email và password
     * @param email email người dùng nhập
     * @param plainPassword mật khẩu người dùng nhập (plain text)
     * @return Object là Admin/Vendor/Shipper/Customer nếu thành công, null nếu thất bại
     */
    public AuthResult authenticate(String email, String plainPassword) {
        if (email == null || plainPassword == null || email.isEmpty() || plainPassword.isEmpty()) {
            return null;
        }

        // Kiểm tra lần lượt theo thứ tự ưu tiên: Admin → Vendor → Shipper → Customer
        Object user = checkInDAO(adminDAO, email, plainPassword);
        if (user != null) {
            return new AuthResult(user, "admin");
        }

        user = checkInDAO(vendorDAO, email, plainPassword);
        if (user != null) {
            return new AuthResult(user, "vendor");
        }

        user = checkInDAO(shipperDAO, email, plainPassword);
        if (user != null) {
            return new AuthResult(user, "shipper");
        }

        user = checkInDAO(customerDAO, email, plainPassword);
        if (user != null) {
            return new AuthResult(user, "customer");
        }

        return null; // Không tìm thấy hoặc sai mật khẩu
    }

    /**
     * Kiểm tra trong một DAO cụ thể: lấy user theo email → so sánh password bằng BCrypt
     */
    private Object checkInDAO(Object dao, String email, String plainPassword) {
        try {
            // Mỗi DAO có phương thức findByEmail(email)
            java.lang.reflect.Method findByEmailMethod = dao.getClass().getMethod("findByEmail", String.class);
            Object user = findByEmailMethod.invoke(dao, email);

            if (user != null) {
                // Lấy trường password từ entity (giả sử tất cả entity đều có getPassword())
                java.lang.reflect.Method getPasswordMethod = user.getClass().getMethod("getPassword");
                String hashedPassword = (String) getPasswordMethod.invoke(user);

                // So sánh bằng BCrypt
                if (PasswordUtil.verifyPassword(plainPassword, hashedPassword)) {
                    return user;
                }
            }
        } catch (Exception e) {
            // In toàn bộ stack trace ra console (rất chi tiết)
            e.printStackTrace();

            // In thêm thông báo dễ đọc
            System.err.println("=== LỖI KHI KIỂM TRA ĐĂNG NHẬP ===");
            System.err.println("DAO đang kiểm tra: " + dao.getClass().getName());
            System.err.println("Email đang thử: " + email);
            System.err.println("Lỗi cụ thể: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            System.err.println("=====================================");
            // Nếu lỗi (không có method, không tìm thấy user, v.v.) → bỏ qua
            return null;
        }
        return null;
    }

    /**
     * Class nội bộ để trả về kết quả xác thực
     */
    public static class AuthResult {
        private final Object user;
        private final String role;

        public AuthResult(Object user, String role) {
            this.user = user;
            this.role = role;
        }

        public Object getUser() {
            return user;
        }

        public String getRole() {
            return role;
        }
    }
}