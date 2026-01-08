package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/careers")
public class CareersServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CareersServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("Accessing Careers page");

        request.getRequestDispatcher("/WEB-INF/views/careers.jsp")
                .forward(request, response);
    }

    // TODO: Implement doPost() to handle Vendor and Shipper registration
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        // TODO: Process registration form
        // - Validate data
        // - Create Vendor/Shipper entity
        // - Save to database
        // - Send confirmation email
        // - Set success message
        
        logger.info("Career application received");
        
        request.getSession().setAttribute("successMessage", 
                "Đơn đăng ký của bạn đã được gửi thành công! Chúng tôi sẽ liên hệ lại trong vòng 3-5 ngày làm việc.");
        
        response.sendRedirect(request.getContextPath() + "/careers");
    }
}