package org.cheyenne.flexitrack.gardening;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class DisplayReplant extends VBox {
    private final FlowPane content = new FlowPane();
    private final Replant replant = new Replant();
    private final TextField searchBar = new TextField();

    public DisplayReplant() {
        Label title = new Label("REPLANT SCHEDULE");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 30;");
        
        Button addButton = new Button("+");
        addButton.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 18;
            -fx-background-color: #88EAA4;
        """);
        addButton.setOnMouseEntered(event -> addButton.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 18;
            -fx-background-color: #209641;
        """));
        addButton.setOnMouseExited(event -> addButton.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 18;
            -fx-background-color: #88EAA4;
        """));
        addButton.setOnMouseClicked(event -> {
            replant.Data(0, "Plant", "Container", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            replant.Create();
            RefreshData();
        });

        searchBar.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.24);
        searchBar.setStyle("""
            -fx-border-color: black;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-border-width: 1px;
            -fx-font-size: 18;
        """);
        searchBar.setPromptText("Search...");

        Button searchButton = new Button("Search");
        searchButton.setStyle("""
            -fx-background-color: #88EAA4;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """);
        searchButton.setOnMouseEntered(event -> searchButton.setStyle("""
            -fx-background-color: #209641;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        searchButton.setOnMouseExited(event -> searchButton.setStyle("""
            -fx-background-color: #88EAA4;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        searchButton.setOnMouseClicked(event -> RefreshData());

        HBox searchPanel = new HBox(10);
        searchPanel.getChildren().addAll(searchBar, searchButton);
        BorderPane panel = new BorderPane();
        panel.setLeft(addButton);
        panel.setRight(searchPanel);

        ScrollPane plantList = new ScrollPane(content);
        plantList.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        plantList.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        plantList.setStyle("-fx-background-color: transparent;");
        plantList.setFitToWidth(true);

        this.setSpacing(10);
        this.getChildren().addAll(title, panel, plantList);
        RefreshData();
    }

    public void RefreshData() {
        content.getChildren().clear();
        String[][] replantList = replant.getReplant(searchBar.getText());
        for (String[] row : replantList) {
            Plant plant = new Plant(Integer.parseInt(row[0]), row[1], row[2], row[3], this);
            FlowPane.setMargin(plant, new Insets(20, 20, 20, 0));
            content.getChildren().add(plant);
        }
    }
}
