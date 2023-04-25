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

    public void insertIntoWebsite(String url, String country){
        try {
            String sql=String.format("insert into website (url,country) values (\"%s\",\"%s\")",url,country);
            //String sql = String.format("insert into website values ('1,\"%s\"')", url);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoWebsite\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoRequest(String status, String region, String ipUsed, String date, boolean wasDirected, String redirectedPage, String protocol, int website){
        try {
            String sql = String.format("insert into request (request_status,origin_region,ip_address_used,request_date,was_directed,redirected_page,protocol,website) " +
                    "values (\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",%s)", status,region,ipUsed,date,wasDirected,redirectedPage,protocol,website);
            stmt.execute(sql);
        } catch (Exception e) {
            System.err.println("Error at: insertIntoRequest\nmessage: " + e.getMessage());
        }
    }

    public void insertIntoCookie(String name, String value, String expiryDate, int request){
        try {
            String sql = String.format("insert into cookie (cookie_name,cookie_value,expiry_date,request) " +
                    "values (\"%s\",\"%s\",\"%s\",%s)", name, value, expiryDate, request);
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

    int selectIdFromRequestWhere(String status,String originRegion,String ipUsed,String date,boolean wasDirected,String redirectedPage,String protocol,int websiteId) {
        int id=0;

        try {
            String query=String.format("select id from request where website=%s",websiteId);
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
