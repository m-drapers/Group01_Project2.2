package org.LP.solver;


public class SimplexSolver {
    private static double[][] A; //Matrix of coefficients
    private static double[] C; // Objective Function coefficient
    private static double[] b;// RHS of the constraints
    private static int m; //number of constraints
    private static int n; //number of decision variables
    private static double[] minimumRatio;

    private static int[] basicVar;

    private static double[] maximumRatio; //objective function coefficient
    private static double result = -1;
    private static int idxOfIn = -1; //the index of the entering variable
    private static int idxOfOut = -1; //the index of the leaving variable

    public void SimplexSolver(double[][] A, double[] C, double[] b) {
        this.A = A;
        this.C = C;
        this.b = b;
        m = A.length;
        n = A[0].length;
        minimumRatio = new double[m];
        basicVar = new int[m];
        maximumRatio = new double[n];
        inputNums();
        findBasedVariables();
        while (!isOptimum()) {
            idxOfIn = getVariableIn();
            printVector();
            idxOfOut = getVariableOut();
            if(idxOfOut == -1)
                return;
            updateVectors();
            printVector();
            System.out.println("\n");
        }
        printOptimum();
    }
    private static void printVector() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(A[i][j] + "\t");
            }
            System.out.println(b[i]);
        }
        System.out.println("-----------------------------------------------");
    }
    private static void inputNums() {
        for (int i = 0; i < maximumRatio.length; i++) {
            maximumRatio[i] = C[i];
        }
    }
    private static void findBasedVariables() {
        //add the slack variables to the basic variables
        for (int i = 0; i < m; i++) {
            basicVar[m-i-1] = n-i ;
        }
        System.out.println("basis：");
        for (int i = 0; i < basicVar.length; i++) {
            System.out.print("x" + (basicVar[i]) + "\t");
        }
        System.out.println();
    }
    private static boolean isOptimum() {
        if (idxOfIn != -1 && idxOfOut != -1) {
            //replace the entering variable with the leaving variable
            basicVar[idxOfOut] = idxOfIn + 1;
        }
        //Update the minimum ratio coefficient
        for (int i = 0; i < n; i++) {
            double temp = maximumRatio[i];
            for (int j = 0; j < m; j++) {
                temp -= A[j][i] * C[basicVar[j] - 1];
            }
            maximumRatio[i] = temp;
        }
        boolean hasPositive = false;
        for (int i = 0; i < maximumRatio.length; i++) {
            if(maximumRatio[i] > 0)
                hasPositive = true;
        }
        System.out.println("Is exists a optimal value:" + !hasPositive);
        return !hasPositive;
    }
    private static int getVariableIn() {
        //find the maximum ratio and input this variable into the basis
        int index = 0;
        System.out.println("maximum ratio test：");
        for (int i = 0; i < maximumRatio.length; i++) {
            System.out.print(maximumRatio[i] + "\t");
            if(maximumRatio[i] > maximumRatio[index]){
                index = i;
            }
        }
        System.out.println();
        System.out.println("input variable is" + (index+1));
        return index;
    }

    private static int getVariableOut() {
        System.out.println("minimum ratio test：");
        for (int i = 0; i < m; i++) {
            if( Double.compare(A[i][idxOfIn], 0) != 0)
                minimumRatio[i] = b[i] / A[i][idxOfIn];
            else {
                minimumRatio[i] = 0;
            }
            System.out.print(minimumRatio[i] + "\t");
        }
        System.out.println();

        int index = -1;
        for (int i = 0; i < minimumRatio.length; i++) {
            if(minimumRatio[i] > 0){
                if (index == -1)
                    index = i;
                else if(minimumRatio[i] < minimumRatio[index])
                    index = i;
            }
        }
        if(index == -1){
            System.out.println("It is a unbounded problem");
        }else {
            System.out.println("output variable is " + (basicVar[index]));
        }
        return index;
    }
    // update the tabular
    private static void updateVectors() {
        //make the entering variable to 1
        double temp = A[idxOfOut][idxOfIn];
        for (int i = 0; i < n; i++) {
            A[idxOfOut][i] /= temp;
        }
        b[idxOfOut] /= temp;
        printVector();
        //make the other elements in the column of the entering variable to 0
        for (int i = 0; i < m; i++) {
            double temp1 = A[i][idxOfIn]/A[idxOfOut][idxOfIn];
            if(i != idxOfOut){
                for (int j = 0; j < n; j++) {
                    A[i][j] -= A[idxOfOut][j]*temp1;
                }
                b[i] -= b[idxOfOut] * temp1;
            }
        }
        System.out.println("finish once update");
    }
    private static void printOptimum() {
        result = 0;
        for (int i = 0; i < basicVar.length; i++) {
            result += C[basicVar[i]-1] * b[i];
        }
        System.out.println("Optimum：z = " + result);
    }
}
