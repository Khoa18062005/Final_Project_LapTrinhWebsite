package viettech.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


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