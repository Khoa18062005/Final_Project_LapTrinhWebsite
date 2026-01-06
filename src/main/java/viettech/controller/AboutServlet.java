package viettech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/about")
public class AboutServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AboutServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("Accessing About page");

        request.getRequestDispatcher("/WEB-INF/views/about.jsp")
                .forward(request, response);
    }
}