package viettech.controller;

import viettech.dao.*;
import viettech.dto.CartCheckoutItemDTO;
import viettech.entity.Address;
import viettech.entity.order.Order;
import viettech.entity.order.OrderStatus;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.entity.voucher.Voucher;
import viettech.entity.voucher.VoucherUsage;
import viettech.service.VendorService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "CODPaymentServlet", urlPatterns = {"/checkout/payment-cod-confirmed"})
public class CODPaymentServlet extends HttpServlet {

    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;
    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;
    private VoucherDAO voucherDAO;
    private VoucherUsageDAO voucherUsageDAO;
    private VendorService vendorService;

    @Override
    public void init() throws ServletException {
        super.init();
        customerDAO = new CustomerDAO();
        addressDAO = new AddressDAO();
        orderDAO = new OrderDAO();
        orderStatusDAO = new OrderStatusDAO();
        voucherDAO = new VoucherDAO();
        voucherUsageDAO = new VoucherUsageDAO();
        vendorService = new VendorService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Customer customer = customerDAO.findById(user.getUserId());

        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy thông tin từ session
            Address shippingAddress = (Address) session.getAttribute("shippingAddress");
            String shippingNote = (String) session.getAttribute("shippingNote");
            String paymentMethod = (String) session.getAttribute("paymentMethod");
            List<CartCheckoutItemDTO> selectedCartItems =
                (List<CartCheckoutItemDTO>) session.getAttribute("selectedCartItems");
            Voucher appliedVoucher = (Voucher) session.getAttribute("appliedVoucher");

            if (shippingAddress == null || selectedCartItems == null || selectedCartItems.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            // Tính toán giá
            double subtotal = 0;
            for (CartCheckoutItemDTO item : selectedCartItems) {
                subtotal += item.getSubtotal();
            }

            double shippingFee = 30000;
            double tax = 0;
            double voucherDiscount = 0.0;

            // Tính discount từ voucher
            if (appliedVoucher != null) {
                if ("PERCENTAGE".equals(appliedVoucher.getType())) {
                    voucherDiscount = subtotal * (appliedVoucher.getDiscountPercent() / 100.0);
                    if (appliedVoucher.getMaxDiscount() > 0 && voucherDiscount > appliedVoucher.getMaxDiscount()) {
                        voucherDiscount = appliedVoucher.getMaxDiscount();
                    }
                } else if ("FIXED_AMOUNT".equals(appliedVoucher.getType())) {
                    voucherDiscount = appliedVoucher.getDiscountAmount();
                } else if ("SHIPPING".equals(appliedVoucher.getType())) {
                    voucherDiscount = appliedVoucher.getDiscountAmount();
                    shippingFee -= voucherDiscount;
                    if (shippingFee < 0) shippingFee = 0;
                }
            }

            double totalPrice = subtotal + shippingFee + tax - voucherDiscount;

            // Tạo đơn hàng
            String orderNumber = generateOrderNumber();
            int vendorId = 1;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 3);
            Date estimatedDelivery = cal.getTime();

            Customer fullCustomer = customerDAO.findById(customer.getUserId());

            Order order = new Order(
                    orderNumber,
                    fullCustomer.getUserId(),
                    vendorId,
                    shippingAddress.getAddressId(),
                    "Pending",
                    subtotal,
                    shippingFee,
                    0,
                    tax,
                    voucherDiscount,
                    0,
                    0,
                    totalPrice,
                    shippingNote,
                    estimatedDelivery
            );

            orderDAO.insert(order);
            Order createdOrder = orderDAO.findByOrderNumber(orderNumber);

            if (createdOrder != null) {
                // Tạo OrderStatus
                OrderStatus orderStatus = new OrderStatus(
                        createdOrder.getOrderId(),
                        "Pending",
                        "Đơn hàng đã được tạo thành công, thanh toán khi nhận hàng (COD)",
                        null,
                        "SYSTEM",
                        null
                );
                orderStatusDAO.insert(orderStatus);

                // Notify vendor about new order
                try {
                    vendorService.notifyNewOrder(createdOrder.getOrderId(), vendorId);
                } catch (Exception e) {
                    // Log error but don't fail the order creation
                    System.err.println("Failed to notify vendor about new order: " + e.getMessage());
                }

                // Lưu VoucherUsage nếu có voucher
                if (appliedVoucher != null) {
                    VoucherUsage voucherUsage = new VoucherUsage(
                            appliedVoucher.getVoucherId(),
                            fullCustomer.getUserId(),
                            createdOrder.getOrderId(),
                            new Date(),
                            subtotal,
                            voucherDiscount
                    );
                    voucherUsageDAO.insert(voucherUsage);

                    // Cập nhật usage count của voucher
                    appliedVoucher.setUsageCount(appliedVoucher.getUsageCount() + 1);
                    voucherDAO.update(appliedVoucher);
                }
            }

            // Set attributes cho JSP
            request.setAttribute("order", createdOrder);
            request.setAttribute("customer", fullCustomer);
            request.setAttribute("shippingAddress", shippingAddress);
            request.setAttribute("cartItems", selectedCartItems);
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("shippingFee", shippingFee);
            request.setAttribute("tax", tax);
            request.setAttribute("voucherDiscount", voucherDiscount);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("appliedVoucher", appliedVoucher);

            // Xóa session sau khi hoàn tất
            session.removeAttribute("selectedCartItems");
            session.removeAttribute("shippingAddress");
            session.removeAttribute("shippingNote");
            session.removeAttribute("paymentMethod");
            session.removeAttribute("appliedVoucher");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/order-confirmation.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tạo đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis();
    }
}