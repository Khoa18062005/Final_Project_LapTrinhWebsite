package viettech.service;

import viettech.config.JPAConfig;
import viettech.dao.NotificationDAO;
import viettech.entity.Notification;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * Service lấy thông báo cho Shipper
 * - Kết hợp thông báo từ bảng notifications (đã nhận đơn, hoàn thành đơn)
 * - Và đơn hàng mới (Ready) từ bảng orders cho tất cả shipper
 */
public class ShipperNotificationApiService {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    /**
     * Lấy danh sách thông báo cho shipper
     * @param shipperId ID của shipper
     * @return Danh sách thông báo
     */
    public List<Map<String, Object>> getShipperNotifications(int shipperId) {
        List<Map<String, Object>> notifications = new ArrayList<>();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();

        try {
            // === 1. LẤY THÔNG BÁO TỪ BẢNG NOTIFICATIONS (đã nhận đơn, hoàn thành, v.v.) ===
            List<Notification> savedNotifications = notificationDAO.findByUserId(shipperId);

            if (savedNotifications != null) {
                for (Notification n : savedNotifications) {
                    // Chỉ lấy thông báo liên quan đến shipper
                    String type = n.getType();
                    if (type != null && (type.contains("shipper") || type.contains("DELIVERY") || type.equals("order"))) {
                        Map<String, Object> notif = new HashMap<>();
                        notif.put("id", "notif_" + n.getNotificationId());

                        // Map type
                        if (type.contains("accepted") || type.equals("shipper_accepted")) {
                            notif.put("type", "ACCEPTED");
                        } else if (type.contains("completed") || type.equals("shipper_completed")) {
                            notif.put("type", "COMPLETED");
                        } else if (type.contains("order") || type.equals("shipper_order") || type.equals("DELIVERY_READY")) {
                            notif.put("type", "NEW_ORDER");
                        } else {
                            notif.put("type", "SYSTEM");
                        }

                        notif.put("title", n.getTitle());
                        notif.put("message", n.getMessage());
                        notif.put("createdAt", n.getCreatedAt());
                        notif.put("read", n.isRead());
                        notif.put("actionUrl", n.getActionUrl());

                        notifications.add(notif);
                    }
                }
            }

            // === 2. LẤY ĐƠN HÀNG MỚI TRÊN SÀN (status = Ready) ===
            // Hiển thị cho tất cả shipper để họ biết có đơn mới
            String sqlNewOrders =
                "SELECT o.order_id, o.order_number, o.total_price, o.shipping_fee, o.order_date, " +
                "       CONCAT(COALESCE(a.street, ''), ', ', COALESCE(a.district, ''), ', ', COALESCE(a.city, '')) as address, " +
                "       CONCAT(COALESCE(u.last_name, ''), ' ', COALESCE(u.first_name, '')) as customer_name " +
                "FROM orders o " +
                "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                "LEFT JOIN users u ON o.customer_id = u.user_id " +
                "WHERE LOWER(o.status) = 'ready' " +
                "ORDER BY o.order_date DESC " +
                "LIMIT 10";

            Query queryNew = em.createNativeQuery(sqlNewOrders);
            @SuppressWarnings("unchecked")
            List<Object[]> newOrders = queryNew.getResultList();

            for (Object[] row : newOrders) {
                Map<String, Object> notif = new HashMap<>();
                notif.put("id", "ready_" + row[0]);
                notif.put("type", "NEW_ORDER");
                notif.put("title", "🆕 Đơn hàng mới trên sàn!");

                String orderNumber = row[1] != null ? row[1].toString() : "N/A";
                Double shippingFee = row[3] != null ? ((Number) row[3]).doubleValue() : 0;
                String address = row[5] != null ? row[5].toString() : "Chưa có địa chỉ";

                notif.put("message", String.format("Đơn #%s - Phí ship: %,.0f₫ - Địa chỉ: %s",
                        orderNumber, shippingFee, address));
                notif.put("createdAt", row[4]);
                notif.put("read", false); // Đơn mới luôn là chưa đọc
                notif.put("status", "Ready");
                notif.put("orderNumber", orderNumber);
                notif.put("orderId", row[0]);
                notif.put("actionUrl", "/shipper#orders");

                notifications.add(notif);
            }

            // === 3. SẮP XẾP THEO THỜI GIAN MỚI NHẤT ===
            notifications.sort((a, b) -> {
                Date dateA = (Date) a.get("createdAt");
                Date dateB = (Date) b.get("createdAt");
                if (dateA == null && dateB == null) return 0;
                if (dateA == null) return 1;
                if (dateB == null) return -1;
                return dateB.compareTo(dateA);
            });

            // Giới hạn 20 thông báo
            if (notifications.size() > 20) {
                notifications = new ArrayList<>(notifications.subList(0, 20));
            }

        } catch (Exception e) {
            System.out.println("❌ Error getting shipper notifications: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return notifications;
    }

    /**
     * Đếm số thông báo chưa đọc + đơn hàng Ready
     */
    public int countUnread(int shipperId) {
        int count = 0;
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();

        try {
            // Đếm thông báo chưa đọc từ bảng notifications
            long notifCount = notificationDAO.countUnreadByUserId(shipperId);

            // Đếm số đơn hàng Ready (mới trên sàn)
            String sql = "SELECT COUNT(*) FROM orders WHERE LOWER(status) = 'ready'";
            Query query = em.createNativeQuery(sql);
            Object result = query.getSingleResult();
            int readyCount = ((Number) result).intValue();

            count = (int) notifCount + readyCount;

        } catch (Exception e) {
            System.out.println("❌ Error counting unread: " + e.getMessage());
        } finally {
            em.close();
        }

        return count;
    }

    /**
     * Đếm số đơn hàng mới (Ready) - dùng làm badge
     */
    public int countNewOrders() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String sql = "SELECT COUNT(*) FROM orders WHERE LOWER(status) = 'ready'";
            Query query = em.createNativeQuery(sql);
            Object result = query.getSingleResult();
            return ((Number) result).intValue();
        } catch (Exception e) {
            System.out.println("❌ Error counting new orders: " + e.getMessage());
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Đánh dấu thông báo đã đọc
     * @param notificationId ID thông báo (có thể là "notif_123" hoặc "ready_456")
     * @param shipperId ID shipper
     * @return true nếu thành công
     */
    public boolean markAsRead(String notificationId, int shipperId) {
        try {
            if (notificationId == null || notificationId.isEmpty()) {
                return false;
            }

            // Nếu là thông báo từ bảng notifications
            if (notificationId.startsWith("notif_")) {
                String idStr = notificationId.replace("notif_", "");
                int id = Integer.parseInt(idStr);
                notificationDAO.markAsRead(id);
                System.out.println("✓ Marked notification " + id + " as read");
                return true;
            }

            // Nếu là thông báo đơn Ready - không cần làm gì vì đơn này sẽ biến mất khi được nhận
            if (notificationId.startsWith("ready_")) {
                System.out.println("✓ Ready order notification clicked: " + notificationId);
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("❌ Error marking as read: " + e.getMessage());
            return false;
        }
    }

    /**
     * Đánh dấu tất cả thông báo của shipper đã đọc
     */
    public boolean markAllAsRead(int shipperId) {
        try {
            notificationDAO.markAllAsReadByUserId(shipperId);
            System.out.println("✓ Marked all notifications as read for shipper: " + shipperId);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error marking all as read: " + e.getMessage());
            return false;
        }
    }
}
