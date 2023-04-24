package database;

import java.sql.*;

public class DatabaseHelper {

    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/bachelorprojekt";
    private static final String USER = "root";
    private static final String PASS = "password";

    private static Statement stmt;
    private static Connection con;


    public DatabaseHelper() {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASS);
            stmt = con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertIntoWebsite(String name){
        try {
            String sql = String.format("INSERT INTO WEBSITE (NAME) VALUES ('%s')",name);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoPerson\nmessage: " + e.getMessage());
        }
    }

    public void close()  {
        try {
            stmt.close();
            con.close();
        } catch (Exception ignored) {
        }
    }
}
