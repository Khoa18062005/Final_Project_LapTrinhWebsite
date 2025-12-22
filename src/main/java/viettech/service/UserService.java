package viettech.service;

import viettech.dao.CustomerDAO;
import viettech.dto.Register_dto;
import viettech.entity.user.Customer;
import viettech.util.PasswordUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserService {

    private final CustomerDAO customerDAO;

    public UserService() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Đăng ký khách hàng mới
     *
     * @param dto DTO chứa thông tin từ form đăng ký
     * @return 1 - Thành công tạo tài khoản mới (email chưa tồn tại)
     *         2 - Email đã tồn tại
     *         3 - Các trường hợp khác (email null, lỗi insert, v.v.) - vẫn cố gắng tạo nếu có thể
     */
    public int register(Register_dto dto) {
        if (dto == null) {
            return 3;
        }

        String email = dto.getEmail();
        String password = dto.getPassword();

        // Trường hợp email null hoặc rỗng → coi như lỗi, trả về 3
        if (email == null || email.trim().isEmpty()) {
            // Vẫn cố gắng tạo tài khoản nếu các field khác hợp lệ
            return createCustomerIfPossible(dto);
        }

        // Kiểm tra email đã tồn tại chưa
        if (customerDAO.findByEmail(email.trim()) != null) {
            return 2; // Đã có tài khoản
        }

        // Email chưa tồn tại → tạo mới
        if (password == null || password.isEmpty()) {
            return 3; // Không có mật khẩu → lỗi
        }

        try {
            Customer customer = dtoToCustomer(dto);
            // Hash mật khẩu bằng BCrypt trong PasswordUtil
            customer.setPassword(PasswordUtil.hashPassword(password));

            customerDAO.insert(customer);
            return 1; // Thành công tạo mới
        } catch (Exception e) {
            // Lỗi khi insert (constraint, DB error, v.v.)
            e.printStackTrace();
            return 3;
        }
    }

    /**
     * Chuyển từ Register_dto sang entity Customer
     */
    private Customer dtoToCustomer(Register_dto dto) {
        Customer customer = new Customer();

        customer.setFirstName(dto.getFirstName() != null ? dto.getFirstName().trim() : "");
        customer.setLastName(dto.getLastName() != null ? dto.getLastName().trim() : "");
        customer.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : "");
        customer.setPhone(dto.getPhone() != null ? dto.getPhone().trim() : "");
        customer.setGender(dto.getGender() != null ? dto.getGender().trim() : "");

        // Username: nếu không có thì dùng email làm username (có thể tùy chỉnh sau)
        String username = dto.getEmail();
        if (username == null || username.isEmpty()) {
            username = dto.getPhone(); // fallback
        }
        if (username == null || username.isEmpty()) {
            username = "user_" + System.currentTimeMillis();
        }
        customer.setUsername(username);

        // Avatar mặc định
        customer.setAvatar("");

        // Date of Birth: chuyển từ String (yyyy-MM-dd) sang Date
        if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                Date dob = sdf.parse(dto.getDateOfBirth().trim());
                customer.setDateOfBirth(dob);
            } catch (ParseException e) {
                customer.setDateOfBirth(null); // nếu format sai → để null
            }
        } else {
            customer.setDateOfBirth(null);
        }

        // Các field mặc định
        customer.setActive(true); // hoặc false tùy yêu cầu kích hoạt
        customer.setUpdatedAt(new Date());

        // Các field của Customer
        customer.setLoyaltyPoints(0);
        customer.setMembershipTier("Bronze"); // hoặc "" tùy yêu cầu
        customer.setTotalSpent(0.0);

        return customer;
    }

    /**
     * Dùng khi email null/rỗng hoặc các case lỗi → vẫn cố gắng tạo nếu có dữ liệu cơ bản
     */
    private int createCustomerIfPossible(Register_dto dto) {
        try {
            Customer customer = dtoToCustomer(dto);
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                customer.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
            } else {
                customer.setPassword(""); // hoặc throw exception tùy yêu cầu
            }
            customerDAO.insert(customer);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }
    }

    public Customer findCustomerByEmail(String email) {
        return customerDAO.findByEmail(email);
    }
}