package org.cheyenne.flexitrack;

import org.cheyenne.flexitrack.reminder.DisplayReminder;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("FlexiTrack");
        DisplayReminder home = new DisplayReminder();
        stage.setScene(home.createScene());
        stage.setMaximized(true);
        stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
