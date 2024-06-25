package org.LP.data;

import org.LP.solver.BB;


import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class setUp {


    static String sqlFilePath = "D:\\IntelliJ IDEA 2023.3.4\\Project\\Group01_Project2.2\\app\\src\\main\\java\\org\\LP\\testData\\data25.sql";
    static double[][] housesArray;
    static double[][] studentsArray;

    public setUp(String sqlFilePath) {
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
                c[i * numStudents + j] = housesArray[i][0]; // price of house
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
        String sqlFilePath = "D:\\IntelliJ IDEA 2023.3.4\\Project\\Group01_Project2.2\\app\\src\\main\\java\\org\\LP\\testData\\data25.sql";
        // Initialize setUp class
        setUp setup = new setUp(sqlFilePath);

        // Get data from the SQL file
        setUp.getData();
        studentsArray = setUp.getStudentsArray();
        housesArray = setUp.getHousesArray();

        double[] studentPrice = new double[studentsArray.length];
        for (int i = 0; i < studentPrice.length; i++) {
            studentPrice[i] = studentsArray[i][0];
        }
        double[] housePrice = new double[housesArray.length];
        for (int i = 0; i < housePrice.length; i++) {
            housePrice[i] = housesArray[i][0];
        }
        double[] housingNumber = new double[housesArray.length];
        for (int i = 0; i < housingNumber.length; i++) {
            housingNumber[i] = housesArray[i][2];
        }
        double[] singlePrice = new double[housesArray.length];
        for (int i = 0; i < singlePrice.length; i++) {
            singlePrice[i] = housePrice[i] / housingNumber[i];
        }
        ArrayList<Double> cArray = new ArrayList<>();
        while (cArray.size() < 351) {
            for (int i = 0; i < singlePrice.length; i++) {
                cArray.add(singlePrice[i]);
            }
        }
        double[] c = new double[350];
        for (int i = 0; i < c.length; i++) {
            c[i] = cArray.get(i);
            System.out.println(c[i]);
        }
        double[][] A = new double[39][350];
        for (int i = 0; i < A.length; i++) {
            if (i == 0) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 1) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 2) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 3) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 4) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 5) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 6) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 7) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 8) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 9) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 10) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 11) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 12) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 13) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i == 14) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i == 15) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i == 16) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i==17) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            } else if (i==18) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i==19) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i==20) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i==21) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i==22) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i==23) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i==24) {
                for (int j = 14 * i; j < 14 * (i + 1); j++) {
                    A[i][j] = 1;
                }
            }else if (i > 24) {
                for (int j = 0; j < A[0].length; j++) {
                    if (j % 14 == i - 15) {
                        A[i][j] = 1;
                    }
                    A[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
        double[] b = new double[39];
        for (int i = 0; i < studentPrice.length; i++) {
            b[i] = studentPrice[i];
        }
        for (int i = 25; i < b.length; i++) {
            b[i] = housingNumber[i-25];
        }
        double[][] A1 = new double[15][350];
        for (int i = 0; i < A1.length; i++) {
            for (int j = i*14; j < (i+1)*14; j++) {
                A1[i][j] = 1;
            }
        }
        double[] b1 = new double[15];
        for (int i = 0; i < b1.length; i++) {
            b1[i] = 1;
        }
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                System.out.print(A[i][j]+" ");
            }
            System.out.println();
        }
        BB branchAndBound = new BB(A, b, c, A1, b1);
        branchAndBound.solve();
        System.out.println("Optimal value = " + branchAndBound.getOptimalValue());
        System.out.println("Solution:");
        for (int i = 0; i < branchAndBound.getSolution().size(); i++) {
            System.out.print("x" + (i + 1) + " = " + branchAndBound.getSolution().get(i)+" ");
            if (i % 14 == 0) {
                System.out.println();
            }
        }
    }
}




