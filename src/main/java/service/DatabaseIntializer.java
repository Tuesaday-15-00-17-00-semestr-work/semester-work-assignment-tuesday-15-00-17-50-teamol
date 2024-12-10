package service;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseIntializer {
    public static void initializeDatabase() {
        String createBooksTable = """
        CREATE TABLE IF NOT EXISTS books (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            author TEXT NOT NULL,
            genre TEXT NOT NULL
        );
    """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createBooksTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

