package viettech.controller;

import viettech.dto.ProductCardDTO;
import viettech.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/search-suggestions")
public class SearchSuggestionServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Cấu hình trả về JSON và UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String query = req.getParameter("q");

        List<ProductCardDTO> suggestions = productService.getSearchSuggestions(query);

        // Chuyển List<ProductCardDTO> thành chuỗi JSON thủ công
        // Nếu bạn có thư viện Gson, chỉ cần: String json = new Gson().toJson(suggestions);
        String json = convertListToJson(suggestions);

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    // Hàm hỗ trợ convert list sang JSON string đơn giản
    private String convertListToJson(List<ProductCardDTO> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < list.size(); i++) {
            ProductCardDTO p = list.get(i);
            json.append("{");
            json.append("\"id\":").append(p.getId()).append(",");
            // Escape dấu ngoặc kép trong tên sản phẩm để tránh lỗi JSON
            json.append("\"name\":\"").append(p.getName().replace("\"", "\\\"")).append("\",");
            json.append("\"price\":").append(p.getPrice()).append(",");
            json.append("\"image\":\"").append(p.getPrimaryImage()).append("\"");
            json.append("}");

            if (i < list.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}