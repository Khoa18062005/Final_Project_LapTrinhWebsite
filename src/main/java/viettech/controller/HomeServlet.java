package viettech.controller;

import viettech.dao.ProductDAO;
import viettech.dto.ProductCardDTO;
import viettech.entity.product.Product;
import viettech.service.ProductService;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet({"", "/", "/index"})
public class HomeServlet extends HttpServlet {

    ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy danh sách sản phẩm từ DB
        List<ProductCardDTO> products = productService.getHomeProducts();

        // 2. Đẩy dữ liệu sang JSP
        request.setAttribute("products", products);

        // 3. Forward sang index.jsp
        request.getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }
}