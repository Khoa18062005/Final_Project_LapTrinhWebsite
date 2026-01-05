package viettech.controller;

import viettech.entity.user.User;
import viettech.service.CartService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/checkout/buy-now"})
public class BuyNowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1️⃣ Kiểm tra đăng nhập
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=buy-now");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int variantId = request.getParameter("variantId") != null
                    ? Integer.parseInt(request.getParameter("variantId"))
                    : 0;

            // 2️⃣ Add vào cart (DB hoặc session)
            CartService cartService = new CartService();
            cartService.addToCart(session, productId, variantId, quantity);

            // 3️⃣ Lưu item được chọn vào session (QUAN TRỌNG)
            session.setAttribute("BUY_NOW_PRODUCT_ID", productId);
            session.setAttribute("BUY_NOW_VARIANT_ID", variantId);

            // 4️⃣ Redirect sang cart
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
