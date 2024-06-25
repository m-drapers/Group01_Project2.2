package org.LP.generator;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;




public class DataWriter {
    //sql connection
    static String url = util.getUrl();
    static String user = util.getUser();
    static String password = util.getPassword();

    static final String sqlFilePath = util.getSqlFilePath();

    //housing peramiters
    static int numOfHouses = 2;
    static int numOfStudents = 2; 


    //wykys
   static final WyckData amby = new WyckData("Amby", 400.0, 750.0, 6.2, 7.0);
   static final WyckData biesland = new WyckData("Biesland", 430.0, 700.0, 4.7, 5.5);
//    static final WyckData binnenstad = new WyckData("Binnenstad", 430.0, 800.0, 3.9, 4.6);
//    static final WyckData borgharen = new WyckData("Borgharen", 400.0, 750.0, 8.1, 9.0);
//    static final WyckData boschpoort = new WyckData("Boschpoort", 400.0, 800.0, 6.1, 6.9);
//    static final WyckData boschstraatkwartier = new WyckData("Boschstraatkwartier", 430.0, 800.0, 4.7, 5.7);
//    static WyckData heer = new WyckData("Heer", 430.0, 850.0, 2.4, 2.8);
//    static WyckData itteren = new WyckData("Itteren", 400.0, 700.0, 10.0, 10.7);
//    static WyckData jekerkwartier = new WyckData("Jekerkwartier", 450.0, 850.0, 3.3, 4.1);
//    static WyckData sintPieter = new WyckData("Sint Pieter", 400.0, 800.0, 5.5, 6.1);
//    static WyckData villapark = new WyckData("Villapark", 450.0, 850, 3.7, 4.0);
//    static WyckData wyck = new WyckData("Wyck", 400.0, 900.0, 3.5, 4.1);
//    static WyckData[] wycks = {amby, biesland, binnenstad, borgharen, boschpoort,boschstraatkwartier, heer, itteren, jekerkwartier, sintPieter, villapark, wyck};

   static WyckData[] wycks = {amby, biesland};
   //student budget ratios/ranges
    static final int[] priceRangesLower = {100, 300, 500, 700, 900};
    static final int[] priceRangesHigher = {300, 500, 700, 900, 1200};
    static final double[] priceRatios = {2.1, 51.1, 40.3, 4.3, 2.1};
   //student max distance ratios/ranges
    static final int[] maxDisRangesLower = {5, 10, 20, 30, 40};
    static final int[] maxDisRangesHigher = {10, 20, 30, 40, 50};
    static final double[] maxDisRatios = {74.5, 23.4, 2.1, 0, 0};

    static StudentData data = new StudentData(numOfStudents, priceRangesHigher, priceRangesLower, priceRatios, maxDisRangesHigher, maxDisRangesLower, maxDisRatios);



    public static void main(String[] args) {

       createSQLFile();
       generateHouseData();
       generateStudentData();
       //checkConnectionDB();
       //fileToDatabase();
       //runSimplexSolver();

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

    public static void generateHouseData() {
        int totalGeneratedUnits = 0;
        int numberOfWycks = wycks.length;
        try {
            FileWriter writer = new FileWriter(sqlFilePath, true);
            for (int i = 0; i < numberOfWycks; i++) {
                WyckData current = wycks[i];
                int unitsToGenerate = numOfHouses / numberOfWycks;
                
                // For the last iteration, ensure we cover any remaining units
                if (i == numberOfWycks - 1) {
                    unitsToGenerate = numOfHouses - totalGeneratedUnits;
                }
                
                int count = 0;
                while (count < unitsToGenerate) {
                    double price = current.generateRandomPrice();
                    double distance = current.generateRandomDistance();
                    
                    // Generate a unit number that does not exceed unitsToGenerate
                    int units = current.generateNumberOfUnits();
                    if (count + units > unitsToGenerate) {
                        units = unitsToGenerate - count;
                    }
                    
                    String insertQuery = "INSERT INTO Houses (Price, Distance, Places) VALUES (" + price + "," + distance + "," + units + ") \n";
                    writer.write(insertQuery);
                    count += units;
                    totalGeneratedUnits += units;
                    
                    // Break if we have generated the required number of houses
                    if (totalGeneratedUnits >= numOfHouses) {
                        break;
                    }
                }
            }
            writer.close();
            System.out.println("housing data has been written to data.sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // Closing brace for the method
    
    

    public static void generateStudentData(){
        try {
            FileWriter writer = new FileWriter(sqlFilePath, true);
            List<Double> prices = data.generateRandomPreferencesPrice();
            List<Double> distances = data.generateRandomPreferencesDistance();
            for(int i = 0; i < numOfStudents; i++){
                double budget = prices.get(i);
                double maxDistance = distances.get(i);
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
