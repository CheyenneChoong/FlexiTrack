package org.cheyenne.flexitrack.inventory;

import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class CategoryTab extends TextField {
    private final int id;
    private final Category category = new Category();
    private final DisplayInventory mainControl;

    public CategoryTab(int categoryID, String name, DisplayInventory control) {
        mainControl = control;
        this.id = categoryID;
        category.Data(categoryID, name);
        this.setPromptText("New Sheet");
        this.setAlignment(Pos.CENTER);
        this.setText(name);
        this.setPrefColumnCount(name.length());
        this.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """);
        this.setEditable(false);
        this.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                this.setEditable(true);
            } else if (this.getText().isBlank()) {
                this.setEditable(true);
            } else {
                this.setEditable(false);
                UpdateItem();
            }
        });

        ContextMenu menu = new ContextMenu();
        MenuItem deleteCategory = new MenuItem("Delete");
        deleteCategory.setOnAction(e -> DeleteItem());
        menu.getItems().add(deleteCategory);
        this.setContextMenu(menu);
    }

    private void UpdateItem() {
        String name = this.getText();
        category.Data(id, name);
        category.Update();
        this.setPrefColumnCount(name.length());
        mainControl.SwitchTab(id, name);
    }

    private void DeleteItem() {
        category.Delete();
        mainControl.DisplayTabs();
        mainControl.DisplayItem(0);
    }
}
