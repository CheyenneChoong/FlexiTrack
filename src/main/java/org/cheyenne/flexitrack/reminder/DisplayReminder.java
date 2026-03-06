package org.cheyenne.flexitrack.reminder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.cheyenne.flexitrack.gardening.DisplayGardening;
import org.cheyenne.flexitrack.inventory.DisplayInventory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DisplayReminder {
    private final VBox reminderArea = new VBox(10);
    private final VBox allReminder = new VBox(10);
    private final Reminder reminderEditor = new Reminder();

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");
        
        Label title = new Label("WELCOME");
        title.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 30;
        """);
        root.setTop(title);
        BorderPane.setMargin(title, new Insets(50, 50, 0, 50));

        String style = """
            -fx-background-color: #E2FFFA;
            -fx-background-radius: 5;
            -fx-border-width: 1;
            -fx-border-radius: 5;
            -fx-border-color: black;
        """;

        HBox content = new HBox(10);
        VBox notificationArea = new VBox();
        notificationArea.setStyle(style);
        notificationArea.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(notificationArea, Priority.ALWAYS);
        Label date = new Label(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        date.setStyle("""
            -fx-font-size: 20;
            -fx-font-weight: bold;
        """);
        ScrollPane reminderScroll = new ScrollPane(reminderArea);
        reminderScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        reminderScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        reminderScroll.setStyle("-fx-background-color: transparent;");
        reminderScroll.setFitToWidth(true);
        notificationArea.getChildren().addAll(date, reminderScroll);
        VBox.setMargin(date, new Insets(10));
        VBox.setMargin(reminderScroll, new Insets(10));

        VBox rightPanel = new VBox(10);
        rightPanel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        VBox redirect = new VBox();
        redirect.setStyle(style);
        redirect.setMaxWidth(Double.MAX_VALUE);
        Button inventoryButton = new Button("INVENTORY");
        inventoryButton.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-weight: bold;
            -fx-font-size: 20;
        """);
        inventoryButton.setOnMouseEntered(event -> inventoryButton.setStyle("""
            -fx-background-color: #247896;
            -fx-font-weight: bold;
            -fx-font-size: 20;
        """));
        inventoryButton.setOnMouseExited(event -> inventoryButton.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-weight: bold;
            -fx-font-size: 20;
        """));
        inventoryButton.setOnAction(event -> {
            Stage inventory = new Stage();
            DisplayInventory display = new DisplayInventory();
            inventory.setTitle("Inventory");
            inventory.setScene(display.createScene());
            inventory.setMaximized(true);
            inventory.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth());
            inventory.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
            inventory.show();
        });
        Button gardeningButton = new Button("GARDENING");
        gardeningButton.setStyle("""
            -fx-background-color: #88EAA4;
            -fx-font-weight: bold;
            -fx-font-size: 20;
        """);
        gardeningButton.setOnMouseEntered(event -> gardeningButton.setStyle("""
            -fx-background-color: #209641;
            -fx-font-weight: bold;
            -fx-font-size: 20;
        """));
        gardeningButton.setOnMouseExited(event -> gardeningButton.setStyle("""
            -fx-background-color: #88EAA4;
            -fx-font-weight: bold;
            -fx-font-size: 20;
        """));
        gardeningButton.setOnAction(event -> {
            Stage gardening = new Stage();
            DisplayGardening display = new DisplayGardening();
            gardening.setTitle("Gardening");
            gardening.setScene(display.createScene());
            gardening.setMaximized(true);
            gardening.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth());
            gardening.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
            gardening.show();
        });
        redirect.getChildren().addAll(inventoryButton, gardeningButton);
        redirect.setAlignment(Pos.CENTER);
        VBox.setMargin(inventoryButton, new Insets(10));
        VBox.setMargin(gardeningButton, new Insets(10));

        VBox manageReminder = new VBox();
        manageReminder.setStyle(style);
        manageReminder.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(manageReminder, Priority.ALWAYS);
        Label reminderTitle = new Label("Manage Reminder");
        reminderTitle.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 18;
        """);
        Button addReminder = new Button("+");
        addReminder.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-weight: bold;
            -fx-font-size: 18;
        """);
        addReminder.setOnMouseEntered(event -> addReminder.setStyle("""
            -fx-background-color: #247896;
            -fx-font-weight: bold;
            -fx-font-size: 18;
        """));
        addReminder.setOnMouseExited(event -> addReminder.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-weight: bold;
            -fx-font-size: 18;
        """));
        addReminder.setOnMouseClicked(event -> {
            reminderEditor.Data(0, "Reminder Note", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            reminderEditor.Create();
            RefreshData();
        });
        ScrollPane manageScroll = new ScrollPane(allReminder);
        manageScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        manageScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        manageScroll.setStyle("-fx-background-color: transparent");
        manageScroll.setFitToWidth(true);
        manageReminder.getChildren().addAll(reminderTitle, addReminder, manageScroll);
        VBox.setMargin(reminderTitle, new Insets(10));
        VBox.setMargin(addReminder, new Insets(10));
        VBox.setMargin(manageScroll, new Insets(10));

        rightPanel.getChildren().addAll(redirect, manageReminder);
        content.getChildren().addAll(notificationArea, rightPanel);

        root.setCenter(content);
        BorderPane.setMargin(content, new Insets(0, 50, 50, 50));
        Scene scene = new Scene(root, Color.WHITE);
        RefreshData();
        return scene;
    }

    public void RefreshData() {
        reminderArea.getChildren().clear();
        allReminder.getChildren().clear();
        String[][] reminderList = reminderEditor.getReminder("");
        String[][] todayList = reminderEditor.getReminder(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        for (String[] row : reminderList) {
            Message message = new Message(Integer.parseInt(row[0]), row[1], row[2], this);
            allReminder.getChildren().add(message);
        }
        for (String[] row : todayList) {
            Message message = new Message(Integer.parseInt(row[0]), row[1], row[2], this);
            reminderArea.getChildren().add(message);
        }
    }
}
