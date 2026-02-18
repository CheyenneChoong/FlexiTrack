package org.cheyenne.flexitrack;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FlexiTrack {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY, name TEXT, qty INT)");
            stmt.execute("INSERT INTO items (name, qty) VALUES ('Seeds', 10)");
            ResultSet rs = stmt.executeQuery("SELECT * FROM items");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " + rs.getString("name") + " | " + rs.getInt("qty") + " | ");
            }
        } catch (SQLException e) {
            System.out.println("Error" + e);
        }
    }
}
