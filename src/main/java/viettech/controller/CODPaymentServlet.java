package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.*;
import viettech.dto.CartItemDTO;
import viettech.entity.Address;
import viettech.entity.Notification;
import viettech.entity.order.Order;
import viettech.entity.order.OrderDetail;
import viettech.entity.order.OrderStatus;
import viettech.entity.payment.Payment;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.entity.voucher.Voucher;
import viettech.entity.voucher.VoucherUsage;
import viettech.service.NotificationService;
import viettech.util.EmailUtilBrevo;
import viettech.util.NotificationTemplateUtil;
import viettech.dto.CartCheckoutItemDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "CODPaymentServlet", urlPatterns = {"/checkout/payment-cod-confirmed"})
public class CODPaymentServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CODPaymentServlet.class);

    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;
    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;
    private VoucherDAO voucherDAO;
    private VoucherUsageDAO voucherUsageDAO;
    private NotificationService notificationService;

    @Override
    public void init() throws ServletException {
        super.init();
        customerDAO = new CustomerDAO();
        addressDAO = new AddressDAO();
        orderDAO = new OrderDAO();
        orderStatusDAO = new OrderStatusDAO();
        voucherDAO = new VoucherDAO();
        voucherUsageDAO = new VoucherUsageDAO();
        notificationService = new NotificationService();
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
            double usedLoyaltyPoints = 0;

            Object lpObj = session.getAttribute("usedLoyaltyPoints");
            if (lpObj != null) {
                usedLoyaltyPoints = Double.parseDouble(lpObj.toString());
            }

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

            double totalPrice = subtotal + shippingFee + tax - voucherDiscount - usedLoyaltyPoints*1000;

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
                    "pending",
                    subtotal,
                    shippingFee,
                    voucherDiscount,
                    tax,
                    voucherDiscount,
                    (int)usedLoyaltyPoints,
                    (int)usedLoyaltyPoints*1000,
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
                        "pending",
                        "Đơn hàng đã được tạo thành công, thanh toán khi nhận hàng (COD)",
                        null,
                        "SYSTEM",
                        null
                );
                orderStatusDAO.insert(orderStatus);

                // Lưu order details
                for(CartCheckoutItemDTO cartItemDTO : selectedCartItems) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(order.getOrderId());
                    orderDetail.setDiscount(order.getDiscount());
                    orderDetail.setQuantity(cartItemDTO.getQuantity());
                    orderDetail.setStatus("active");
                    orderDetail.setProductId(cartItemDTO.getProductId());
                    orderDetail.setProductName(cartItemDTO.getProductName());
                    orderDetail.setSubtotal(cartItemDTO.getSubtotal());
                    orderDetail.setVariantId(cartItemDTO.getVariantId());
                    orderDetail.setUnitPrice(cartItemDTO.getPrice());
                    orderDetail.setVariantInfo(cartItemDTO.getVariantDisplay());
                    OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                    orderDetailDAO.insert(orderDetail);
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

                // Lưu payment
                Payment payment = new Payment();
                payment.setOrderId(order.getOrderId());
                payment.setAmount(order.getTotalPrice());
                payment.setPaymentDate(new Date());
                payment.setPaidAt(new Date());
                payment.setMethod("COD");
                payment.setStatus("pending");
                payment.setProvider("Viettech");
                PaymentDAO paymentDAO = new PaymentDAO();
                paymentDAO.insert(payment);

                // Cập nhật loyalty points
                fullCustomer.setLoyaltyPoints(fullCustomer.getLoyaltyPoints() + (int)totalPrice/1000000 - (int)usedLoyaltyPoints);
                customerDAO.update(fullCustomer);

                // ========== GỬI EMAIL XÁC NHẬN ==========
                sendOrderConfirmationEmail(fullCustomer, createdOrder, estimatedDelivery);

                // ========== TẠO NOTIFICATION ==========
                createOrderNotification(fullCustomer, createdOrder, estimatedDelivery);
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
            session.removeAttribute("cartItems");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/order-confirmation.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            logger.error("✗ Error creating COD order", e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tạo đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    /**
     * Gửi email xác nhận đơn hàng COD
     */
    private void sendOrderConfirmationEmail(Customer customer, Order order, Date estimatedDelivery) {
        try {
            String customerName = customer.getFirstName() + " " + customer.getLastName();
            String estimatedDeliveryStr = new SimpleDateFormat("dd/MM/yyyy").format(estimatedDelivery);

            boolean emailSent = EmailUtilBrevo.sendOrderConfirmationCOD(
                    customer.getEmail(),
                    customerName,
                    order.getOrderNumber(),
                    order.getTotalPrice(),
                    estimatedDeliveryStr
            );

            if (emailSent) {
                logger.info("✓ Order confirmation email sent successfully to: {}", customer.getEmail());
            } else {
                logger.warn("✗ Failed to send order confirmation email to: {}", customer.getEmail());
            }
        } catch (Exception e) {
            logger.error("✗ Error sending order confirmation email", e);
        }
    }

    /**
     * Tạo notification cho đơn hàng COD
     */
    private void createOrderNotification(Customer customer, Order order, Date estimatedDelivery) {
        try {
            String estimatedDeliveryStr = new SimpleDateFormat("dd/MM/yyyy").format(estimatedDelivery);

            Notification notification = NotificationTemplateUtil.createOrderConfirmationCODNotification(
                    customer.getUserId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    order.getOrderNumber(),
                    order.getTotalPrice(),
                    estimatedDeliveryStr
            );

            boolean notificationCreated = notificationService.createNotification(notification);

            if (notificationCreated) {
                logger.info("✓ Order notification created successfully for user: {}", customer.getUserId());
            } else {
                logger.warn("✗ Failed to create order notification for user: {}", customer.getUserId());
            }
        } catch (Exception e) {
            logger.error("✗ Error creating order notification", e);
        }
    }

    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis();
    }
}