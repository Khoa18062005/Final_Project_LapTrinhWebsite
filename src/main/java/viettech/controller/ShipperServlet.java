package viettech.controller;

import viettech.dto.Shipper_dto;
import viettech.entity.user.User;
import viettech.service.ShipperService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig; // <-- Bắt buộc để nhận file
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; // Import Part, HttpServletRequest, ...
import java.io.IOException;

@WebServlet("/shipper")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB (Lưu bộ nhớ đệm)
        maxFileSize = 1024 * 1024 * 10,      // 10MB (Kích thước file tối đa)
        maxRequestSize = 1024 * 1024 * 50    // 50MB (Tổng kích thước request)
)
public class ShipperServlet extends HttpServlet {

    private final ShipperService service = new ShipperService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy Session hiện tại
        HttpSession session = request.getSession(false);

        // 2. Kiểm tra đăng nhập
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 3. Kiểm tra quyền (Shipper = Role 3)
        if (user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 4. Lấy dữ liệu Dashboard
        int shipperId = user.getUserId();
        Shipper_dto data = service.getDashboardData(shipperId);

        // 5. Xử lý trường hợp data rỗng
        if (data == null) {
            data = new Shipper_dto();
        }

        // 6. Đẩy dữ liệu và chuyển hướng
        request.setAttribute("data", data);
        request.getRequestDispatcher("/WEB-INF/views/shipper.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        // --- CASE 1: XỬ LÝ ĐƠN HÀNG (Nhận / Hoàn thành) ---
        if ("accept".equals(action) || "complete".equals(action)) {
            String idStr = request.getParameter("id");
            String orderIdStr = request.getParameter("orderId");

            try {
                if (orderIdStr != null && !orderIdStr.isEmpty()) {
                    // Accept by orderId - from notification
                    int orderId = Integer.parseInt(orderIdStr);
                    boolean success = acceptOrderByOrderId(orderId, user.getUserId());

                    if (isAjax) {
                        sendJsonResponse(response, success,
                            success ? "Đã nhận đơn hàng thành công" : "Không thể nhận đơn hàng", null);
                        return;
                    }
                } else if (idStr != null && !idStr.isEmpty()) {
                    // Accept by assignmentId - from order list
                    int assignmentId = Integer.parseInt(idStr);
                    service.updateStatus(assignmentId, action);

                    if (isAjax) {
                        sendJsonResponse(response, true, "Thành công", null);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (isAjax) {
                    sendJsonResponse(response, false, "ID không hợp lệ", null);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (isAjax) {
                    sendJsonResponse(response, false, "Có lỗi xảy ra: " + e.getMessage(), null);
                    return;
                }
            }
        }

        // --- CASE 2: CẬP NHẬT PROFILE (BAO GỒM AVATAR, BIỂN SỐ, GPLX) ---
        else if ("updateProfile".equals(action)) {
            try {
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String phone = request.getParameter("phone");
                String password = request.getParameter("password");
                String vehiclePlate = request.getParameter("vehiclePlate");
                String licenseNumber = request.getParameter("licenseNumber");

                // --- XỬ LÝ ẢNH ĐẠI DIỆN ---
                String avatarUrl = user.getAvatar(); // Mặc định giữ ảnh cũ

                // 1. Kiểm tra cờ xóa ảnh (từ JS gửi lên)
                String deleteAvatar = request.getParameter("deleteAvatar");
                if ("true".equals(deleteAvatar)) {
                    avatarUrl = null; // Xóa ảnh trong DB (hoặc set link ảnh default)
                } else {
                    // 2. Upload ảnh mới (Sử dụng CloudinaryUtil vừa sửa)
                    Part filePart = request.getPart("avatarFile");

                    // Hàm uploadAvatar đã tự kiểm tra null và size, nên gọi thẳng
                    String uploadedUrl = viettech.util.CloudinaryUtil.uploadAvatar(filePart);

                    if (uploadedUrl != null) {
                        avatarUrl = uploadedUrl;
                        System.out.println("DEBUG: Đã upload ảnh mới lên Cloudinary: " + avatarUrl);
                    }
                }

                // Gọi Service cập nhật xuống Database (Thêm tham số avatarUrl)
                service.updateProfile(
                        user.getUserId(),
                        firstName,
                        lastName,
                        phone,
                        password,
                        vehiclePlate,
                        licenseNumber,
                        avatarUrl
                );

                // Cập nhật lại Session để giao diện hiển thị thông tin mới ngay lập tức
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                if (avatarUrl != null) {
                    user.setAvatar(avatarUrl);
                }

                session.setAttribute("user", user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Redirect để tránh lỗi resubmission khi F5
        response.sendRedirect(request.getContextPath() + "/shipper");
    }

    /**
     * Accept order by orderId - creates delivery assignment and updates order status
     */
    private boolean acceptOrderByOrderId(int orderId, int shipperId) {
        try {
            // Use VendorService's assignOrder logic or create a new one in ShipperService
            viettech.config.JPAConfig.getEntityManager();
            javax.persistence.EntityManager em = viettech.config.JPAConfig.getEntityManager();
            javax.persistence.EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                // Check if order exists and is in READY status
                viettech.entity.order.Order order = em.find(viettech.entity.order.Order.class, orderId);
                if (order == null) {
                    return false;
                }

                String status = order.getStatus() != null ? order.getStatus().trim().toUpperCase() : "";
                if (!"READY".equals(status)) {
                    System.out.println("Order " + orderId + " is not in READY status, current: " + status);
                    return false;
                }

                // Update order status to SHIPPING
                order.setStatus("SHIPPING");
                em.merge(order);

                // Create or update delivery assignment
                // First check if delivery exists
                String checkDelivery = "SELECT d FROM viettech.entity.delivery.Delivery d WHERE d.orderId = :orderId";
                java.util.List<viettech.entity.delivery.Delivery> deliveries = em.createQuery(checkDelivery, viettech.entity.delivery.Delivery.class)
                        .setParameter("orderId", orderId)
                        .getResultList();

                viettech.entity.delivery.Delivery delivery;
                if (deliveries.isEmpty()) {
                    // Create new delivery
                    delivery = new viettech.entity.delivery.Delivery();
                    delivery.setOrderId(orderId);
                    delivery.setStatus("In Transit");
                    em.persist(delivery);
                    em.flush(); // Get ID
                } else {
                    delivery = deliveries.get(0);
                    delivery.setStatus("In Transit");
                    em.merge(delivery);
                }

                // Check if assignment exists
                String checkAssignment = "SELECT da FROM viettech.entity.delivery.DeliveryAssignment da WHERE da.deliveryId = :deliveryId";
                java.util.List<viettech.entity.delivery.DeliveryAssignment> assignments = em.createQuery(checkAssignment, viettech.entity.delivery.DeliveryAssignment.class)
                        .setParameter("deliveryId", delivery.getDeliveryId())
                        .getResultList();

                if (assignments.isEmpty()) {
                    // Create new assignment
                    viettech.entity.delivery.DeliveryAssignment assignment = new viettech.entity.delivery.DeliveryAssignment();
                    assignment.setDeliveryId(delivery.getDeliveryId());
                    assignment.setShipperId(shipperId);
                    assignment.setStatus("Accepted");
                    assignment.setAssignedAt(new java.util.Date());
                    assignment.setAcceptedAt(new java.util.Date());
                    em.persist(assignment);
                } else {
                    // Update existing assignment
                    viettech.entity.delivery.DeliveryAssignment assignment = assignments.get(0);
                    assignment.setShipperId(shipperId);
                    assignment.setStatus("Accepted");
                    assignment.setAcceptedAt(new java.util.Date());
                    em.merge(assignment);
                }

                tx.commit();

                // Send notification to vendor
                try {
                    viettech.service.NotificationService notificationService = new viettech.service.NotificationService();
                    viettech.entity.Notification vendorNotif = new viettech.entity.Notification();
                    vendorNotif.setUserId(order.getVendorId());
                    vendorNotif.setType("ORDER_SHIPPING");
                    vendorNotif.setTitle("Shipper đã nhận đơn hàng");
                    vendorNotif.setMessage("Đơn hàng #" + order.getOrderNumber() + " đã được shipper nhận. Đang giao hàng.");
                    vendorNotif.setActionUrl("/vendor?action=shipping");
                    vendorNotif.setCreatedAt(new java.util.Date());
                    notificationService.createNotification(vendorNotif);
                } catch (Exception e) {
                    System.err.println("Failed to notify vendor: " + e.getMessage());
                }

                return true;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            } finally {
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send JSON response for AJAX calls
     */
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":").append(success).append(",");
        json.append("\"message\":\"").append(message != null ? message.replace("\"", "\\\"") : "").append("\"");
        if (data != null) {
            json.append(",\"data\":").append(new com.google.gson.Gson().toJson(data));
        }
        json.append("}");

        response.getWriter().write(json.toString());
    }
}