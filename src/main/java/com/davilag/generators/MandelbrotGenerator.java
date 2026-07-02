package com.davilag.generators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MandelbrotGenerator {
    private final int size, maxIter;
    private final String palette;

    public MandelbrotGenerator(int size, int maxIter, String palette) {
        this.size = size; this.maxIter = maxIter; this.palette = palette;
    }

    public void draw(GraphicsContext gc) {
        WritableImage img = new WritableImage(size, size);
        PixelWriter pw = img.getPixelWriter();
        double xMin = -2.5, xMax = 1.0, yMin = -1.25, yMax = 1.25;
        for (int px = 0; px < size; px++) {
            for (int py = 0; py < size; py++) {
                double x0 = xMin + (xMax - xMin) * px / size;
                double y0 = yMin + (yMax - yMin) * py / size;
                double x = 0, y = 0; int iter = 0;
                while (x * x + y * y <= 4 && iter < maxIter) {
                    double xt = x * x - y * y + x0;
                    y = 2 * x * y + y0; x = xt; iter++;
                }
                pw.setColor(px, py, colorize(iter, maxIter, palette));
            }
        }
        gc.drawImage(img, 0, 0);
    }

    public static Color colorize(int iter, int maxIter, String palette) {
        if (iter == maxIter) return Color.BLACK;
        double t = (double) iter / maxIter;
        switch (palette) {
            case "Fire":   return Color.color(Math.min(1, t * 2), Math.max(0, t * 2 - 0.5), 0);
            case "Ocean":  return Color.color(0, t * 0.5, t);
            case "Forest": return Color.color(0, t * 0.8, t * 0.3);
            case "Neon":   return Color.color(Math.abs(Math.sin(t * Math.PI * 3)), 0, Math.abs(Math.cos(t * Math.PI * 3)));
            default:       return Color.color(t * 0.5, t * 0.2, t);
        }
    }
}
