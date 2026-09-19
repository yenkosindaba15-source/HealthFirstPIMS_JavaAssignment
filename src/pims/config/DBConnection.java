package pims.config;

import java.sql.*;

public class DBConnection{
    private static final String URL = "jdbc:mysql://localhost:3306/healthfirst_pims";
    private static final String USER = "root";
    private static final String PASSWORD = "56_appLe";

    public static Connection getConnection() throws Exception{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}