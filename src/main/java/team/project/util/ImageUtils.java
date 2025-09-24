package team.project.util;

import java.util.Base64;

public final class ImageUtils {
    public static String encodeBase64Image(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(imageData);
    }

    public static String imageDataUri(byte[] imageData, String mimeType) {
        String base64Image = encodeBase64Image(imageData);

        if (base64Image == null) {
            return null;
        }
        return "data:" + mimeType + ";base64," + base64Image;
    }
}
