package org.cheyenne.flexitrack.inventory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class DisplayInventory {
    private final Label title = new Label();
    private final HBox tabs = new HBox(10);
    private final VBox itemList = new VBox(10);
    private final TextField searchBar = new TextField();
    private final Category category = new Category();
    private final Inventory inventory = new Inventory();
    private int currentTab = 0;

    public Scene createScene() {
        BorderPane root = new BorderPane();

        VBox content = new VBox(10);
        root.setCenter(content);
        BorderPane.setMargin(content, new Insets(50, 50, 50, 50));
        
        title.setText("Layout Purposes");
        title.setStyle("""
            -fx-font-weight: bold;
            -fx-font-size: 30;
        """);


        Button addItem = new Button("+");
        addItem.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """);
        addItem.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.06);
        addItem.setOnMouseEntered(e -> addItem.setStyle("""
            -fx-background-color: #247896;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        addItem.setOnMouseExited(e -> addItem.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        addItem.setOnMouseClicked(e -> AddItem());

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
            -fx-background-color: #88D0EA;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """);
        searchButton.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.09);
        searchButton.setOnMouseEntered(e -> searchButton.setStyle("""
            -fx-background-color: #247896;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        searchButton.setOnMouseClicked(e -> DisplayItem(currentTab));

        HBox searchPanel = new HBox(10);
        searchPanel.getChildren().addAll(searchBar, searchButton);
        BorderPane panel = new BorderPane();
        panel.setLeft(addItem);
        panel.setRight(searchPanel);

        ScrollPane itemScroll = new ScrollPane(itemList);
        itemScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        itemScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        itemScroll.setStyle("-fx-background-color: transparent;");
        itemScroll.setFitToWidth(true);

        content.getChildren().addAll(title, panel, itemScroll);

        HBox navigation = new HBox(10);
        navigation.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.1);
        navigation.setStyle("-fx-background-color: #0F6499;");
        navigation.setAlignment(Pos.CENTER_LEFT);
        root.setBottom(navigation);

        Button addTab = new Button("+");
        addTab.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """);
        addTab.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.05);
        addTab.setOnMouseEntered(e -> addTab.setStyle("""
            -fx-background-color: #247896;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        addTab.setOnMouseExited(e -> addTab.setStyle("""
            -fx-background-color: #88D0EA;
            -fx-font-size: 18;
            -fx-font-weight: bold;
        """));
        addTab.setOnMouseClicked(e -> NewCategory());

        tabs.setStyle("-fx-background-color: #0F6499");
        tabs.setAlignment(Pos.CENTER_LEFT);
        ScrollPane tabScroll = new ScrollPane(tabs);
        tabScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tabScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tabScroll.setStyle("-fx-background-color: transparent;");
        tabScroll.setFitToHeight(true);

        navigation.getChildren().addAll(addTab, tabScroll);
        HBox.setMargin(addTab, new Insets(0, 10, 0, 10));
        HBox.setMargin(tabScroll, new Insets(0, 10, 0, 0));

        Scene scene = new Scene(root, 400, 300, Color.WHITE);
        DisplayTabs();
        DisplayItem(currentTab);
        return scene;
    }

    public void DisplayTabs() {
        String[][] categoryList = category.getCategory();
        if (categoryList == null || categoryList.length == 0) {
            category.Data(0, "New Sheet");
            category.Create();
            categoryList = category.getCategory();
        }

        tabs.getChildren().clear();
        for (String[] row : categoryList) {
            CategoryTab tab = new CategoryTab(Integer.parseInt(row[0]), row[1], this);
            tabs.getChildren().add(tab);
        }
    }

    private void NewCategory() {
        category.Data(0, "New Sheet");
        category.Create();
        DisplayTabs();
    }

    public void DisplayItem(int selected) {
        if (selected == 0) {
            String[][] categoryList = category.getCategory();
            currentTab = Integer.parseInt(categoryList[0][0]);
            title.setText(categoryList[0][1].toUpperCase());
        }
        itemList.getChildren().clear();
        inventory.Data(0, currentTab, "", "", 0);
        String[][] allItems = inventory.getInventory(searchBar.getText());
        for (String[] row : allItems) {
            Item item = new Item(Integer.parseInt(row[0]), Integer.parseInt(row[1]), row[2], row[3], Integer.parseInt(row[4]), this);
            itemList.getChildren().add(item);
        }
        searchBar.setText("");
    }

    private void AddItem() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        inventory.Data(0, currentTab, "New Item", today.format(format), 0);
        inventory.Create();
        DisplayItem(currentTab);
    }

    public void SwitchTab(int categoryID, String category) {
        currentTab = categoryID;
        title.setText(category.toUpperCase());
        DisplayItem(currentTab);
    }
}
