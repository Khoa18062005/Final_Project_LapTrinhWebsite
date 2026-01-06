package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ContactServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("Accessing Contact page");

        request.getRequestDispatcher("/WEB-INF/views/contact.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        // Get form data
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        logger.info("Contact form submitted - Name: {}, Email: {}, Subject: {}", fullName, email, subject);

        // TODO: Process contact form (save to database, send email, etc.)
        
        // Set success message
        request.getSession().setAttribute("successMessage", 
                "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi trong vòng 24 giờ.");

        // Redirect back to contact page
        response.sendRedirect(request.getContextPath() + "/contact");
    }
}