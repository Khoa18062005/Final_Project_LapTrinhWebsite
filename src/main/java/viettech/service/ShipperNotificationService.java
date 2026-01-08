package viettech.service;

import viettech.dao.NotificationDAO;
import viettech.dao.ShipperDAO;
import viettech.entity.Notification;
import viettech.entity.user.Shipper;

import java.util.Date;
import java.util.List;

/**
 * Service xử lý thông báo dành riêng cho Shipper
 */
public class ShipperNotificationService {

    private final NotificationDAO notificationDAO;
    private final ShipperDAO shipperDAO;

    public ShipperNotificationService() {
        this.notificationDAO = new NotificationDAO();
        this.shipperDAO = new ShipperDAO();
    }

    /**
     * Tạo thông báo khi có đơn hàng mới trên sàn
     * Gửi cho TẤT CẢ shipper đang hoạt động
     * @param orderNumber Mã đơn hàng
     * @param warehouseName Tên kho lấy hàng
     * @param shippingFee Phí ship shipper được hưởng
     */
    public void notifyNewOrderAvailable(String orderNumber, String warehouseName, double shippingFee) {
        try {
            // Lấy danh sách tất cả shipper đang hoạt động
            List<Shipper> activeShippers = shipperDAO.findAllActiveShippers();

            if (activeShippers == null || activeShippers.isEmpty()) {
                System.out.println("⚠️ Không có shipper nào đang hoạt động để gửi thông báo");
                return;
            }

            String title = "🚚 Đơn hàng mới trên sàn!";
            String message = String.format(
                "Đơn hàng #%s cần giao. Lấy tại: %s. Phí ship: %,.0f₫. Nhận đơn ngay!",
                orderNumber, warehouseName, shippingFee
            );

            for (Shipper shipper : activeShippers) {
                Notification notification = new Notification();
                notification.setUserId(shipper.getUserId());
                notification.setType("shipper_order");
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setActionUrl("/shipper#orders");
                notification.setRead(false);
                notification.setCreatedAt(new Date());

                notificationDAO.insert(notification);
                System.out.println("📬 Đã gửi thông báo đơn mới cho shipper: " + shipper.getUserId());
            }

        } catch (Exception e) {
            System.out.println("❌ Lỗi gửi thông báo đơn hàng mới: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo thông báo khi shipper nhận đơn hàng thành công
     * @param shipperId ID của shipper
     * @param orderNumber Mã đơn hàng
     * @param customerName Tên khách hàng
     * @param address Địa chỉ giao hàng
     */
    public void notifyOrderAccepted(int shipperId, String orderNumber, String customerName, String address) {
        try {
            String title = "✅ Bạn đã nhận đơn thành công!";
            String message = String.format(
                "Đơn hàng #%s - Khách hàng: %s. Giao đến: %s. Chúc bạn giao hàng thành công!",
                orderNumber, customerName, address
            );

            Notification notification = new Notification();
            notification.setUserId(shipperId);
            notification.setType("shipper_accepted");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setActionUrl("/shipper#overview");
            notification.setRead(false);
            notification.setCreatedAt(new Date());

            notificationDAO.insert(notification);
            System.out.println("📬 Đã gửi thông báo nhận đơn cho shipper: " + shipperId);

        } catch (Exception e) {
            System.out.println("❌ Lỗi gửi thông báo nhận đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo thông báo khi shipper hoàn thành đơn hàng
     * @param shipperId ID của shipper
     * @param orderNumber Mã đơn hàng
     * @param earnings Số tiền shipper được hưởng
     */
    public void notifyOrderCompleted(int shipperId, String orderNumber, double earnings) {
        try {
            String title = "🎉 Giao hàng thành công!";
            String message = String.format(
                "Đơn hàng #%s đã hoàn thành. Bạn nhận được %,.0f₫. Tiếp tục nhận đơn mới nhé!",
                orderNumber, earnings
            );

            Notification notification = new Notification();
            notification.setUserId(shipperId);
            notification.setType("shipper_completed");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setActionUrl("/shipper#history");
            notification.setRead(false);
            notification.setCreatedAt(new Date());

            notificationDAO.insert(notification);
            System.out.println("📬 Đã gửi thông báo hoàn thành đơn cho shipper: " + shipperId);

        } catch (Exception e) {
            System.out.println("❌ Lỗi gửi thông báo hoàn thành: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo cho khách hàng khi shipper nhận đơn
     * @param customerId ID của khách hàng
     * @param orderNumber Mã đơn hàng
     * @param shipperName Tên shipper
     */
    public void notifyCustomerOrderPickedUp(int customerId, String orderNumber, String shipperName) {
        try {
            String title = "📦 Đơn hàng đang được giao!";
            String message = String.format(
                "Đơn hàng #%s đã được shipper %s nhận và đang trên đường giao đến bạn.",
                orderNumber, shipperName
            );

            Notification notification = new Notification();
            notification.setUserId(customerId);
            notification.setType("order");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setActionUrl("/order-details?orderNumber=" + orderNumber);
            notification.setRead(false);
            notification.setCreatedAt(new Date());

            notificationDAO.insert(notification);
            System.out.println("📬 Đã gửi thông báo cho khách hàng: " + customerId);

        } catch (Exception e) {
            System.out.println("❌ Lỗi gửi thông báo cho khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo cho khách hàng khi shipper giao hàng thành công
     * @param customerId ID của khách hàng
     * @param orderNumber Mã đơn hàng
     */
    public void notifyCustomerOrderDelivered(int customerId, String orderNumber) {
        try {
            String title = "✅ Đơn hàng đã giao thành công!";
            String message = String.format(
                "Đơn hàng #%s đã được giao thành công. Cảm ơn bạn đã mua hàng! Hãy đánh giá shipper để giúp chúng tôi cải thiện dịch vụ.",
                orderNumber
            );

            Notification notification = new Notification();
            notification.setUserId(customerId);
            notification.setType("order");
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setActionUrl("/order-details?orderNumber=" + orderNumber);
            notification.setRead(false);
            notification.setCreatedAt(new Date());

            notificationDAO.insert(notification);
            System.out.println("📬 Đã gửi thông báo giao hàng thành công cho khách hàng: " + customerId);

        } catch (Exception e) {
            System.out.println("❌ Lỗi gửi thông báo cho khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lấy số thông báo chưa đọc của shipper
     * @param shipperId ID của shipper
     * @return Số thông báo chưa đọc
     */
    public int getUnreadCount(int shipperId) {
        return (int) notificationDAO.countUnreadByUserId(shipperId);
    }

    /**
     * Lấy danh sách thông báo gần đây của shipper
     * @param shipperId ID của shipper
     * @param limit Số lượng tối đa
     * @return Danh sách thông báo
     */
    public List<Notification> getRecentNotifications(int shipperId, int limit) {
        List<Notification> all = notificationDAO.findByUserId(shipperId);
        if (all == null) return null;
        if (all.size() <= limit) return all;
        return all.subList(0, limit);
    }

    /**
     * Đánh dấu thông báo đã đọc
     * @param notificationId ID thông báo
     */
    public void markAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    /**
     * Đánh dấu tất cả thông báo đã đọc
     * @param shipperId ID của shipper
     */
    public void markAllAsRead(int shipperId) {
        notificationDAO.markAllAsReadByUserId(shipperId);
    }
}

