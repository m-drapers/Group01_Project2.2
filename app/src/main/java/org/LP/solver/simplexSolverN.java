package org.LP.solver;

public class simplexSolverN {
    private double[][] A; // Matrix of coefficients
    private double[] C; // Objective function coefficients
    private double[] b; // Right-hand side of the constraints
    private int m; // Number of constraints
    private int n; // Number of decision variables
    private double[] minimumRatio;
    private int[] basicVar;
    private double[] maximumRatio; // For checking optimality
    private double result = -1;
    private int idxOfIn = -1; // Index of the entering variable
    private int idxOfOut = -1; // Index of the leaving variable

    // Constructor initializes the solver with data directly
    public simplexSolverN(double[][] A, double[] C, double[] b) {
        this.A = A;
        this.C = C;
        this.b = b;
        this.m = A.length; // Number of constraints
        this.n = A[0].length; // Number of decision variables
        this.minimumRatio = new double[m];
        this.basicVar = new int[m];
        this.maximumRatio = new double[n];
        initializeSolver();
    }

    private void initializeSolver() {
        inputNums();
        findBaseVariables();
        executeSimplex();
    }

    private void executeSimplex() {
        while (!isOptimum()) {
            idxOfIn = getVariableIn();
            if (idxOfIn == -1) return; // Stop if problem is unbounded
            idxOfOut = getVariableOut();
            if (idxOfOut == -1) return; // Stop if no suitable leaving variable found
            updateVectors();
        }
        printOptimum();
    }

    private void inputNums() {
        System.arraycopy(C, 0, maximumRatio, 0, n);
    }

    private void findBaseVariables() {
        // Basic variables are typically the slack variables added to each constraint
        for (int i = 0; i < m; i++) {
            basicVar[i] = n + i; // Slack variables are positioned after decision variables in A
        }
    }

    private boolean isOptimum() {
        boolean hasPositive = false;
        for (int i = 0; i < n; i++) {
            double temp = C[i];
            for (int j = 0; j < m; j++) {
                temp -= A[j][i] * C[basicVar[j] - 1];
            }
            maximumRatio[i] = temp;
            if (maximumRatio[i] > 0) hasPositive = true;
        }
        return !hasPositive;
    }

    private int getVariableIn() {
        double max = Double.NEGATIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < n; i++) {
            if (maximumRatio[i] > max) {
                max = maximumRatio[i];
                index = i;
            }
        }
        return index;
    }

    private int getVariableOut() {
        double minRatio = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < m; i++) {
            if (A[i][idxOfIn] > 0) {
                double ratio = b[i] / A[i][idxOfIn];
                if (ratio < minRatio) {
                    minRatio = ratio;
                    index = i;
                }
            }
        }
        return index;
    }

    private void updateVectors() {
        double pivot = A[idxOfOut][idxOfIn];
        for (int i = 0; i < n; i++) {
            A[idxOfOut][i] /= pivot;
        }
        b[idxOfOut] /= pivot;

        for (int i = 0; i < m; i++) {
            if (i != idxOfOut) {
                double factor = A[i][idxOfIn];
                for (int j = 0; j < n; j++) {
                    A[i][j] -= A[idxOfOut][j] * factor;
                }
                b[i] -= b[idxOfOut] * factor;
            }
        }
    }

    private void printOptimum() {
        result = 0;
        for (int i = 0; i < m; i++) {
            if (basicVar[i] < n) { // Only consider non-slack variables in the result
                result += C[basicVar[i] - 1] * b[i];
            }
        }
        System.out.println("Optimum result: z = " + result);
    }
}
