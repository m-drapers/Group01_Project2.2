package org.LP.generator;

public class LPProblemPreparer {

    public static double[][] addSlackSurplusArtificial(double[][] A, double[] b, String[] constraintTypes) {
        int numConstraints = A.length;
        int numVariables = A[0].length;

        // CCount the number of slack, surplus, and artificial variables needed
        int numSlackSurplus = 0;
        int numArtificial = 0;
        for (String type : constraintTypes) {
            if ("<=".equals(type) || ">=".equals(type)) {
                numSlackSurplus++;
            }
            if (">=".equals(type) || "=".equals(type)) {
                numArtificial++;
            }
        }

        // initialize new matrix with extra columns for slack, surplus, and artiificial variables
        double[][] newA = new double[numConstraints][numVariables + numSlackSurplus + numArtificial];

        int slackSurplusIndex = numVariables;
        int artificialIndex = numVariables + numSlackSurplus;

        for (int i = 0; i < numConstraints; i++) {
            System.arraycopy(A[i], 0, newA[i], 0, numVariables);
            if ("<=".equals(constraintTypes[i])) {
                newA[i][slackSurplusIndex++] = 1; // Add slack variable
            } else if (">=".equals(constraintTypes[i])) {
                newA[i][slackSurplusIndex++] = -1; // Add surplus variable
                newA[i][artificialIndex++] = 1; // Add artificial variable
            } else if ("=".equals(constraintTypes[i])) {
                newA[i][artificialIndex++] = 1; // Add artificial variable
            }
        }

        return newA;
    }

    // Mmrthod to print matrix (for debugging)
    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    // Method to print vector (for debugging)
    public static void printVector(double[] vector) {
        for (double val : vector) {
            System.out.print(val + " ");
        }
        System.out.println();
    }

    // Example method to prepare the full LP problem
    public static void prepareLPProblem(double[][] A, double[] b, double[] C, String[] constraintTypes) {
        double[][] newA = addSlackSurplusArtificial(A, b, constraintTypes);
        System.out.println("Matrix A with slack, surplus, and artificial variables:");
        printMatrix(newA);
        System.out.println("Vector C:");
        printVector(C);
        System.out.println("Vector b:");
        printVector(b);
    }
}
