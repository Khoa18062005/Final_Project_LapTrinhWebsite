package viettech.controller;

import viettech.dto.BuyNowDTO;
import viettech.entity.user.User;
import viettech.service.BuyNowService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout/buy-now"})
public class BuyNowServlet extends HttpServlet {

    private BuyNowService buyNowService;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng POST
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Kiểm tra đăng nhập
        if (((User) session.getAttribute("user")) == null) {
            String redirectURL = request.getContextPath() + "/login?redirect=buy-now";
            response.sendRedirect(redirectURL);
            return;
        }


        try {
            buyNowService = new BuyNowService();
            // Lấy thông tin từ form
            int productId = Integer.parseInt(session.getAttribute("productId").toString());

            // Lấy thông tin sản phẩm
            BuyNowDTO buyNowItem = buyNowService.getProductForBuyNow(productId);
            List<BuyNowDTO> buyNowList = buyNowService.createBuyNowList(productId);

            // Lưu thông tin vào session
            session.setAttribute("buyNowItem", buyNowItem);
            session.setAttribute("buyNowList", buyNowList);
            session.setAttribute("isBuyNow", true); // Flag để phân biệt với checkout từ giỏ hàng

            // Chuyển hướng đến trang checkout
            response.sendRedirect(request.getContextPath() + "/checkout");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Thông tin sản phẩm không hợp lệ");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi xử lý mua ngay: " + e.getMessage());
            // Quay lại trang chi tiết sản phẩm
            String referer = request.getHeader("Referer");
            if (referer != null) {
                response.sendRedirect(referer + "?error=" + e.getMessage());
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }
    }
}