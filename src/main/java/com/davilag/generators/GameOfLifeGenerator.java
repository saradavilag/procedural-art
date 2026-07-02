package com.davilag.generators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameOfLifeGenerator {
    private final int size;
    private static final int CELL = 4;

    public GameOfLifeGenerator(int size) { this.size = size; }

    public void draw(GraphicsContext gc) {
        int cols = size / CELL, rows = size / CELL;
        boolean[][] grid = new boolean[cols][rows];

        for (int x = 0; x < cols; x++)
            for (int y = 0; y < rows; y++)
                grid[x][y] = Math.random() < 0.3;

        gc.setFill(Color.color(0.05, 0.05, 0.1));
        gc.fillRect(0, 0, size, size);

        for (int gen = 0; gen < 80; gen++) {
            boolean[][] next = new boolean[cols][rows];
            for (int x = 0; x < cols; x++) {
                for (int y = 0; y < rows; y++) {
                    int n = countNeighbors(grid, x, y, cols, rows);
                    next[x][y] = grid[x][y] ? (n == 2 || n == 3) : (n == 3);
                }
            }
            grid = next;
        }

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (grid[x][y]) {
                    double t = (double)(x + y) / (cols + rows);
                    gc.setFill(Color.color(0.2 + t * 0.5, 0.8 - t * 0.3, 1.0 - t * 0.5, 0.9));
                    gc.fillRect(x * CELL, y * CELL, CELL - 1, CELL - 1);
                }
            }
        }
    }

    private int countNeighbors(boolean[][] g, int x, int y, int cols, int rows) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = (x + dx + cols) % cols, ny = (y + dy + rows) % rows;
                if (g[nx][ny]) count++;
            }
        return count;
    }
}
