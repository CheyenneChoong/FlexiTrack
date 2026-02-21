package org.cheyenne.flexitrack.compost;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.cheyenne.flexitrack.manage.Manage;

public class Compost extends Manage{
    private int compostID;
    private String pot;
    private String status;
    private String startDate;

    public Compost() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS compost (
                    compostID INTEGER PRIMARY KEY AUTOINCREMENT,
                    pot TEXT NOT NULL,
                    status TEXT NOT NULL,
                    startDate TEXT NOT NULL
                    )
                    """);
        } catch (SQLException e) {
            System.out.println("Error in Compost: " + e);
        }
    }

    public void Pot(int compostID, String pot, String status, String startDate) {
        this.compostID = compostID;
        this.pot = pot;
        this.status = status;
        this.startDate = startDate;
    }

    public String[][] getCompost() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT COUNT(compostID) AS row FROM compost;");
            int rowCount = 0;
            if (result.next()) {
                rowCount = result.getInt("row");
            } 
            result = statement.executeQuery("SELECT * FROM compost;");
            String[][] compostList = new String[rowCount][6];
            rowCount = 0;
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate start;
            LocalDate later;
            LocalDate today = LocalDate.now();
            long days;
            while (result.next()) {
                compostList[rowCount][0] = Integer.toString(result.getInt("compostID"));
                compostList[rowCount][1] = result.getString("pot");
                compostList[rowCount][2] = result.getString("status");
                compostList[rowCount][3] = result.getString("startDate");
                start = LocalDate.parse(result.getString("startDate"), format);
                days = ChronoUnit.DAYS.between(start, today);
                later = start.plusMonths(1);
                compostList[rowCount][4] = later.format(format);
                compostList[rowCount][5] = Long.toString(days);
            }
            return compostList;
        } catch (SQLException e) {
            System.out.println("Error in Compost getCompost: " + e);
            return null;
        }
    }

    @Override
    public void Create() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute(String.format(
                "INSERT INTO compost (pot, status, startDate) VALUES ('%s', '%s', '%s');",
                this.pot, this.status, this.startDate
            ));
        } catch (SQLException e) {
            System.out.println("Error in Compost Create: " + e);
        }
    }

    @Override
    public void Update() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute(String.format(
                "UPDATE compost SET pot = '%s', status = '%s', startDate = '%s' WHERE compostID = %d;",
                this.pot, this.status, this.startDate, this.compostID
            ));
        } catch (SQLException e) {
            System.out.println("Error in Compost Update: " + e);
        }
    }

    @Override
    public void Delete() {
        try (Connection connect = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            Statement statement = connect.createStatement();
            statement.execute(String.format("DELETE FROM compost WHERE compostID = %d", this.compostID));
        } catch (SQLException e) {
            System.out.println("Error in Compost Delete: " + e);
        }
    }
}
