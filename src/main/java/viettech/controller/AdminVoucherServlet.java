package viettech.controller;
import com.google.gson.Gson;
import viettech.dao.VoucherDAO;
import viettech.entity.voucher.Voucher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/admin/voucher", "/admin/voucher/*"})
public class AdminVoucherServlet extends HttpServlet {

    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final Gson gson = new Gson();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if ("get".equals(action)) {
            // API: Lấy thông tin 1 voucher theo ID (cho modal edit)
            getVoucherById(req, resp);
        } else if ("list".equals(action)) {
            // API: Lấy danh sách tất cả voucher (JSON)
            getVoucherList(req, resp);
        } else {
            // Mặc định: Forward đến trang admin với danh sách voucher
            List<Voucher> vouchers = voucherDAO.findAll();
            req.setAttribute("voucherList", vouchers);
            req.setAttribute("totalVouchers", voucherDAO.count());
            req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "add":
                addVoucher(req, resp);
                break;
            case "update":
                updateVoucher(req, resp);
                break;
            case "delete":
                deleteVoucher(req, resp);
                break;
            case "toggle_status":
                toggleVoucherStatus(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin#voucher");
        }
    }

    // === API: Lấy thông tin voucher theo ID ===
    private void getVoucherById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Map<String, Object> result = new HashMap<>();

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Voucher voucher = voucherDAO.findById(id);

            if (voucher != null) {
                result.put("success", true);
                result.put("voucher", voucherToMap(voucher));
            } else {
                result.put("success", false);
                result.put("message", "Không tìm thấy voucher");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        out.print(gson.toJson(result));
        out.flush();
    }

    // === API: Lấy danh sách voucher ===
    private void getVoucherList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Map<String, Object> result = new HashMap<>();

        try {
            List<Voucher> vouchers = voucherDAO.findAll();
            result.put("success", true);
            result.put("vouchers", vouchers);
            result.put("total", vouchers.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        out.print(gson.toJson(result));
        out.flush();
    }

    // === THÊM VOUCHER ===
    private void addVoucher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();

        try {
            Voucher voucher = new Voucher();
            populateVoucherFromRequest(voucher, req);
            voucher.setUsageCount(0);

            voucherDAO.insert(voucher);

            result.put("success", true);
            result.put("message", "Thêm voucher thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        sendJsonResponse(resp, result);
    }

    // === CẬP NHẬT VOUCHER ===
    private void updateVoucher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();

        try {
            int id = Integer.parseInt(req.getParameter("voucherId"));
            Voucher voucher = voucherDAO.findById(id);

            if (voucher == null) {
                result.put("success", false);
                result.put("message", "Không tìm thấy voucher!");
            } else {
                populateVoucherFromRequest(voucher, req);
                voucherDAO.update(voucher);

                result.put("success", true);
                result.put("message", "Cập nhật voucher thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        sendJsonResponse(resp, result);
    }

    // === XÓA VOUCHER ===
    private void deleteVoucher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            voucherDAO.delete(id);

            result.put("success", true);
            result.put("message", "Xóa voucher thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        sendJsonResponse(resp, result);
    }

    // === BẬT/TẮT TRẠNG THÁI VOUCHER ===
    private void toggleVoucherStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Voucher voucher = voucherDAO.findById(id);

            if (voucher != null) {
                voucher.setActive(!voucher.isActive());
                voucherDAO.update(voucher);

                result.put("success", true);
                result.put("message", voucher.isActive() ? "Đã kích hoạt voucher!" : "Đã tắt voucher!");
                result.put("newStatus", voucher.isActive());
            } else {
                result.put("success", false);
                result.put("message", "Không tìm thấy voucher!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        sendJsonResponse(resp, result);
    }

    // === HELPER: Đọc dữ liệu từ request và set vào Voucher ===
    private void populateVoucherFromRequest(Voucher voucher, HttpServletRequest req) throws ParseException {
        voucher.setCode(req.getParameter("code"));
        voucher.setName(req.getParameter("name"));
        voucher.setDescription(req.getParameter("description"));
        voucher.setType(req.getParameter("type"));
        voucher.setScope(req.getParameter("scope") != null ? req.getParameter("scope") : "all");

        // Discount
        String discountPercentStr = req.getParameter("discountPercent");
        String discountAmountStr = req.getParameter("discountAmount");
        voucher.setDiscountPercent(discountPercentStr != null && !discountPercentStr.isEmpty()
                ? Double.parseDouble(discountPercentStr) : 0);
        voucher.setDiscountAmount(discountAmountStr != null && !discountAmountStr.isEmpty()
                ? Double.parseDouble(discountAmountStr) : 0);

        // Limits
        String maxDiscountStr = req.getParameter("maxDiscount");
        String minOrderStr = req.getParameter("minOrderValue");
        voucher.setMaxDiscount(maxDiscountStr != null && !maxDiscountStr.isEmpty()
                ? Double.parseDouble(maxDiscountStr) : 0);
        voucher.setMinOrderValue(minOrderStr != null && !minOrderStr.isEmpty()
                ? Double.parseDouble(minOrderStr) : 0);

        // Dates
        String startDateStr = req.getParameter("startDate");
        String expiryDateStr = req.getParameter("expiryDate");
        if (startDateStr != null && !startDateStr.isEmpty()) {
            voucher.setStartDate(dateFormat.parse(startDateStr));
        }
        if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
            voucher.setExpiryDate(dateFormat.parse(expiryDateStr));
        }

        // Usage limits
        String usageLimitStr = req.getParameter("usageLimit");
        String usageLimitPerUserStr = req.getParameter("usageLimitPerUser");
        voucher.setUsageLimit(usageLimitStr != null && !usageLimitStr.isEmpty()
                ? Integer.parseInt(usageLimitStr) : 100);
        voucher.setUsageLimitPerUser(usageLimitPerUserStr != null && !usageLimitPerUserStr.isEmpty()
                ? Integer.parseInt(usageLimitPerUserStr) : 1);

        // Booleans
        voucher.setActive("on".equals(req.getParameter("isActive")) || "true".equals(req.getParameter("isActive")));
        voucher.setPublic("on".equals(req.getParameter("isPublic")) || "true".equals(req.getParameter("isPublic")));

        // Created by (từ session nếu có)
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            voucher.setCreatedBy("Admin");
        }
    }

    // === HELPER: Chuyển Voucher thành Map (cho JSON) ===
    private Map<String, Object> voucherToMap(Voucher v) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        map.put("voucherId", v.getVoucherId());
        map.put("code", v.getCode());
        map.put("name", v.getName());
        map.put("description", v.getDescription());
        map.put("type", v.getType());
        map.put("scope", v.getScope());
        map.put("discountPercent", v.getDiscountPercent());
        map.put("discountAmount", v.getDiscountAmount());
        map.put("maxDiscount", v.getMaxDiscount());
        map.put("minOrderValue", v.getMinOrderValue());
        map.put("startDate", v.getStartDate() != null ? isoFormat.format(v.getStartDate()) : "");
        map.put("expiryDate", v.getExpiryDate() != null ? isoFormat.format(v.getExpiryDate()) : "");
        map.put("usageLimit", v.getUsageLimit());
        map.put("usageLimitPerUser", v.getUsageLimitPerUser());
        map.put("usageCount", v.getUsageCount());
        map.put("isActive", v.isActive());
        map.put("isPublic", v.isPublic());

        return map;
    }

    // === HELPER: Gửi JSON response ===
    private void sendJsonResponse(HttpServletResponse resp, Map<String, Object> result) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(result));
        out.flush();
    }
}

