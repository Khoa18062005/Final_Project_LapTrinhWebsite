package viettech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.CustomerDAO;
import viettech.dto.Register_dto;
import viettech.entity.user.Customer;
import viettech.util.PasswordUtil;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User Service - Xử lý đăng ký và quản lý user
 * @author VietTech Team
 */
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final CustomerDAO customerDAO;
    private static final String USERNAME_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Bỏ I, O, 0, 1 dễ nhầm
    private static final int USERNAME_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();


    public UserService() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Đăng ký khách hàng mới
     *
     * @param dto DTO chứa thông tin từ form đăng ký
     * @return 1 - Thành công tạo tài khoản mới
     *         2 - Email đã tồn tại
     *         3 - Lỗi (thiếu thông tin hoặc lỗi DB)
     */
    public int register(Register_dto dto) {
        // Validate DTO
        if (dto == null) {
            logger.error("✗ Registration failed: DTO is null");
            return 3;
        }

        String email = dto.getEmail();
        String password = dto.getPassword();

        // Validate email và password
        if (email == null || email.trim().isEmpty()) {
            logger.warn("✗ Registration failed: Email is empty");
            return 3;
        }

        if (password == null || password.trim().isEmpty()) {
            logger.warn("✗ Registration failed: Password is empty for email: {}", email);
            return 3;
        }

        logger.debug("Processing registration for email: {}", email);

        // Kiểm tra email đã tồn tại chưa
        if (customerDAO.findByEmail(email.trim()) != null) {
            logger.warn("✗ Registration failed: Email already exists: {}", email);
            return 2; // Email đã tồn tại
        }

        // Tạo customer mới
        try {
            Customer customer = dtoToCustomer(dto);
            customer.setPassword(PasswordUtil.hashPassword(password));

            customerDAO.insert(customer);
            logger.info("✓ Registration successful for email: {}", email);
            return 1; // Thành công
        } catch (Exception e) {
            logger.error("✗ Registration failed for email: {} - Database error", email, e);
            return 3;
        }
    }

    /**
     * Chuyển từ Register_dto sang entity Customer
     */
    private Customer dtoToCustomer(Register_dto dto) {
        Customer customer = new Customer();

        // Set basic info
        customer.setFirstName(trimOrEmpty(dto.getFirstName()));
        customer.setLastName(trimOrEmpty(dto.getLastName()));
        customer.setEmail(trimOrEmpty(dto.getEmail()));
        customer.setPhone(trimOrEmpty(dto.getPhone()));
        customer.setGender(trimOrEmpty(dto.getGender()));

        // Set username (dùng email làm username)
        customer.setUsername(generateUsername());

        // Set avatar mặc định
        customer.setAvatar("");

        // Parse date of birth
        customer.setDateOfBirth(parseDateOfBirth(dto.getDateOfBirth()));

        return customer;
    }

    /**
     * Trim string hoặc trả về empty string
     */
    private String trimOrEmpty(String value) {
        return (value != null) ? value.trim() : "";
    }

    /**
     * Generate username từ email hoặc phone
     */
    private String generateUsername() {
        StringBuilder username = new StringBuilder("user_");

        for (int i = 0; i < USERNAME_LENGTH; i++) {
            int index = random.nextInt(USERNAME_CHARS.length());
            username.append(USERNAME_CHARS.charAt(index));
        }

        return username.toString();
    }
    /**
     * Parse date of birth từ String (yyyy-MM-dd) sang Date
     */
    private Date parseDateOfBirth(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            return sdf.parse(dateString.trim());
        } catch (ParseException e) {
            logger.warn("✗ Failed to parse date of birth: {}", dateString);
            return null;
        }
    }
    /**
     * Tìm customer theo email
     */
    public Customer findCustomerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.warn("✗ findCustomerByEmail: email is empty");
            return null;
        }
        logger.debug("Finding customer by email: {}", email);
        Customer customer = customerDAO.findByEmail(email.trim());

        if (customer != null) {
            logger.debug("✓ Customer found: {}", email);
        } else {
            logger.debug("✗ Customer not found: {}", email);
        }
        return customer;
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean isEmailExist(String email) {
        Customer customer = customerDAO.findByEmail(email);
        return customer != null;
    }

    public void UpdateLastLoginAt(Customer customer) {
        customerDAO.update(customer);
    }
}