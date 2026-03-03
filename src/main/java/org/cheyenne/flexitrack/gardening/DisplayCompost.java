package org.cheyenne.flexitrack.gardening;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class DisplayCompost extends VBox {
    private final FlowPane content = new FlowPane();
    private final Compost compost = new Compost();

    public DisplayCompost() {
        Label title = new Label("COMPOST SCHEDULE");
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
            compost.Pot(0, "Pot", "Empty", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            compost.Create();
            RefreshData();
        });

        ScrollPane potList = new ScrollPane(content);
        potList.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        potList.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        potList.setStyle("-fx-background-color: transparent;");
        potList.setFitToWidth(true);

        RefreshData();
        this.setSpacing(10);
        this.getChildren().addAll(title, addButton, potList);
    }

    public void RefreshData() {
        content.getChildren().clear();
        String[][] pots = compost.getCompost();
        for (String[] row : pots) {
            Pot data = new Pot(Integer.parseInt(row[0]), row[1], row[2], row[3], this);
            FlowPane.setMargin(data, new Insets(20, 20, 20, 0));
            content.getChildren().add(data);
        }
    }
}
