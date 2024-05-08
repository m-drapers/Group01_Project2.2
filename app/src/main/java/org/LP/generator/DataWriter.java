package org.LP.generator;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;



public class DataWriter {
    //sql connection
    static String url = "jdbc:mysql://localhost:3306/projectData";
    static String user = "root";
    static String password = "meow";

    static final String sqlFilePath = "app/src/main/java/org/LP/generator/data.sql";
    int numOfHouses = 100;
    int numOfStudents = 200; 
    
    public static void main(String[] args) {
       createSQLFile();
       checkConnectionDB();
       fileToDatabase();
        
    }

    public static void createSQLFile() {
        try {
            clearFile(sqlFilePath);
            FileWriter writer = new FileWriter(sqlFilePath);
                String createHousesTable = "CREATE TABLE Houses (id INT AUTO_INCREMENT PRIMARY KEY, Price DECIMAL(10,2), Distance DECIMAL(10,2), Places INT)\n";
                writer.write(createHousesTable);
                String createStudentsTable = "CREATE TABLE Students (id INT AUTO_INCREMENT PRIMARY KEY, Budget DECIMAL(10,2), MaxDis DECIMAL(10,2))\n";
                writer.write(createStudentsTable);
            
            writer.close();
            System.out.println("data has been written to data.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //checkWrittenData();
        //checkDataBase();
    }

    public static void generateData(){

    }

    public static void clearFile(String filePath) throws IOException {
            File file = new File(sqlFilePath);
            if (file.exists()) {
                file.delete(); // delete the existing file
            }
            file.createNewFile(); //recreate the file
        }
    
  
    public static void checkFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath));
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

    public static void checkConnectionDB(){


        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    public static void fileToDatabase(){ 
        
    try{
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();

        // Empty the database
        resetDatabase(statement);


        BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // Execute each SQL statement
                if (!line.trim().isEmpty()) {
                    statement.executeUpdate(line);
                }
            }
            reader.close();

            // Close resources
            statement.close();
            connection.close();
            System.out.println("SQL file executed successfully.");

    } catch(Exception e){
        System.err.println("Error executing SQL file: " + e.getMessage());
        e.printStackTrace();    }
    }

    private static void resetDatabase(Statement statement) throws Exception {
        // Define SQL statements to drop and recreate the database
        String[] sqlStatements = {
                "DROP DATABASE IF EXISTS projectData",
                "CREATE DATABASE projectData",
                "USE projectData"
        };
    
        // Execute each SQL statement individually
        for (String sql : sqlStatements) {
            statement.executeUpdate(sql);
        }
        System.out.println("Database reset successfully.");
    }
    

    

}
