package viettech.controller;

import com.google.gson.Gson;
import viettech.dao.VoucherDAO;
import viettech.entity.user.User;
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/voucher/user-usage")
public class VoucherAPIServlet extends HttpServlet {

    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Check login
        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        try {
            int voucherId = Integer.parseInt(request.getParameter("voucherId"));
            int customerId = user.getUserId();

            long userUsageCount = voucherDAO.countUserUsage(voucherId, customerId);

            Map<String, Object> result = new HashMap<>();
            result.put("voucherId", voucherId);
            result.put("customerId", customerId);
            result.put("userUsageCount", userUsageCount);

            response.getWriter().write(gson.toJson(result));

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Internal server error\"}");
            e.printStackTrace();
        }
    }
}