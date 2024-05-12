package org.LP.solver;

public class SimplexHardcodedTest {

    public static void main(String[] args) {

        // Define the constraint coefficients
        double[][] A = {};

        // objective function coefficients
        double[] C = {-300, -300, -300, -400, -400, -400, -1, -1, -1};

        // RHS constraints
        double[] b = {1, 1, 1, 450, 450, 350, 450, 450, 350, 15, 11, 12, 15, 11, 12, 2, 3};

        // an instance of SimplexSolver
        SimplexSolver simplexSolver = new SimplexSolver();

        // Call the SimplexSolver method to solve the linear programming problem
        double optimumValue = simplexSolver.SimplexSolver(A, C, b);

        if (optimumValue != -1) {
            System.out.println("Optimum value: " + optimumValue);
        } else {
            System.out.println("Problem is unbounded.");
        }
    }
}
