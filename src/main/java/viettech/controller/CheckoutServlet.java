package viettech.controller;

import viettech.dao.AddressDAO;
import viettech.dao.CustomerDAO;
import viettech.dto.CartCheckoutItemDTO;
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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            String redirectURL = request.getContextPath() + "/login?redirect=checkout";
            response.sendRedirect(redirectURL);
            return;
        }

        try {
            Customer fullCustomer = customerDAO.findById(user.getUserId());

            if (fullCustomer == null) {
                session.removeAttribute("user");
                response.sendRedirect(request.getContextPath() + "/login?redirect=checkout");
                return;
            }

            List<Address> savedAddresses = addressDAO.findByCustomerId(fullCustomer.getUserId());
            Address defaultAddress = addressDAO.findDefaultByCustomerId(user.getUserId());

            // Tìm địa chỉ mặc định để pre-select
            for (Address address : savedAddresses) {
                if (address == defaultAddress) {
                    savedAddresses.remove(defaultAddress);
                }
            }

            if (defaultAddress == null && !savedAddresses.isEmpty()) {
                defaultAddress = savedAddresses.get(0);
            }

            // Đặt các attribute cho JSP
            request.setAttribute("customer", fullCustomer);
            request.setAttribute("savedAddresses", savedAddresses);
            request.setAttribute("defaultAddress", defaultAddress);

            // Kiểm tra xem có selectedCartItems trong session không
            List<CartCheckoutItemDTO> selectedCartItems = (List<CartCheckoutItemDTO>) session.getAttribute("selectedCartItems");
            if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
                request.setAttribute("selectedCartItems", selectedCartItems);

                // Tính tổng tiền cho các item đã chọn
                double total = 0;
                for (CartCheckoutItemDTO item : selectedCartItems) {
                    total += item.getSubtotal();
                }
                request.setAttribute("total", total);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/shipping-info.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
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
            String selectedAddressIdStr = request.getParameter("selectedAddressId");
            String note = request.getParameter("note");

            if (selectedAddressIdStr == null || selectedAddressIdStr.trim().isEmpty()) {
                throw new Exception("Vui lòng chọn địa chỉ giao hàng");
            }

            int selectedAddressId = Integer.parseInt(selectedAddressIdStr);

            Customer fullCustomer = customerDAO.findById(customer.getUserId());
            if (fullCustomer == null) {
                throw new Exception("Không tìm thấy thông tin khách hàng");
            }

            List<Address> savedAddresses = addressDAO.findByCustomerId(fullCustomer.getUserId());
            Address selectedAddress = null;
            for (Address address : savedAddresses) {
                if (address.getAddressId() == selectedAddressId) {
                    selectedAddress = address;
                    break;
                }
            }

            if (selectedAddress == null) {
                throw new Exception("Địa chỉ không tồn tại hoặc không thuộc về tài khoản của bạn");
            }

            // Lưu thông tin giao hàng vào session
            session.setAttribute("shippingAddress", selectedAddress);
            session.setAttribute("shippingNote", note);

            // Kiểm tra xem là buy-now hay từ giỏ hàng
            Boolean isBuyNow = (Boolean) session.getAttribute("isBuyNow");

            if (isBuyNow != null && isBuyNow) {
                // Nếu là buy-now, chuyển hướng đến payment
                response.sendRedirect(request.getContextPath() + "/checkout/payment");
            } else {
                // Nếu là từ giỏ hàng, chuyển hướng đến payment-cart
                response.sendRedirect(request.getContextPath() + "/checkout/payment-cart");
            }

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