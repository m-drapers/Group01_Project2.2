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

    //housing peramiters
    static int numOfHouses = 12;
    static int numOfStudents = 10; 
    static StudentData data = new StudentData(numOfStudents);


    //wykys
   static Wyck amby = new Wyck("Amby", 400.0, 750.0, 6.2, 7.0);
   static Wyck biesland = new Wyck("Biesland", 430.0, 700.0, 4.7, 5.5);
   static Wyck binnenstad = new Wyck("Binnenstad", 430.0, 800.0, 3.9, 4.6);
   static Wyck borgharen = new Wyck("Borgharen", 400.0, 750.0, 8.1, 9.0);
   static Wyck boschpoort = new Wyck("Boschpoort", 400.0, 800.0, 6.1, 6.9);
   static Wyck boschstraatkwartier = new Wyck("Boschstraatkwartier", 430.0, 800.0, 4.7, 5.7);
   static Wyck heer = new Wyck("Heer", 430.0, 850.0, 2.4, 2.8);
   static Wyck itteren = new Wyck("Itteren", 400.0, 700.0, 10.0, 10.7);
   static Wyck jekerkwartier = new Wyck("Jekerkwartier", 450.0, 850.0, 3.3, 4.1);
   static Wyck sintPieter = new Wyck("Sint Pieter", 400.0, 800.0, 5.5, 6.1);
   static Wyck villapark = new Wyck("Villapark", 450.0, 850, 3.7, 4.0);
   static Wyck wyck = new Wyck("Wyck", 400.0, 900.0, 3.5, 4.1);
   static Wyck[] wycks = {amby, biesland, binnenstad, borgharen, boschpoort,boschstraatkwartier, heer, itteren, jekerkwartier, sintPieter, villapark, wyck};
    
   //file writer
   //static FileWriter writer;

    public static void main(String[] args) {

       createSQLFile();
       generateHouseData();
       generateStudentData();
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
    }

    public static void generateHouseData(){
        int numberOfUnits = numOfHouses/wycks.length;
        System.out.println("num: " + numberOfUnits );
        try {
        FileWriter writer = new FileWriter(sqlFilePath, true);
        for(int i = 0; i < wycks.length; i++ ){
            Wyck current = wycks[i];
            int count = 0;
            while(count < numberOfUnits){
                double price = current.generateRandomPrice();
                double distance = current.generateRandomPrice();
                int units = current.generateNumberOfUnits();
                String insertQuery = "INSERT INTO Houses (Price, Distance, Places) VALUES (" + price+ "," + distance +"," + units+ ")\n";
                writer.write(insertQuery);
                count += units;
            }
        }
        writer.close();
        System.out.println("housing data has been written to data.sql");
    }catch (IOException e) {
        e.printStackTrace();
    }
    }

    public static void generateStudentData(){
        try {
            FileWriter writer = new FileWriter(sqlFilePath, true);
            for(int i = 0; i < numOfStudents; i++){
                double budget = data.generateRandomPreferencesPrice();
                double maxDistance = data.generateRandomPreferencesDistance();
                String insertQuery = "INSERT INTO Students (Budget, MaxDis) VALUES (" + budget +"," + maxDistance +") \n";
                writer.write(insertQuery);
            }
            
            writer.close();
            System.out.println("student data has been written to data.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void clearFile(String filePath) throws IOException {
            File file = new File(sqlFilePath);
            if (file.exists()) {
                file.delete(); // delete the existing file
            }
            file.createNewFile(); //recreate the file
            System.out.println("File reset successfully");
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
