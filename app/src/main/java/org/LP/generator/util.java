package org.LP.generator;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class util {

    static String url = "jdbc:mysql://localhost:3306/projectData";
    static String user = "root";
    static String password = "meow";

    static final String sqlFilePath = "app/src/main/java/org/LP/generator/data.sql";

        // Getter and setter for url
        public static String getUrl() {
            return url;
        }
    
        public static void setUrl(String newUrl) {
            url = newUrl;
        }
    
        // Getter and setter for user
        public static String getUser() {
            return user;
        }
    
        public static void setUser(String newUser) {
            user = newUser;
        }
    
        // Getter and setter for password
        public static String getPassword() {
            return password;
        }
    
        public static void setPassword(String newPassword) {
            password = newPassword;
        }
    
        // Getter for sqlFilePath (no setter because it's final)
        public static String getSqlFilePath() {
            return sqlFilePath;
        }
    
    
    public static void runSimplexSolver() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to the database
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connected to the database successfully!");

                // Fetch house data
                List<Double> prices = new ArrayList<>();
                List<Double> distances = new ArrayList<>();
                String houseQuery = "SELECT Price, Distance FROM Houses";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(houseQuery)) {
                    while (rs.next()) {
                        prices.add(rs.getDouble("Price"));
                        distances.add(rs.getDouble("Distance"));
                    }
                }

                // Fetch student data
                List<Double> budgets = new ArrayList<>();
                List<Double> maxDistances = new ArrayList<>();
                String studentQuery = "SELECT Budget, MaxDis FROM Students";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(studentQuery)) {
                    while (rs.next()) {
                        budgets.add(rs.getDouble("Budget"));
                        maxDistances.add(rs.getDouble("MaxDis"));
                    }
                }

                // Prepare data for Simplex Solver
                double[] C = new double[prices.size()];
                double[][] A = new double[budgets.size()][prices.size()];
                double[] b = new double[budgets.size()];

                for (int i = 0; i < prices.size(); i++) {
                    C[i] = prices.get(i); // Minimize cost
                }

                for (int i = 0; i < budgets.size(); i++) {
                    for (int j = 0; j < prices.size(); j++) {
                        A[i][j] = distances.get(j); // Constraint: distances
                    }
                    b[i] = maxDistances.get(i);
                }

                
                // simplexSolver solver = new simplexSolver(A, C, b);
                
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
