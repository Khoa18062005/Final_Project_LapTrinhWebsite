package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/policy/shipping")
public class ShippingPolicyServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ShippingPolicyServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("Accessing Shipping Policy page");

        request.getRequestDispatcher("/WEB-INF/views/shipping-policy.jsp")
                .forward(request, response);
    }
}