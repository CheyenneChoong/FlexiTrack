package org.cheyenne.flexitrack.gardening;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class Plant extends VBox {
    private int replantID;
    private String plant;
    private String container;
    private String date;
    private final Replant replantEditor = new Replant();

    private final TextField plantName = new TextField();
    private final TextField containerName = new TextField();
    private final TextField dateInput = new TextField();
    private final Label days = new Label();
    private final Button harvestButton = new Button("Harvest");

    public Plant(int replantID, String plant, String container, String date, DisplayReplant mainControl) {
        this.replantID = replantID;
        this.plant = plant;
        this.container = container;
        this.date = date;
        this.setStyle("""
            -fx-background-color: #E1FFEF;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-border-width: 1;
            -fx-border-color: black;
        """);
        this.setSpacing(10);
        replantEditor.Data(this.replantID, this.plant, this.container, this.date);
        
        plantName.setText(plant);
        plantName.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 25;
            -fx-font-weight: bold;
        """);
        plantName.setEditable(false);
        plantName.setPadding(new Insets(0));
        plantName.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                plantName.setEditable(true);
            } else if (!plantName.getText().isBlank()) {
                this.plant = plantName.getText();
                replantEditor.Data(this.replantID, this.plant, this.container, this.date);
                replantEditor.Update();
                plantName.setEditable(false);
            }
        });

        containerName.setText(container);
        containerName.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        containerName.setEditable(false);
        containerName.setPadding(new Insets(0));
        containerName.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                containerName.setEditable(true);
            } else if (!containerName.getText().isBlank()) {
                this.container = containerName.getText();
                replantEditor.Data(this.replantID, this.plant, this.container, this.date);
                replantEditor.Update();
                containerName.setEditable(false);
            }
        });

        dateInput.setText("Date Started: " + date);
        dateInput.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 15;
        """);
        dateInput.setEditable(false);
        dateInput.setPadding(new Insets(0));
        dateInput.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                dateInput.setText(this.date);
                dateInput.setEditable(true);
            } else if (!dateInput.getText().isBlank() && dateInput.isEditable()) {
                try {
                    LocalDate.parse(dateInput.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException e) {
                    return;
                }
                this.date = dateInput.getText();
                dateInput.setText("Date Started: " + this.date);
                dateInput.setEditable(false);
                replantEditor.Data(this.replantID, this.plant, this.container, this.date);
                replantEditor.Update();
                days.setText("Days: " + Long.toString(ChronoUnit.DAYS.between(LocalDate.parse(this.date, DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.now())));
            }
        });

        days.setText("Days: " + Long.toString(ChronoUnit.DAYS.between(LocalDate.parse(this.date, DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.now())));
        days.setStyle("-fx-background-color: transparent; -fx-font-size: 18;");

        harvestButton.setStyle("""
            -fx-background-color: #88EAA4;
            -fx-font-weight: bold;
            -fx-font-size: 18;
        """);
        harvestButton.setOnMouseEntered(event -> harvestButton.setStyle("""
            -fx-background-color: #209641;
            -fx-font-weight: bold;
            -fx-font-size: 18;
        """));
        harvestButton.setOnMouseExited(event -> harvestButton.setStyle("""
            -fx-background-color: #88EAA4;
            -fx-font-weight: bold;
            -fx-font-size: 18;
        """));
        harvestButton.setOnMouseClicked(event -> {
            replantEditor.Data(this.replantID, this.plant, this.container, this.date);
            replantEditor.Delete();
            Harvest harvest = new Harvest();
            harvest.Data(0, this.plant, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 1.00f);
            harvest.Create();
            mainControl.RefreshData();
        });

        ContextMenu menu = new ContextMenu();
        MenuItem deletePlant = new MenuItem("Delete");
        deletePlant.setOnAction(event -> {
            replantEditor.Delete();
            mainControl.RefreshData();
        });
        menu.getItems().add(deletePlant);
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(this, event.getScreenX(), event.getScreenY());
            } else {
                menu.hide();
            }
        });
        this.setPadding(new Insets(10));
        this.getChildren().addAll(plantName, containerName, dateInput, days, harvestButton);
    }
}
