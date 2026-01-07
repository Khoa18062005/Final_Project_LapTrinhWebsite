package viettech.controller;

import viettech.config.VNPayConfig;
import viettech.dao.*;
import viettech.dto.CartCheckoutItemDTO;
import viettech.entity.Address;
import viettech.entity.order.Order;
import viettech.entity.order.OrderStatus;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.entity.voucher.Voucher;
import viettech.entity.voucher.VoucherUsage;

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
    private VoucherDAO voucherDAO;
    private VoucherUsageDAO voucherUsageDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        customerDAO = new CustomerDAO();
        addressDAO = new AddressDAO();
        orderDAO = new OrderDAO();
        orderStatusDAO = new OrderStatusDAO();
        voucherDAO = new VoucherDAO();
        voucherUsageDAO = new VoucherUsageDAO();
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

            // Lấy danh sách voucher active và valid
            List<Voucher> availableVouchers = voucherDAO.findActiveAndValid();
            request.setAttribute("availableVouchers", availableVouchers);

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
            String voucherCode = request.getParameter("voucherCode");

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

            double shippingFee = 30000;
            double tax = 0;
            double voucherDiscount = 0.0;
            Voucher appliedVoucher = null;

            // Xử lý voucher nếu có
            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                appliedVoucher = voucherDAO.findByCode(voucherCode);

                if (appliedVoucher != null) {
                    // Kiểm tra voucher còn hợp lệ không
                    Date now = new Date();
                    if (!appliedVoucher.isActive() ||
                            appliedVoucher.getStartDate().after(now) ||
                            appliedVoucher.getExpiryDate().before(now)) {
                        throw new Exception("Voucher không còn hiệu lực");
                    }

                    // Kiểm tra đã sử dụng hết chưa
                    if (appliedVoucher.getUsageCount() >= appliedVoucher.getUsageLimit()) {
                        throw new Exception("Voucher đã hết lượt sử dụng");
                    }

                    // Kiểm tra giá trị đơn hàng tối thiểu
                    if (subtotal < appliedVoucher.getMinOrderValue()) {
                        throw new Exception("Đơn hàng chưa đạt giá trị tối thiểu để áp dụng voucher");
                    }

                    // Kiểm tra user đã dùng voucher này chưa (nếu có giới hạn per user)
                    if (appliedVoucher.getUsageLimitPerUser() > 0) {
                        long userUsageCount = voucherUsageDAO.countByVoucherId(appliedVoucher.getVoucherId());
                        if (userUsageCount >= appliedVoucher.getUsageLimitPerUser()) {
                            throw new Exception("Bạn đã sử dụng hết lượt áp dụng voucher này");
                        }
                    }

                    // Tính toán giảm giá
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
                    }

                    // Lưu voucher vào session để dùng sau
                    session.setAttribute("appliedVoucher", appliedVoucher);
                } else {
                    throw new Exception("Mã voucher không hợp lệ");
                }
            }

            double totalPrice = subtotal + shippingFee + tax - voucherDiscount;

            // Xử lý theo phương thức thanh toán
            if ("VNPAY".equals(paymentMethod)) {
                String orderNumber = VNPayConfig.generateOrderNumber();
                int vendorId = 1;

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 3);
                Date estimatedDelivery = cal.getTime();

                Order order = new Order(
                        orderNumber,
                        fullCustomer.getUserId(),
                        vendorId,
                        selectedAddress.getAddressId(),
                        "Pending_Payment",
                        subtotal,
                        shippingFee,
                        0,
                        tax,
                        voucherDiscount,
                        0,
                        0,
                        totalPrice,
                        note,
                        estimatedDelivery
                );

                orderDAO.insert(order);
                Order createdOrder = orderDAO.findByOrderNumber(orderNumber);

                if (createdOrder != null) {
                    // Tạo OrderStatus
                    OrderStatus orderStatus = new OrderStatus(
                            createdOrder.getOrderId(),
                            "Pending_Payment",
                            "Đơn hàng đã được tạo, đang chờ thanh toán qua VNPay",
                            null,
                            "SYSTEM",
                            null
                    );
                    orderStatusDAO.insert(orderStatus);

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

                        // Tăng usage count của voucher
                        appliedVoucher.setUsageCount(appliedVoucher.getUsageCount() + 1);
                        voucherDAO.update(appliedVoucher);
                    }
                }

                session.setAttribute("pendingOrderNumber", orderNumber);

                String orderInfo = "Thanh toan don hang " + orderNumber;
                long amount = (long) totalPrice;
                String vnpayUrl = VNPayConfig.createPaymentUrl(request, orderNumber, amount, orderInfo);

                response.sendRedirect(vnpayUrl);

            } else if ("COD".equals(paymentMethod)) {
                Boolean isBuyNow = (Boolean) session.getAttribute("isBuyNow");
                if (isBuyNow != null && isBuyNow) {
                    response.sendRedirect(request.getContextPath() + "/checkout/payment");
                } else {
                    response.sendRedirect(request.getContextPath() + "/checkout/payment-cart");
                }

            } else if ("MOMO".equals(paymentMethod)) {
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