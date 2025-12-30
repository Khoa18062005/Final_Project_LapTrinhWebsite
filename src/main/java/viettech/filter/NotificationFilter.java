package viettech.filter;

import viettech.util.NotificationUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class NotificationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Bỏ qua các request API và static resources
        if (!path.startsWith("/api") &&
                !path.startsWith("/assets/") &&
                !path.endsWith(".css") &&
                !path.endsWith(".js") &&
                !path.endsWith(".png") &&
                !path.endsWith(".jpg") &&
                !path.endsWith(".jpeg")) {

            NotificationUtil.addNotificationAttributes(httpRequest);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}