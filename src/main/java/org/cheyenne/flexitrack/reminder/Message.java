package org.cheyenne.flexitrack.reminder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Message extends VBox {
    private int reminderID;
    private String reminder;
    private String date;

    private final TextField reminderField = new TextField();
    private final DatePicker dateField = new DatePicker();
    private final Reminder reminderEditor = new Reminder();
    
    public Message(int reminderID, String reminder, String date, DisplayReminder mainControl) {
        this.reminderID = reminderID;
        this.reminder = reminder;
        this.date = date;

        this.setStyle("""
            -fx-background-color: #E1FFEF;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-border-width: 1;
            -fx-border-color: black;
        """);

        reminderField.setText(reminder);
        reminderField.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        reminderField.setEditable(false);
        reminderField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                reminderField.setEditable(true);
            } else if (!reminderField.getText().isBlank() && reminderField.isEditable()) {
                reminderField.setEditable(false);
                this.reminder = reminderField.getText();
                reminderEditor.Data(this.reminderID, this.reminder, this.date);
                reminderEditor.Update();
                mainControl.RefreshData();
            }
        });

        dateField.setValue(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateField.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        dateField.getEditor().setStyle("-fx-background-color: transparent;");
        dateField.setOnAction(event -> {
            this.date = dateField.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            reminderEditor.Data(this.reminderID, this.reminder, this.date);
            reminderEditor.Update();
            mainControl.RefreshData();
        });

        this.getChildren().addAll(reminderField, dateField);
        VBox.setMargin(reminderField, new Insets(10, 10, 0, 10));
        VBox.setMargin(dateField, new Insets(0, 10, 10, 10));
        VBox.setVgrow(dateField, Priority.ALWAYS);
        VBox.setVgrow(reminderField, Priority.ALWAYS);

        ContextMenu menu = new ContextMenu();
        MenuItem deleteMessage = new MenuItem("Delete");
        deleteMessage.setOnAction(event -> {
            reminderEditor.Data(this.reminderID, this.reminder, this.date);
            reminderEditor.Delete();
            mainControl.RefreshData();
        });
        menu.getItems().add(deleteMessage);
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(this, event.getScreenX(), event.getScreenY());
            } else {
                menu.hide();
            }
        });
    }
}
