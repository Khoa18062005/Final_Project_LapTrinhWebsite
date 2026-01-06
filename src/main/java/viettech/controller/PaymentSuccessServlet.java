package viettech.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * Servlet hiển thị trang thanh toán thành công
 */
@WebServlet(name = "PaymentSuccessServlet", urlPatterns = {"/payment-success"})
public class PaymentSuccessServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Kiểm tra xem có thông tin thanh toán thành công không
        Boolean paymentSuccess = (Boolean) session.getAttribute("paymentSuccess");
        
        if (paymentSuccess == null || !paymentSuccess) {
            // Nếu không có thông tin thanh toán hoặc không thành công, redirect về trang chủ
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // Forward đến JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payment-success.jsp");
        dispatcher.forward(request, response);
        
        // Xóa thông tin thanh toán khỏi session sau khi hiển thị
        // (Optional - có thể giữ lại để user có thể F5 trang)
        // session.removeAttribute("paymentSuccess");
        // session.removeAttribute("orderNumber");
        // session.removeAttribute("transactionNo");
        // session.removeAttribute("amount");
        // session.removeAttribute("bankCode");
        // session.removeAttribute("payDate");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

/**
 * Servlet hiển thị trang thanh toán thất bại
 */
@WebServlet(name = "PaymentFailedServlet", urlPatterns = {"/payment-failed"})
class PaymentFailedServlet extends HttpServlet {
    
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