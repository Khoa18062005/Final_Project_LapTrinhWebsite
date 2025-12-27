package viettech.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.util.Map;

public class CloudinaryUtil {
    // Thay thế các thông tin này bằng thông tin trong Dashboard Cloudinary của bạn
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dzjlcbwwh",
            "api_key", "951369439215336",
            "api_secret", "0PfC_2eS_0jQqAbk2FwViXRQUwQ",
            "secure", true
    ));

    public static String uploadImage(File file) throws Exception {
        // Sử dụng upload_preset "upload_img" mà bạn đã tạo trong ảnh
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                "upload_preset", "upload_img",
                "folder", "img" // Lưu vào folder img như bạn đã cấu hình
        ));

        // Trả về URL bảo mật (https) để lưu vào MySQL
        return uploadResult.get("secure_url").toString();
    }
}