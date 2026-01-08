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
    public static final String TYPE_REFERRAL = "referral";  // ← THÊM MỚI
    public static final String TYPE_CAREER = "career";  // ← Type mới
    public static final String TYPE_CONTACT = "contact";  // ← Type mới


    // ========== IMAGE URLS ==========
    private static final String IMG_LOGIN = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767279453/login_ndmwr5.png";
    private static final String IMG_REGISTER = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767279473/register_iozsha.png";
    private static final String IMG_PASSWORD = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767339496/reset_password_gudomz.png";  // ← THÊM MỚI (bạn có thể đổi URL)
    private static final String IMG_REFERRAL = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767514645/z7395974565366_948ddc35641f7189f5fb9119c7a934ed_eexyg8.jpg";  // ← THÊM MỚI (bạn có thể đổi)
    private static final String IMG_VENDOR = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767775253/867c9a11-c20b-4725-9fa3-25ae3063bb35.png";
    private static final String IMG_SHIPPER = "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1767775253/867c9a11-c20b-4725-9fa3-25ae3063bb35.png";
    private static final String IMG_CONTACT = "https://bom.edu.vn/public/upload/2024/12/meme-buaa-53.webp";



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



    public static Notification createReferralRewardNotification(int userId, String firstName, String lastName,
                                                                String referredUserName, int points) {
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setType(TYPE_REFERRAL);

        notification.setTitle("🎁 Bạn nhận được phần thưởng giới thiệu!");
        notification.setMessage(
                String.format("Chúc mừng %s %s! Bạn đã nhận được %d điểm thưởng vì giới thiệu %s đến với VietTech. " +
                                "Cảm ơn bạn đã giúp cộng đồng VietTech phát triển! " +
                                "Điểm thưởng đã được cộng vào tài khoản của bạn.",
                        firstName, lastName, points, referredUserName)
        );

        notification.setImageUrl(IMG_REFERRAL);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl(null);

        return notification;
    }

    /**
     * ========== THÔNG BÁO NGƯỜI ĐƯỢC GIỚI THIỆU NHẬN THƯỞNG ==========
     */
    public static Notification createReferralWelcomeNotification(int userId, String firstName, String lastName,
                                                                 String referrerCode, int points) {
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setType(TYPE_REFERRAL);

        notification.setTitle("🎉 Chào mừng! Bạn nhận được điểm thưởng!");
        notification.setMessage(
                String.format("Xin chào %s %s! Cảm ơn bạn đã sử dụng mã giới thiệu %s. " +
                                "Bạn đã nhận được %d điểm thưởng để bắt đầu hành trình mua sắm tại VietTech. " +
                                "Hãy khám phá và tận hưởng những ưu đãi tuyệt vời nhé!",
                        firstName, lastName, referrerCode, points)
        );

        notification.setImageUrl(IMG_REFERRAL);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl(null);

        return notification;
    }


    /**
     * ========== THÔNG BÁO USER ĐÃ GỬI ĐƠN VENDOR ==========
     */
    public static Notification createVendorApplicationUserNotification(int userId, String firstName, String lastName, String businessName) {
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setType(TYPE_CAREER);

        notification.setTitle("📝 Đơn đăng ký Đối tác Kinh doanh đã được gửi!");
        notification.setMessage(
                String.format("Xin chào %s %s! Cảm ơn bạn đã gửi đơn đăng ký trở thành Đối tác Kinh doanh của VietTech " +
                                "với doanh nghiệp \"%s\". Chúng tôi sẽ xem xét hồ sơ và liên hệ lại với bạn trong vòng 3-5 ngày làm việc. " +
                                "Vui lòng kiểm tra email để biết thêm chi tiết.",
                        firstName, lastName, businessName)
        );

        notification.setImageUrl(IMG_VENDOR);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl(null);

        return notification;
    }

    /**
     * ========== THÔNG BÁO ADMIN NHẬN ĐƠN VENDOR ==========
     */
    public static Notification createVendorApplicationAdminNotification(
            int adminId,
            String userFullName,
            String userEmail,
            String userPhone,
            String userGender,
            String businessName,
            String taxId,
            String bankAccount,
            String warehouseAddress,
            String productDescription) {

        Notification notification = new Notification();

        notification.setUserId(adminId);
        notification.setType(TYPE_CAREER);

        notification.setTitle("🏢 Đơn đăng ký Đối tác Kinh doanh mới!");

        // ========== MESSAGE ĐẦY ĐỦ THÔNG TIN ==========
        StringBuilder message = new StringBuilder();
        message.append("📋 ĐƠN ĐĂNG KÝ ĐỐI TÁC KINH DOANH\n\n");

        message.append("👤 THÔNG TIN CÁ NHÂN:\n");
        message.append(String.format("• Họ tên: %s\n", userFullName));
        message.append(String.format("• Email: %s\n", userEmail));
        message.append(String.format("• Số điện thoại: %s\n", userPhone));
        message.append(String.format("• Giới tính: %s\n\n", userGender));

        message.append("🏢 THÔNG TIN DOANH NGHIỆP:\n");
        message.append(String.format("• Tên doanh nghiệp: %s\n", businessName));
        message.append(String.format("• Mã số thuế: %s\n", taxId));
        message.append(String.format("• Số tài khoản ngân hàng: %s\n\n", bankAccount));

        if (warehouseAddress != null && !warehouseAddress.trim().isEmpty()) {
            message.append("📍 ĐỊA CHỈ KHO HÀNG:\n");
            message.append(String.format("• %s\n\n", warehouseAddress));
        }

        if (productDescription != null && !productDescription.trim().isEmpty()) {
            message.append("📦 GIỚI THIỆU SẢN PHẨM/DỊCH VỤ:\n");
            message.append(String.format("• %s\n\n", productDescription));
        }

        message.append("⚠️ Vui lòng vào hệ thống quản trị để xem chi tiết và phê duyệt đơn này.");

        notification.setMessage(message.toString());
        notification.setImageUrl(IMG_VENDOR);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl("/admin/career-applications");

        return notification;
    }


    /**
     * ========== THÔNG BÁO USER ĐÃ GỬI ĐƠN SHIPPER ==========
     */
    public static Notification createShipperApplicationUserNotification(int userId, String firstName, String lastName, String vehicleType) {
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setType(TYPE_CAREER);

        notification.setTitle("🚚 Đơn đăng ký Tài xế Giao hàng đã được gửi!");
        notification.setMessage(
                String.format("Xin chào %s %s! Cảm ơn bạn đã gửi đơn đăng ký trở thành Tài xế Giao hàng của VietTech " +
                                "với phương tiện \"%s\". Chúng tôi sẽ xem xét hồ sơ và liên hệ lại với bạn trong vòng 3-5 ngày làm việc. " +
                                "Vui lòng kiểm tra email để biết thêm chi tiết.",
                        firstName, lastName, vehicleType)
        );

        notification.setImageUrl(IMG_SHIPPER);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl(null);

        return notification;
    }
    /**
     * ========== THÔNG BÁO ADMIN NHẬN ĐƠN SHIPPER (FULL INFO) ==========
     * ← SỬA LẠI ĐỂ HIỂN THỊ ĐẦY ĐỦ THÔNG TIN
     */
    public static Notification createShipperApplicationAdminNotification(
            int adminId,
            String userFullName,
            String userEmail,
            String userPhone,
            String userGender,
            String licenseNumber,
            String vehicleType,
            String vehiclePlate,
            String currentAddress,
            String workExperience) {

        Notification notification = new Notification();

        notification.setUserId(adminId);
        notification.setType(TYPE_CAREER);

        notification.setTitle("🚚 Đơn đăng ký Tài xế Giao hàng mới!");

        // ========== MESSAGE ĐẦY ĐỦ THÔNG TIN ==========
        StringBuilder message = new StringBuilder();
        message.append("📋 ĐƠN ĐĂNG KÝ TÀI XẾ GIAO HÀNG\n\n");

        message.append("👤 THÔNG TIN CÁ NHÂN:\n");
        message.append(String.format("• Họ tên: %s\n", userFullName));
        message.append(String.format("• Email: %s\n", userEmail));
        message.append(String.format("• Số điện thoại: %s\n", userPhone));
        message.append(String.format("• Giới tính: %s\n\n", userGender));

        message.append("🚗 THÔNG TIN PHƯƠNG TIỆN:\n");
        message.append(String.format("• Số bằng lái xe: %s\n", licenseNumber));
        message.append(String.format("• Loại phương tiện: %s\n", vehicleType));
        message.append(String.format("• Biển số xe: %s\n\n", vehiclePlate));

        if (currentAddress != null && !currentAddress.trim().isEmpty()) {
            message.append("📍 ĐỊA CHỈ HIỆN TẠI:\n");
            message.append(String.format("• %s\n\n", currentAddress));
        }

        if (workExperience != null && !workExperience.trim().isEmpty()) {
            message.append("💼 KINH NGHIỆM LÀM VIỆC:\n");
            message.append(String.format("• %s\n\n", workExperience));
        }

        message.append("⚠️ Vui lòng vào hệ thống quản trị để xem chi tiết và phê duyệt đơn này.");

        notification.setMessage(message.toString());
        notification.setImageUrl(IMG_SHIPPER);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl("/admin/career-applications");

        return notification;
    }

    /**
     * ========== THÔNG BÁO USER ĐÃ GỬI TIN NHẮN CONTACT ==========
     */
    public static Notification createContactUserNotification(int userId, String fullName, String subject) {
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setType(TYPE_CONTACT);

        notification.setTitle("📩 Tin nhắn của bạn đã được gửi!");
        notification.setMessage(
                String.format("Xin chào %s! Cảm ơn bạn đã liên hệ với VietTech về chủ đề \"%s\". " +
                                "Chúng tôi đã nhận được tin nhắn và sẽ phản hồi qua email trong vòng 24 giờ làm việc. " +
                                "Vui lòng kiểm tra hộp thư để biết thêm chi tiết.",
                        fullName, subject)
        );

        notification.setImageUrl(IMG_CONTACT);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl(null);

        return notification;
    }

    /**
     * ========== THÔNG BÁO ADMIN NHẬN TIN NHẮN CONTACT ==========
     */
    public static Notification createContactAdminNotification(
            int adminId,
            String fullName,
            String email,
            String phone,
            String subject,
            String message) {

        Notification notification = new Notification();

        notification.setUserId(adminId);
        notification.setType(TYPE_CONTACT);

        notification.setTitle("📧 Tin nhắn liên hệ mới từ khách hàng!");

        // ========== MESSAGE ĐẦY ĐỦ THÔNG TIN ==========
        StringBuilder msg = new StringBuilder();
        msg.append("📬 TIN NHẮN LIÊN HỆ MỚI\n\n");

        msg.append("👤 THÔNG TIN KHÁCH HÀNG:\n");
        msg.append(String.format("• Họ tên: %s\n", fullName));
        msg.append(String.format("• Email: %s\n", email));
        msg.append(String.format("• Số điện thoại: %s\n\n", phone));

        msg.append("📋 CHỦ ĐỀ:\n");
        msg.append(String.format("• %s\n\n", subject));

        msg.append("💬 NỘI DUNG:\n");
        msg.append(String.format("• %s\n\n", message));

        msg.append("⚠️ Vui lòng phản hồi khách hàng trong vòng 24 giờ làm việc.");

        notification.setMessage(msg.toString());
        notification.setImageUrl(IMG_CONTACT);
        notification.setRead(false);
        notification.setReadAt(null);
        notification.setCreatedAt(new Date());
        notification.setExpiresAt(null);
        notification.setData(null);
        notification.setActionUrl("/admin/contact-messages");

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