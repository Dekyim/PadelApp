package org.example.apiweb;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    static {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "doqev0ese",
                "api_key", "992389942268255",
                "api_secret", "g5I-mfE8ul4J-dHiKQuH_lEYjQQ"
        ));
    }

    public static Cloudinary getInstance() {
        return cloudinary;
    }
}
