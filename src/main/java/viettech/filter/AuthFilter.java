package viettech.filter;

import viettech.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/profile", "/orders", "/checkout"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Kiểm tra đã đăng nhập chưa
        if (!SessionUtil.isAuthenticated(req)) {
            // Chưa đăng nhập → redirect về trang login
            SessionUtil.setErrorMessage(req, "Vui lòng đăng nhập để tiếp tục!");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Đã đăng nhập → cho phép tiếp tục
        chain.doFilter(request, response);
    }
}