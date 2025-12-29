package viettech.controller;

import viettech.dao.AddressDAO;
import viettech.dao.CustomerDAO;
import viettech.entity.Address;
import viettech.entity.user.Customer;
import viettech.entity.user.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Khởi tạo DAO
        customerDAO = new CustomerDAO();
        addressDAO = new AddressDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra đăng nhập - Lấy customer từ session
        HttpSession session = request.getSession();
        User customer = (User) session.getAttribute("user");

        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
        if (customer == null) {
            String redirectURL = request.getContextPath() + "/login?redirect=checkout";
            response.sendRedirect(redirectURL);
            return;
        }

        try {
            // 2. Lấy customer mới nhất từ database (để có dữ liệu mới nhất)
            Customer fullCustomer = customerDAO.findById(customer.getUserId());

            if (fullCustomer == null) {
                // Nếu không tìm thấy customer, có thể session đã cũ
                session.removeAttribute("user");
                response.sendRedirect(request.getContextPath() + "/login?redirect=checkout");
                return;
            }

            // 3. Lấy TẤT CẢ địa chỉ của customer từ database
            List<Address> savedAddresses = addressDAO.findByCustomerId(fullCustomer.getUserId());

            // 4. Debug: In ra số lượng địa chỉ tìm được
            System.out.println("Found " + savedAddresses.size() + " addresses for customer ID: " + fullCustomer.getUserId());
            for (Address addr : savedAddresses) {
                System.out.println("Address ID: " + addr.getAddressId() +
                        ", Receiver: " + addr.getReceiverName() +
                        ", Street: " + addr.getStreet());
            }

            // 5. Tìm địa chỉ mặc định để pre-select
            Address defaultAddress = null;
            for (Address address : savedAddresses) {
                if (address.isDefault()) {
                    defaultAddress = address;
                    break;
                }
            }

            // Nếu không có địa chỉ mặc định, lấy địa chỉ đầu tiên (nếu có)
            if (defaultAddress == null && !savedAddresses.isEmpty()) {
                defaultAddress = savedAddresses.get(0);
            }

            // 6. Đặt các attribute cho JSP
            request.setAttribute("customer", fullCustomer);
            request.setAttribute("savedAddresses", savedAddresses); // DANH SÁCH ĐỊA CHỈ
            request.setAttribute("defaultAddress", defaultAddress);

            // 7. Forward tới trang JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/shipping-info.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi
            request.setAttribute("errorMessage", "Lỗi khi tải thông tin giao hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Xử lý khi người dùng CHỌN địa chỉ và submit form
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");

        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // 1. Lấy địa chỉ được CHỌN từ form (radio button)
            String selectedAddressIdStr = request.getParameter("selectedAddressId");
            String note = request.getParameter("note");

            if (selectedAddressIdStr == null || selectedAddressIdStr.trim().isEmpty()) {
                throw new Exception("Vui lòng chọn địa chỉ giao hàng");
            }

            int selectedAddressId = Integer.parseInt(selectedAddressIdStr);

            // 2. Lấy lại customer từ database
            Customer fullCustomer = customerDAO.findById(customer.getUserId());
            if (fullCustomer == null) {
                throw new Exception("Không tìm thấy thông tin khách hàng");
            }

            // 3. Lấy DANH SÁCH địa chỉ của customer từ database
            List<Address> savedAddresses = addressDAO.findByCustomerId(fullCustomer.getUserId());

            // 4. Tìm địa chỉ được CHỌN trong danh sách
            Address selectedAddress = null;
            for (Address address : savedAddresses) {
                if (address.getAddressId() == selectedAddressId) {
                    selectedAddress = address;
                    break;
                }
            }

            // 5. Kiểm tra địa chỉ có thuộc về customer này không
            if (selectedAddress == null) {
                throw new Exception("Địa chỉ không tồn tại hoặc không thuộc về tài khoản của bạn");
            }

            // 6. Lưu thông tin giao hàng vào SESSION
            session.setAttribute("shippingAddress", selectedAddress);
            session.setAttribute("shippingNote", note);

            // 7. Chuyển hướng sang trang thanh toán
            response.sendRedirect(request.getContextPath() + "/checkout/payment");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Địa chỉ không hợp lệ");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            doGet(request, response);
        }
    }
}