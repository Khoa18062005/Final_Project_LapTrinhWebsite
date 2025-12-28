package viettech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.CustomerDAO;
import viettech.dto.Profile_dto;
import viettech.dto.Profile_dto;
import viettech.entity.user.Customer;
import viettech.entity.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Profile Service - Xử lý cập nhật thông tin profile
 * @author VietTech Team
 */
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private final CustomerDAO customerDAO;

    public ProfileService() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Cập nhật thông tin profile
     * 
     * @param user User hiện tại
     * @param dto ProfileDTO chứa thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateProfile(User user, Profile_dto dto) {
        if (user == null || dto == null) {
            logger.error("✗ Update failed: User or DTO is null");
            return false;
        }

        try {
            // Cập nhật thông tin cơ bản
            user.setFirstName(trimOrEmpty(dto.getFirstName()));
            user.setLastName(trimOrEmpty(dto.getLastName()));
            user.setEmail(trimOrEmpty(dto.getEmail()));
            user.setPhone(trimOrEmpty(dto.getPhone()));
            user.setGender(trimOrEmpty(dto.getGender()));

            // Cập nhật ngày sinh
            Date dateOfBirth = parseDateOfBirth(dto.getDay(), dto.getMonth(), dto.getYear());
            if (dateOfBirth != null) {
                user.setDateOfBirth(dateOfBirth);
            }

            // Lưu vào database (chỉ với Customer)
            if (user instanceof Customer) {
                customerDAO.update((Customer) user);
                logger.info("✓ Profile updated successfully for: {}", user.getEmail());
                return true;
            } else {
                logger.warn("✗ User is not a Customer instance");
                return false;
            }

        } catch (Exception e) {
            logger.error("✗ Failed to update profile for: {}", user.getEmail(), e);
            return false;
        }
    }

    /**
     * Kiểm tra email đã tồn tại chưa (trừ email của chính user)
     */
    public boolean isEmailExistForOtherUser(String email, int currentUserId) {
        Customer customer = customerDAO.findByEmail(email);
        if (customer == null) {
            return false; // Email chưa tồn tại
        }
        // Email tồn tại nhưng là của chính user → OK
        return customer.getUserId() != currentUserId;
    }

    /**
     * Trim string hoặc trả về empty string
     */
    private String trimOrEmpty(String value) {
        return (value != null) ? value.trim() : "";
    }

    /**
     * Parse date of birth từ day, month, year
     */
    private Date parseDateOfBirth(String day, String month, String year) {
        if (day == null || day.trim().isEmpty() ||
            month == null || month.trim().isEmpty() ||
            year == null || year.trim().isEmpty()) {
            return null;
        }

        try {
            String dateString = String.format("%s-%s-%s", year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date parsed = sdf.parse(dateString);
            logger.debug("✓ Parsed date of birth: {}", dateString);
            return parsed;
        } catch (ParseException e) {
            logger.warn("✗ Failed to parse date of birth: {}-{}-{}", year, month, day);
            return null;
        }
    }
}