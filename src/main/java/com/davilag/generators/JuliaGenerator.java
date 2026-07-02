package com.davilag.generators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class JuliaGenerator {
    private final int size, maxIter;
    private final String palette;

    public JuliaGenerator(int size, int maxIter, String palette) {
        this.size = size; this.maxIter = maxIter; this.palette = palette;
    }

    public void draw(GraphicsContext gc) {
        WritableImage img = new WritableImage(size, size);
        PixelWriter pw = img.getPixelWriter();
        double cx = -0.7269, cy = 0.1889;
        for (int px = 0; px < size; px++) {
            for (int py = 0; py < size; py++) {
                double x = (px - size / 2.0) * 3.5 / size;
                double y = (py - size / 2.0) * 3.5 / size;
                int iter = 0;
                while (x * x + y * y <= 4 && iter < maxIter) {
                    double xt = x * x - y * y + cx;
                    y = 2 * x * y + cy; x = xt; iter++;
                }
                pw.setColor(px, py, MandelbrotGenerator.colorize(iter, maxIter, palette));
            }
        }
        gc.drawImage(img, 0, 0);
    }
}
