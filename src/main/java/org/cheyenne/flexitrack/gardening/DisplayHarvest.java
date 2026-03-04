package org.cheyenne.flexitrack.gardening;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class DisplayHarvest extends VBox {
    private final Harvest harvest = new Harvest();
    private final Label month = new Label();
    private final Label year = new Label();
    private final Label lifetime = new Label();
    private final TextField searchBar = new TextField();
    private final VBox content = new VBox();

    public DisplayHarvest() {
        Label title = new Label("HARVEST LOG");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 30;");

        month.setWrapText(true);
        year.setWrapText(true);
        lifetime.setWrapText(true);
        String style = """
            -fx-background-color: #E1FFEF;
            -fx-border-width: 1px;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-border-color: black;
            -fx-font-size: 20;
            -fx-padding: 10;
            -fx-text-alignment: center;
        """;
        month.setStyle(style);
        month.setAlignment(Pos.CENTER);
        year.setStyle(style);
        year.setAlignment(Pos.CENTER);
        lifetime.setStyle(style);
        lifetime.setAlignment(Pos.CENTER);
        month.setMaxWidth(Double.MAX_VALUE);
        year.setMaxWidth(Double.MAX_VALUE);
        lifetime.setMaxWidth(Double.MAX_VALUE);
        HBox summary = new HBox(10);
        summary.setMaxWidth(Double.MAX_VALUE);
        summary.getChildren().addAll(month, year, lifetime);
        HBox.setHgrow(month, Priority.ALWAYS);
        HBox.setHgrow(year, Priority.ALWAYS);
        HBox.setHgrow(lifetime, Priority.ALWAYS);

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
            harvest.Data(0, "Plant", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 1.00f);
            harvest.Create();
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

        ScrollPane harvestList = new ScrollPane(content);
        harvestList.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        harvestList.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        harvestList.setStyle("-fx-background-color: transparent;");
        harvestList.setFitToWidth(true);
        content.setSpacing(10);

        this.setSpacing(10);
        this.getChildren().addAll(title, summary, panel, harvestList);
        RefreshData();
    }

    public void RefreshData() {
        content.getChildren().clear();
        String[][] harvestList = harvest.getHarvest(searchBar.getText());
        for (String[] row : harvestList) {
            Log log = new Log(Integer.parseInt(row[0]), row[1], row[2], Float.parseFloat(row[3]), this);
            content.getChildren().add(log);
        }
        float[] sum = harvest.getSum(searchBar.getText());
        month.setText(new DecimalFormat("0.00").format(sum[0]) + "\nMonth");
        year.setText(new DecimalFormat("0.00").format(sum[1]) + "\nYear");
        lifetime.setText(new DecimalFormat("0.00").format(sum[2]) + "\nLifetime");
    }
}
