package com.example.fx_shoes;

import java.sql.Connection;
import java.sql.DriverManager;



class DbConnection {
    static Connection connection;

    private DbConnection() {}

    public static Connection createConnection () {
        final String URL = "jdbc:mysql://localhost/products";
        final String PASSWORD = "0104";
        final String USER = "root";
        connection = null;
       try {
           connection = DriverManager.getConnection(URL, USER, PASSWORD);

       } catch (Exception e) {
           e.printStackTrace();
       }
       return connection;

   }
   public static void closeDb () {
        if(connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

   }




}
