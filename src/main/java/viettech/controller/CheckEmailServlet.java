package viettech.controller;

import com.google.gson.Gson;
import viettech.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/check-email")
public class CheckEmailServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String email = req.getParameter("email");
        
        Map<String, Object> response = new HashMap<>();
        
        if (email == null || email.trim().isEmpty()) {
            response.put("exists", false);
            response.put("message", "");
        } else {
            // Kiểm tra email có tồn tại không
            boolean emailExists = userService.isEmailExist(email);
            
            response.put("exists", emailExists);
            if (emailExists) {
                response.put("message", "Email đã được sử dụng. Vui lòng đăng nhập.");
            } else {
                response.put("message", "Email khả dụng");
            }
        }

        resp.getWriter().write(gson.toJson(response));
    }
}