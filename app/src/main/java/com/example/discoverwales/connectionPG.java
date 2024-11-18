package com.example.discoverwales;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;

public class connectionPG {
    Connection connection =null;

    public Connection connectionDB(){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("org.postgresql.Driver");
            System.out.println("1");
            connection = DriverManager.getConnection("jdbc:postgresql://10.0.2.2:5432/database_discover_wales?connectTimeout=10&loggerLevel=DEBUG", "postgres", "password");
            System.out.println("2");
            //connection = DriverManager.getConnection("jdbc:postgresql://192.168.0.15:5432/madan_tumbes", "postgres", "admin");
        }catch (Exception er){
            System.err.println("Connection error"+ er.toString());
        }
        return  connection;
    }

    protected  void closeConnection(Connection con)throws  Exception{
        con.close();
    }
}


