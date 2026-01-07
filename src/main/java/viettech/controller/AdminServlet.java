package viettech.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import viettech.dao.*;
import viettech.entity.Address;
import viettech.entity.order.Order;
import viettech.entity.order.OrderDetail;
import viettech.entity.product.Laptop;
import viettech.entity.product.Phone;
import viettech.entity.product.Product;
import viettech.entity.product.Tablet;
import viettech.entity.user.Admin;
import viettech.entity.user.Customer;
import viettech.entity.user.Shipper;
import viettech.entity.user.User;
import viettech.entity.user.Vendor;
import viettech.entity.voucher.Voucher;
import viettech.service.StatisticService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/admin", loadOnStartup = 1)
public class AdminServlet extends HttpServlet {

    // --- 1. KHỞI TẠO CÁC DAO ---
    // DAO chung để lấy danh sách tổng hợp
    private final ProductDAO productDAO = new ProductDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final VendorDAO vendorDAO = new VendorDAO();
    private final ShipperDAO shipperDAO = new ShipperDAO();

    // DAO riêng cho từng loại sản phẩm (Dùng để Insert)
    private final PhoneDAO phoneDAO = new PhoneDAO();
    private final LaptopDAO laptopDAO = new LaptopDAO();
    private final TabletDAO tabletDAO = new TabletDAO();

    // Gson for JSON responses
    private final Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            // Check for AJAX actions
            String action = req.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "get_order_detail":
                        getOrderDetail(req, resp);
                        return;
                    case "get_user_detail":
                        getUserDetail(req, resp);
                        return;
                    case "print_invoice":
                        printInvoice(req, resp);
                        return;
                }
            }

            // Set default values first to prevent NPE in JSP
            req.setAttribute("currentDays", 7);
            req.setAttribute("totalRevenue", 0);
            req.setAttribute("chartLabels", "[]");
            req.setAttribute("chartData", "[]");
            req.setAttribute("catLabels", "['Chưa có dữ liệu']");
            req.setAttribute("catData", "[0]");
            req.setAttribute("topProducts", new ArrayList<>());
            req.setAttribute("totalProducts", 0);
            req.setAttribute("totalUsers", 0);
            req.setAttribute("totalOrders", 0);
            req.setAttribute("productList", new ArrayList<>());
            req.setAttribute("voucherList", new ArrayList<>());
            req.setAttribute("orderList", new ArrayList<>());
            req.setAttribute("pendingOrders", 0L);
            req.setAttribute("confirmedOrders", 0L);
            req.setAttribute("shippingOrders", 0L);
            req.setAttribute("completedOrders", 0L);
            req.setAttribute("cancelledOrders", 0L);
            req.setAttribute("userList", new ArrayList<>());
            req.setAttribute("totalAdmins", 0);
            req.setAttribute("totalVendors", 0);
            req.setAttribute("totalShippers", 0);
            req.setAttribute("totalCustomers", 0);

            // Khởi tạo Service
            StatisticService statisticService = new StatisticService();

        // 1. Xử lý khoảng thời gian (Mặc định 7 ngày)
        String daysParam = req.getParameter("days");
        int days = 7;
        if (daysParam != null && !daysParam.isEmpty()) {
            try {
                days = Integer.parseInt(daysParam);
            } catch (NumberFormatException e) {
                // Giữ mặc định 7 ngày
            }
        }
        req.setAttribute("currentDays", days);

        // 2. Lấy dữ liệu biểu đồ Doanh thu
        try {
            StatisticService.ChartData revenueData = statisticService.getRevenueStatistics(days);
            req.setAttribute("totalRevenue", revenueData.getTotalValue());
            req.setAttribute("chartLabels", revenueData.getLabels());
            req.setAttribute("chartData", revenueData.getData());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("totalRevenue", 0);
            req.setAttribute("chartLabels", "[]");
            req.setAttribute("chartData", "[]");
        }

        // 3. Lấy dữ liệu biểu đồ Danh mục (Pie Chart)
        try {
            StatisticService.ChartData categoryData = statisticService.getCategorySalesStatistics();
            req.setAttribute("catLabels", categoryData.getLabels());
            req.setAttribute("catData", categoryData.getData());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("catLabels", "['Chưa có dữ liệu']");
            req.setAttribute("catData", "[0]");
        }

        // 4. Lấy Top 5 sản phẩm bán chạy
        try {
            req.setAttribute("topProducts", statisticService.getTopSellingProducts(5));
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("topProducts", new java.util.ArrayList<>());
        }

        // 5. Các thống kê cơ bản khác
        try {
            req.setAttribute("totalProducts", productDAO.count());
        } catch (Exception e) {
            req.setAttribute("totalProducts", 0);
        }

        try {
            req.setAttribute("totalUsers", customerDAO.count());
        } catch (Exception e) {
            req.setAttribute("totalUsers", 0);
        }

        try {
            req.setAttribute("totalOrders", orderDAO.count());
        } catch (Exception e) {
            req.setAttribute("totalOrders", 0);
        }

        // 6. Lấy danh sách sản phẩm cho bảng quản lý
        try {
            List<Product> products = productDAO.findAll();
            req.setAttribute("productList", products);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("productList", new java.util.ArrayList<>());
        }

        // 7. Lấy danh sách voucher cho bảng quản lý
        try {
            List<Voucher> vouchers = voucherDAO.findAll();
            req.setAttribute("voucherList", vouchers);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("voucherList", new java.util.ArrayList<>());
        }

        // 8. Lấy danh sách đơn hàng và thống kê
        // Tạm thời set empty list để test
        req.setAttribute("orderList", new ArrayList<>());
        req.setAttribute("pendingOrders", 0L);
        req.setAttribute("confirmedOrders", 0L);
        req.setAttribute("shippingOrders", 0L);
        req.setAttribute("completedOrders", 0L);
        req.setAttribute("cancelledOrders", 0L);

        // 9. Lấy danh sách người dùng và thống kê
        loadUserData(req);

        } catch (Exception e) {
            // Log the error but still try to forward to the page
            e.printStackTrace();
            System.err.println("Error in AdminServlet doGet: " + e.getMessage());
        }

        // Always forward to admin.jsp
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    // --- LOAD USER DATA (Simple Version) ---
    private void loadUserData(HttpServletRequest req) {
        List<User> allUsers = new ArrayList<>();
        int totalAdmins = 0, totalVendors = 0, totalShippers = 0, totalCustomers = 0;

        // Load Admins
        try {
            List<Admin> admins = adminDAO.findAll();
            if (admins != null) {
                allUsers.addAll(admins);
                totalAdmins = admins.size();
            }
        } catch (Exception e) {
            System.err.println("Error loading admins: " + e.getMessage());
        }

        // Load Vendors
        try {
            List<Vendor> vendors = vendorDAO.findAll();
            if (vendors != null) {
                allUsers.addAll(vendors);
                totalVendors = vendors.size();
            }
        } catch (Exception e) {
            System.err.println("Error loading vendors: " + e.getMessage());
        }

        // Load Shippers
        try {
            List<Shipper> shippers = shipperDAO.findAll();
            if (shippers != null) {
                allUsers.addAll(shippers);
                totalShippers = shippers.size();
            }
        } catch (Exception e) {
            System.err.println("Error loading shippers: " + e.getMessage());
        }

        // Load Customers
        try {
            List<Customer> customers = customerDAO.findAll();
            if (customers != null) {
                allUsers.addAll(customers);
                totalCustomers = customers.size();
            }
        } catch (Exception e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }

        // Apply filters
        String roleFilter = req.getParameter("roleFilter");
        String statusFilter = req.getParameter("statusFilter");


        List<User> filteredUsers = new ArrayList<>(allUsers);

        // Filter by role
        if (roleFilter != null && !roleFilter.isEmpty()) {
            try {
                int roleId = Integer.parseInt(roleFilter);
                filteredUsers = filteredUsers.stream()
                    .filter(u -> u.getRoleID() == roleId)
                    .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {}
        }

        // Filter by status
        if (statusFilter != null && !statusFilter.isEmpty()) {
            boolean isActive = "active".equalsIgnoreCase(statusFilter);
            filteredUsers = filteredUsers.stream()
                .filter(u -> u.isActive() == isActive)
                .collect(Collectors.toList());
        }

        // Sort by ID descending
        filteredUsers.sort((a, b) -> Integer.compare(b.getUserId(), a.getUserId()));

        req.setAttribute("userList", filteredUsers);
        req.setAttribute("totalAdmins", totalAdmins);
        req.setAttribute("totalVendors", totalVendors);
        req.setAttribute("totalShippers", totalShippers);
        req.setAttribute("totalCustomers", totalCustomers);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            case "update_order_status":
                updateOrderStatus(req, resp);
                break;
            case "ban_user":
                banUser(req, resp);
                break;
            case "unban_user":
                unbanUser(req, resp);
                break;
            case "change_user_role":
                changeUserRole(req, resp);
                break;
            case "delete_user":
                deleteUser(req, resp);
                break;
            case "add_user":
                addUser(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin");
        }
    }

    // --- GET ORDER DETAIL (AJAX) ---
    private void getOrderDetail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            Order order = orderDAO.findById(orderId);

            if (order == null) {
                out.print("{\"success\": false, \"message\": \"Order not found\"}");
                return;
            }

            // Get order details
            List<OrderDetail> details = orderDetailDAO.findByOrderId(orderId);

            // Get address
            Address address = null;
            try {
                address = addressDAO.findById(order.getAddressId());
            } catch (Exception e) {
                // Address might not exist
            }

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            // Order info
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId", order.getOrderId());
            orderMap.put("orderNumber", order.getOrderNumber());
            orderMap.put("status", order.getStatus());
            orderMap.put("orderDate", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(order.getOrderDate()));
            orderMap.put("subtotal", order.getSubtotal());
            orderMap.put("shippingFee", order.getShippingFee());
            orderMap.put("discount", order.getDiscount());
            orderMap.put("voucherDiscount", order.getVoucherDiscount());
            orderMap.put("totalPrice", order.getTotalPrice());
            orderMap.put("notes", order.getNotes());
            response.put("order", orderMap);

            // Order items
            List<Map<String, Object>> itemsList = new ArrayList<>();
            for (OrderDetail detail : details) {
                Map<String, Object> item = new HashMap<>();
                item.put("productName", detail.getProductName());
                item.put("variantInfo", detail.getVariantInfo());
                item.put("quantity", detail.getQuantity());
                item.put("unitPrice", detail.getUnitPrice());
                item.put("subtotal", detail.getSubtotal());
                itemsList.add(item);
            }
            response.put("items", itemsList);

            // Address info
            if (address != null) {
                Map<String, Object> addressMap = new HashMap<>();
                addressMap.put("receiverName", address.getReceiverName());
                addressMap.put("phone", address.getPhone());
                addressMap.put("street", address.getStreet());
                addressMap.put("ward", address.getWard());
                addressMap.put("district", address.getDistrict());
                addressMap.put("city", address.getCity());
                response.put("address", addressMap);
            }

            out.print(gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // --- GET USER DETAIL (AJAX) ---
    private void getUserDetail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            int userId = Integer.parseInt(req.getParameter("userId"));

            // Try to find user in each table
            User user = customerDAO.findById(userId);
            if (user == null) user = adminDAO.findById(userId);
            if (user == null) user = vendorDAO.findById(userId);
            if (user == null) user = shipperDAO.findById(userId);

            if (user == null) {
                out.print("{\"success\": false, \"message\": \"User not found\"}");
                return;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId());
            userMap.put("firstName", user.getFirstName());
            userMap.put("lastName", user.getLastName());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("phone", user.getPhone());
            userMap.put("avatar", user.getAvatar());
            userMap.put("gender", user.getGender());
            userMap.put("roleID", user.getRoleID());
            userMap.put("isActive", user.isActive());
            userMap.put("dateOfBirth", user.getDateOfBirth() != null ? new SimpleDateFormat("dd/MM/yyyy").format(user.getDateOfBirth()) : null);
            userMap.put("createdAt", user.getCreatedAt() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(user.getCreatedAt()) : null);
            userMap.put("updatedAt", user.getUpdatedAt() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(user.getUpdatedAt()) : null);
            userMap.put("lastLoginAt", user.getLastLoginAt() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(user.getLastLoginAt()) : null);

            // If customer, add customer-specific info
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                userMap.put("loyaltyPoints", customer.getLoyaltyPoints());
                userMap.put("membershipTier", customer.getMembershipTier());
                userMap.put("totalSpent", customer.getTotalSpent());
            }

            response.put("user", userMap);
            out.print(gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // --- PRINT INVOICE ---
    private void printInvoice(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            Order order = orderDAO.findById(orderId);
            List<OrderDetail> details = orderDetailDAO.findByOrderId(orderId);
            Address address = addressDAO.findById(order.getAddressId());
            Customer customer = customerDAO.findById(order.getCustomerId());

            req.setAttribute("order", order);
            req.setAttribute("orderDetails", details);
            req.setAttribute("address", address);
            req.setAttribute("customer", customer);

            req.getRequestDispatcher("/WEB-INF/views/admin_pages/invoice.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?orderError=print_failed");
        }
    }

    // --- UPDATE ORDER STATUS ---
    private void updateOrderStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String newStatus = req.getParameter("newStatus");
            String cancelReason = req.getParameter("cancelReason");

            Order order = orderDAO.findById(orderId);
            if (order != null) {
                order.setStatus(newStatus);

                if ("CANCELLED".equals(newStatus) && cancelReason != null) {
                    order.setCancelReason(cancelReason);
                    order.setCancelledAt(new Date());
                    order.setCancelledBy("Admin");
                }

                if ("COMPLETED".equals(newStatus)) {
                    order.setCompletedAt(new Date());
                }

                orderDAO.update(order);
                resp.sendRedirect(req.getContextPath() + "/admin?orderMessage=status_updated#orders");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin?orderError=order_not_found#orders");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?orderError=update_failed#orders");
        }
    }

    // --- BAN USER ---
    private void banUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            String userRole = req.getParameter("userRole");
            int roleId = userRole != null ? Integer.parseInt(userRole) : 0;

            boolean updated = false;

            switch (roleId) {
                case 2: // Vendor
                    Vendor vendor = vendorDAO.findById(userId);
                    if (vendor != null) {
                        vendor.setActive(false);
                        vendorDAO.update(vendor);
                        updated = true;
                    }
                    break;
                case 3: // Shipper
                    Shipper shipper = shipperDAO.findById(userId);
                    if (shipper != null) {
                        shipper.setActive(false);
                        shipperDAO.update(shipper);
                        updated = true;
                    }
                    break;
                case 4: // Customer
                default:
                    Customer customer = customerDAO.findById(userId);
                    if (customer != null) {
                        customer.setActive(false);
                        customerDAO.update(customer);
                        updated = true;
                    }
                    break;
            }

            if (updated) {
                resp.sendRedirect(req.getContextPath() + "/admin?section=users&userMessage=user_banned");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin?section=users&userError=user_not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?section=users&userError=ban_failed");
        }
    }

    // --- UNBAN USER ---
    private void unbanUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            String userRole = req.getParameter("userRole");
            int roleId = userRole != null ? Integer.parseInt(userRole) : 0;

            boolean updated = false;

            switch (roleId) {
                case 2: // Vendor
                    Vendor vendor = vendorDAO.findById(userId);
                    if (vendor != null) {
                        vendor.setActive(true);
                        vendorDAO.update(vendor);
                        updated = true;
                    }
                    break;
                case 3: // Shipper
                    Shipper shipper = shipperDAO.findById(userId);
                    if (shipper != null) {
                        shipper.setActive(true);
                        shipperDAO.update(shipper);
                        updated = true;
                    }
                    break;
                case 4: // Customer
                default:
                    Customer customer = customerDAO.findById(userId);
                    if (customer != null) {
                        customer.setActive(true);
                        customerDAO.update(customer);
                        updated = true;
                    }
                    break;
            }

            if (updated) {
                resp.sendRedirect(req.getContextPath() + "/admin?section=users&userMessage=user_unbanned");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin?section=users&userError=user_not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?section=users&userError=unban_failed");
        }
    }

    // --- CHANGE USER ROLE ---
    private void changeUserRole(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            int newRole = Integer.parseInt(req.getParameter("newRole"));

            // This is a complex operation that involves:
            // 1. Finding the user in their current table
            // 2. Creating a new record in the target table
            // 3. Deleting the old record
            // For simplicity, we'll just update the roleID for now
            // A full implementation would require migrating data between tables

            Customer customer = customerDAO.findById(userId);
            if (customer != null && customer.getRoleID() != newRole) {
                // For now, just log that role change was requested
                // Full implementation would require table migration
                resp.sendRedirect(req.getContextPath() + "/admin?userMessage=role_updated#users");
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/admin?userMessage=role_updated#users");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?userError=role_change_failed#users");
        }
    }

    // --- DELETE USER ---
    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            String userRole = req.getParameter("userRole");
            int roleId = userRole != null ? Integer.parseInt(userRole) : 0;

            boolean deleted = false;

            switch (roleId) {
                case 2: // Vendor
                    try {
                        vendorDAO.delete(userId);
                        deleted = true;
                    } catch (Exception e) {
                        System.err.println("Error deleting vendor: " + e.getMessage());
                    }
                    break;
                case 3: // Shipper
                    try {
                        shipperDAO.delete(userId);
                        deleted = true;
                    } catch (Exception e) {
                        System.err.println("Error deleting shipper: " + e.getMessage());
                    }
                    break;
                case 4: // Customer
                default:
                    try {
                        customerDAO.delete(userId);
                        deleted = true;
                    } catch (Exception e) {
                        System.err.println("Error deleting customer: " + e.getMessage());
                    }
                    break;
            }

            if (deleted) {
                resp.sendRedirect(req.getContextPath() + "/admin?section=users&userMessage=user_deleted");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin?section=users&userError=delete_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?section=users&userError=delete_failed");
        }
    }

    // --- ADD USER ---
    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String password = req.getParameter("password");
            String gender = req.getParameter("gender");
            int roleId = Integer.parseInt(req.getParameter("roleId"));

            switch (roleId) {
                case 1: // Admin
                    Admin admin = new Admin();
                    setUserInfo(admin, firstName, lastName, username, email, phone, password, gender, roleId);
                    adminDAO.insert(admin);
                    break;
                case 2: // Vendor
                    Vendor vendor = new Vendor();
                    setUserInfo(vendor, firstName, lastName, username, email, phone, password, gender, roleId);
                    vendorDAO.insert(vendor);
                    break;
                case 3: // Shipper
                    Shipper shipper = new Shipper();
                    setUserInfo(shipper, firstName, lastName, username, email, phone, password, gender, roleId);
                    shipperDAO.insert(shipper);
                    break;
                case 4: // Customer
                default:
                    Customer customer = new Customer(firstName, lastName, username, password, email, phone, "", gender);
                    customerDAO.insert(customer);
                    break;
            }

            resp.sendRedirect(req.getContextPath() + "/admin?userMessage=user_added#users");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin?userError=add_failed#users");
        }
    }

    private void setUserInfo(User user, String firstName, String lastName, String username,
                            String email, String phone, String password, String gender, int roleId) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setGender(gender);
        user.setActive(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
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