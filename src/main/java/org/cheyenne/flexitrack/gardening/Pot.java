package org.cheyenne.flexitrack.gardening;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Pot extends VBox {
    private int compostID;
    private String name;
    private String status;
    private String date;
    private final Compost compostEditor = new Compost();

    private final TextField potName = new TextField();
    private final Button emptyOption = new Button("Empty");
    private final Button fillingOption = new Button("Filling");
    private final Button fullOption = new Button("Full");
    private final TextField dateStarted = new TextField();
    private final Label oneMonth = new Label();
    private final Label days = new Label();
    
    public Pot(int compostID, String name, String status, String date, DisplayCompost mainControl) {
        this.compostID = compostID;
        this.name = name;
        this.status = status;
        this.date = date;
        this.setStyle("""
            -fx-background-color: #E1FFEF;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-border-width: 1;
            -fx-border-color: black;
        """);
        this.setSpacing(10);
        compostEditor.Pot(compostID, name, status, date);

        potName.setText(name);
        potName.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 25;
            -fx-font-weight: bold;
        """);
        potName.setEditable(false);
        potName.setPadding(new Insets(0));
        potName.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                potName.setEditable(true);
            } else if (!potName.getText().isBlank()) {
                potName.setEditable(false);
                Update();
            }
        });

        HBox statusPanel = new HBox();
        statusPanel.getChildren().addAll(emptyOption, fillingOption, fullOption);
        statusPanel.setMaxWidth(Double.MAX_VALUE); 
        emptyOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
        fillingOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
        fullOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
        emptyOption.setMaxWidth(Double.MAX_VALUE);
        fillingOption.setMaxWidth(Double.MAX_VALUE);
        fullOption.setMaxWidth(Double.MAX_VALUE);
        emptyOption.setOnMouseClicked(event -> {
            this.status = "Empty";
            Update();
        });
        fillingOption.setOnMouseClicked(event -> {
            this.status = "Filling";
            Update();
        });
        fullOption.setOnMouseClicked(event -> {
            this.status = "Full";
            Update();
        });
        HBox.setHgrow(emptyOption, Priority.ALWAYS);
        HBox.setHgrow(fillingOption, Priority.ALWAYS);
        HBox.setHgrow(fullOption, Priority.ALWAYS);
        VBox.setVgrow(statusPanel, Priority.ALWAYS);

        dateStarted.setText("Date Started: " + date);
        dateStarted.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 15;
        """);
        dateStarted.setEditable(false);
        dateStarted.setPadding(new Insets(0));
        dateStarted.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                dateStarted.setText(this.date);
                dateStarted.setEditable(true);
            } else if (!dateStarted.getText().isBlank() && dateStarted.isEditable()) {
                try {
                    LocalDate.parse(dateStarted.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException e) {
                    return;
                }
                this.date = dateStarted.getText();
                dateStarted.setText("Date Started: " + this.date);
                dateStarted.setEditable(false);
                Update();
            }
        });

        String[] calculated = compostEditor.getCalculated();
        oneMonth.setText("One Month: " + calculated[0]);
        oneMonth.setStyle("-fx-font-size: 15;");
        days.setText("Days: " + calculated[1]);
        days.setStyle("-fx-font-size: 15");
        
        ContextMenu menu = new ContextMenu();
        MenuItem deletePot = new MenuItem("Delete");
        deletePot.setOnAction(event -> {
            compostEditor.Delete();
            mainControl.RefreshData();
        });
        menu.getItems().add(deletePot);
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(this, event.getScreenX(), event.getScreenY());
            } else {
                menu.hide();
            }
        });
        this.setPadding(new Insets(10));
        this.getChildren().addAll(potName, statusPanel, dateStarted, oneMonth, days);
    }

    private void Update() {
        compostEditor.Pot(compostID, name, status, date);
        compostEditor.Update();
        String[] calculated = compostEditor.getCalculated();
        oneMonth.setText("One Month: " + calculated[0]);
        days.setText("Days: " + calculated[1]);
        switch (status) {
            case "Empty" -> {
                emptyOption.setStyle("-fx-background-color: #209641; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
                fillingOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
                fullOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
            }
            case "Filling" -> {
                emptyOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
                fillingOption.setStyle("-fx-background-color: #209641; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
                fullOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
            }
            case "Full" -> {
                emptyOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
                fillingOption.setStyle("-fx-background-color: #88EAA4; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
                fullOption.setStyle("-fx-background-color: #209641; -fx-font-size: 15; -fx-border-radius: 0; -fx-background-radius: 0;");
            }
        }
    }
}
