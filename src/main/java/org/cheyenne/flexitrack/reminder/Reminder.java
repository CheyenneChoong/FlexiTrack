package org.cheyenne.flexitrack.reminder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Reminder {
    public Reminder() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS reminder (
                    reminderID INTEGER PRIMARY KEY AUTOINCREMENT,
                    reminder TEXT NOT NULL,
                    date TEXT NOT NULL
                    );
                    """);
        } catch (SQLException e) {
            System.out.println("Error in Reminder: " + e);
        }
    }

    public String[][] getReminder(String date) {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            ResultSet result;
            int rowCount = 0;
            if (date.isBlank()) {
                result = statement.executeQuery("SELECT COUNT(reminderID) AS row FROM reminder;");
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery("SELECT * FROM reminder;");
            } else {
                result = statement.executeQuery(String.format("SELECT COUNT(reminderID) AS row FROM reminder WHERE date = '%s';", date));
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery(String.format("SELECT * FROM reminder WHERE date = '%s';", date));
            }
            String[][] reminderList = new String[rowCount][3];
            rowCount = 0;
            while (result.next()) {
                reminderList[rowCount][0] = Integer.toString(result.getInt("reminderID"));
                reminderList[rowCount][1] = result.getString("reminder");
                reminderList[rowCount][2] = result.getString("date");
                rowCount++;
            }
            return reminderList;
        } catch (SQLException e) {
            System.out.println("Error in Reminder getReminder: " + e);
            return null;
        }
    }

    public void Create(String reminder, String date) {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format(
                "INSERT INTO reminder (reminder, date) VALUES ('%s', '%s');",
                reminder, date
            ));
        } catch (SQLException e) {
            System.out.println("Error in Reminder Create: " + e);
        }
    }

    public void Delete(int reminderID) {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("DELETE FROM reminder WHERE reminderID = %d", reminderID);
        } catch (SQLException e) {
            System.out.println("Error in Reminder Delete: " + e);
        }
    }
}
