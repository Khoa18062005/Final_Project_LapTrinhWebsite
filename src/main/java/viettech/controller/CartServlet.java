package viettech.controller;

import viettech.dto.CartItemDTO;
import viettech.entity.user.User;
import viettech.service.CartService;
import viettech.service.ProductService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    
    private CartService cartService;

    
    @Override
    public void init() throws ServletException {
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) SessionUtil.getAttribute(request, "user");
        // Check login
        if (user == null) {
            // Redirect to login với redirect back (như code redirectURL trước của bạn)
            response.sendRedirect(request.getContextPath() + "/login?redirect=cart");
            return;
        }

        try {
            List<CartItemDTO> cartItems = cartService.getCartItems(session);
            double total = cartService.calculateTotal(cartItems);
            int cartCount = cartService.getCartCount(session);

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            request.setAttribute("itemCount", cartItems.size());
            request.setAttribute("cartCount", cartCount);

            request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle error, ví dụ forward error page hoặc redirect
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check login cho tất cả action (trừ getCount nếu muốn public)
        if (session.getAttribute("user") == null && !"getCount".equals(request.getParameter("action"))) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + request.getRequestURI());
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                addToCart(request, session, response);
            } else if ("update".equals(action)) {
                updateCartItem(request, session, response);
            } else if ("remove".equals(action)) {
                removeCartItem(request, session, response);
            } else if ("clear".equals(action)) {
                clearCart(request, session, response);
            } else if ("getCount".equals(action)) {
                getCartCount(session, response);
            }
        } catch (Exception e) {
            // Global catch cho POST
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Trong clearCart, thêm try-catch nếu cần
    private void clearCart(HttpServletRequest request, HttpSession session, HttpServletResponse response)
            throws IOException {
        try {
            cartService.clearCart(session);
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cart?error=" + e.getMessage());
        }
    }
    
    private void addToCart(HttpServletRequest request, HttpSession session, 
                          HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int variantId = request.getParameter("variantId") != null ? 
                           Integer.parseInt(request.getParameter("variantId")) : 0;
            
            cartService.addToCart(session, productId, variantId, quantity);
            
            // Trả về JSON response cho AJAX
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"count\": " + 
                                      cartService.getCartCount(session) + "}");
        } catch (Exception e) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"error\": \"" + 
                                      e.getMessage() + "\"}");
        }
    }
    
    private void updateCartItem(HttpServletRequest request, HttpSession session,
                               HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int variantId = request.getParameter("variantId") != null ? 
                           Integer.parseInt(request.getParameter("variantId")) : 0;
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            cartService.updateCartItem(session, productId, variantId, quantity);
            
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cart?error=" + 
                                e.getMessage());
        }
    }
    
    private void removeCartItem(HttpServletRequest request, HttpSession session,
                               HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int variantId = request.getParameter("variantId") != null ? 
                           Integer.parseInt(request.getParameter("variantId")) : 0;
            
            cartService.removeCartItem(session, productId, variantId);
            
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cart?error=" + 
                                e.getMessage());
        }
    }

    private void getCartCount(HttpSession session, HttpServletResponse response)
            throws IOException {
        int cartCount = cartService.getCartCount(session);
        response.setContentType("application/json");
        response.getWriter().write("{\"count\": " + cartCount + "}");
    }
}