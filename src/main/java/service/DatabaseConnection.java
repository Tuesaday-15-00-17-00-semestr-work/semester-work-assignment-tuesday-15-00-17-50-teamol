package service;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:library.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
}
