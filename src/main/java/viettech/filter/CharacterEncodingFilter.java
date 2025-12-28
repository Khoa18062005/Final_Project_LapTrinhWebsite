package viettech.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần làm gì
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // ✅ LOẠI TRỪ static resources (CSS, JS, images, uploads)
        if (requestURI.startsWith(httpRequest.getContextPath() + "/assets/") ||
                requestURI.startsWith(httpRequest.getContextPath() + "/uploads/") ||
                requestURI.endsWith(".css") ||
                requestURI.endsWith(".js") ||
                requestURI.endsWith(".png") ||
                requestURI.endsWith(".jpg") ||
                requestURI.endsWith(".jpeg") ||
                requestURI.endsWith(".gif") ||
                requestURI.endsWith(".ico") ||
                requestURI.endsWith(".woff") ||
                requestURI.endsWith(".woff2") ||
                requestURI.endsWith(".ttf") ||
                requestURI.endsWith(".svg")) {

            // ✅ BỎ QUA filter cho static files
            chain.doFilter(request, response);
            return;
        }

        // ✅ CHỈ set encoding cho dynamic content (HTML, JSP, Servlet)
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Tiếp tục filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Không cần làm gì
    }
}