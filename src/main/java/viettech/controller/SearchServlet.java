package viettech.controller;

import viettech.service.ProductService;
import viettech.dto.ProductCardDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        String brandFilter = req.getParameter("brand"); // THÊM: lọc theo hãng

        Double minPrice = parseDoubleOrNull(req.getParameter("minPrice"));
        Double maxPrice = parseDoubleOrNull(req.getParameter("maxPrice"));
        Double minRating = parseDoubleOrNull(req.getParameter("minRating"));

        // === 2. Thực hiện tìm kiếm ===
        List<ProductCardDTO> products = productService.searchProducts(keyword);

        // === 3. Áp dụng lọc giá + sắp xếp + rating (trước khi xác định danh mục chính) ===
        products = productService.filterAndSort(products, sort, minPrice, maxPrice, minRating);

        // === 4. PHÁT HIỆN DANH MỤC CHÍNH & LẤY DANH SÁCH BRAND ===
        boolean showBrandFilter = false;
        List<String> availableBrands = new ArrayList<>();
        int dominantCategoryId = -1;

        if (!products.isEmpty()) {
            // Đếm số lượng sản phẩm theo category
            Map<Integer, Long> categoryCount = products.stream()
                    .collect(Collectors.groupingBy(
                            p -> productService.getCategoryById(p.getId()),
                            Collectors.counting()));

            // Tìm category có số lượng lớn nhất
            dominantCategoryId = categoryCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(-1);

            long dominantCount = categoryCount.getOrDefault(dominantCategoryId, 0L);
            long total = products.size();

            // Nếu category chiếm >= 80% hoặc toàn bộ kết quả → coi là danh mục chính
            if (dominantCount >= total * 0.8 || dominantCount == total) {
                // Chỉ áp dụng cho 4 danh mục chính
                if (dominantCategoryId == ProductService.CATEGORY_PHONE ||
                        dominantCategoryId == ProductService.CATEGORY_TABLET ||
                        dominantCategoryId == ProductService.CATEGORY_LAPTOP ||
                        dominantCategoryId == ProductService.CATEGORY_HEADPHONE) {

                    showBrandFilter = true;

                    // Lấy danh sách brand distinct từ các sản phẩm thuộc danh mục chính
                    availableBrands = products.stream()
                            .filter(p -> productService.getCategoryById(p.getId()) == dominantCategoryId)
                            .map(ProductCardDTO::getBrand)
                            .filter(b -> b != null && !b.trim().isEmpty())
                            .map(String::trim)
                            .distinct()
                            .sorted(String.CASE_INSENSITIVE_ORDER) // Sắp xếp A-Z không phân biệt hoa/thường
                            .collect(Collectors.toList());
                }
            }
        }

        // === 5. ÁP DỤNG LỌC THEO BRAND (nếu có) ===
        if (brandFilter != null && !brandFilter.trim().isEmpty() && showBrandFilter) {
            String finalBrandFilter = brandFilter.trim();
            products = products.stream()
                    .filter(p -> {
                        String productBrand = p.getBrand();
                        return productBrand != null && productBrand.trim().equalsIgnoreCase(finalBrandFilter);
                    })
                    .collect(Collectors.toList());

            // Sau khi lọc brand, vẫn giữ nguyên danh sách availableBrands (để hiển thị nút)
        }

        // === 6. Đưa dữ liệu vào request để JSP hiển thị ===
        req.setAttribute("products", products);
        req.setAttribute("keyword", keyword);
        req.setAttribute("pageTitle", "Tìm kiếm: " + keyword);

        // Các tham số hiện tại để JSP rebuild URL
        req.setAttribute("currentSort", sort);
        req.setAttribute("currentMinPrice", minPrice);
        req.setAttribute("currentMaxPrice", maxPrice);
        req.setAttribute("currentBrand", brandFilter);

        // Dữ liệu cho phần lọc hãng
        req.setAttribute("showBrandFilter", showBrandFilter);
        req.setAttribute("availableBrands", availableBrands);
        req.setAttribute("dominantCategoryId", dominantCategoryId);

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
            return Double.parseDouble(value.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}