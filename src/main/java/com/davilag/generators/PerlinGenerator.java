package com.davilag.generators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PerlinGenerator {
    private final int size;
    private final String palette;
    private final int[] perm;

    public PerlinGenerator(int size, String palette) {
        this.size = size; this.palette = palette;
        perm = new int[512];
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) p[i] = i;
        for (int i = 255; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            int tmp = p[i]; p[i] = p[j]; p[j] = tmp;
        }
        for (int i = 0; i < 512; i++) perm[i] = p[i & 255];
    }

    public void draw(GraphicsContext gc) {
        WritableImage img = new WritableImage(size, size);
        PixelWriter pw = img.getPixelWriter();
        double scale = 4.0;
        for (int px = 0; px < size; px++) {
            for (int py = 0; py < size; py++) {
                double nx = px / (double) size * scale;
                double ny = py / (double) size * scale;
                double v  = (noise(nx, ny) + 1) / 2.0;
                double v2 = (noise(nx * 2 + 5, ny * 2 + 5) + 1) / 2.0;
                double combined = v * 0.7 + v2 * 0.3;
                pw.setColor(px, py, terrainColor(combined));
            }
        }
        gc.drawImage(img, 0, 0);
    }

    private Color terrainColor(double t) {
        switch (palette) {
            case "Ocean":
                if (t < 0.4) return Color.color(0, 0.1 + t, 0.4 + t * 0.5);
                if (t < 0.5) return Color.color(0.8, 0.75, 0.5);
                return Color.color(0.1 + t * 0.3, 0.4 + t * 0.3, 0.1);
            case "Fire":   return Color.color(Math.min(1, t * 2), Math.max(0, t * 2 - 0.5), 0);
            case "Neon":   return Color.color(Math.abs(Math.sin(t * Math.PI)), t, Math.abs(Math.cos(t * Math.PI)));
            case "Forest": return Color.color(t * 0.3, 0.2 + t * 0.7, t * 0.2);
            default:       return Color.color(t * 0.4 + 0.1, t * 0.1, t * 0.6 + 0.2);
        }
    }

    private double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255, Y = (int) Math.floor(y) & 255;
        x -= Math.floor(x); y -= Math.floor(y);
        double u = fade(x), v = fade(y);
        int a = perm[X] + Y, b = perm[X + 1] + Y;
        return lerp(v, lerp(u, grad(perm[a], x, y), grad(perm[b], x - 1, y)),
                       lerp(u, grad(perm[a + 1], x, y - 1), grad(perm[b + 1], x - 1, y - 1)));
    }

    private double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
    private double lerp(double t, double a, double b) { return a + t * (b - a); }
    private double grad(int hash, double x, double y) {
        switch (hash & 3) {
            case 0: return  x + y;
            case 1: return -x + y;
            case 2: return  x - y;
            default: return -x - y;
        }
    }
}
