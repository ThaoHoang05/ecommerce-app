package com.stationeryshop.dao;

import com.stationeryshop.utils.DBConnection;
import com.stationeryshop.utils.PwdHash;
import com.stationeryshop.utils.RandomUserId;

import javax.swing.*;
import java.sql.*;

public class UserDAO {
    static DBConnection db;
    public UserDAO(String user_admin, String pwd_admin){
        this.db = new DBConnection(user_admin, pwd_admin);
    }
    void createUser(String username, String password, String role){
        //Lưu thông tin người dùng vào database
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "insert into user_view(user_id,user_name,pwd_hash,role_id) values(?,?,?,?)";
        String pwd = new PwdHash(password).getHash();
        String user_id = new RandomUserId().getRandomUserId();
        try{
            conn = db.connect();
            if(conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(query);
            stmt.setString(1,user_id);
            stmt.setString(2,username);
            stmt.setString(3,pwd);
            stmt.setString(4,role);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnect();
    }
    public boolean findUserByUsername(String username){
        Connection conn = null;
        Statement stmt = null;
        String query = "select user_name from users where username like"+ username;
        try{
            conn = db.connect();
            stmt = conn.createStatement();
            ResultSet result= stmt.executeQuery(query);
            return result != null;
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnect();
        return false;
    }
    public boolean verifyPassword(String username, String password){
        Connection conn = null;
        Statement stmt = null;
        String query = "select pwd_hash from users where username like"+ username;
        try{
            conn = db.connect();
            stmt = conn.createStatement();
            ResultSet result= stmt.executeQuery(query);
            if(result != null){
                String pwd_hash = result.getString("pwd_hash");
                return new PwdHash(password).verify(pwd_hash);
            }else return false;
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.closeConnect();
        return false;
    }
}
