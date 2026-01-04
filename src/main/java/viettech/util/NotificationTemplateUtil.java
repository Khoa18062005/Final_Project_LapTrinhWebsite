package viettech.util;

import viettech.entity.Notification;
import java.util.Date;

/**
 * Utility class để tạo các mẫu thông báo chuẩn
 * Chứa các template thông báo thường dùng trong hệ thống
 * 
 * @author VietTech Team
 */
public class NotificationTemplateUtil {

    // ========== NOTIFICATION TYPES ==========
    public static final String TYPE_SYSTEM = "system";
    public static final String TYPE_ACCOUNT = "account";
    public static final String TYPE_ORDER = "order";
    public static final String TYPE_PROMOTION = "promotion";
    public static final String TYPE_SECURITY = "security";  // ← THÊM MỚI


    // ========== IMAGE URLS ==========
    private static final String IMG_LOGIN = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767279453/login_ndmwr5.png";
    private static final String IMG_REGISTER = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767279473/register_iozsha.png";
    private static final String IMG_PASSWORD = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767339496/reset_password_gudomz.png";  // ← THÊM MỚI (bạn có thể đổi URL)


    /**
     * ========== THÔNG BÁO ĐĂNG NHẬP THÀNH CÔNG ==========
     * Được gọi khi user đăng nhập thành công vào hệ thống
     * 
     * @param userId ID của user
     * @param firstName Tên của user
     * @param lastName Họ của user
     * @return Notification object đã được điền đầy đủ thông tin
     */
    public static Notification createLoginNotification(int userId, String firstName, String lastName) {
        Notification notification = new Notification();
        
        // ===== THÔNG TIN CƠ BẢN =====
        notification.setUserId(userId);
        notification.setType(TYPE_ACCOUNT);
        
        // ===== TIÊU ĐỀ VÀ NỘI DUNG =====
        notification.setTitle("🎉 Chào mừng bạn quay trở lại!");
        notification.setMessage(
            String.format("Xin chào %s %s! Bạn đã đăng nhập thành công vào hệ thống VietTech. " +
                         "Chúc bạn có trải nghiệm mua sắm tuyệt vời!", 
                         firstName, lastName)
        );
        
        // ===== HÌNH ẢNH =====
        notification.setImageUrl(IMG_LOGIN);
        
        // ===== TRẠNG THÁI =====
        notification.setRead(false);  // Chưa đọc
        notification.setReadAt(null); // Chưa đọc nên null
        
        // ===== THỜI GIAN =====
        notification.setCreatedAt(new Date()); // Thời điểm tạo
        notification.setExpiresAt(null);       // Không hết hạn
        
        // ===== CÁC TRƯỜNG KHÔNG DÙNG =====
        notification.setData(null);
        notification.setActionUrl(null);
        
        return notification;
    }

    /**
     * ========== THÔNG BÁO ĐĂNG KÝ THÀNH CÔNG ==========
     * Được gọi khi user đăng ký tài khoản mới
     * 
     * @param userId ID của user
     * @param firstName Tên của user
     * @param lastName Họ của user
     * @return Notification object đã được điền đầy đủ thông tin
     */
    public static Notification createRegisterNotification(int userId, String firstName, String lastName) {
        Notification notification = new Notification();
        
        // ===== THÔNG TIN CƠ BẢN =====
        notification.setUserId(userId);
        notification.setType(TYPE_ACCOUNT);
        
        // ===== TIÊU ĐỀ VÀ NỘI DUNG =====
        notification.setTitle("🎊 Chào mừng bạn đến với VietTech!");
        notification.setMessage(
            String.format("Xin chào %s %s! Cảm ơn bạn đã đăng ký tài khoản tại VietTech. " +
                         "Chúng tôi rất vui được đồng hành cùng bạn trong hành trình mua sắm. " +
                         "Hãy khám phá hàng ngàn sản phẩm chất lượng ngay bây giờ!", 
                         firstName, lastName)
        );
        
        // ===== HÌNH ẢNH =====
        notification.setImageUrl(IMG_REGISTER);
        
        // ===== TRẠNG THÁI =====
        notification.setRead(false);
        notification.setReadAt(null);
        
        // ===== THỜI GIAN =====
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        
        // ===== CÁC TRƯỜNG KHÔNG DÙNG =====
        notification.setData(null);
        notification.setActionUrl(null);
        
        return notification;
    }

    /**
     * ========== THÔNG BÁO CHÀO MỪNG LẦN ĐẦU TIÊN ==========
     * Được gọi khi user mới đăng ký và đăng nhập lần đầu
     * Khác với createRegisterNotification (dùng cho sau khi đăng ký)
     * Cái này dùng cho lần đăng nhập đầu tiên sau khi đăng ký
     */
    public static Notification createWelcomeNotification(int userId, String firstName, String lastName) {
        Notification notification = new Notification();
        
        notification.setUserId(userId);
        notification.setType(TYPE_SYSTEM);
        
        notification.setTitle("👋 Chào mừng bạn lần đầu đăng nhập!");
        notification.setMessage(
            String.format("Xin chào %s %s! Đây là lần đầu tiên bạn đăng nhập vào VietTech. " +
                         "Hãy bắt đầu khám phá các tính năng tuyệt vời của chúng tôi nhé!", 
                         firstName, lastName)
        );
        
        notification.setImageUrl(IMG_LOGIN);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl(null);
        
        return notification;
    }

    /**
     * ========== THÔNG BÁO ĐỔI MẬT KHẨU THÀNH CÔNG ==========
     * ← METHOD MỚI
     * Được gọi khi user đặt lại mật khẩu thành công
     *
     * @param userId ID của user
     * @param firstName Tên của user
     * @param lastName Họ của user
     * @return Notification object đã được điền đầy đủ thông tin
     */
    public static Notification createPasswordResetNotification(int userId, String firstName, String lastName) {
        Notification notification = new Notification();

        // ===== THÔNG TIN CƠ BẢN =====
        notification.setUserId(userId);
        notification.setType(TYPE_SECURITY);  // Dùng type SECURITY vì liên quan bảo mật

        // ===== TIÊU ĐỀ VÀ NỘI DUNG =====
        notification.setTitle("🔐 Mật khẩu đã được thay đổi thành công!");
        notification.setMessage(
                String.format("Xin chào %s %s! Mật khẩu của bạn đã được thay đổi thành công vào lúc %s. " +
                                "Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ bộ phận hỗ trợ ngay lập tức để bảo vệ tài khoản của bạn.",
                        firstName, lastName,
                        new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()))
        );

        // ===== HÌNH ẢNH =====
        notification.setImageUrl(IMG_PASSWORD);  // Có thể dùng ảnh khóa/bảo mật

        // ===== TRẠNG THÁI =====
        notification.setRead(false);  // Chưa đọc
        notification.setReadAt(null); // Chưa đọc nên null

        // ===== THỜI GIAN =====
        notification.setCreatedAt(new Date()); // Thời điểm tạo
        notification.setExpiresAt(null);       // Không hết hạn

        // ===== CÁC TRƯỜNG KHÔNG DÙNG =====
        notification.setData(null);
        notification.setActionUrl(null);

        return notification;
    }


    /**
     * ========== THÔNG BÁO KHÁC ==========
     * Bạn có thể thêm các template khác ở đây:
     * - createOrderPlacedNotification()
     * - createOrderShippedNotification()
     * - createOrderDeliveredNotification()
     * - createPromotionNotification()
     * - createPasswordChangedNotification()
     * - ...
     */
}