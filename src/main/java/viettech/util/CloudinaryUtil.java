package viettech.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class CloudinaryUtil {

    private static Cloudinary cloudinary;

    // --- CẤU HÌNH ---
    private static final String CLOUD_NAME = "dzjlcbwwh";
    private static final String API_KEY = "951369439215336";
    private static final String API_SECRET = "0PfC_2eS_0jQqAbk2FwViXRQUwQ";

    private static final String AVATAR_FOLDER = "viettech/avatars";
    private static final String PRODUCT_FOLDER = "viettech/products";

    private static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            try {
                cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", CLOUD_NAME,
                        "api_key", API_KEY,
                        "api_secret", API_SECRET,
                        "secure", true
                ));
                System.out.println("✅ Cloudinary initialized successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Không thể khởi tạo Cloudinary.");
            }
        }
        return cloudinary;
    }

    /**
     * 1. Upload Avatar
     */
    public static String uploadAvatar(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) return null;

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            System.err.println("File không phải là ảnh!");
            return null;
        }

        try (InputStream is = filePart.getInputStream()) { // Dùng try-with-resources để tự đóng luồng
            String publicId = AVATAR_FOLDER + "/" + UUID.randomUUID().toString();

            // SỬA LỖI Ở ĐÂY: Thêm .readAllBytes()
            Map uploadResult = getCloudinary().uploader().upload(is.readAllBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", AVATAR_FOLDER,
                            "resource_type", "image",
                            "overwrite", false,
                            "width", 500,
                            "height", 500,
                            "crop", "fill",
                            "gravity", "face",
                            "fetch_format", "auto",
                            "quality", "auto"
                    )
            );
            return (String) uploadResult.get("secure_url");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 2. Upload ảnh sản phẩm
     */
    public static String uploadProductImage(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) return null;

        try (InputStream is = filePart.getInputStream()) {
            String publicId = PRODUCT_FOLDER + "/" + UUID.randomUUID().toString();

            // SỬA LỖI Ở ĐÂY: Thêm .readAllBytes()
            Map uploadResult = getCloudinary().uploader().upload(is.readAllBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", PRODUCT_FOLDER,
                            "resource_type", "image",
                            "width", 1200,
                            "crop", "limit",
                            "fetch_format", "auto",
                            "quality", "auto"
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 3. Upload ảnh chung
     */
    public static String uploadImage(Part filePart, String folder) throws IOException {
        if (filePart == null || filePart.getSize() == 0) return null;

        try (InputStream is = filePart.getInputStream()) {
            String publicId = folder + "/" + UUID.randomUUID().toString();

            // SỬA LỖI Ở ĐÂY: Thêm .readAllBytes()
            Map uploadResult = getCloudinary().uploader().upload(is.readAllBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", folder,
                            "resource_type", "image",
                            "overwrite", false
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 4. Xóa ảnh (Giữ nguyên)
     */
    public static boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return false;
        if (!imageUrl.contains("cloudinary.com")) return false;

        try {
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId != null && !publicId.isEmpty()) {
                Map result = getCloudinary().uploader().destroy(publicId, ObjectUtils.emptyMap());
                String status = (String) result.get("result");
                if ("ok".equals(status)) {
                    System.out.println("✅ Đã xóa ảnh: " + publicId);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String extractPublicIdFromUrl(String url) {
        try {
            String[] parts = url.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];
                path = path.replaceFirst("v\\d+/", "");
                path = path.replaceFirst("\\.[^.]+$", "");
                return path;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}