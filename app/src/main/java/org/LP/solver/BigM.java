package org.LP.solver;

public class BigM {
    private static double A[][]; //Matrix of coefficients
    private static double C[]; // Objective Function coefficient
    private static double b[];// RHS of the constraints
    private static int m; //number of constraints
    private static int n; //number of decision variables
    private static double minimumRatio[];
    private static double c[];//ORIGINAL Objective Function coefficient


    private static int basicVar[];

    private static double maximumRatio[]; //objective function coefficient
    private static double result = -1;
    private static int idxOfIn = -1; //the index of the entering variable
    private static int idxOfOut = -1; //the index of the leaving variable
    private static int M = 100000;
    private static int numOfArtificialVar = 0;
    private static int bigMConstraint[];
    private static int numOriginalVar = 0;


    public double SimplexSolver(double[][] A, double[] C, double[] b, int numOfArtificialVar, int[] bigMConstraint) {
        this.numOfArtificialVar = numOfArtificialVar;
        this.bigMConstraint = bigMConstraint;
        this.numOriginalVar = C.length-b.length;
        if (numOfArtificialVar > 0 && bigMConstraint.length == numOfArtificialVar) {
            double[][] newA = new double[A.length][A[0].length + numOfArtificialVar];
            double[] newC = new double[C.length + numOfArtificialVar];
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < A[0].length; j++) {
                    newA[i][j] = A[i][j];
                }
                for (int j = A[0].length; j < A[0].length + numOfArtificialVar; j++) {
                    newA[i][j] = 0;
                }
            }
            for (int i = 0; i < bigMConstraint.length; i++) {
                newA[bigMConstraint[i]-1][A[0].length + i] = 1;
            }
            for (int i = C.length; i < newC.length; i++) {
                newC[i] = -M;
            }
            for (int i = 0; i < C.length; i++) {
                newC[i] = C[i];
            }
            this.A = newA;
            this.C = newC;
        }else {
            System.out.println("The number of artificial variables is not equal to the number of constraints");
        }
        this.c=C;
        this.b = b;
        m = this.A.length;
        n = this.A[0].length;
        minimumRatio = new double[m+numOfArtificialVar];
        basicVar = new int[m];
        maximumRatio = new double[n];
        inputNums();
        findBasedVariables();
        while (!isOptimum()) {
            idxOfIn = getVariableIn();
            printVector();
            idxOfOut = getVariableOut();
            if (idxOfOut == -1)
                return -1;
            updateVectors();
            printVector();
            System.out.println("\n");
        }
        printOptimum();
        return result;
    }

    private static void printVector() {
        System.out.println("The objective function coefficient(Row 0) is ");
        for (int i = 0; i < C.length; i++) {
            System.out.print(C[i] + "\t");
        }
        System.out.println();
        System.out.println("The matrix is ");
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
        if (numOfArtificialVar> 0 && bigMConstraint.length==numOfArtificialVar) {
            int temp = 0;
            if (bigMConstraint.length<m) {
                boolean[] exist = new boolean[m];
                for (int num: bigMConstraint) {
                    exist[num-1] = true;
                }
                for (int i = 0; i < m; i++) {
                    if (!exist[i]) {
                        basicVar[temp] = m + i+1;
                        temp++;
                    }
                }
                for (int i = 0; i < bigMConstraint.length; i++) {
                    basicVar[m-bigMConstraint.length+i] = numOriginalVar+m +i+1;
                }
            }else {
                for (int i = 0; i < bigMConstraint.length; i++) {
                    basicVar[i] = numOriginalVar+m +i+1;
                }
            }
        }else {
            for (int i = 0; i < m; i++) {
                basicVar[m - i - 1] = n - i;
            }
        }
        //add the slack variables to the basic variables
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
            if (maximumRatio[i] > 0)
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
            C[i] = maximumRatio[i];
            if (maximumRatio[i] > maximumRatio[index]) {
                index = i;
            }
        }
        if (maximumRatio[index] <= 0) {
            System.out.println("It is a unbounded problem");
            return -1;
        }
        System.out.println();
        System.out.println("input variable is " + (index + 1));
        return index;
    }

    private static int getVariableOut() {
        System.out.println("minimum ratio test：");
        for (int i = 0; i < m; i++) {
            if (Double.compare(A[i][idxOfIn], 0) != 0)
                minimumRatio[i] = b[i] / A[i][idxOfIn];
            else {
                minimumRatio[i] = 0;
            }
            System.out.print(minimumRatio[i] + "\t");
        }
        System.out.println();

        int index = -1;
        for (int i = 0; i < minimumRatio.length; i++) {
            if (minimumRatio[i] > 0) {
                if (index == -1)
                    index = i;
                else if (minimumRatio[i] < minimumRatio[index])
                    index = i;
            }
        }
        if (index == -1) {
            System.out.println("It is a unbounded problem");
        } else {
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
            double temp1 = A[i][idxOfIn] / A[idxOfOut][idxOfIn];
            if (i != idxOfOut) {
                for (int j = 0; j < n; j++) {
                    A[i][j] -= A[idxOfOut][j] * temp1;
                }
                b[i] -= b[idxOfOut] * temp1;
            }
        }

        System.out.println("finish once update");
    }

    private static void printOptimum() {
        result = 0;
        for (int i = 0; i < basicVar.length; i++) {
            if (basicVar[i] <= m)
                result += c[basicVar[i] - 1] * b[i];
        }
        System.out.println("Optimum：z = " + result);
    }
}
