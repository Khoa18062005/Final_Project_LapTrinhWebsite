package viettech.controller;

import viettech.dto.ProductDetailDTO; // Giả sử bạn có DTO chi tiết, hoặc dùng ProductCardDTO tạm
import viettech.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {

    ProductService productService = new ProductService();

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        HttpSession session = request.getSession();

        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            ProductDetailDTO product = productService.getProductDetail(id);

            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }

            session.setAttribute("productId", product.getProductId());
            request.setAttribute("product", product);

            // ✅ FORWARD — KHÔNG redirect
            request.getRequestDispatcher("/WEB-INF/views/product-detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}