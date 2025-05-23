package com.stationeryshop.dao;

import com.stationeryshop.utils.DBConnection;

public class UserDAO {
    static DBConnection db;
    public UserDAO(String username,String password){
        String pwd;
        db = new DBConnection(username, password);
    }
    void createUser(String username, String password){
        //Lưu thông tin người dùng vào database

    }
    public boolean findUserByUsername(String username){
        return false;
    }
    public boolean verifyPassword(String username, String password){
        return false;
    }
}
