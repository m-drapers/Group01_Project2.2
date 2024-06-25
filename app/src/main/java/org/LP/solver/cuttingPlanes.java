package org.LP.solver;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class cuttingPlanes {

    private static final BigDecimal EPSILON = new BigDecimal("1e-9");
    private static final BigDecimal BIG_M = new BigDecimal("1e5");
    private static double[][] A;
    private static double[] b;
    private static double[] C; // Objective Function coefficient
    private static simplexSolverCP solver;

    public static class GomoryCut {
        public double[] coefficients;
        public double rhs;

        public GomoryCut(double[] coefficients, double rhs) {
            this.coefficients = coefficients;
            this.rhs = rhs;
        }
    }

    public static void setA(double[][] a) {
        cuttingPlanes.A = a;
    }

    public static void setB(double[] b) {
        cuttingPlanes.b = b;
    }

    public static void setC(double[] c) {
        cuttingPlanes.C = c;
    }

    public static double[][] getA() {
        return A;
    }

    public static double[] getB() {
        return b;
    }

    public static double[] getC() {
        return C;
    }

    public static void setSolver() {
        solver = new simplexSolverCP();
    }

    public static ArrayList<GomoryCut> generateGomoryCuts() {
        ArrayList<GomoryCut> cuts = new ArrayList<>();
        int numRows = A.length;
        int numCols = A[0].length;

        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            BigDecimal rhs = BigDecimal.valueOf(b[rowIndex]);
            BigDecimal fractionalRHS = roundToFractional(rhs);

            if (fractionalRHS.compareTo(EPSILON) > 0 && fractionalRHS.compareTo(BigDecimal.ONE.subtract(EPSILON)) < 0) {
                double[] cutCoefficients = new double[numCols];
                boolean hasFractionalCoeff = false;
                for (int colIndex = 0; colIndex < numCols; colIndex++) {
                    BigDecimal coeff = BigDecimal.valueOf(A[rowIndex][colIndex]);
                    BigDecimal fractionalCoeff = roundToFractional(coeff);
                    if (fractionalCoeff.compareTo(EPSILON) > 0 && fractionalCoeff.compareTo(BigDecimal.ONE.subtract(EPSILON)) < 0) {
                        cutCoefficients[colIndex] = fractionalCoeff.doubleValue();
                        hasFractionalCoeff = true;
                    } else {
                        cutCoefficients[colIndex] = 0;
                    }
                }
                if (hasFractionalCoeff) {
                    GomoryCut cut = new GomoryCut(cutCoefficients, fractionalRHS.doubleValue());
                    cuts.add(cut);
                }
            }
        }

        System.out.println("Generated cuts:");
        for (int i = 0; i < cuts.size(); i++) {
            GomoryCut cut = cuts.get(i);
            System.out.print("Cut " + (i + 1) + ": ");
            for (int j = 0; j < cut.coefficients.length; j++) {
                System.out.print(cut.coefficients[j] + " ");
            }
            System.out.println("RHS: " + cut.rhs);
        }

        System.out.println("Number of cuts: " + cuts.size());

        return cuts;
    }

    public static void applyCuts(List<GomoryCut> cuts) {
        int numRows = A.length;
        int numCols = A[0].length;

        // New dimensions for A matrix to include artificial variables
        int newNumCols = numCols + cuts.size() + cuts.size();  // Adding surplus and artificial variables
        int totalNumRows = numRows + cuts.size(); // Adding new cuts as rows

        // Create new matrices for A and b with the updated dimensions
        double[][] newA = new double[totalNumRows][newNumCols];
        double[] newB = new double[totalNumRows];
        double[] newC = new double[newNumCols]; // New objective function coefficients

        // Copy the original A matrix and b vector to the new matrices
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                newA[i][j] = roundUp(A[i][j]);
            }
            newB[i] = roundUp(b[i]);
        }

        // Copy the original c vector to the new c vector
        for (int j = 0; j < numCols; j++) {
            newC[j] = roundUp(C[j]);
        }

        // Add the Gomory cuts as new constraints
        for (int i = 0; i < cuts.size(); i++) {
            GomoryCut cut = cuts.get(i);
            for (int j = 0; j < numCols; j++) {
                newA[numRows + i][j] = roundUp(cut.coefficients[j]);
            }
            // Add the surplus variable
            newA[numRows + i][numCols + i] = -1.0; // Surplus variable has a coefficient of -1
            // Add the artificial variable
            newA[numRows + i][numCols + cuts.size() + i] = 1.0; // Artificial variable

            newB[numRows + i] = roundUp(cut.rhs);
            newC[numCols + cuts.size() + i] = -BIG_M.doubleValue(); // Set artificial variable cost to -BIG_M
        }

        // Update the global A, b, and c with the new matrices
        A = newA;
        b = newB;
        C = newC;

        System.out.println("Applied cuts:");
    }

    public static void GomoryCutSolver(double[][] A, double[] C, double[] b) {
        // Set the A matrix and b vector for the Big-M solver
        setA(A);
        setB(b);
        setC(C);
        setSolver();

        // Initialize the solution
        double[] solution = solver.solver(A, C, b);

        // Initialize the tolerance for integer values
        double tolerance = 1e-6;

        // Initialize the maximum number of iterations
        int maxIterations = 100;

        // Initialize the iteration counter
        int iteration = 0;

        while (iteration < maxIterations) {
            // Check if the solution is integer
            boolean isInteger = true;
            for (double value : solution) {
                if (Math.abs(value - Math.round(value)) > tolerance) {
                    isInteger = false;
                    break;
                }
            }

            if (isInteger) {
                break;
            }

            // Generate Gomory cuts
            List<GomoryCut> cuts = generateGomoryCuts();

            // Apply the cuts
            applyCuts(cuts);

            // Solve the updated LP problem
            double[] previousSolution = solution;
            solution = solver.solver(getA(), getC(), getB());
            if (solution == null) {
                solution = previousSolution;
                break;
            }

            // Get updated A, b, and C from SimplexSolver
            setA(solver.getUpdatedA());
            setB(solver.getUpdatedB());
            setC(solver.getUpdatedC());
            System.out.println("Iteration: " + iteration);
            iteration++;
        }

        // Print the final solution
        System.out.println("Final solution:");
        for (int i = 0; i < solution.length; i++) {
            System.out.printf("x%d = %.2f\n", i + 1, solution[i]);
        }
        double finalObjectiveValue = 0;
        for (int i = 0; i < C.length; i++) {
            finalObjectiveValue += C[i] * solution[i];
        }
        System.out.printf("Objective function value: %.2f\n", finalObjectiveValue);
    }

    // public static void main(String[] args) {
    //     // Including slack variables
    //     double[][] A = {
    //         {1, 1, 1, 0, 0},
    //         {-1, 1, 0, 1, 0},
    //         {6, 2, 0, 0, 1}
    //     };
    //     double[] b = {5, 0, 21};
    //     double[] C = {2, 1, 0, 0, 0};
    //     cuttingPlanes.GomoryCutSolver(A, C, b);
    // }

    private static double roundUp(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(10, RoundingMode.HALF_UP);  // Increased precision
        return bd.doubleValue();
    }

    private static BigDecimal roundToFractional(BigDecimal coeff) {
        BigDecimal fractionalPart = coeff.subtract(coeff.setScale(0, RoundingMode.FLOOR));
        fractionalPart = fractionalPart.setScale(10, RoundingMode.HALF_UP); // Increased precision
        return fractionalPart;
    }
    

    private static void printMatrix(double[][] matrix, String name) {
        System.out.println("Matrix " + name + ":");
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    private static void printArray(double[] array, String name) {
        System.out.println("Array " + name + ":");
        for (double value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}




/* 
Expected Output:
Gomory cut: 0.00 0.50 0.30 >= 0.20
New matrix A:
1.00 2.50 3.50 
0.00 1.50 2.30 
0.00 0.00 1.00 
0.00 0.50 0.30 
New vector b:
4.00
3.20
1.50
0.20
*/
