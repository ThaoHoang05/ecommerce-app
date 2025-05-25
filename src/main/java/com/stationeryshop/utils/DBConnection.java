package com.stationeryshop.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/*
    Lớp DBConnection có nhiệm vụ thực hiện kết nối tới database khi có yêu cầu từ người dùng hoặc
    nhân viên quản lý.
    Trong lớp này gồm:
        phương thức khởi tạo;
        phương thức kết nối;
        phương thức đóng kết nối.
 */

public class DBConnection {
    private String className = "org.postgresql.Driver";
    private String urlDB = "jdbc:postgresql://localhost:5432/stationeryshop";
    private String username;
    private String password;
    private Connection conn;
    private Statement stmt;
    public DBConnection(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Statement connect(){
        try{
            Class.forName(className);
            conn = DriverManager.getConnection(urlDB, username, password);
            stmt = conn.createStatement();
            return stmt;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    public void closeConnect(){
        try{
            stmt.close();
            conn.close();
        }catch(Exception e) {
            System.out.println(e);
        }
    }
}
