package com.busreservation.busresevationsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "bus_reservation";
    private static final String USER = "root";  
    private static final String PASSWORD = "chirag";  
    private static final String SQL_FILE_PATH = "C:/Users/Chirag/Documents/NetBeansProjects/BusResevationSystem/src/main/java/com/busreservation/busresevationsystem/NewUserSchema.sql"; // Change to your SQL file path

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL without specifying a database
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            createDatabaseIfNotExists(conn);

            // Now connect to the actual database
            conn = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
            createUsersTableIfNotExists(conn);  // Creating table

            // Execute SQL file (if exists)
            executeSQLFile(conn, SQL_FILE_PATH);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static void createDatabaseIfNotExists(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(createDBQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createUsersTableIfNotExists(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "username VARCHAR(100) NOT NULL UNIQUE,"
                    + "email VARCHAR(100) NOT NULL UNIQUE, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "weburl VARCHAR(225), "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
            stmt.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeSQLFile(Connection conn, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Statement stmt = conn.createStatement()) {
            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sql.append(line).append(" ");
                if (line.trim().endsWith(";")) { // Execute query when a semicolon is encountered
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
