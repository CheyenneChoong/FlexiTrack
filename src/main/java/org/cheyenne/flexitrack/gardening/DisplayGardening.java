package org.cheyenne.flexitrack.gardening;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class DisplayGardening {
    public Scene createScene() {
        BorderPane root = new BorderPane();

        HBox navigation = new HBox(10);
        navigation.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.1);
        navigation.setStyle("-fx-background-color: #0F9964");
        navigation.setAlignment(Pos.CENTER);
        root.setBottom(navigation);

        String buttonRegularStyle = """
        -fx-background-color: #88EAA4;
        -fx-font-size: 18;
        -fx-font-weight: bold;
        """;
        String buttonHoverStyle = """
        -fx-background-color: #209641;
        -fx-font-size: 18;
        -fx-font-weight: bold;
        """;

        Button compostTab = new Button("Compost Schedule");
        compostTab.setStyle(buttonRegularStyle);
        compostTab.setMaxWidth(Double.MAX_VALUE);
        compostTab.setOnMouseEntered(event -> compostTab.setStyle(buttonHoverStyle));
        compostTab.setOnMouseExited(event -> compostTab.setStyle(buttonRegularStyle));
        compostTab.setOnMouseClicked(event -> {
            DisplayCompost compost = new DisplayCompost();
            root.setCenter(compost);
            BorderPane.setMargin(compost, new Insets(50));
        });

        Button replantTab = new Button("Replant Schedule");
        replantTab.setStyle(buttonRegularStyle);
        replantTab.setMaxWidth(Double.MAX_VALUE);
        replantTab.setOnMouseEntered(event -> replantTab.setStyle(buttonHoverStyle));
        replantTab.setOnMouseExited(event -> replantTab.setStyle(buttonRegularStyle));
        replantTab.setOnMouseClicked(event -> {
            DisplayReplant replant = new DisplayReplant();
            root.setCenter(replant);
            BorderPane.setMargin(replant, new Insets(50));
        });

        Button harvestTab = new Button("Harvest Log");
        harvestTab.setStyle(buttonRegularStyle);
        harvestTab.setMaxWidth(Double.MAX_VALUE);
        harvestTab.setOnMouseEntered(event -> harvestTab.setStyle(buttonHoverStyle));
        harvestTab.setOnMouseExited(event -> harvestTab.setStyle(buttonRegularStyle));
        
        navigation.getChildren().addAll(compostTab, replantTab, harvestTab);
        HBox.setMargin(compostTab, new Insets(0, 20, 0, 20));
        HBox.setMargin(harvestTab, new Insets(0, 20, 0, 20));
        HBox.setHgrow(compostTab, Priority.ALWAYS);
        HBox.setHgrow(replantTab, Priority.ALWAYS);
        HBox.setHgrow(harvestTab, Priority.ALWAYS);

        Scene scene = new Scene(root, Color.WHITE);
        return scene;
    }
}
