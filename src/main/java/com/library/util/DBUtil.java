package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String DB_USERNAME = "root";  // Change this to your MySQL username
    private static final String DB_PASSWORD = "div2025";      // Change this to your MySQL password
    
    // Static connection instance
    private static Connection connection = null;
    
    // Static block to initialize the connection
    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established successfully!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("Make sure:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'library_db' exists");
            System.err.println("3. Username and password are correct");
            System.err.println("4. MySQL is running on localhost:3306");
            e.printStackTrace();
        }
    }
    
    // Method to get connection
    public static Connection getConnection() {
        try {
            // Check if connection is closed or null, reconnect if needed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    // Method to close connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Method to test database connection
    public static boolean testConnection() {
        try {
            Connection testConn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if (testConn != null) {
                System.out.println("✓ Database connection test successful!");
                System.out.println("✓ Connected to: " + DB_URL);
                testConn.close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Database connection test failed!");
            System.err.println("Error: " + e.getMessage());
            return false;
        }
        return false;
    }
}