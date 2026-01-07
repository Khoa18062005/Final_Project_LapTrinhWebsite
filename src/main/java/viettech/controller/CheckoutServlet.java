package viettech.controller;

import viettech.config.VNPayConfig;
import viettech.dao.AddressDAO;
import viettech.dao.CustomerDAO;
import viettech.dao.OrderDAO;
import viettech.dao.OrderStatusDAO;
import viettech.dto.CartCheckoutItemDTO;
import viettech.entity.Address;
import viettech.entity.order.Order;
import viettech.entity.order.OrderStatus;
import viettech.entity.user.Customer;
import viettech.entity.user.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;
    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        customerDAO = new CustomerDAO();
        addressDAO = new AddressDAO();
        orderDAO = new OrderDAO();
        orderStatusDAO = new OrderStatusDAO();
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

            for (Address address : savedAddresses) {
                if (address == defaultAddress) {
                    savedAddresses.remove(defaultAddress);
                    break;
                }
            }

            if (defaultAddress == null && !savedAddresses.isEmpty()) {
                defaultAddress = savedAddresses.get(0);
            }

            request.setAttribute("customer", fullCustomer);
            request.setAttribute("savedAddresses", savedAddresses);
            request.setAttribute("defaultAddress", defaultAddress);

            List<CartCheckoutItemDTO> selectedCartItems = (List<CartCheckoutItemDTO>) session.getAttribute("selectedCartItems");
            if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
                request.setAttribute("selectedCartItems", selectedCartItems);

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

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");

        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String selectedAddressIdStr = request.getParameter("selectedAddressId");
            String paymentMethod = request.getParameter("paymentMethod");
            String note = request.getParameter("note");

            if (selectedAddressIdStr == null || selectedAddressIdStr.trim().isEmpty()) {
                throw new Exception("Vui lòng chọn địa chỉ giao hàng");
            }

            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                throw new Exception("Vui lòng chọn phương thức thanh toán");
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

            // Lưu thông tin vào session
            session.setAttribute("shippingAddress", selectedAddress);
            session.setAttribute("shippingNote", note);
            session.setAttribute("paymentMethod", paymentMethod);

            // Lấy thông tin giỏ hàng
            List<CartCheckoutItemDTO> selectedCartItems = (List<CartCheckoutItemDTO>) session.getAttribute("selectedCartItems");
            if (selectedCartItems == null || selectedCartItems.isEmpty()) {
                throw new Exception("Giỏ hàng trống");
            }

            // Tính tổng tiền
            double subtotal = 0;
            for (CartCheckoutItemDTO item : selectedCartItems) {
                subtotal += item.getSubtotal();
            }

            double shippingFee = 30000; // Phí ship cố định
            double tax = 0; // VAT 10%
            double totalPrice = subtotal + shippingFee + tax;

            // Xử lý theo phương thức thanh toán
            if ("VNPAY".equals(paymentMethod)) {
                // Tạo đơn hàng tạm với status PENDING_PAYMENT
                String orderNumber = VNPayConfig.generateOrderNumber();

                // Giả sử vendorId = 1 (bạn cần logic để lấy vendorId thực tế)
                int vendorId = 1;

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 3); // Giao hàng sau 3 ngày
                Date estimatedDelivery = cal.getTime();

                Order order = new Order(
                        orderNumber,
                        fullCustomer.getUserId(),
                        vendorId,
                        selectedAddress.getAddressId(),
                        "Pending_Payment", // Chờ thanh toán
                        subtotal,
                        shippingFee,
                        0, // discount
                        tax,
                        0, // voucherDiscount
                        0, // loyaltyPointsUsed
                        0, // loyaltyPointsDiscount
                        totalPrice,
                        note,
                        estimatedDelivery
                );

                // Lưu order vào database
                orderDAO.insert(order);

                // Lấy order vừa tạo để có orderId
                Order createdOrder = orderDAO.findByOrderNumber(orderNumber);

                if (createdOrder != null) {
                    // Tạo OrderStatus đầu tiên: PENDING_PAYMENT
                    OrderStatus orderStatus = new OrderStatus(
                            createdOrder.getOrderId(),
                            "Pending_Payment",
                            "Đơn hàng đã được tạo, đang chờ thanh toán qua VNPay",
                            null, // location
                            "SYSTEM", // updatedBy
                            null  // images
                    );
                    orderStatusDAO.insert(orderStatus);
                }

                // Lưu orderNumber vào session để xử lý sau khi thanh toán
                session.setAttribute("pendingOrderNumber", orderNumber);

                // Tạo URL thanh toán VNPay
                String orderInfo = "Thanh toan don hang " + orderNumber;
                long amount = (long) totalPrice;
                String vnpayUrl = VNPayConfig.createPaymentUrl(request, orderNumber, amount, orderInfo);

                // Redirect tới VNPay
                response.sendRedirect(vnpayUrl);

            } else if ("COD".equals(paymentMethod)) {
                // Xử lý COD như cũ
                Boolean isBuyNow = (Boolean) session.getAttribute("isBuyNow");
                if (isBuyNow != null && isBuyNow) {
                    response.sendRedirect(request.getContextPath() + "/checkout/payment");
                } else {
                    response.sendRedirect(request.getContextPath() + "/checkout/payment-cart");
                }

            } else if ("MOMO".equals(paymentMethod)) {
                // TODO: Implement MoMo payment
                throw new Exception("Phương thức thanh toán MoMo chưa được hỗ trợ");
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