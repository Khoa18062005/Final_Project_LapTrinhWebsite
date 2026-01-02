package viettech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.*;
import viettech.dto.Login_dto;
import viettech.entity.cart.Cart;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.util.PasswordUtil;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Login Service - Xử lý xác thực người dùng
 * @author VietTech Team
 */
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final AdminDAO adminDAO = new AdminDAO();
    private final VendorDAO vendorDAO = new VendorDAO();
    private final ShipperDAO shipperDAO = new ShipperDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    /**
     * Xác thực người dùng theo email và password
     * @param dto cặp dữ liệu email và password
     * @return AuthResult chứa User và role nếu thành công, null nếu thất bại
     */
    public AuthResult authenticate(Login_dto dto) {
        String email = dto.getEmail();
        String plainPassword = dto.getPassword();

        // Validate input
        if (email == null || plainPassword == null || email.isEmpty() || plainPassword.isEmpty()) {
            logger.warn("✗ Authentication failed: email or password is empty");
            return null;
        }

        logger.debug("Authenticating user: {}", email);

        // Kiểm tra lần lượt: Admin → Vendor → Shipper → Customer
        User user = checkInDAO(adminDAO, email, plainPassword);
        if (user != null) {
            logger.info("✓ User authenticated as Admin: {}", email);
            return new AuthResult(user, "admin");
        }

        user = checkInDAO(vendorDAO, email, plainPassword);
        if (user != null) {
            logger.info("✓ User authenticated as Vendor: {}", email);
            return new AuthResult(user, "vendor");
        }

        user = checkInDAO(shipperDAO, email, plainPassword);
        if (user != null) {
            logger.info("✓ User authenticated as Shipper: {}", email);
            return new AuthResult(user, "shipper");
        }

        user = checkInDAO(customerDAO, email, plainPassword);
        if (user != null) {
            logger.info("✓ User authenticated as Customer: {}", email);
            return new AuthResult(user, "customer");
        }

        logger.warn("✗ Authentication failed for email: {} - Invalid credentials", email);
        return null;
    }

    /**
     * Kiểm tra trong một DAO cụ thể: lấy user theo email → so sánh password bằng BCrypt
     */
    private User checkInDAO(Object dao, String email, String plainPassword) {
        try {
            // Gọi method findByEmail() của DAO
            Method findByEmailMethod = dao.getClass().getMethod("findByEmail", String.class);
            Object userObject = findByEmailMethod.invoke(dao, email);

            if (userObject != null) {
                // Cast về User (vì tất cả entity đều extends User)
                User user = (User) userObject;

                // So sánh password bằng BCrypt
                if (PasswordUtil.verifyPassword(plainPassword, user.getPassword())) {
                    logger.debug("✓ Password verified for user: {} in {}", email, dao.getClass().getSimpleName());
                    return user;
                } else {
                    logger.debug("✗ Password mismatch for user: {} in {}", email, dao.getClass().getSimpleName());
                }
            }
        } catch (Exception e) {
            logger.error("✗ Error checking login in DAO: {} for email: {}",
                    dao.getClass().getSimpleName(), email, e);
        }
        return null;
    }

    /**
     * Class để trả về kết quả xác thực
     */
    public static class AuthResult {
        private final User user;
        private final String role;

        public AuthResult(User user, String role) {
            this.user = user;
            this.role = role;
        }

        public User getUser() {
            return user;
        }

        public String getRole() {
            return role;
        }
    }

    public boolean checkCart(Customer customer) {
        CartDAO cartDAO = new CartDAO();
        return cartDAO.findByCustomerId(customer.getUserId()) == null;
    }

    public void addCart(Customer customer) {
        Date currentDate = new Date();
        CartDAO cartDAO = new CartDAO();
        Cart cart = new Cart();
        cart.setCustomerId(customer.getUserId());
        cartDAO.insert(cart);
    }
}