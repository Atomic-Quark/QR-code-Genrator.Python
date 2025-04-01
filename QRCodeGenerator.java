import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    private int size;
    private int border;
    private String fillColor;
    private String backColor;
    private int boxSize;

    public QRCodeGenerator() {
        this.size = 4;
        this.border = 4;
        this.fillColor = "black";
        this.backColor = "white";
        this.boxSize = 10;
    }

    public void setProperties(Integer size, Integer border, String fillColor, String backColor, Integer boxSize) {
        if (size != null) {
            this.size = size;
        }
        if (border != null) {
            this.border = border;
        }
        if (fillColor != null) {
            this.fillColor = fillColor;
        }
        if (backColor != null) {
            this.backColor = backColor;
        }
        if (boxSize != null) {
            this.boxSize = boxSize;
        }
    }

    public String generate(String data, String outputFile) throws WriterException, IOException {
        if (outputFile == null || outputFile.isEmpty()) {
            outputFile = "qrcode6.png";
        }

        // Set QR code properties
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // Equivalent to ERROR_CORRECT_H in Python
        hints.put(EncodeHintType.MARGIN, border);

        // Create QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 
                                                 boxSize * size * 6, boxSize * size * 6, hints);
        
        // Calculate dimensions including border
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        
        // Create image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        // Convert color strings to Color objects
        Color fill = parseColor(fillColor);
        Color back = parseColor(backColor);
        
        // Draw QR code
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(back);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(fill);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
        
        // Save image
        File qrFile = new File(outputFile);
        ImageIO.write(image, "png", qrFile);
        
        return qrFile.getAbsolutePath();
    }
    
    private Color parseColor(String colorName) {
        switch (colorName.toLowerCase()) {
            case "black":
                return Color.BLACK;
            case "white":
                return Color.WHITE;
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            // Add more colors as needed
            default:
                try {
                    return Color.decode(colorName); // For hex colors
                } catch (NumberFormatException e) {
                    return Color.BLACK; // Default
                }
        }
    }

    public static void main(String[] args) {
        QRCodeGenerator generator = new QRCodeGenerator();
        
        try {
            String outputPath = generator.generate(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlYxTsrHiM5jFyET2Cec3NTSRY7vMKIsQ1XQ&s", 
                "qrcode7.png"
            );
            System.out.println("QR code created at: " + outputPath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}