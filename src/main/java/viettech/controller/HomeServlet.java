package viettech.controller;

import viettech.service.ProductService;
import viettech.util.NotificationUtil;
import viettech.dto.ProductCardDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet({"", "/", "/index"})
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tất cả sản phẩm theo từng loại riêng biệt
        List<ProductCardDTO> phones = productService.getAllPhones();
        List<ProductCardDTO> laptops = productService.getAllLaptops();
        List<ProductCardDTO> tablets = productService.getAllTablets();
        List<ProductCardDTO> headphones = productService.getAllHeadphones();

        // Đẩy từng list riêng vào request attribute
        request.setAttribute("phones", phones);
        request.setAttribute("laptops", laptops);
        request.setAttribute("tablets", tablets);
        request.setAttribute("headphones", headphones);

        // Thêm thông tin thông báo vào request
        NotificationUtil.addNotificationAttributes(request);

        // Forward sang index.jsp
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}