package main;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class UtilityTool {
    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        // Create a new image with ARGB type to ensure color support
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics();
        
        // Set high-quality rendering hints
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        
        // Draw the original image onto the scaled image
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        
        // If the original image was grayscale, convert the scaled image back to grayscale
        if (original.getType() == BufferedImage.TYPE_BYTE_GRAY || 
            original.getType() == BufferedImage.TYPE_USHORT_GRAY) {
            BufferedImage grayImage = new BufferedImage(width, height, original.getType());
            Graphics2D g2Gray = grayImage.createGraphics();
            g2Gray.drawImage(scaledImage, 0, 0, null);
            g2Gray.dispose();
            return grayImage;
        }
        
        return scaledImage;
    }
}
