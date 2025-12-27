package viettech.controller;

import viettech.dao.*;
import viettech.entity.product.Laptop;
import viettech.entity.product.Phone;
import viettech.entity.product.Product;
import viettech.entity.product.Tablet;
import viettech.entity.storage.Inventory; // Nếu cần dùng Inventory
import viettech.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@WebServlet(urlPatterns = "/admin", loadOnStartup = 1)
public class AdminServlet extends HttpServlet {

    // --- 1. KHỞI TẠO CÁC DAO ---
    // DAO chung để lấy danh sách tổng hợp
    private final ProductDAO productDAO = new ProductDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    // DAO riêng cho từng loại sản phẩm (Dùng để Insert)
    private final PhoneDAO phoneDAO = new PhoneDAO();
    private final LaptopDAO laptopDAO = new LaptopDAO();
    private final TabletDAO tabletDAO = new TabletDAO();

    // DAO kho hàng
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // Lấy thống kê
        req.setAttribute("totalProducts", productDAO.count());
        req.setAttribute("totalUsers", customerDAO.count());
        // req.setAttribute("totalOrders", orderDAO.count());

        // Lấy danh sách sản phẩm (Polymorphism: JPA tự lấy tất cả Phone/Laptop/Tablet)
        List<Product> products = productDAO.findAll();
        req.setAttribute("productList", products);

        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "add_product":
                addProduct(req, resp);
                break;
            case "delete_product":
                deleteProduct(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin");
                break;
        }
    }

    // --- XỬ LÝ THÊM SẢN PHẨM ---
    private void addProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 1. Lấy dữ liệu từ Form
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));
            String description = req.getParameter("description");
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            int stockQuantity = Integer.parseInt(req.getParameter("stock"));

            // 2. XỬ LÝ THEO TỪNG LOẠI DAO
            Product savedProduct = null; // Biến tạm để dùng cho Inventory nếu cần

            switch (categoryId) {
                case 1: // Điện thoại
                    Phone phone = new Phone();
                    setCommonProductInfo(phone, name, price, description, categoryId);
                    // Có thể set thêm thuộc tính riêng của Phone ở đây nếu form có
                    phoneDAO.insert(phone);
                    savedProduct = phone;
                    break;

                case 2: // Laptop
                    Laptop laptop = new Laptop();
                    setCommonProductInfo(laptop, name, price, description, categoryId);
                    laptopDAO.insert(laptop);
                    savedProduct = laptop;
                    break;

                case 3: // Tablet
                    Tablet tablet = new Tablet();
                    setCommonProductInfo(tablet, name, price, description, categoryId);
                    tabletDAO.insert(tablet);
                    savedProduct = tablet;
                    break;

                default:
                    // Nếu không thuộc 3 loại trên thì báo lỗi hoặc redirect
                    resp.sendRedirect(req.getContextPath() + "/admin?error=invalid_category");
                    return;
            }

            // 3. Xử lý tồn kho (Inventory) - Nếu bạn muốn kích hoạt tính năng này
            /*
            if (savedProduct != null && savedProduct.getProductId() > 0) {
                Inventory inventory = new Inventory();
                inventory.setProductId(savedProduct.getProductId());
                inventory.setQuantity(stockQuantity);
                inventory.setLastUpdated(Timestamp.from(Instant.now()));
                inventoryDAO.insert(inventory);
            }
            */

            resp.sendRedirect(req.getContextPath() + "/admin?message=added_success");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?error=add_failed");
        }
    }

    // --- Helper: Set các thuộc tính chung để đỡ viết lại code ---
    private void setCommonProductInfo(Product p, String name, double price, String desc, int catId) {
        p.setName(name);
        p.setBasePrice(price);
        p.setDescription(desc);
        p.setCategoryId(catId);
        p.setStatus("Active");
        p.setSlug(createSlug(name));
        p.setUpdatedAt(Timestamp.from(Instant.now()));
    }

    // --- XỬ LÝ XÓA SẢN PHẨM ---
    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            productDAO.delete(id); // Vẫn dùng ProductDAO để xóa vì ID là duy nhất
            resp.sendRedirect(req.getContextPath() + "/admin?message=delete_success");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?error=delete_failed");
        }
    }

    // Utility: Tạo slug
    private String createSlug(String name) {
        if (name == null) return "";
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                + "-" + System.currentTimeMillis();
    }
}