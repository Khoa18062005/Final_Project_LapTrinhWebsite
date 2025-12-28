package viettech.controller;

import viettech.dao.AddressDAO;
import viettech.entity.Address;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile/address/add")
public class AddAddressServlet extends HttpServlet {
    
    private final AddressDAO addressDAO = new AddressDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("📍 ===== ADD ADDRESS DEBUG =====");
        
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
            SessionUtil.setErrorMessage(request, "Chỉ khách hàng mới có thể thêm địa chỉ!");
            response.sendRedirect(request.getContextPath() + "/profile/address");
            return;
        }
        
        Customer customer = (Customer) user;
        
        try {
            // Lấy dữ liệu từ form
            String receiverName = request.getParameter("receiverName");
            String phone = request.getParameter("phone");
            String street = request.getParameter("street");
            String ward = request.getParameter("wardName"); // Từ input ẩn
            String district = request.getParameter("districtName"); // Từ input ẩn
            String city = request.getParameter("cityName"); // Từ input ẩn
            boolean isDefault = request.getParameter("isDefault") != null;
            
            System.out.println("📝 Form Data:");
            System.out.println("  - Receiver: " + receiverName);
            System.out.println("  - Phone: " + phone);
            System.out.println("  - Street: " + street);
            System.out.println("  - Ward: " + ward);
            System.out.println("  - District: " + district);
            System.out.println("  - City: " + city);
            System.out.println("  - Is Default: " + isDefault);
            
            // Validate dữ liệu
            if (receiverName == null || receiverName.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                street == null || street.trim().isEmpty() ||
                ward == null || ward.trim().isEmpty() ||
                district == null || district.trim().isEmpty() ||
                city == null || city.trim().isEmpty()) {
                
                System.out.println("❌ Validation failed");
                SessionUtil.setErrorMessage(request, "Vui lòng điền đầy đủ thông tin!");
                response.sendRedirect(request.getContextPath() + "/profile/address");
                return;
            }
            
            // Nếu đặt làm mặc định, bỏ mặc định của các địa chỉ cũ
            if (isDefault) {
                Address currentDefault = addressDAO.findDefaultByCustomerId(customer.getUserId());
                if (currentDefault != null) {
                    currentDefault.setDefault(false);
                    addressDAO.update(currentDefault);
                }
            }
            
            // Tạo địa chỉ mới
            Address newAddress = new Address(
                customer,
                receiverName.trim(),
                phone.trim(),
                street.trim(),
                ward.trim(),
                district.trim(),
                city.trim(),
                isDefault
            );
            
            // Lưu vào database
            addressDAO.insert(newAddress);
            
            System.out.println("✅ Address added successfully! ID: " + newAddress.getAddressId());
            
            SessionUtil.setSuccessMessage(request, "Thêm địa chỉ thành công!");
            response.sendRedirect(request.getContextPath() + "/profile/address");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR in AddAddressServlet:");
            e.printStackTrace();
            SessionUtil.setErrorMessage(request, "Có lỗi xảy ra khi thêm địa chỉ!");
            response.sendRedirect(request.getContextPath() + "/profile/address");
        }
    }
}