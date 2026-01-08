package viettech.service;

import viettech.dao.NotificationDAO;
import viettech.dto.NotificationReadDTO;
import viettech.dto.NotificationDeleteDTO;
import viettech.entity.Notification;

import java.util.List;

/**
 * Service xử lý logic thông báo với DTO pattern
 */
public class NotificationService {
    private final NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    // ========== METHOD MỚI: TẠO NOTIFICATION ==========
    /**
     * Tạo thông báo mới và lưu vào database
     * @param notification Notification object cần lưu
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean createNotification(Notification notification) {
        try {
            if (notification == null) {
                System.out.println("❌ Service: Notification is null");
                return false;
            }

            System.out.println("📝 Service: Creating notification for user " + notification.getUserId());
            notificationDAO.insert(notification);
            System.out.println("✅ Service: Notification created successfully");
            return true;

        } catch (Exception e) {
            System.out.println("❌ Service Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationDAO.findByUserId(userId);
    }

    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        return notificationDAO.findUnreadByUserId(userId);
    }

    public int getUnreadCountByUserId(int userId) {
        return (int) notificationDAO.countUnreadByUserId(userId);
    }

    /**
     * Xử lý đánh dấu đã đọc thông báo (một hoặc tất cả)
     * @param dto DTO chứa notificationId, markAll và userId
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean markAsRead(NotificationReadDTO dto) {
        try {
            if (dto == null) {
                System.out.println("❌ Service: DTO is null");
                return false;
            }

            if (dto.isMarkAll()) {
                // Đánh dấu tất cả đã đọc
                System.out.println("🔔 Service: Marking ALL notifications as read for user " + dto.getUserId());

                long unreadCount = notificationDAO.countUnreadByUserId(dto.getUserId());
                System.out.println("ℹ️ Service: Found " + unreadCount + " unread notifications");

                if (unreadCount == 0) {
                    System.out.println("ℹ️ Service: No unread notifications");
                    return true;
                }

                notificationDAO.markAllAsReadByUserId(dto.getUserId());
                System.out.println("✅ Service: All notifications marked as read");
                return true;
            } else {
                // Đánh dấu một thông báo đã đọc
                System.out.println("🔔 Service: Marking notification " + dto.getNotificationId() +
                        " as read for user " + dto.getUserId());

                // Kiểm tra thông báo có thuộc về user không
                Notification notification = notificationDAO.findById(dto.getNotificationId());
                if (notification == null) {
                    System.out.println("❌ Service: Notification not found");
                    return false;
                }

                if (notification.getUserId() != dto.getUserId()) {
                    System.out.println("❌ Service: Notification doesn't belong to user");
                    return false;
                }

                notificationDAO.markAsRead(dto.getNotificationId());
                System.out.println("✅ Service: Notification marked as read");
                return true;
            }

        } catch (Exception e) {
            System.out.println("❌ Service Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa thông báo
     * @param dto DTO chứa notificationId và userId
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteNotification(NotificationDeleteDTO dto) {
        try {
            if (dto == null) {
                System.out.println("❌ Service: DTO is null");
                return false;
            }

            System.out.println("🗑️ Service: Deleting notification " + dto.getNotificationId());

            // Kiểm tra thông báo có thuộc về user không
            Notification notification = notificationDAO.findById(dto.getNotificationId());
            if (notification == null) {
                System.out.println("❌ Service: Notification not found");
                return false;
            }

            if (notification.getUserId() != dto.getUserId()) {
                System.out.println("❌ Service: Notification doesn't belong to user");
                return false;
            }

            // Xóa thông báo
            notificationDAO.delete(dto.getNotificationId());
            System.out.println("✅ Service: Notification deleted");
            return true;

        } catch (Exception e) {
            System.out.println("❌ Service Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách thông báo theo action URL
     * @param actionUrl URL action cần tìm
     * @return List notification có actionUrl tương ứng
     */
    public List<Notification> getNotificationsByActionUrl(String actionUrl) {
        return notificationDAO.findByActionUrl(actionUrl);
    }

    /**
     * Lấy danh sách thông báo theo action URL với phân trang
     * @param actionUrl URL action cần tìm
     * @param page Số trang (bắt đầu từ 1)
     * @param pageSize Số lượng mỗi trang
     * @return List notification có actionUrl tương ứng
     */
    public List<Notification> getNotificationsByActionUrlPaginated(String actionUrl, int page, int pageSize) {
        return notificationDAO.findByActionUrlPaginated(actionUrl, page, pageSize);
    }

    /**
     * Đếm tổng số notification theo action URL
     * @param actionUrl URL action cần đếm
     * @return Số lượng notification
     */
    public long countNotificationsByActionUrl(String actionUrl) {
        return notificationDAO.countByActionUrl(actionUrl);
    }

    /**
     * Đếm số notification chưa đọc theo action URL
     * @param actionUrl URL action cần đếm
     * @return Số lượng notification chưa đọc
     */
    public long countUnreadNotificationsByActionUrl(String actionUrl) {
        return notificationDAO.countUnreadByActionUrl(actionUrl);
    }

    /**
     * Helper method để tạo NotificationReadDTO từ request parameters
     */
    public static NotificationReadDTO createReadDTOFromRequest(int userId,
                                                               String notificationIdParam, String markAllParam) {

        boolean markAll = "true".equalsIgnoreCase(markAllParam);
        int notificationId = 0;

        if (notificationIdParam != null && !notificationIdParam.trim().isEmpty()) {
            try {
                notificationId = Integer.parseInt(notificationIdParam);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid notification ID format: " + notificationIdParam);
            }
        }

        return new NotificationReadDTO(notificationId, markAll, userId);
    }

    /**
     * Helper method để tạo NotificationDeleteDTO từ request parameters
     */
    public static NotificationDeleteDTO createDeleteDTOFromRequest(int userId,
                                                                   String notificationIdParam) {

        if (notificationIdParam == null || notificationIdParam.trim().isEmpty()) {
            return null;
        }

        try {
            int notificationId = Integer.parseInt(notificationIdParam);
            return new NotificationDeleteDTO(notificationId, userId);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid notification ID format: " + notificationIdParam);
            return null;
        }
    }
}