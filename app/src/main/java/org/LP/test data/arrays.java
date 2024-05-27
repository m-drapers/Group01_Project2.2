import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class arrays {

        
        static String sqlFilePath = "/Users/milou/Desktop/Project/Group01_Project2.2/app/src/main/java/org/LP/test data/data25.sql";
        static double[][] housesArray; 
        static double[][] studentsArray;

        public static void main(String[] args) {
            getData();
            printMatrix(studentsArray);
            
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
   

}
