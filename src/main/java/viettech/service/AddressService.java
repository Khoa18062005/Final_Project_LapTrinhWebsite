package viettech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.AddressDAO;
import viettech.dto.Address_dto;
import viettech.entity.Address;
import viettech.entity.user.Customer;
import java.util.*;

/**
 * Service xử lý logic địa chỉ
 */
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);
    private final AddressDAO addressDAO;

    public AddressService() {
        this.addressDAO = new AddressDAO();
    }

    /**
     * Thêm địa chỉ mới
     *
     * @param dto DTO chứa thông tin địa chỉ
     * @param customer Customer cần thêm địa chỉ
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean addAddress(Address_dto dto, Customer customer) {
        // Validate DTO
        if (dto == null || !dto.isValid()) {
            logger.error("✗ Add address failed: DTO is null or invalid");
            System.out.println("❌ Service: DTO is invalid");
            return false;
        }

        if (customer == null) {
            logger.error("✗ Add address failed: Customer is null");
            System.out.println("❌ Service: Customer is null");
            return false;
        }

        System.out.println("✅ Service: Processing add address for customer: " + customer.getEmail());
        try {
            // Nếu đặt làm mặc định, bỏ mặc định của các địa chỉ cũ
            if (dto.isDefault()) {
                System.out.println("ℹ️ Service: Setting as default address");
                Address currentDefault = addressDAO.findDefaultByCustomerId(customer.getUserId());
                if (currentDefault != null) {
                    currentDefault.setDefault(false);
                    addressDAO.update(currentDefault);
                    System.out.println("✅ Service: Updated old default address");
                }
            }

            // Tạo entity từ DTO
            Address address = dtoToEntity(dto, customer);
            System.out.println("✅ Service: Created address entity");

            // Lưu vào database
            addressDAO.insert(address);

            System.out.println("✅ Service: Address inserted to database");
            logger.info("✓ Address added successfully for customer: {}", customer.getEmail());
            return true;

        } catch (Exception e) {
            System.out.println("❌ Service Exception: " + e.getMessage());
            e.printStackTrace();
            logger.error("✗ Add address failed for customer: {}", customer.getEmail(), e);
            return false;
        }
    }

    /**
     * Chuyển từ AddressDTO sang entity Address
     */
    private Address dtoToEntity(Address_dto dto, Customer customer) {
        Address address = new Address();
        
        address.setCustomer(customer);
        address.setReceiverName(dto.getReceiverName().trim());
        address.setPhone(dto.getPhone().trim());
        address.setStreet(dto.getStreet().trim());
        address.setWard(dto.getWard().trim());
        address.setDistrict(dto.getDistrict().trim());
        address.setCity(dto.getCity().trim());
        address.setDefault(dto.isDefault());

        
        return address;
    }

    /**
     * Chuyển từ entity Address sang AddressDTO
     */
    public Address_dto entityToDto(Address address) {
        if (address == null) {
            return null;
        }
        
        return new Address_dto(
            address.getReceiverName(),
            address.getPhone(),
            address.getStreet(),
            address.getWard(),
            address.getDistrict(),
            address.getCity(),
            address.isDefault()
        );
    }

    /**
     * Kiểm tra xem customer có địa chỉ mặc định chưa
     */
    public boolean hasDefaultAddress(int customerId) {
        Address defaultAddress = addressDAO.findDefaultByCustomerId(customerId);
        return defaultAddress != null;
    }

    /**
     * Lấy danh sách địa chỉ của customer
     */
    public java.util.List<Address> getAddressesByCustomerId(int customerId) {
        return addressDAO.findByCustomerId(customerId);
    }

    /**
     * Lấy địa chỉ mặc định của customer
     */
    public Address getDefaultAddress(int customerId) {
        return addressDAO.findDefaultByCustomerId(customerId);
    }

    /**
     * Đếm số địa chỉ của customer
     */
    public long countAddressesByCustomerId(int customerId) {
        List<Address> addresses = addressDAO.findByCustomerId(customerId);
        return addresses.size();
    }

    /**
     * Đặt địa chỉ làm mặc định
     *
     * @param addressId ID của địa chỉ cần đặt làm mặc định
     * @param customerId ID của customer
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean setDefaultAddress(int addressId, int customerId) {
        System.out.println("🎯 Service: Setting default address");
        System.out.println("  - addressId: " + addressId);
        System.out.println("  - customerId: " + customerId);

        try {
            // 1. Tìm địa chỉ cần đặt làm mặc định
            Address targetAddress = addressDAO.findById(addressId);

            if (targetAddress == null) {
                System.out.println("❌ Service: Address not found");
                logger.error("✗ Address not found with ID: {}", addressId);
                return false;
            }

            // 2. Kiểm tra địa chỉ có thuộc về customer không
            if (targetAddress.getCustomer().getUserId() != customerId) {
                System.out.println("❌ Service: Address does not belong to customer");
                logger.error("✗ Address {} does not belong to customer {}", addressId, customerId);
                return false;
            }

            // 3. Bỏ default của địa chỉ mặc định hiện tại (nếu có)
            Address currentDefault = addressDAO.findDefaultByCustomerId(customerId);
            if (currentDefault != null && currentDefault.getAddressId() != addressId) {
                System.out.println("ℹ️ Service: Removing default from address: " + currentDefault.getAddressId());
                currentDefault.setDefault(false);
                addressDAO.update(currentDefault);
                System.out.println("✅ Service: Updated old default address");
            }

            // 4. Đặt địa chỉ mới làm mặc định
            targetAddress.setDefault(true);
            addressDAO.update(targetAddress);

            System.out.println("✅ Service: Set address " + addressId + " as default");
            logger.info("✓ Set address {} as default for customer {}", addressId, customerId);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Service Exception: " + e.getMessage());
            e.printStackTrace();
            logger.error("✗ Failed to set default address {} for customer {}", addressId, customerId, e);
            return false;
        }
    }
}