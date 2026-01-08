package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/policy/payment")
public class PaymentPolicyServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(PaymentPolicyServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("Accessing Payment Policy page");

        request.getRequestDispatcher("/WEB-INF/views/payment-policy.jsp")
                .forward(request, response);
    }
}