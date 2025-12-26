package viettech.controller;

import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet({"", "/", "/index"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // TODO: Load danh sách sản phẩm từ database
        // List<Product> products = productService.findAll();
        // req.setAttribute("products", products);
        // Forward tới index.jsp
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}