package com.davilag;

import com.davilag.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainWindow window = new MainWindow(primaryStage);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
