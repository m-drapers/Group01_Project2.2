package org.LP.data;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class setUp {

        
        static String sqlFilePath = "/Users/milou/Desktop/Project/Group01_Project2.2/app/src/main/java/org/LP/test data/data25.sql";
        static double[][] housesArray; 
        static double[][] studentsArray;

        public setUp(String sqlFilePath){
            this.sqlFilePath = sqlFilePath;
        }
    
        public static double[][] getHousesArray() {
            return housesArray;
        }
        public static double[][] getStudentsArray() {
            return studentsArray;
        }

    
        public static void getData() {
            List<List<Float>> housesDataList = new ArrayList<>();
             List<List<Float>> studentsDataList = new ArrayList<>();
        
        try {
            List<String> sqlLines = Files.readAllLines(Paths.get(sqlFilePath));
            
            //go through SQL file
            for (String line : sqlLines) {
                line = line.trim();
                if (line.startsWith("INSERT INTO Houses")) {
                    //get house data  
                    String valuesPart = line.split("VALUES")[1].trim();
                    List<Float> values = parseValues(valuesPart);
                    housesDataList.add(values);
                } else if (line.startsWith("INSERT INTO Students")) {
                    //get student data
                    String valuesPart = line.split("VALUES")[1].trim();
                    List<Float> values = parseValues(valuesPart);
                    studentsDataList.add(values);
                }
            }
            
            housesDataList = sortHousesData(housesDataList);
            studentsDataList = sortStudentsData(studentsDataList);

            housesArray = convertListToArray(housesDataList);
            studentsArray = convertListToArray(studentsDataList);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static List<Float> parseValues(String valuesPart) {
        valuesPart = valuesPart.replaceAll("[()]", "").trim();
        return Arrays.stream(valuesPart.split(","))
                .map(String::trim)
                .map(Float::parseFloat)
                .collect(Collectors.toList());
    }

    private static double[][] convertListToArray(List<List<Float>> dataList) {
        double[][] array = new double[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            List<Float> row = dataList.get(i);
            array[i] = new double[row.size()];
            for (int j = 0; j < row.size(); j++) {
                array[i][j] = row.get(j);
            }
        }
        return array;
    }
    private static List<List<Float>> sortStudentsData(List<List<Float>> studentsData) {
        studentsData.sort(Comparator
            .comparing((List<Float> student) -> student.get(0)) // Primary: budget (first column)
            .thenComparing(student -> student.get(1)) // Secondary: distance (second column)
        );
        return studentsData;
    }

    private static List<List<Float>> sortHousesData(List<List<Float>> housesData) {
        housesData.sort(Comparator
            .comparing((List<Float> house) -> house.get(0)) // Primary: price (first column)
            .thenComparing(house -> house.get(1)) // Secondary: distance to university (second column)
        );
        return housesData;
    }

    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
   
    public static SimplexModelData matrixMaker(double[][] housesArray, double[][] studentsArray) {
        int numHouses = housesArray.length;
        int numStudents = studentsArray.length;

        // c matrix (objective function coefficients)
        double[] c = new double[numHouses * numStudents + 3 * numStudents + numHouses];
        for (int i = 0; i < numHouses; i++) {
            for (int j = 0; j < numStudents; j++) {
                //to go from minimzation to maximization we multiply by -1
                c[i * numStudents + j] = -1* housesArray[i][0]; // price of house
            }
        }

        // A matrix (constraint coefficients)
        int numRows = 3 * numStudents + numHouses;
        int numCols = numHouses * numStudents + numRows;
        double[][] A = new double[numRows][numCols];
        int rowIndex = 0;

        // Constraint 1: Each student is assigned to exactly one house
        for (int i = 0; i < numStudents; i++) {
            for (int j = 0; j < numHouses; j++) {
                A[rowIndex][i * numHouses + j] = 1;
            }
            A[rowIndex][numHouses * numStudents + rowIndex] = 1; // slack variable
            rowIndex++;
        }

        // Constraint 2: House price constraint
        for (int i = 0; i < numStudents; i++) {
            for (int j = 0; j < numHouses; j++) {
                A[rowIndex][i * numHouses + j] = -housesArray[j][0]; // price of house
            }
            A[rowIndex][numHouses * numStudents + numStudents + i] = 1; // slack variable
            rowIndex++;
        }

        // Constraint 3: Distance constraint
        for (int i = 0; i < numStudents; i++) {
            for (int j = 0; j < numHouses; j++) {
                A[rowIndex][i * numHouses + j] = -housesArray[j][1]; // distance of house
            }
            A[rowIndex][numHouses * numStudents + 2 * numStudents + i] = 1; // slack variable
            rowIndex++;
        }

        // Constraint 4: House capacity constraint
        for (int i = 0; i < numHouses; i++) {
            for (int j = 0; j < numStudents; j++) {
                A[rowIndex][j * numHouses + i] = housesArray[i][2]; // units in house
            }
            A[rowIndex][numHouses * numStudents + 3 * numStudents + i] = 1; // slack variable
            rowIndex++;
        }

        // b matrix (right-hand side values)
        double[] b = new double[numRows];
        for (int i = 0; i < numStudents; i++) {
            b[i] = 1; // Constraint 1
            b[numStudents + i] = studentsArray[i][0]; // Constraint 2
            b[2 * numStudents + i] = studentsArray[i][1]; // Constraint 3
        }
        for (int i = 0; i < numHouses; i++) {
            b[3 * numStudents + i] = housesArray[i][2]; // Constraint 4
        }

        
        return new SimplexModelData(A, b, c);
    }

    // Helper class to store simplex model data
    static class SimplexModelData {
        double[][] A;
        double[] b;
        double[] c;

        SimplexModelData(double[][] A, double[] b, double[] c) {
            this.A = A;
            this.b = b;
            this.c = c;
        }
    }

    public static void main(String[] args) {
        // Specify the path to your SQL file
        String sqlFilePath = "/Users/milou/Desktop/Project/Group01_Project2.2/app/src/main/java/org/LP/generator/data.sql";
        
        // Initialize setUp class
        setUp setup = new setUp(sqlFilePath);
        
        // Get data from the SQL file
        setUp.getData();
    
        
        // Generate the SimplexModelData
        setUp.SimplexModelData simplexData = setUp.matrixMaker(housesArray, studentsArray);
        
        // Print Simplex model data A, b, c
        System.out.println("Matrix A:");
        setUp.printMatrix(simplexData.A);
        
        System.out.println("Vector b:");
        System.out.println(Arrays.toString(simplexData.b));
        
        System.out.println("Vector c:");
        System.out.println(Arrays.toString(simplexData.c));
    }
    
    }


