package viettech.controller;

import viettech.service.ProductService;
import viettech.dto.ProductCardDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet({"/search", "/tim-kiem"})
public class SearchServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String q = req.getParameter("q");

        List<ProductCardDTO> products = productService.searchProducts(q);

        req.setAttribute("products", products);
        req.setAttribute("keyword", q != null ? q : "");
        req.setAttribute("pageTitle", q != null && !q.isEmpty() ? "Tìm kiếm: " + q : "Tất cả sản phẩm");

        req.getRequestDispatcher("/WEB-INF/views/search-results.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}