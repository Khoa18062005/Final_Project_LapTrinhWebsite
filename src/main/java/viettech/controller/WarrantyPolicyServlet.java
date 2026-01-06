package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/policy/warranty")
public class WarrantyPolicyServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WarrantyPolicyServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("Accessing Warranty Policy page");

        request.getRequestDispatcher("/WEB-INF/views/warranty-policy.jsp")
                .forward(request, response);
    }
}