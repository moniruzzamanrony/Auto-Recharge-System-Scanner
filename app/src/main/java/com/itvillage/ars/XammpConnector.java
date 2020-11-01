package com.itvillage.ars;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class XammpConnector {


    public Connection CONN() {
        Connection con = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://167.99.76.96:3306/auto_recharge_system?characterEncoding=utf8", "arm", "L2#j6^%hcyb_?ABAB");

        } catch (SQLException e) {
            Log.e("1",e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e("2",e.getMessage());
        }

        return con;
    }
}
