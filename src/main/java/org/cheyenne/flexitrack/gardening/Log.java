package org.cheyenne.flexitrack.gardening;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

public class Log extends HBox {
    private int harvestID;
    private String plant;
    private String date;
    private float worth;

    private final Harvest harvestEditor = new Harvest();
    private final DatePicker dateField = new DatePicker();
    private final TextField plantName = new TextField();
    private final TextField worthField = new TextField();
    
    public Log(int harvestID, String plant, String date, float worth, DisplayHarvest mainControl) {
        this.harvestID = harvestID;
        this.plant = plant;
        this.date = date;
        this.worth = worth;
        harvestEditor.Data(harvestID, plant, date, worth);

        this.setStyle("""
            -fx-background-color: #E1FFEF;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-border-width: 1;
            -fx-border-color: black;
        """);

        dateField.setValue(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateField.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        dateField.getEditor().setStyle("""
            -fx-background-color: transparent;
        """);
        dateField.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.08);
        dateField.setOnAction(event -> {
            this.date = dateField.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            harvestEditor.Data(this.harvestID, this.plant, this.date, this.worth);
            harvestEditor.Update();
            mainControl.RefreshData();
        });

        plantName.setText(plant);
        plantName.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        plantName.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.5);
        plantName.setEditable(false);
        plantName.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                plantName.setEditable(true);
            } else if (!plantName.getText().isBlank()) {
                plantName.setEditable(false);
                this.plant = plantName.getText();
                harvestEditor.Data(this.harvestID, this.plant, this.date, this.worth);
                harvestEditor.Update();
                mainControl.RefreshData();
            }
        });

        worthField.setText(new DecimalFormat("0.00").format(worth));
        worthField.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        worthField.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.2);
        worthField.setEditable(false);
        worthField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                worthField.setEditable(true);
            } else if (!worthField.getText().isBlank() && worthField.getText().matches("^\\d+(\\.\\d{2})$")) {
                this.worth = Float.parseFloat(worthField.getText());
                harvestEditor.Data(this.harvestID, this.plant, this.date, this.worth);
                harvestEditor.Update();
                worthField.setEditable(false);
                mainControl.RefreshData();
            }
        });

        ContextMenu menu = new ContextMenu();
        MenuItem deleteLog = new MenuItem("Delete");
        deleteLog.setOnAction(event -> {
            harvestEditor.Delete();
            mainControl.RefreshData();
        });
        menu.getItems().add(deleteLog);
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(this, event.getScreenX(), event.getScreenY());
            } else {
                menu.hide();
            }
        });
        this.getChildren().addAll(dateField, plantName, worthField);
    }
}
