package org.cheyenne.flexitrack.inventory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cheyenne.flexitrack.manage.Manage;

public class Inventory extends Manage {
    private int itemID;
    private int categoryID;
    private String item;
    private String expiry;
    private int quantity;

    public Inventory() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS item (
                    itemID INTEGER PRIMARY KEY AUTOINCREMENT,
                    categoryID INTEGER,
                    item TEXT NOT NULL,
                    expiry TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    FOREIGN KEY (categoryID) REFERENCES category(categoryID) ON DELETE CASCADE
                    )
                    """);
        } catch (SQLException e) {
            System.out.println("Error in Inventory: " + e);
        }
    }

    public void Data(int itemID, int categoryID, String item, String expiry, int quantity) {
        this.itemID = itemID;
        this.categoryID = categoryID;
        this.item = item;
        this.expiry = expiry;
        this.quantity = quantity;
    }

    public String[][] getInventory(String search) {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            ResultSet result;
            int rowCount = 0;
            if (search.isEmpty()) {
                result = statement.executeQuery(String.format("SELECT COUNT(itemID) AS row FROM item WHERE categoryID = %d", this.categoryID));
                if (result.next()) {
                    rowCount = result.getInt("row"); 
                }
                result = statement.executeQuery(String.format("SELECT * FROM item WHERE categoryID = %d;", this.categoryID));
            } else {
                result = statement.executeQuery(String.format(
                    "SELECT COUNT(itemID) AS row FROM item WHERE item LIKE '%%%s%%' AND categoryID = %d ORDER BY expiry ASC;",
                    search, this.categoryID 
                ));
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery(String.format(
                    "SELECT * FROM item WHERE item LIKE '%%%s%%' AND categoryID = %d ORDER BY expiry ASC;", 
                    search, this.categoryID
                ));
            }
            String[][] searchResult = new String[rowCount][5];
            rowCount = 0;
            while (result.next()) {
                searchResult[rowCount][0] = Integer.toString(result.getInt("itemID"));
                searchResult[rowCount][1] = Integer.toString(result.getInt("categoryID"));
                searchResult[rowCount][2] = result.getString("item");
                searchResult[rowCount][3] = result.getString("expiry");
                searchResult[rowCount][4] = Integer.toString(result.getInt("quantity"));
                rowCount++;
            }
            return searchResult;
        } catch (SQLException e) {
            System.out.println("Error in Inventory getInventory: " + e);
            return null;
        }
    }

    @Override
    public void Create() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format(
                "INSERT INTO item (categoryID, item, expiry, quantity) VALUES (%d, '%s', '%s', %d);",
                this.categoryID, this.item, this.expiry, this.quantity
            ));
        } catch (SQLException e) {
            System.out.println("Error in Inventory Create: " + e);
        }
    }

    @Override
    public void Update() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format(
                "UPDATE item SET categoryID = %d, item = '%s', expiry = '%s', quantity = %d WHERE itemID = %d;",
                this.categoryID, this.item, this.expiry, this.quantity, this.itemID
            ));
        } catch (SQLException e) {
            System.out.println("Error in Inventory Update: " + e);
        }
    }

    @Override
    public void Delete() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format("DELETE FROM item WHERE itemID = %d", this.itemID));
        } catch (SQLException e) {
            System.out.println("Error in Inventory Delete: " + e);
        }
    }
}