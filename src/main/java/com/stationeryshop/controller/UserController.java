package com.stationeryshop.controller;

import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.DBConnection;

import java.util.Properties;

public class UserController {
    public int handleLogin(String username, String password){
        UserDAO loging = new UserDAO();
        if(loging.findUserByUsername(username)){
            if(loging.verifyPassword(username, password)) return 1;
            else return 0;
        }else return -1;
    }

    public int handleSignup(String username){
        Properties prop = new Properties();
        UserDAO user;
        try{
            prop.load(getClass().getResourceAsStream("/db.properties"));
            String admin = prop.getProperty("db.admin");
            String adminpass = prop.getProperty("db.adminpwd");
            user = new UserDAO(admin, adminpass);
    }catch (Exception e){
        e.printStackTrace();
        return -1;}
        if(!user.findUserByUsername(username)){
            return 1;
        }else return 0;
    }
}
