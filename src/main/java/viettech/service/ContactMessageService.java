package viettech.service;

import viettech.dao.NotificationDAO;
import viettech.entity.Notification;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service xử lý logic liên quan đến contact messages từ khách hàng
 */
public class ContactMessageService {
    private static final String CONTACT_ACTION_URL = "/admin/contact-messages";

    private final NotificationDAO notificationDAO;
    private final SentimentAnalysis sentimentAnalysis;

    public ContactMessageService() {
        this.notificationDAO = new NotificationDAO();
        this.sentimentAnalysis = SentimentAnalysis.getInstance();
    }

    /**
     * Lấy danh sách contact messages với phân trang và bộ lọc
     *
     * @param page Số trang (bắt đầu từ 1)
     * @param pageSize Số lượng mỗi trang
     * @param statusFilter Bộ lọc trạng thái ("read", "unread", hoặc null để lấy tất cả)
     * @param userIdFilter ID của user cần lọc (null để lấy tất cả)
     * @return ContactMessageResult chứa danh sách messages và metadata
     */
    public ContactMessageResult getContactMessages(int page, int pageSize, String statusFilter, Integer userIdFilter) {
        try {
            // Validate page
            if (page < 1) page = 1;


            // Lấy danh sách tin nhắn với phân trang
            List<Notification> contactMessages = notificationDAO.findByActionUrlPaginated(
                CONTACT_ACTION_URL, page, pageSize);

            // Áp dụng bộ lọc userId nếu có
            if (userIdFilter != null && userIdFilter > 0) {
                contactMessages = contactMessages.stream()
                    .filter(n -> n.getUserId() == userIdFilter)
                    .collect(Collectors.toList());
            }

            // Áp dụng bộ lọc trạng thái nếu có
            if (statusFilter != null && !statusFilter.isEmpty()) {
                if ("unread".equals(statusFilter)) {
                    contactMessages = contactMessages.stream()
                        .filter(n -> !n.isRead())
                        .collect(Collectors.toList());
                } else if ("read".equals(statusFilter)) {
                    contactMessages = contactMessages.stream()
                        .filter(Notification::isRead)
                        .collect(Collectors.toList());
                }
            }

            // Phân tích sentiment cho mỗi tin nhắn
            Map<Integer, String> sentimentMap = analyzeSentiments(contactMessages);



            int totalMessages = contactMessages.size();
            long unreadMessages = contactMessages.stream().filter(Notification::isRead).count();
            // Tính số trang
            int totalPages = (int) Math.ceil((double) totalMessages / pageSize);
            if (totalPages < 1) totalPages = 1;
            page = Math.min(page, totalPages);

            System.out.println("✓ ContactMessageService: Loaded " + contactMessages.size() +
                    " contact messages (total: " + totalMessages + ", unread: " + unreadMessages +
                    (userIdFilter != null ? ", filtered by userId: " + userIdFilter : "") + ")");


            return new ContactMessageResult(
                contactMessages,
                sentimentMap,
                totalMessages,
                unreadMessages,
                page,
                totalPages
            );

        } catch (Exception e) {
            System.err.println("Error loading contact messages: " + e.getMessage());
            e.printStackTrace();
            return new ContactMessageResult(
                new ArrayList<>(),
                new HashMap<>(),
                0L,
                0L,
                1,
                1
            );
        }
    }

    /**
     * Lấy danh sách contact messages với phân trang và bộ lọc (overload method cho backward compatibility)
     *
     * @param page Số trang (bắt đầu từ 1)
     * @param pageSize Số lượng mỗi trang
     * @param statusFilter Bộ lọc trạng thái ("read", "unread", hoặc null để lấy tất cả)
     * @return ContactMessageResult chứa danh sách messages và metadata
     */
    public ContactMessageResult getContactMessages(int page, int pageSize, String statusFilter) {
        return getContactMessages(page, pageSize, statusFilter, null);
    }

    /**
     * Phân tích sentiment cho danh sách messages
     *
     * @param messages Danh sách notification
     * @return Map với key là notificationId và value là sentiment ("positive", "negative", "neutral")
     */
    private Map<Integer, String> analyzeSentiments(List<Notification> messages) {
        Map<Integer, String> sentimentMap = new HashMap<>();

        for (Notification message : messages) {
            String sentiment = "positive"; // Mặc định

            try {
                String messageContent = message.getMessage();
                if (messageContent != null && !messageContent.trim().isEmpty()) {
                    String apiResponse = sentimentAnalysis.getResult(messageContent);

                    if (apiResponse != null && apiResponse.contains("\"sentiment\"")) {
                        String sentimentValue = apiResponse
                            .replaceAll(".*\"sentiment\"\\s*:\\s*\"([^\"]+)\".*", "$1")
                            .trim();

                        sentiment = switch (sentimentValue.toUpperCase()) {
                            case "POS" -> "positive";
                            case "NEG" -> "negative";
                            case "NEU" -> "neutral";
                            default -> {
                                System.out.println("⚠️ Unknown sentiment value: " + sentimentValue);
                                yield "positive";
                            }
                        };
                    }
                }
            } catch (Exception e) {
                System.err.println("Error analyzing sentiment for message " +
                    message.getNotificationId() + ": " + e.getMessage());
            }

            sentimentMap.put(message.getNotificationId(), sentiment);
        }

        return sentimentMap;
    }

    /**
     * Đánh dấu contact message là đã đọc
     *
     * @param notificationId ID của notification
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean markAsRead(int notificationId) {
        try {
            Notification notification = notificationDAO.findById(notificationId);
            if (notification != null) {
                notification.setRead(true);
                notification.setReadAt(new Date());
                notificationDAO.update(notification);
                System.out.println("✓ ContactMessageService: Marked message " + notificationId + " as read");
                return true;
            }
            System.err.println("ContactMessageService: Notification " + notificationId + " not found");
            return false;
        } catch (Exception e) {
            System.err.println("Error marking contact as read: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa contact message
     *
     * @param notificationId ID của notification
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean deleteMessage(int notificationId) {
        try {
            notificationDAO.delete(notificationId);
            System.out.println("✓ ContactMessageService: Deleted message " + notificationId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting contact message: " + e.getMessage());
            return false;
        }
    }

    /**
     * Trả lời contact message
     *
     * @param originalNotificationId ID của notification gốc
     * @param userId ID của user sẽ nhận phản hồi
     * @param replyTitle Tiêu đề phản hồi
     * @param replyMessage Nội dung phản hồi
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean replyToMessage(int originalNotificationId, int userId, String replyTitle, String replyMessage) {
        try {
            // Đánh dấu tin nhắn gốc đã đọc
            Notification originalNotification = notificationDAO.findById(originalNotificationId);
            if (originalNotification != null && !originalNotification.isRead()) {
                originalNotification.setRead(true);
                originalNotification.setReadAt(new Date());
                notificationDAO.update(originalNotification);
            }

            // Tạo thông báo phản hồi cho khách hàng
            Notification replyNotification = new Notification();
            replyNotification.setUserId(userId);
            replyNotification.setType("ADMIN_REPLY");
            replyNotification.setTitle(replyTitle);
            replyNotification.setMessage(replyMessage);
            replyNotification.setActionUrl("/notifications");
            replyNotification.setRead(false);
            replyNotification.setCreatedAt(new Date());

            notificationDAO.insert(replyNotification);

            System.out.println("✓ ContactMessageService: Sent reply to user " + userId);
            return true;

        } catch (Exception e) {
            System.err.println("Error replying to contact message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inner class để chứa kết quả truy vấn contact messages
     */
    public static class ContactMessageResult {
        private final List<Notification> messages;
        private final Map<Integer, String> sentimentMap;
        private final long totalMessages;
        private final long unreadMessages;
        private final int currentPage;
        private final int totalPages;

        public ContactMessageResult(List<Notification> messages, Map<Integer, String> sentimentMap,
                                   long totalMessages, long unreadMessages,
                                   int currentPage, int totalPages) {
            this.messages = messages;
            this.sentimentMap = sentimentMap;
            this.totalMessages = totalMessages;
            this.unreadMessages = unreadMessages;
            this.currentPage = currentPage;
            this.totalPages = totalPages;
        }

        public List<Notification> getMessages() {
            return messages;
        }

        public Map<Integer, String> getSentimentMap() {
            return sentimentMap;
        }

        public long getTotalMessages() {
            return totalMessages;
        }

        public long getUnreadMessages() {
            return unreadMessages;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }
}

