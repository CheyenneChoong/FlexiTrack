package org.cheyenne.flexitrack.inventory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

public class Item extends HBox {
    private DisplayInventory mainControl;
    private int itemID;
    private int categoryID;
    private String item;
    private String expiry;
    private int quantity;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TextField itemName = new TextField();
    private final DatePicker expiryField = new DatePicker();
    private final TextField quantityField = new TextField();
    private final Inventory inventory = new Inventory();

    public Item(int itemID, int categoryID, String item, String expiry, int quantity, DisplayInventory control) {
        mainControl = control;
        this.itemID = itemID;
        this.categoryID = categoryID;
        this.item = item;
        this.expiry = expiry;
        this.quantity = quantity;

        inventory.Data(itemID, categoryID, item, expiry, quantity);

        this.setStyle("""
            -fx-background-color: #DDF2FF;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-border-width: 1;
            -fx-border-color: black;
        """);

        itemName.setText(item);
        itemName.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        itemName.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.5);
        itemName.setEditable(false);
        itemName.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                itemName.setEditable(true);
            } else if (itemName.getText().isBlank()) {
                itemName.setEditable(true);
            } else {
                itemName.setEditable(false);
                UpdateItem();
            }
        });
        
        expiryField.setValue(LocalDate.parse(expiry, format));
        expiryField.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        expiryField.getEditor().setStyle("""
            -fx-background-color: transparent;
        """);
        expiryField.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.08);
        expiryField.setOnAction(e -> UpdateItem());

        quantityField.setText(Integer.toString(quantity));
        quantityField.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
        """);
        quantityField.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.03);
        quantityField.setAlignment(Pos.CENTER);
        quantityField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("[0-9]*")) {
                return change;
            }
            return null;
        }));
        quantityField.setEditable(false);
        
        Button increaseButton = new Button("+");
        increaseButton.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """);
        increaseButton.setOnMouseClicked(e -> UpdateQuantity(1));

        Button decreaseButton = new Button("-");
        decreaseButton.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """);
        decreaseButton.setOnMouseClicked(e -> UpdateQuantity(-1));

        this.getChildren().addAll(itemName, expiryField, increaseButton, quantityField, decreaseButton);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        ContextMenu menu = new ContextMenu();
        MenuItem deleteCategory = new MenuItem("Delete");
        deleteCategory.setOnAction(e -> DeleteItem());
        menu.getItems().add(deleteCategory);
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(this, event.getScreenX(), event.getScreenY());
            } else {
                menu.hide();
            }
        });
    }

    private void UpdateItem() {
        item = itemName.getText();
        expiry = expiryField.getValue().format(format);
        quantity = Integer.parseInt(quantityField.getText());
        inventory.Data(itemID, categoryID, item, expiry, quantity);
        inventory.Update();
    }

    private void UpdateQuantity(int mode) {
        quantity = quantity + mode;
        quantityField.setText(Integer.toString(quantity));
        UpdateItem();
    }
    
    private void DeleteItem() {
        inventory.Delete();
        mainControl.DisplayItem(categoryID);
    }
}
