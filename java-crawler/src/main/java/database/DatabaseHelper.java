package database;

import java.sql.*;
import java.util.ArrayList;

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

    public void insertIntoWebsite(String url, int rank, String country){
        try {
            String sql=String.format("insert into website (url,website_rank,country) values (\"%s\",%s,\"%s\")",url,rank,country);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoWebsite\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoRequest(String status, String region, String ipUsed, String date, String originalUrl, boolean wasDirected, String redirectedUrl, String protocol, int website){
        try {
            String sql = String.format("insert into request (request_status,origin_region,ip_address_used,request_date,original_url,was_directed,redirected_url,protocol,website) " +
                    "values (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",%s)", status,region,ipUsed,date,originalUrl,wasDirected,redirectedUrl,protocol,website);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoRequest\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoCookie(String name, String value, int request){
        try {
            String sql = String.format("insert into cookie (cookie_name,cookie_value,request) " +
                    "values (\"%s\",\"%s\",%s)", name, value, request);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoCookie\nmessage: " + e.getMessage());
        }
    }

    ArrayList<String> selectUrlsFromWebsite() {
        ArrayList<String> urls = new ArrayList<>();

        try {
            ResultSet rs = stmt.executeQuery("select url from website");
            while (rs.next()) {
                urls.add(rs.getString("url"));
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(("Error at: selectUrlsFromWebsite\n message: " + e.getMessage()).trim());
        }
        return urls;
    }

    int selectIdFromWebsiteWhere(String url) {
        int id=0;

        try {
            ResultSet rs = stmt.executeQuery("select id from website where url=\""+url+"\"");
            while (rs.next()) {
                id=rs.getInt("id");
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(("Error at: selectIdFromWebsiteWhereUrl\n message: " + e.getMessage()).trim());
        }
        return id;
    }

    int selectIdFromRequestWhere(String status,String originRegion,String ipUsed,String date,String originalUrl,boolean wasDirected,String redirectedUrl,String protocol,int websiteId) {
        int id=0;

        try {
            String query=String.format("select id from request where website=%s and origin_region=\"%s\" and request_date=\"%s\"",websiteId,originRegion,date);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                id=rs.getInt("id");
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(("Error at: selectIdFromRequestWhere\n message: " + e.getMessage()).trim());
        }
        return id;
    }

    public void close()  {
        try {
            stmt.close();
            con.close();
        } catch (Exception ignored) {
        }
    }
}
