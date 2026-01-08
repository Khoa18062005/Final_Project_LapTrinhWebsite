package viettech.controller;

import viettech.dao.*;
import viettech.entity.order.Order;
import viettech.entity.user.Customer;
import viettech.entity.user.User;
import viettech.entity.voucher.Voucher;
import viettech.entity.voucher.VoucherUsage;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PaymentSuccessServlet", urlPatterns = {"/payment-success"})
public class PaymentSuccessServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;
    private VoucherDAO voucherDAO;
    private VoucherUsageDAO voucherUsageDAO;
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
        orderStatusDAO = new OrderStatusDAO();
        voucherDAO = new VoucherDAO();
        voucherUsageDAO = new VoucherUsageDAO();
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Customer customer = customerDAO.findById(user.getUserId());

        Boolean paymentSuccess = (Boolean) session.getAttribute("paymentSuccess");

        if (paymentSuccess == null || !paymentSuccess) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String orderNumber = (String) session.getAttribute("orderNumber");
        Order order = orderDAO.findByOrderNumber(orderNumber);

        if (orderNumber != null) {
            try {
                if (order != null) {
                    request.setAttribute("order", order);
                    request.setAttribute("orderStatuses", orderStatusDAO.findByOrderId(order.getOrderId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        VoucherUsage voucherUsage = (VoucherUsage) session.getAttribute("voucherUsage");
        Voucher appliedVoucher = (Voucher) session.getAttribute("appliedVoucher");

        if (voucherUsage != null && appliedVoucher != null) {
            try {
                voucherUsageDAO.insert(voucherUsage);

                appliedVoucher.setUsageCount(appliedVoucher.getUsageCount() + 1);
                voucherDAO.update(appliedVoucher);
            } catch (Exception e) {
                e.printStackTrace();
                // Có thể log hoặc thông báo cho user rằng voucher không được áp dụng
            }

            // Xóa sau khi insert thành công
            session.removeAttribute("voucherUsage");
            session.removeAttribute("appliedVoucher");
        }

        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + (int) order.getSubtotal()/1000000);
        customer.setTotalSpent(customer.getTotalSpent() + (int) order.getSubtotal());
        customerDAO.update(customer);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payment-success.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}