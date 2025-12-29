package viettech.filter;

import viettech.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auth Filter - Bảo vệ các trang yêu cầu đăng nhập
 */
@WebFilter(urlPatterns = {
        "/profile/*",      // ✅ Bảo vệ TẤT CẢ /profile/xxx
        "/orders/*",       // ✅ Bảo vệ TẤT CẢ /orders/xxx
        "/checkout/*"      // ✅ Bảo vệ TẤT CẢ /checkout/xxx
})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔒 AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();

        // ✅ LOẠI TRỪ static resources (CSS, JS, images)
        if (requestURI.endsWith(".css") ||
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

        System.out.println("🔒 AuthFilter checking: " + requestURI);

        // Kiểm tra đã đăng nhập chưa
        if (!SessionUtil.isAuthenticated(req)) {
            System.out.println("❌ Not authenticated - redirecting to login");
            // Chưa đăng nhập → redirect về trang login
            SessionUtil.setErrorMessage(req, "Vui lòng đăng nhập để tiếp tục!");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        System.out.println("✅ Authenticated - allowing access");
        // Đã đăng nhập → cho phép tiếp tục
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("🔒 AuthFilter destroyed");
    }
}