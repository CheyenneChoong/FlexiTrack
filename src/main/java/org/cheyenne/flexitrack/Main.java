package org.cheyenne.flexitrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cheyenne.flexitrack.plant.Replant;

public class Main {

    public static void main(String[] args) {
        Replant replantTest = new Replant();
        replantTest.Data(3, "", "", "");
        replantTest.Delete();
        String[][] test = replantTest.getReplant("");
        for (String[] row : test) {
            System.out.println(String.format("%s, %s, %s, %s, %s", row[0], row[1], row[2], row[3], row[4]));
        }

        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("DELETE FROM plant WHERE plantID NOT IN (SELECT DISTINCT plantID FROM replant);");
            ResultSet result = statement.executeQuery("SELECT * FROM plant");
            while (result.next()) {
                System.out.println(String.format("%d, %s", result.getInt("plantID"), result.getString("plant")));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
}
