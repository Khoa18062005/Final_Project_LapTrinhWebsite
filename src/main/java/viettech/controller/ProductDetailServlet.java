package viettech.controller;

import viettech.dto.ProductDetailDTO; // Giả sử bạn có DTO chi tiết, hoặc dùng ProductCardDTO tạm
import viettech.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {

    ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductDetailDTO product = productService.getProductDetail(id);

        if (product != null) {
            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(request, response);
        } else {
            // Redirect về home nếu không tìm thấy
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}