package com.heallink.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/heallink";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method to establish connection with the MySQL database
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database Connected Successfully!");
        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed: " + e.getMessage());
        }
        return con;
    }

    // Test method for inserting a new patient record (for demo purpose)
    public static void insertSamplePatient() {
        String query = "INSERT INTO patients (name, email, password, gender, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, "Ravi Kumar");
            ps.setString(2, "ravi@example.com");
            ps.setString(3, "ravi123");
            ps.setString(4, "Male");
            ps.setString(5, "9876543210");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Patient Registered Successfully!");
            } else {
                System.out.println("⚠️ Failed to Insert Data.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Test method to fetch all patient details
    public static void fetchAllPatients() {
        String query = "SELECT * FROM patients";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            System.out.println("------ Patient Records ------");
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") + " | Email: " + rs.getString("email"));
            }
        } catch (Exception e) {
            System.out.println("❌ Fetch Error: " + e.getMessage());
        }
    }

    // Main method for testing the database operations
    public static void main(String[] args) {
        // Insert a sample record
        insertSamplePatient();

        // Retrieve all patients
        fetchAllPatients();
    }
}
