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

    public void insertIntoWebsite(int id, String url){
        try {
            String sql=String.format("insert into website values (%s,\"%s\")",id,url);
            //String sql = String.format("insert into website values ('1,\"%s\"')", url);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoWebsite\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoRequest(int id, String status,String region,boolean wasDirected,String redirectedPage,String protocol){
        try {
            String sql = String.format("insert into request values (%s,\"%s\",\"%s\",%s,\"%s\",\"%s\")", id,status,region,wasDirected,redirectedPage,protocol);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoRequest\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoCookie(int id, String name,String value){
        try {
            String sql = String.format("insert into cookie values (%s,\"%s\",\"%s\")", id,name, value);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoCookie\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoWebsiteHasRequest(int websiteId, int requestId){
        try {
            String sql = String.format("insert into website_has_request values (%s,%s)", websiteId, requestId);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoWebsiteHasRequest\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoRequestHasCookie(int requestId, int cookieId){
        try {
            String sql = String.format("insert into request_has_cookie values (%s,%s)", requestId, cookieId);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoRequestHasCookie\nmessage: " + e.getMessage());
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
