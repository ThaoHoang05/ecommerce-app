package com.stationeryshop.dao;

import com.stationeryshop.utils.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class InventoryForControllerDAO {
    DBConnection db = new DBConnection();
    void add(String name, String category, int price, int stock, String description) {
        Connection conn = db.connect();
        String sql = "INSERT INTO productInventory(name, category, price, stock, description) "
        if(conn == null) {
            JOptionPane.showMessageDialog(null, "<UNK>");
        }else{
            PreparedStatement ps = conn.prepareStatement(sql);

        }
    }
}
