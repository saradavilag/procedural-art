package com.davilag.ui;

import com.davilag.generators.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainWindow {

    private static final int CANVAS_SIZE = 700;
    private final Stage stage;
    private Canvas canvas;
    private GraphicsContext gc;

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        gc = canvas.getGraphicsContext2D();

        VBox controls = buildControls();
        controls.setPrefWidth(220);
        controls.setStyle("-fx-background-color: #1a1a2e; -fx-padding: 20;");

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setRight(controls);
        root.setStyle("-fx-background-color: #0f0f1a;");

        Scene scene = new Scene(root);
        stage.setTitle("Procedural Art Generator · Davila.G");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        new MandelbrotGenerator(CANVAS_SIZE, 100, "Cosmic").draw(gc);
    }

    private VBox buildControls() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        Label title = new Label("PROCEDURAL ART");
        title.setStyle("-fx-text-fill: #e8c860; -fx-font-size: 13px; -fx-font-weight: bold; -fx-font-family: monospace;");

        Label subtitle = new Label("Generator v1.0");
        subtitle.setStyle("-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-family: monospace;");

        Separator sep1 = new Separator();

        Label modeLabel = new Label("MODE");
        modeLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 10px; -fx-font-family: monospace;");

        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton btnMandelbrot = styledRadio("Mandelbrot", modeGroup);
        RadioButton btnJulia      = styledRadio("Julia Set", modeGroup);
        RadioButton btnLSystem    = styledRadio("L-System", modeGroup);
        RadioButton btnLife       = styledRadio("Game of Life", modeGroup);
        RadioButton btnPerlin     = styledRadio("Perlin Noise", modeGroup);
        btnMandelbrot.setSelected(true);

        Label iterLabel = new Label("ITERATIONS");
        iterLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 10px; -fx-font-family: monospace;");
        Slider iterSlider = new Slider(50, 500, 100);
        Label iterValue = new Label("100");
        iterValue.setStyle("-fx-text-fill: #e8c860; -fx-font-family: monospace; -fx-font-size: 10px;");
        iterSlider.valueProperty().addListener((obs, old, val) ->
            iterValue.setText(String.valueOf(val.intValue())));

        Label paletteLabel = new Label("COLOR PALETTE");
        paletteLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 10px; -fx-font-family: monospace;");
        ComboBox<String> paletteBox = new ComboBox<>();
        paletteBox.getItems().addAll("Cosmic", "Fire", "Ocean", "Forest", "Neon");
        paletteBox.setValue("Cosmic");
        paletteBox.setMaxWidth(Double.MAX_VALUE);

        Separator sep2 = new Separator();

        Button btnGenerate = styledButton("GENERATE", "#e8c860", "#1a1a2e");
        Button btnRandom   = styledButton("RANDOM",   "#2a2a4a", "#8888ff");

        Label status = new Label("Ready.");
        status.setStyle("-fx-text-fill: #555; -fx-font-size: 9px; -fx-font-family: monospace;");
        status.setWrapText(true);

        btnGenerate.setOnAction(e -> {
            String mode    = ((RadioButton) modeGroup.getSelectedToggle()).getText();
            int iters      = (int) iterSlider.getValue();
            String palette = paletteBox.getValue();
            status.setText("Generating...");
            generate(mode, iters, palette);
            status.setText("Done: " + mode);
        });

        btnRandom.setOnAction(e -> {
            String[] modes    = {"Mandelbrot","Julia Set","L-System","Game of Life","Perlin Noise"};
            String[] palettes = {"Cosmic","Fire","Ocean","Forest","Neon"};
            String m = modes[(int)(Math.random() * modes.length)];
            String p = palettes[(int)(Math.random() * palettes.length)];
            paletteBox.setValue(p);
            generate(m, (int) iterSlider.getValue(), p);
            status.setText("Random: " + m);
        });

        box.getChildren().addAll(
            title, subtitle, sep1,
            modeLabel, btnMandelbrot, btnJulia, btnLSystem, btnLife, btnPerlin,
            iterLabel, iterSlider, iterValue,
            paletteLabel, paletteBox,
            sep2, btnGenerate, btnRandom, status
        );

        return box;
    }

    private void generate(String mode, int iterations, String palette) {
        gc.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
        if (mode.equals("Mandelbrot")) {
            new MandelbrotGenerator(CANVAS_SIZE, iterations, palette).draw(gc);
        } else if (mode.equals("Julia Set")) {
            new JuliaGenerator(CANVAS_SIZE, iterations, palette).draw(gc);
        } else if (mode.equals("L-System")) {
            new LSystemGenerator(CANVAS_SIZE).draw(gc);
        } else if (mode.equals("Game of Life")) {
            new GameOfLifeGenerator(CANVAS_SIZE).draw(gc);
        } else if (mode.equals("Perlin Noise")) {
            new PerlinGenerator(CANVAS_SIZE, palette).draw(gc);
        }
    }

    private RadioButton styledRadio(String text, ToggleGroup group) {
        RadioButton rb = new RadioButton(text);
        rb.setToggleGroup(group);
        rb.setStyle("-fx-text-fill: #ccc; -fx-font-family: monospace; -fx-font-size: 11px;");
        return rb;
    }

    private Button styledButton(String text, String bg, String fg) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg +
                   "; -fx-font-weight: bold; -fx-font-family: monospace; -fx-cursor: hand;");
        return b;
    }
}
