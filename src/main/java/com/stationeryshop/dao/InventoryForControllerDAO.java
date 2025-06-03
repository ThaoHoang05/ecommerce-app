package com.stationeryshop.dao;

import com.stationeryshop.utils.DBConnection;

import javax.swing.*;
import java.sql.Connection;

public class InventoryForControllerDAO {
    DBConnection db = new DBConnection();
    void add(String name, String category, int price, int stock, String description) {
        Connection conn = db.connect();
        if(conn == null) {
            JOptionPane.showMessageDialog(null, "<UNK>");
        }else{
            
        }
    }
}
