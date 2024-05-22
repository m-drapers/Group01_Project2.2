package org.LP.solver;

public class BranchAndBound {

    private static double[][] A; //Matrix of coefficients
    private static double[] C; // Objective Function coefficient
    private static double[] b;// RHS of the constraints
    private static int numOfArtificialVar;
    private static int[] bigMConstraint;

    private static int number_of_variables;

    public double BranchAndBound(double[][] A, double[] C, double[] b, int numOfArtificialVar, int[] bigMConstraint) {
        BranchAndBound.A = A;
        BranchAndBound.C = C;
        BranchAndBound.b = b;
        BranchAndBound.numOfArtificialVar = numOfArtificialVar;
        BranchAndBound.bigMConstraint = bigMConstraint;


        //Compute the number of variables that needs to be considered
        for (int i = 0; C[i] > 0; i++) {
            number_of_variables++;
        }

        //Create a solution space
        double[] solution = new double[number_of_variables];

        //Step one: Compute the LP relaxation solution
        BigM bigM = new BigM();
        double initialOptimal = bigM.SimplexSolver(A, C, b, numOfArtificialVar, bigMConstraint);

        //Step two: Check if the solution is binary
        //The check for this is still missing
        //However, it is extremely unlikely that the initial solution is binary
        boolean binary = false;

        if (binary) {
            return initialOptimal;
        }


        //Step three: Branching and bounding
        //If step two did not give a binary solution continue
        //Take here x1 = 0 and x1 = 1 and split the problem into two cases
        //Update the matrices
        //Use a recursive structure to compute all cases and return the optimal value
        //This is computed in the branchAndOptimalValue method

        return branchAndBoundOptimalValue(0, initialOptimal);
    }

    private double branchAndBoundOptimalValue(int count, double optimal) {
        // Base case: If we've considered all variables, return the optimal value found
        if (count >= number_of_variables) {
            return optimal;
        }

        BigM bigM = new BigM();

        // Update matrices for x = 0 and x1
        double[][] splitMatrixA = dropColumn(A, 0);
        double[] splitMatrixC = dropIndex(C, 0);
        double[] splitMatrixB1 = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            splitMatrixB1[i] = b[i] - A[i][0];
        }

        A = splitMatrixA;
        C = splitMatrixC;
        //Value of b needs to be adjusted

        // Solve for x = 0
        double optimalValueForZero = bigM.SimplexSolver(splitMatrixA, splitMatrixC, b, numOfArtificialVar, bigMConstraint);

        // Solve for x = 1
        double optimalValueForOne = C[0] + bigM.SimplexSolver(splitMatrixA, splitMatrixC, splitMatrixB1, numOfArtificialVar, bigMConstraint);

        //Decide on which branch to continue
        if (optimalValueForZero > optimalValueForOne) {
            return branchAndBoundOptimalValue(count + 1, optimalValueForZero);
        }
        else {
            return branchAndBoundOptimalValue(count + 1, optimalValueForOne);
        }
    }

    public static double[][] dropColumn(double[][] matrix, int columnIndex) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        double[][] newMatrix = new double[numRows][numCols - 1];

        for (int i = 0; i < numRows; i++) {
            int newColIndex = 0;
            for (int j = 0; j < numCols; j++) {
                if (j != columnIndex) {
                    newMatrix[i][newColIndex] = matrix[i][j];
                    newColIndex++;
                }
            }
        }

        return newMatrix;
    }

    public static double[] dropIndex(double[] matrix, int columnIndex) {
        int numCols = matrix.length;

        double[] newMatrix = new double[numCols - 1];

        System.arraycopy(matrix, 1, newMatrix, 0, numCols-1);

        return newMatrix;
    }
}