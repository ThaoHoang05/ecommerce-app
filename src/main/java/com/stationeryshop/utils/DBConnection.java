
package com.stationeryshop.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*
    Lớp DBConnection có nhiệm vụ thực hiện kết nối tới database khi có yêu cầu từ người dùng hoặc
    nhân viên quản lý.
    Trong lớp này gồm:
        phương thức khởi tạo;
        phương thức đóng kết nối.
 */

public class DBConnection {
    private String className = "org.postgresql.Driver";
    private String urlDB ;
    private String username;
    private String password;
    private Connection conn;
    private Statement stmt;
    public DBConnection(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public DBConnection() {
        Properties prop = new Properties();
        try{
            FileInputStream fix = new FileInputStream("src/main/resources/db.properties");
            prop.load(fix);
            this.username = prop.getProperty("db.admin");
            this.password = prop.getProperty("db.adminpwd");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Connection connect(){
        Properties props = new Properties();
        try{
            FileInputStream fis = new FileInputStream("src/main/resources/db.properties");
            props.load(fis);
        urlDB = props.getProperty("db.url");
        if(urlDB == null) throw new RuntimeException("Database URL not found");else{
        try{
            Class.forName(className);
            conn = DriverManager.getConnection(urlDB, username, password);
            return conn;
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
        }} catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
