package viettech.controller;

import viettech.dao.OrderDAO;
import viettech.dao.OrderStatusDAO;
import viettech.entity.order.Order;
import viettech.entity.order.OrderStatus;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * Servlet hiển thị trang thanh toán thất bại
 */
@WebServlet(name = "PaymentFailedServlet", urlPatterns = {"/payment-failed"})
public class PaymentFailedServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
        orderStatusDAO = new OrderStatusDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Kiểm tra xem có thông tin thanh toán thất bại không
        Boolean paymentSuccess = (Boolean) session.getAttribute("paymentSuccess");

        if (paymentSuccess == null) {
            // Nếu không có thông tin, redirect về trang chủ
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Lấy thông tin đơn hàng nếu có
        String orderNumber = (String) session.getAttribute("orderNumber");
        if (orderNumber != null) {
            try {
                Order order = orderDAO.findByOrderNumber(orderNumber);
                if (order != null) {
                    // Truyền thông tin order sang JSP
                    request.setAttribute("order", order);

                    // Lấy lịch sử trạng thái của order
                    request.setAttribute("orderStatuses", orderStatusDAO.findByOrderId(order.getOrderId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Không cần throw exception, chỉ log lỗi
            }
        }
        session.removeAttribute("cartItems"       );

        // Forward đến JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payment-failed.jsp");
        dispatcher.forward(request, response);

        // Xóa thông tin lỗi khỏi session sau khi hiển thị
        // (Optional)
        // session.removeAttribute("paymentSuccess");
        // session.removeAttribute("paymentError");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}