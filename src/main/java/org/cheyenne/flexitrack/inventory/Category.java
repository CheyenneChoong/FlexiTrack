package org.cheyenne.flexitrack.inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cheyenne.flexitrack.manage.Manage;

public class Category extends Manage {
    private int categoryID;
    private String category;

    public Category() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS category (
                    categoryID INTEGER PRIMARY KEY AUTOINCREMENT,
                    category TEXT NOT NULL
                    );
                    """);
        } catch (SQLException e) {
            System.out.println("Error in Category: " + e);
        }
    }

    public void Data(int categoryID, String category) {
        this.categoryID = categoryID;
        this.category = category;
    }

    public String[][] getCategory() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            ResultSet result = statement.executeQuery("SELECT COUNT(categoryID) AS row FROM category;");
            int rowCount = 0;
            if (result.next()) {
                rowCount = result.getInt("row");
            }
            result = statement.executeQuery("SELECT * FROM category;");
            String[][] categoryList = new String[rowCount][2];
            rowCount = 0;
            while (result.next()) {
                categoryList[rowCount][0] = Integer.toString(result.getInt("categoryID"));
                categoryList[rowCount][1] = result.getString("category");
                rowCount++;
            }
            return categoryList;
        } catch (SQLException e) {
            System.out.println("Error in Category getCategory: " + e);
            return null;
        }
    }

    @Override
    public void Create() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format("INSERT INTO category (category) VALUES ('%s');", this.category));
        } catch (SQLException e) {
            System.out.println("Error in Compost Create: " + e);
        }
    }

    @Override
    public void Update() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format("UPDATE category SET category = '%s' WHERE categoryID = %d;", this.category, this.categoryID));
        } catch (SQLException e) {
            System.out.println("Error in Compost Update: " + e);
        }
    }

    @Override
    public void Delete() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format("DELETE FROM category WHERE categoryID = %d;", this.categoryID));
        } catch (SQLException e) {
            System.out.println("Error in Compost Delete: " + e);
        }
    }
}
