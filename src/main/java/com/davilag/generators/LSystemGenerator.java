package com.davilag.generators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LSystemGenerator {
    private final int size;

    public LSystemGenerator(int size) { this.size = size; }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.color(0.05, 0.08, 0.05));
        gc.fillRect(0, 0, size, size);

        String axiom = "F";
        Map<Character, String> rules = new HashMap<>();
        rules.put('F', "FF+[+F-F-F]-[-F+F+F]");

        String sentence = axiom;
        for (int i = 0; i < 4; i++) {
            StringBuilder sb = new StringBuilder();
            for (char c : sentence.toCharArray())
                sb.append(rules.containsKey(c) ? rules.get(c) : String.valueOf(c));
            sentence = sb.toString();
        }

        double angle = Math.toRadians(25);
        double len = 6;
        double x = size / 2.0, y = size * 0.95;
        double dir = -Math.PI / 2;
        Stack<double[]> stack = new Stack<>();

        for (char c : sentence.toCharArray()) {
            if (c == 'F') {
                double nx = x + len * Math.cos(dir);
                double ny = y + len * Math.sin(dir);
                double depth = stack.size() / 8.0;
                gc.setStroke(Color.color(0.1 + depth * 0.3, 0.5 + depth * 0.4, 0.1, 0.8));
                gc.setLineWidth(Math.max(0.5, 2 - stack.size() * 0.3));
                gc.strokeLine(x, y, nx, ny);
                x = nx; y = ny;
            } else if (c == '+') {
                dir += angle;
            } else if (c == '-') {
                dir -= angle;
            } else if (c == '[') {
                stack.push(new double[]{x, y, dir});
            } else if (c == ']') {
                double[] s = stack.pop(); x = s[0]; y = s[1]; dir = s[2];
            }
        }
    }
}
