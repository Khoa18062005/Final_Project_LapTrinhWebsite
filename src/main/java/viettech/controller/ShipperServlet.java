package viettech.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/shipper")
public class ShipperServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Điều hướng từ Servlet vào thư mục WEB-INF
        request.getRequestDispatcher("/WEB-INF/views/shipper.jsp")
                .forward(request, response);
    }
}