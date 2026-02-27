package org.cheyenne.flexitrack;

import org.cheyenne.flexitrack.inventory.DisplayInventory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        DisplayInventory test = new DisplayInventory();
        Scene scene = test.createScene();
        stage.setTitle("Inventory");
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
