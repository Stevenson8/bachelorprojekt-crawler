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

    public void insertIntoWebsite(int id, String url, String country){
        try {
            String sql=String.format("insert into website values (%s,\"%s\",\"%s\")",id,url,country);
            //String sql = String.format("insert into website values ('1,\"%s\"')", url);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoWebsite\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoRequest(int id, String status, String region, String date, boolean wasDirected, String redirectedPage, String protocol, int website){
        try {
            String sql = String.format("insert into request values (%s,\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",%s)", id,status,region,date,wasDirected,redirectedPage,protocol,website);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoRequest\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoCookie(int id, String name, String value, String expiryDate, int request){
        try {
            String sql = String.format("insert into cookie values (%s,\"%s\",\"%s\",\"%s\",%s)", id,name, value, expiryDate, request);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoCookie\nmessage: " + e.getMessage());
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
