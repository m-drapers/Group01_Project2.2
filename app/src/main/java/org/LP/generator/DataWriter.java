package org.LP.generator;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;



public class DataWriter {
    static final String dbPath = "app/src/main/java/org/LP/generator/data.sql";
    int numOfHouses = 100;
    int numOfStudents = 200; 
    
    public static void main(String[] args) {
        
    }

    public static void createDatabase() {
        try {
            clearFile(dbPath);
            FileWriter writer = new FileWriter(dbPath);
                String createHousesTable = "CREATE TABLE Houses (id INT AUTO_INCREMENT PRIMARY KEY, Price DECIMAL(10,2), Distance DECIMAL(10,2), Places INT)";
                writer.write(createHousesTable);
                String createStudentsTable = "CREATE TABLE Students (id INT AUTO_INCREMENT PRIMARY KEY, Budget DECIMAL(10,2), MaxDis DECIMAL(10,2))";
                writer.write(createStudentsTable);
            
            writer.close();
            System.out.println("data has been written to data.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //checkWrittenData();
        checkDataBase();
    }

    public static void clearFile(String filePath) throws IOException {
            File file = new File(dbPath);
            if (file.exists()) {
                file.delete(); // delete the existing file
            }
            file.createNewFile(); //recreate the file
        }
    
  
    public static void checkWrittenData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.sql"));
            String line;
            while ((line = reader.readLine()) != null) {
                // Print each line (each SQL insert statement)
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkDataBase() {
        final String JDBC_URL = "jdbc:sqlite:/Users/milou/Desktop/Group01_Project2.2/data.sql";
        Connection conn = null;
        try {
            // Connect to the database
            conn = DriverManager.getConnection(JDBC_URL);
            
            // Create a statement
            Statement stmt = conn.createStatement();
            
            // Execute SQL queries to get table structure
            ResultSet rsHouses = stmt.executeQuery("PRAGMA table_info(Houses)");
            ResultSet rsStudents = stmt.executeQuery("PRAGMA table_info(Students)");
            
            // Print table structures
            System.out.println("Houses Table Structure:");
            while (rsHouses.next()) {
                System.out.println(rsHouses.getString("name") + " " + rsHouses.getString("type"));
            }
            System.out.println("\nStudents Table Structure:");
            while (rsStudents.next()) {
                System.out.println(rsStudents.getString("name") + " " + rsStudents.getString("type"));
            }
            
            rsHouses.close();
            rsStudents.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
