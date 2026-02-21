package org.cheyenne.flexitrack.plant;
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
                    CREATE TABLE IF NOT EXISTS plant (
                    plantID INTEGER PRIMARY KEY AUTOINCREMENT,
                    plant TEXT NOT NULL
                    );
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS replant (
                    replantID INTEGER PRIMARY KEY AUTOINCREMENT,
                    plantID INTEGER,
                    container TEXT NOT NULL,
                    startDate TEXT NOT NULL,
                    FOREIGN KEY (plantID) REFERENCES plant(plantID) ON DELETE CASCADE
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
                result = statement.executeQuery("""
                        SELECT r.replantID, p.plant, r.container, r.startDate
                        FROM replant r LEFT JOIN plant p ON r.plantID = p.plantID;
                        """);
            } else {
                result = statement.executeQuery(String.format(
                    "SELECT COUNT(r.replantID) AS row FROM replant r LEFT JOIN plant p ON r.plantID = p.plantID WHERE p.plant LIKE '%%%s%%';",
                    search
                ));
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery(String.format(
                    "SELECT r.replantID, p.plant, r.container, r.startDate FROM replant r LEFT JOIN plant p ON r.plantID = p.plantID WHERE p.plant LIKE '%%%s%%';", 
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
            result = statement.executeQuery(String.format("SELECT plantID FROM plant WHERE plant = '%s';", this.plant));
            int plantID;
            if (result.next()) {
                plantID = result.getInt("plantID");
            } else {
                statement.execute(String.format("INSERT INTO plant (plant) VALUES ('%s');", this.plant));
                result = statement.getGeneratedKeys();
                plantID = result.getInt(1);
            }
            statement.execute(String.format(
                "INSERT INTO replant (plantID, container, startDate) VALUES (%d, '%s', '%s');",
                plantID, this.container, this.startDate
            ));
            DeletePlant();
        } catch (SQLException e) {
            System.out.println("Error in Replant Create: " + e);
        }
    }

    @Override
    public void Update() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            result = statement.executeQuery(String.format("SELECT plantID FROM plant WHERE plant = '%s';", this.plant));
            int plantID;
            if (result.next()) {
                plantID = result.getInt("plantID");
            } else {
                statement.execute(String.format("INSERT INTO plant (plant) VALUES ('%s');", this.plant));
                result = statement.getGeneratedKeys();
                plantID = result.getInt(1);
            }
            statement.execute(String.format(
                "UPDATE replant SET plantID = %d, container = '%s', startDate = '%s' WHERE replantID = %d;",
                plantID, this.container, this.startDate, this.replantID
            ));
            DeletePlant();
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
            DeletePlant();
        } catch (SQLException e) {
            System.out.println("Error in Replant Delete: " + e);
        }
    }

    private void DeletePlant() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("DELETE FROM plant WHERE plantID NOT IN (SELECT DISTINCT plantID FROM replant);");
        } catch (SQLException e) {
            System.out.println("Error in Replant DeletePlant: " + e);
        }
    }
}
