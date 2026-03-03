package org.cheyenne.flexitrack;

import org.cheyenne.flexitrack.gardening.DisplayGardening;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        DisplayGardening test = new DisplayGardening();
        Scene scene = test.createScene();
        stage.setTitle("Gardening");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
