package org.LP.solver;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class simplexSolverCP {
    private double[][] A; // Matrix of coefficients
    private double[] C; // Objective Function coefficient
    private double[] b; // RHS of the constraints
    private int m; // Number of constraints
    private int n; // Number of decision variables
    private double[] minimumRatio;
    private int[] basicVar;
    private double[] maximumRatio; // Objective function coefficient
    private int idxOfIn = -1; // The index of the entering variable
    private int idxOfOut = -1; // The index of the leaving variable

    public double[] solver(double[][] A, double[] C, double[] b) {
        this.A = A;
        this.C = C;
        this.b = b;
        this.m = A.length;
        this.n = A[0].length;
        this.minimumRatio = new double[m];
        this.basicVar = new int[m];
        this.maximumRatio = new double[n];
        inputNums();
        findBasedVariables();

        while (!isOptimum()) {
            idxOfIn = getVariableIn();
            if (idxOfIn == -1) {
                return null; // Unbounded problem
            }
            idxOfOut = getVariableOut();
            if (idxOfOut == -1) {
                return null; // Unbounded problem
            }
            updateVectors();
        }
        System.out.println("finish simplex");
        printVector();
        return getOptimalSolution();
    }

    private void printVector() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(round(A[i][j]) + "\t");
            }
            System.out.println(round(b[i]));
        }
        System.out.println("-----------------------------------------------");
    }

    private void inputNums() {
        for (int i = 0; i < maximumRatio.length; i++) {
            maximumRatio[i] = round(C[i]);
        }
    }

    private void findBasedVariables() {
        for (int i = 0; i < m; i++) {
            basicVar[m - i - 1] = n - i;
        }
    }

    private boolean isOptimum() {
        if (idxOfIn != -1 && idxOfOut != -1) {
            basicVar[idxOfOut] = idxOfIn + 1;
        }
        for (int i = 0; i < n; i++) {
            double temp = round(C[i]);
            for (int j = 0; j < m; j++) {
                temp = round(temp - round(A[j][i] * C[basicVar[j] - 1]));
            }
            maximumRatio[i] = temp;
        }
        boolean hasPositive = false;
        for (int i = 0; i < maximumRatio.length; i++) {
            if (maximumRatio[i] > 0)
                hasPositive = true;
        }
        return !hasPositive;
    }

    private int getVariableIn() {
        int index = -1;
        double maxRatio = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < maximumRatio.length; i++) {
            if (maximumRatio[i] > maxRatio) {
                maxRatio = maximumRatio[i];
                index = i;
            }
        }
        if (maxRatio <= 0) {
            System.out.println("It is an unbounded problem");
            return -1;
        }
        return index;
    }

    private int getVariableOut() {
        for (int i = 0; i < m; i++) {
            if (Double.compare(A[i][idxOfIn], 0) != 0)
                minimumRatio[i] = round(b[i] / A[i][idxOfIn]);
            else {
                minimumRatio[i] = Double.MAX_VALUE; // Avoid division by zero
            }
        }
        int index = -1;
        double minRatio = Double.MAX_VALUE;
        for (int i = 0; i < minimumRatio.length; i++) {
            if (minimumRatio[i] > 0 && minimumRatio[i] < minRatio) {
                minRatio = minimumRatio[i];
                index = i;
            }
        }
        return index;
    }

    private void updateVectors() {
        double temp = A[idxOfOut][idxOfIn];
        for (int i = 0; i < n; i++) {
            A[idxOfOut][i] = round(A[idxOfOut][i] / temp);
        }
        b[idxOfOut] = round(b[idxOfOut] / temp);

        for (int i = 0; i < m; i++) {
            if (i != idxOfOut) {
                double temp1 = A[i][idxOfIn];
                for (int j = 0; j < n; j++) {
                    A[i][j] = round(A[i][j] - round(A[idxOfOut][j] * temp1));
                }
                b[i] = round(b[i] - round(b[idxOfOut] * temp1));
            }
        }
    }

    private double[] getOptimalSolution() {
        double[] solution = new double[n];
        for (int i = 0; i < basicVar.length; i++) {
            if (basicVar[i] <= n) {
                solution[basicVar[i] - 1] = round(b[i]);
            }
        }
        return solution;
    }

    private double round(double value) {
        return  Math.ceil(value * 100.0) / 100.0;

    }

    public double[][] getUpdatedA() {
        return A;
    }

    public double[] getUpdatedB() {
        return b;
    }

    public double[] getUpdatedC() {
        return C;
    }
}
