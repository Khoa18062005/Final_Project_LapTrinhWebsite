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

        // Nếu không có tham số q HOẶC q chỉ là chuỗi rỗng/khoảng trắng → redirect về trang chủ
        if (q == null || keyword.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/"); // Redirect về index (trang chủ)
            return; // Quan trọng: dừng xử lý tiếp theo
        }

        // Các tham số lọc và sắp xếp
        String sort = req.getParameter("sort"); // "price_asc", "price_desc"
        String minPriceStr = req.getParameter("min_price");
        String maxPriceStr = req.getParameter("max_price");

        Double minPrice = null;
        Double maxPrice = null;

        if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
            try {
                minPrice = Double.parseDouble(minPriceStr.trim());
            } catch (NumberFormatException e) {
                minPrice = null;
            }
        }
        if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceStr.trim());
            } catch (NumberFormatException e) {
                maxPrice = null;
            }
        }

        // Có từ khóa hợp lệ → thực hiện tìm kiếm
        List<ProductCardDTO> products = productService.searchProducts(keyword);

        // Áp dụng lọc và sắp xếp (nếu có)
        if ((sort != null && !sort.isEmpty()) || minPrice != null || maxPrice != null) {
            products = productService.filterAndSort(products, sort, minPrice, maxPrice);
        }

        // Đưa dữ liệu ra JSP
        req.setAttribute("products", products);
        req.setAttribute("keyword", keyword);
        req.setAttribute("currentSort", sort);
        req.setAttribute("currentMinPrice", minPrice);
        req.setAttribute("currentMaxPrice", maxPrice);
        req.setAttribute("pageTitle", "Tìm kiếm: " + keyword);

        req.getRequestDispatcher("/WEB-INF/views/search-results.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp); // xử lý giống GET
    }
}