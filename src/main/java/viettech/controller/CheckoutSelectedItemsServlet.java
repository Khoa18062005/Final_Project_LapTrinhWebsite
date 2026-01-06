package viettech.controller;

import viettech.dto.CartCheckoutItemDTO;
import viettech.entity.user.User;
import viettech.service.CartService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout/selected-items"})
public class CheckoutSelectedItemsServlet extends HttpServlet {
    
    private CartService cartService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        cartService = new CartService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra đăng nhập
        if (user == null) {
            String redirectURL = request.getContextPath() + "/login?redirect=checkout/selected-items";
            response.sendRedirect(redirectURL);
            return;
        }
        
        try {
            // Lấy danh sách productId và variantId từ form
            String[] productIds = request.getParameterValues("productId");
            String[] variantIds = request.getParameterValues("variantId");
            
            if (productIds == null || productIds.length == 0) {
                // Nếu không có sản phẩm nào được chọn, quay lại giỏ hàng
                session.setAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // Lấy thông tin các item đã chọn
            List<CartCheckoutItemDTO> selectedItems = new ArrayList<>();
            
            for (int i = 0; i < productIds.length; i++) {
                int productId = Integer.parseInt(productIds[i]);
                int variantId = Integer.parseInt(variantIds[i]);
                
                // Lấy thông tin chi tiết của item từ database
                CartCheckoutItemDTO item = cartService.getCartItemForCheckout(
                    user.getUserId(), productId, variantId);
                
                if (item != null) {
                    selectedItems.add(item);
                }
            }
            
            if (selectedItems.isEmpty()) {
                session.setAttribute("error", "Không tìm thấy thông tin sản phẩm đã chọn");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // Lưu thông tin vào session
            session.setAttribute("selectedCartItems", selectedItems);
            session.setAttribute("isBuyNow", false);
            session.removeAttribute("buyNowItem");
            session.removeAttribute("buyNowList");
            
            // Chuyển hướng đến trang checkout
            response.sendRedirect(request.getContextPath() + "/checkout");
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi xử lý thanh toán: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}