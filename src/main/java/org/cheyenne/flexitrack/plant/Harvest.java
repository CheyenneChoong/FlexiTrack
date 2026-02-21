package org.cheyenne.flexitrack.plant;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.cheyenne.flexitrack.manage.Manage;

public class Harvest extends Manage {
    private int harvestID;
    private String plant;
    private String date;
    private float worth;

    public Harvest() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS harvest (
                    harvestID INTEGER PRIMARY KEY AUTOINCREMENT,
                    plant TEXT NOT NULL,
                    date TEXT NOT NULL,
                    worth REAL NOT NULL,
                    );
                    """);
        } catch (SQLException e) {
            System.out.println("Error in Harvest: " + e);
        }
    }

    public void Data(int harvestID, String plant, String date, float worth) {
        this.harvestID = harvestID;
        this.plant = plant;
        this.date = date;
        this.worth = worth;
    }

    public String[][] getHarvest(String filter) {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            ResultSet result;
            int rowCount = 0;
            if (filter.isBlank()) {
                result = statement.executeQuery("SELECT COUNT(harvestID) AS row FROM harvest;");
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery("SELECT * FROM harvest");
            } else {
                result = statement.executeQuery(String.format("SELECT COUNT(harvestID) AS row FROM harvest WHERE plant = '%s';", filter));
                if (result.next()) {
                    rowCount = result.getInt("row");
                }
                result = statement.executeQuery(String.format("SELECT * FROM harvest WHERE plant = '%s';", filter));
            }
            String[][] harvestLog = new String[rowCount][4];
            rowCount = 0;
            while (result.next()) {
                harvestLog[rowCount][0] = Integer.toString(result.getInt("harvestID"));
                harvestLog[rowCount][1] = result.getString("plant");
                harvestLog[rowCount][2] = result.getString("date");
                harvestLog[rowCount][3] = Float.toString(result.getFloat("worth"));
                rowCount++;
            }
            return harvestLog;
        } catch (SQLException e) {
            System.out.println("Error in Harvest getHarvest: " + e);
            return null;
        }
    }

    public float[] getSum(String filter) {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            ResultSet result;
            if (filter.isBlank()) {
                result = statement.executeQuery("SELECT date, worth FROM harvest;");
            } else {
                result = statement.executeQuery(String.format("SELECT date, worth FROM harvest WHERE plant = '%s';", filter));
            }
            float month = 0f;
            float year = 0f;
            float lifetime = 0f;
            LocalDate currentDate;
            float currentWorth;
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            while (result.next()) {
                currentWorth = result.getFloat("worth");
                lifetime = lifetime + currentWorth;
                currentDate = LocalDate.parse(result.getString("date"), format);
                if (currentDate.getMonth().equals(LocalDate.now().getMonth())) {
                    month = month + currentWorth;
                }
                if (currentDate.getYear() == (LocalDate.now().getYear())) {
                    year = year + currentWorth;
                }
            }
            float[] sum = new float[] {month, year, lifetime};
            return sum;
        } catch (SQLException e) {
            System.out.println("Error in Harvest getSum: " + e);
            return null;
        }
    }

    @Override
    public void Create() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format(
                "INSERT INTO harvest (plant, date, worth) VALUES ('%s', '%s', '%.2f');",
                this.plant, this.date, this.worth
            ));
        } catch (SQLException e) {
            System.out.println("Error in Harvest Create: " + e);
        }
    }

    @Override
    public void Update() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format(
                "UPDATE harvest SET plant = '%s', date = '%s', worth = %.2f WHERE harvestID = %d",
                this.plant, this.date, this.worth, this.harvestID
            ));
        } catch (SQLException e) {
            System.out.println("Error in Harvest Update: " + e);
        }
    }

    @Override
    public void Delete() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.execute(String.format("DELETE FROM harvest WHERE harvestID = %d", this.harvestID));
        } catch (SQLException e) {
            System.out.println("Error in Harvest Delete: " + e);
        }
    }
}
