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

        // Nếu không có từ khóa → redirect về trang chủ
        if (keyword.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // === 1. Lấy các tham số lọc và sắp xếp từ request ===
        String sort = req.getParameter("sort"); // price_asc, price_desc, rating_desc

        Double minPrice = parseDoubleOrNull(req.getParameter("minPrice"));
        Double maxPrice = parseDoubleOrNull(req.getParameter("maxPrice"));
        Double minRating = parseDoubleOrNull(req.getParameter("minRating")); // ví dụ: 4.0

        // === 2. Thực hiện tìm kiếm ===
        List<ProductCardDTO> products = productService.searchProducts(keyword);

        // === 3. Áp dụng lọc + sắp xếp (nếu có) ===
        products = productService.filterAndSort(products, sort, minPrice, maxPrice, minRating);

        // === 4. Đưa dữ liệu vào request để JSP hiển thị ===
        req.setAttribute("products", products);
        req.setAttribute("keyword", keyword);
        req.setAttribute("pageTitle", "Tìm kiếm: " + keyword);

        // Giữ lại các tham số lọc để JSP đánh dấu (selected, checked...)
        req.setAttribute("sort", sort);
        req.setAttribute("minPrice", minPrice);
        req.setAttribute("maxPrice", maxPrice);
        req.setAttribute("minRating", minRating);

        // Đếm số kết quả
        req.setAttribute("totalResults", products.size());

        // Forward sang JSP
        req.getRequestDispatcher("/WEB-INF/views/search-results.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    // Helper method để parse Double an toàn
    private Double parseDoubleOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}