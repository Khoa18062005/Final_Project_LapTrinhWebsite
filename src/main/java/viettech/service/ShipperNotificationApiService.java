package viettech.service;

import viettech.config.JPAConfig;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * Service lấy thông báo cho Shipper dựa trên trạng thái đơn hàng
 * - Đơn mới (Ready): Hiển thị cho tất cả shipper
 * - Đã nhận (In Transit): Chỉ hiển thị cho shipper đã nhận
 * - Đã giao (Completed): Chỉ hiển thị cho shipper đã giao
 */
public class ShipperNotificationApiService {

    /**
     * Lấy danh sách thông báo cho shipper
     * @param shipperId ID của shipper
     * @return Danh sách thông báo
     */
    public List<Map<String, Object>> getShipperNotifications(int shipperId) {
        List<Map<String, Object>> notifications = new ArrayList<>();
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();

        try {
            // 0. Thông báo từ bảng notifications (từ vendor broadcast)
            String sqlNotifications =
                "SELECT n.notification_id, n.type, n.title, n.message, n.action_url, " +
                "       n.is_read, n.created_at " +
                "FROM notifications n " +
                "WHERE n.user_id = :shipperId " +
                "AND n.created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
                "ORDER BY n.created_at DESC " +
                "LIMIT 20";

            Query queryNotif = em.createNativeQuery(sqlNotifications);
            queryNotif.setParameter("shipperId", shipperId);
            List<Object[]> dbNotifications = queryNotif.getResultList();

            for (Object[] row : dbNotifications) {
                Map<String, Object> notif = new HashMap<>();
                notif.put("id", "notif_" + row[0]);
                notif.put("type", row[1] != null ? row[1].toString() : "INFO");
                notif.put("title", row[2] != null ? row[2].toString() : "Thông báo");
                notif.put("message", row[3] != null ? row[3].toString() : "");
                notif.put("actionUrl", row[4] != null ? row[4].toString() : "");
                notif.put("read", row[5] != null && ((Number) row[5]).intValue() == 1);
                notif.put("createdAt", row[6]);
                notif.put("status", "notification");
                notifications.add(notif);
            }

            // 1. Đơn hàng mới trên sàn (status = Ready, chưa có shipper nhận)
            String sqlNewOrders =
                "SELECT da.assignment_id, da.status, da.assigned_at, da.earnings, " +
                "       o.order_number, o.total_price, " +
                "       w.name as warehouse_name, " +
                "       CONCAT(a.street, ', ', a.district) as address " +
                "FROM delivery_assignments da " +
                "JOIN deliveries d ON da.delivery_id = d.delivery_id " +
                "JOIN orders o ON d.order_id = o.order_id " +
                "LEFT JOIN warehouses w ON d.warehouse_id = w.warehouse_id " +
                "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                "WHERE da.status = 'Ready' " +
                "ORDER BY da.assigned_at DESC " +
                "LIMIT 10";

            Query queryNew = em.createNativeQuery(sqlNewOrders);
            List<Object[]> newOrders = queryNew.getResultList();

            for (Object[] row : newOrders) {
                Map<String, Object> notif = new HashMap<>();
                notif.put("id", "new_" + row[0]);
                notif.put("type", "NEW_ORDER");
                notif.put("title", "🆕 Đơn hàng mới trên sàn!");

                String orderNumber = row[4] != null ? row[4].toString() : "N/A";
                String warehouse = row[6] != null ? row[6].toString() : "Kho";
                Double earnings = row[3] != null ? ((Number) row[3]).doubleValue() : 0;

                notif.put("message", String.format("Đơn #%s - Lấy tại: %s - Phí ship: %,.0f₫",
                        orderNumber, warehouse, earnings));
                notif.put("createdAt", row[2]);
                notif.put("read", false);
                notif.put("status", "Ready");
                notif.put("orderNumber", orderNumber);

                notifications.add(notif);
            }

            // 2. Đơn shipper đã nhận (status = In Transit, shipper_id = shipperId)
            String sqlAccepted =
                "SELECT da.assignment_id, da.status, da.accepted_at, da.earnings, " +
                "       o.order_number, o.total_price, " +
                "       CONCAT(c.last_name, ' ', c.first_name) as customer_name, " +
                "       CONCAT(a.street, ', ', a.district) as address " +
                "FROM delivery_assignments da " +
                "JOIN deliveries d ON da.delivery_id = d.delivery_id " +
                "JOIN orders o ON d.order_id = o.order_id " +
                "LEFT JOIN users c ON o.customer_id = c.user_id " +
                "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                "WHERE da.shipper_id = :shipperId " +
                "AND da.status IN ('In Transit', 'Accepted', 'Shipping', 'On Delivery') " +
                "ORDER BY da.accepted_at DESC " +
                "LIMIT 10";

            Query queryAccepted = em.createNativeQuery(sqlAccepted);
            queryAccepted.setParameter("shipperId", shipperId);
            List<Object[]> acceptedOrders = queryAccepted.getResultList();

            for (Object[] row : acceptedOrders) {
                Map<String, Object> notif = new HashMap<>();
                notif.put("id", "accepted_" + row[0]);
                notif.put("type", "ACCEPTED");
                notif.put("title", "📦 Bạn đang giao đơn hàng");

                String orderNumber = row[4] != null ? row[4].toString() : "N/A";
                String customerName = row[6] != null ? row[6].toString() : "Khách hàng";
                String address = row[7] != null ? row[7].toString() : "";

                notif.put("message", String.format("Đơn #%s - KH: %s - Địa chỉ: %s",
                        orderNumber, customerName, address));
                notif.put("createdAt", row[2]);
                notif.put("read", true); // Đã nhận = đã đọc
                notif.put("status", "In Transit");
                notif.put("orderNumber", orderNumber);

                notifications.add(notif);
            }

            // 3. Đơn đã giao thành công (status = Completed, shipper_id = shipperId)
            String sqlCompleted =
                "SELECT da.assignment_id, da.status, da.completed_at, da.earnings, " +
                "       o.order_number, o.total_price " +
                "FROM delivery_assignments da " +
                "JOIN deliveries d ON da.delivery_id = d.delivery_id " +
                "JOIN orders o ON d.order_id = o.order_id " +
                "WHERE da.shipper_id = :shipperId " +
                "AND da.status IN ('Completed', 'Delivered') " +
                "AND da.completed_at >= DATE_SUB(NOW(), INTERVAL 24 HOUR) " +
                "ORDER BY da.completed_at DESC " +
                "LIMIT 10";

            Query queryCompleted = em.createNativeQuery(sqlCompleted);
            queryCompleted.setParameter("shipperId", shipperId);
            List<Object[]> completedOrders = queryCompleted.getResultList();

            for (Object[] row : completedOrders) {
                Map<String, Object> notif = new HashMap<>();
                notif.put("id", "completed_" + row[0]);
                notif.put("type", "COMPLETED");
                notif.put("title", "✅ Giao hàng thành công!");

                String orderNumber = row[4] != null ? row[4].toString() : "N/A";
                Double earnings = row[3] != null ? ((Number) row[3]).doubleValue() : 0;

                notif.put("message", String.format("Đơn #%s hoàn thành - Bạn nhận được %,.0f₫",
                        orderNumber, earnings));
                notif.put("createdAt", row[2]);
                notif.put("read", true); // Đã hoàn thành = đã đọc
                notif.put("status", "Completed");
                notif.put("orderNumber", orderNumber);

                notifications.add(notif);
            }

            // Sắp xếp theo thời gian mới nhất
            notifications.sort((a, b) -> {
                Date dateA = (Date) a.get("createdAt");
                Date dateB = (Date) b.get("createdAt");
                if (dateA == null && dateB == null) return 0;
                if (dateA == null) return 1;
                if (dateB == null) return -1;
                return dateB.compareTo(dateA);
            });

            // Giới hạn 15 thông báo
            if (notifications.size() > 15) {
                notifications = notifications.subList(0, 15);
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
     * Đếm số đơn hàng mới (Ready) + thông báo chưa đọc - dùng làm badge
     */
    public int countNewOrders() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            // Đếm delivery_assignments với status = Ready
            String sql1 = "SELECT COUNT(*) FROM delivery_assignments WHERE status = 'Ready'";
            Query query1 = em.createNativeQuery(sql1);
            int readyCount = ((Number) query1.getSingleResult()).intValue();

            return readyCount;
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Đếm số thông báo chưa đọc cho shipper cụ thể
     */
    public int countUnreadNotifications(int shipperId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, shipperId);
            return ((Number) query.getSingleResult()).intValue();
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }
}

