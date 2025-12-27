package viettech.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {

    private static Cloudinary cloudinary;

    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dzjlcbwwh",
                    "api_key",    "951369439215336",
                    "api_secret", "0PfC_2eS_0jQqAbk2FwViXRQUwQ",
                    "secure", true
            ));
        }
        return cloudinary;
    }
}