package org.cheyenne.flexitrack.gardening;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.cheyenne.flexitrack.manage.Manage;

public class Replant extends Manage {
    private int replantID;
    private String plant;
    private String container;
    private String startDate;
    private ResultSet result;

    public Replant() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS replant (
                    replantID INTEGER PRIMARY KEY AUTOINCREMENT,
                    plant TEXT NOT NULL,
                    container TEXT NOT NULL,
                    startDate TEXT NOT NULL
                    );
                    """);
        } catch (SQLException e) {
            System.out.println("Error in Replant: " + e);
        }
    }

    public void Data(int replantID, String plant, String container, String startDate) {
        this.replantID = replantID;
        this.plant = plant;
        this.container = container;
        this.startDate = startDate;
    }

    public String[][] getReplant(String search) {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            int rowCount = 0;
            if (search.isBlank()) {
                result = statement.executeQuery("SELECT COUNT(replantID) AS row FROM replant;");
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery("SELECT * FROM replant ;");
            } else {
                result = statement.executeQuery(String.format(
                    "SELECT COUNT(replantID) AS row FROM replant WHERE plant LIKE '%%%s%%';",
                    search
                ));
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery(String.format(
                    "SELECT * FROM replant WHERE plant LIKE '%%%s%%';", 
                    search
                ));
            }
            String[][] replantList = new String[rowCount][5];
            rowCount = 0;
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate start;
            LocalDate today = LocalDate.now();
            long days;
            while (result.next()) {
                replantList[rowCount][0] = Integer.toString(result.getInt("replantID"));
                replantList[rowCount][1] = result.getString("plant");
                replantList[rowCount][2] = result.getString("container");
                replantList[rowCount][3] = result.getString("startDate");
                start = LocalDate.parse(result.getString("startDate"), format);
                days = ChronoUnit.DAYS.between(start, today);
                replantList[rowCount][4] = Long.toString(days);
                rowCount++;
            }
            return replantList;
        } catch (SQLException e) {
            System.out.println("Error in Replant getReplant: " + e);
            return null;
        }
    }

    @Override
    public void Create() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format(
                "INSERT INTO replant (plant, container, startDate) VALUES ('%s', '%s', '%s');",
                this.plant, this.container, this.startDate
            ));
        } catch (SQLException e) {
            System.out.println("Error in Replant Create: " + e);
        }
    }

    @Override
    public void Update() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format(
                "UPDATE replant SET plant = '%s', container = '%s', startDate = '%s' WHERE replantID = %d;",
                this.plant, this.container, this.startDate, this.replantID
            ));
        } catch (SQLException e) {
            System.out.println("Error in Replant Update: " + e);
        }
    }

    @Override
    public void Delete() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format("DELETE FROM replant WHERE replantID = %d", this.replantID));
        } catch (SQLException e) {
            System.out.println("Error in Replant Delete: " + e);
        }
    }
}
