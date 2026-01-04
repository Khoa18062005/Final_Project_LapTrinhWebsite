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
        String keyword = (q != null) ? q.trim() : "";

        // Nếu không có từ khóa → redirect về trang chủ (hoặc trang sản phẩm)
        if (keyword.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/"); // về trang chủ
            // Hoặc: resp.sendRedirect(req.getContextPath() + "/products"); // nếu có trang danh sách
            return; // Quan trọng: dừng xử lý tiếp
        }

        // Có từ khóa → thực hiện tìm kiếm bình thường
        List<ProductCardDTO> products = productService.searchProducts(keyword);

        req.setAttribute("products", products);
        req.setAttribute("keyword", keyword);
        req.setAttribute("pageTitle", "Tìm kiếm: " + keyword);

        req.getRequestDispatcher("/WEB-INF/views/search-results.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}