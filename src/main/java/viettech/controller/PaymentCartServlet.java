package viettech.controller;

import viettech.dto.CartCheckoutItemDTO;
import viettech.entity.user.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout/payment-cart"})
public class PaymentCartServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Kiểm tra xem có selectedCartItems không
        List<CartCheckoutItemDTO> selectedItems = (List<CartCheckoutItemDTO>) session.getAttribute("selectedCartItems");
        
        if (selectedItems == null || selectedItems.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy thông tin đơn hàng");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        // Tính tổng tiền
        double total = 0;
        for (CartCheckoutItemDTO item : selectedItems) {
            total += item.getSubtotal();
        }
        
        // Đặt các attribute cho trang thanh toán
        request.setAttribute("selectedItems", selectedItems);
        request.setAttribute("total", total);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payment-cart.jsp");
        dispatcher.forward(request, response);
    }
}