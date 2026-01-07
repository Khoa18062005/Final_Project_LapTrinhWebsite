package viettech.controller;

import viettech.dao.OrderDAO;
import viettech.dao.OrderStatusDAO;
import viettech.entity.order.Order;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PaymentSuccessServlet", urlPatterns = {"/payment-success"})
public class PaymentSuccessServlet extends HttpServlet {

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

        Boolean paymentSuccess = (Boolean) session.getAttribute("paymentSuccess");

        if (paymentSuccess == null || !paymentSuccess) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String orderNumber = (String) session.getAttribute("orderNumber");
        if (orderNumber != null) {
            try {
                Order order = orderDAO.findByOrderNumber(orderNumber);
                if (order != null) {
                    request.setAttribute("order", order);
                    request.setAttribute("orderStatuses", orderStatusDAO.findByOrderId(order.getOrderId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payment-success.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}