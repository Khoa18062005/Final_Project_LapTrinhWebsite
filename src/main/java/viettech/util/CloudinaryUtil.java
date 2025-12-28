package viettech.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class để xử lý upload ảnh lên Cloudinary CDN
 */
public class CloudinaryUtil {

    private static Cloudinary cloudinary;
    private static final String AVATAR_FOLDER = "viettech/avatars";
    private static final String PRODUCT_FOLDER = "viettech/products";

    /**
     * Khởi tạo Cloudinary instance (Singleton pattern)
     */
    private static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            try {
                // Đọc config từ System Environment Variables
                String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
                String apiKey = System.getenv("CLOUDINARY_API_KEY");
                String apiSecret = System.getenv("CLOUDINARY_API_SECRET");

                // Validate config
                if (cloudName == null || cloudName.isEmpty()) {
                    throw new RuntimeException("CLOUDINARY_CLOUD_NAME không được cấu hình!");
                }
                if (apiKey == null || apiKey.isEmpty()) {
                    throw new RuntimeException("CLOUDINARY_API_KEY không được cấu hình!");
                }
                if (apiSecret == null || apiSecret.isEmpty()) {
                    throw new RuntimeException("CLOUDINARY_API_SECRET không được cấu hình!");
                }

                // Khởi tạo Cloudinary
                cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret,
                        "secure", true
                ));

                System.out.println("✅ Cloudinary initialized successfully!");
                System.out.println("   Cloud Name: " + cloudName);

            } catch (Exception e) {
                System.err.println("❌ Lỗi khởi tạo Cloudinary: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Không thể khởi tạo Cloudinary: " + e.getMessage());
            }
        }
        return cloudinary;
    }

    /**
     * Upload ảnh từ Part (multipart/form-data) lên Cloudinary
     * @param filePart - Part từ request
     * @param folder - Thư mục lưu trên Cloudinary (avatars, products, ...)
     * @return URL của ảnh đã upload
     */
    public static String uploadImage(Part filePart, String folder) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IllegalArgumentException("File upload không hợp lệ!");
        }

        // Validate file type
        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File không phải là ảnh!");
        }

        // Validate file size (max 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (filePart.getSize() > maxSize) {
            throw new IllegalArgumentException("Kích thước file vượt quá 5MB!");
        }

        try (InputStream inputStream = filePart.getInputStream()) {
            // Tạo public_id unique
            String publicId = folder + "/" + UUID.randomUUID().toString();

            // Upload lên Cloudinary
            Map uploadResult = getCloudinary().uploader().upload(inputStream.readAllBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", folder,
                            "resource_type", "image",
                            "overwrite", false,
                            "transformation", ObjectUtils.asMap(
                                    "quality", "auto:good",
                                    "fetch_format", "auto"
                            )
                    )
            );

            // Lấy URL của ảnh
            String imageUrl = (String) uploadResult.get("secure_url");
            System.out.println("✅ Upload thành công: " + imageUrl);

            return imageUrl;

        } catch (Exception e) {
            System.err.println("❌ Lỗi upload ảnh: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Upload ảnh thất bại: " + e.getMessage());
        }
    }

    /**
     * Upload avatar (tối ưu cho ảnh đại diện)
     */
    /**
     * Upload avatar (tối ưu cho ảnh đại diện)
     */
    public static String uploadAvatar(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IllegalArgumentException("Vui lòng chọn file ảnh!");
        }

        // Validate file type
        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File không phải là ảnh!");
        }

        // Validate file size (max 5MB)
        if (filePart.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Kích thước file vượt quá 5MB!");
        }

        try (InputStream inputStream = filePart.getInputStream()) {
            String publicId = AVATAR_FOLDER + "/" + UUID.randomUUID().toString();

            // ✅ FIX: Transformation phải là Map đơn giản, KHÔNG lồng nhau
            Map uploadResult = getCloudinary().uploader().upload(inputStream.readAllBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", AVATAR_FOLDER,
                            "resource_type", "image",
                            "overwrite", false,
                            "width", 500,                    // ← FIX: Đưa ra ngoài
                            "height", 500,                   // ← FIX: Đưa ra ngoài
                            "crop", "fill",                  // ← FIX: Đưa ra ngoài
                            "gravity", "face",               // ← FIX: Đưa ra ngoài
                            "quality", "auto:good",          // ← FIX: Đưa ra ngoài
                            "fetch_format", "auto"           // ← FIX: Đưa ra ngoài
                    )
            );

            String imageUrl = (String) uploadResult.get("secure_url");
            System.out.println("✅ Upload avatar thành công: " + imageUrl);

            return imageUrl;

        } catch (IllegalArgumentException e) {
            throw e; // Ném lại validation errors
        } catch (Exception e) {
            System.err.println("❌ Lỗi upload avatar: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Upload avatar thất bại: " + e.getMessage());
        }
    }

    /**
     * Upload ảnh sản phẩm (giữ nguyên tỉ lệ)
     */
    public static String uploadProductImage(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IllegalArgumentException("Vui lòng chọn file ảnh!");
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File không phải là ảnh!");
        }

        if (filePart.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Kích thước file vượt quá 5MB!");
        }

        try (InputStream inputStream = filePart.getInputStream()) {
            String publicId = PRODUCT_FOLDER + "/" + UUID.randomUUID().toString();

            Map uploadResult = getCloudinary().uploader().upload(inputStream.readAllBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", PRODUCT_FOLDER,
                            "resource_type", "image",
                            "overwrite", false,
                            "width", 1200,                   // ← FIX: Đưa ra ngoài
                            "quality", "auto:good",          // ← FIX
                            "fetch_format", "auto"           // ← FIX
                    )
            );

            String imageUrl = (String) uploadResult.get("secure_url");
            System.out.println("✅ Upload product image thành công: " + imageUrl);

            return imageUrl;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Lỗi upload product image: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Upload product image thất bại: " + e.getMessage());
        }
    }

    /**
     * Xóa ảnh từ Cloudinary theo URL
     */
    public static boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return false;
        }

        // Chỉ xóa nếu là ảnh từ Cloudinary
        if (!imageUrl.contains("cloudinary.com")) {
            System.out.println("⚠️ URL không phải từ Cloudinary, bỏ qua xóa: " + imageUrl);
            return false;
        }

        try {
            // Extract public_id từ URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            
            if (publicId != null && !publicId.isEmpty()) {
                Map result = getCloudinary().uploader().destroy(publicId, ObjectUtils.emptyMap());
                String status = (String) result.get("result");
                
                if ("ok".equals(status)) {
                    System.out.println("✅ Xóa ảnh thành công: " + publicId);
                    return true;
                } else {
                    System.out.println("⚠️ Không thể xóa ảnh: " + status);
                    return false;
                }
            }
            
            return false;

        } catch (Exception e) {
            System.err.println("❌ Lỗi xóa ảnh: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract public_id từ Cloudinary URL
     * VD: https://res.cloudinary.com/demo/image/upload/v1234567890/viettech/avatars/abc123.jpg
     * -> viettech/avatars/abc123
     */
    private static String extractPublicIdFromUrl(String url) {
        try {
            // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{format}
            String[] parts = url.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];
                // Bỏ version (v1234567890)
                path = path.replaceFirst("v\\d+/", "");
                // Bỏ extension (.jpg, .png)
                path = path.replaceFirst("\\.[^.]+$", "");
                return path;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi extract public_id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}