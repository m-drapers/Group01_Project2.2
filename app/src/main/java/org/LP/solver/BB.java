package org.LP.solver;

import java.util.ArrayList;

public class BB {
    double[][] A;
    double[] b;
    double[] c;
    double[][] A1;
    double[] b1;
    double []stableC;
    double optimalValue;
    ArrayList<Double> solution;
    int count = 0;

    public BB(double[][] A, double[] b, double[] c,double[][] A1, double[] b1) {
        this.A = A;
        this.b = b;
        this.c = c;
        this.stableC = c;
        this.A1 = A1;
        this.b1 = b1;

    }
    public void solve() {
        Simplex simplex = new Simplex();
        simplex.Simplex(A, b, c,A1,b1);
        this.solution = simplex.getSolution();
        this.optimalValue = simplex.getObjectiveValue();
        while (!isInteger(solution)) {
            int index=-1;
            for (int i = 0; i < solution.size(); i++) {
                if (!isInteger(solution.get(i))) {
                    index = i;
                    break;
                }
                int upperBound = (int) Math.ceil(solution.get(index));
                int lowerBound = (int) Math.floor(solution.get(index));
                double[] newBUpper = createNewB(b, A, index, upperBound);
                double[] newBLower = createNewB(b, A, index, lowerBound);
                double[][] newA = createNewA(A, index);
                double[] newC = createNewC(c, index);
                double[][] newA1 = createNewA(A1, index);
                double[] newB1Upper = createNewB(b1, A1, index, upperBound);
                double[] newB1Lower = createNewB(b1, A1, index, lowerBound);
                simplex.Simplex(newA, newBUpper, newC,newA1,newB1Upper);
                double upperObjective = simplex.objectiveValue;
                simplex.Simplex(newA, newBLower, newC,newA1,newB1Lower);
                double lowerObjective = simplex.objectiveValue;
                if (upperObjective >= lowerObjective) {
                    solution.set(index, (double) upperBound);
                    this.b = newBUpper;

                }else {
                    solution.set(index, (double) lowerBound);
                    this.b = newBLower;
                }
                this.A = newA;
                this.c = newC;
                count++;
            }
        }
    }

    public boolean isInteger(ArrayList<Double> solution) {
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) % 1 != 0) {
                return false;
            }
        }
        return true;
    }
    public boolean isInteger(double x) {
        return x % 1 == 0;
    }
    public double[] createNewB(double[] b, double[][]A, int index, int bound) {
        double[] newb = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            newb[i] = b[i] - A[i][index]*bound;
        }
        return newb;
    }
    public double[][] createNewA(double[][] A, int index) {
        double[][] newA = new double[A.length][A[0].length-1];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (j < index) {
                    newA[i][j] = A[i][j];
                }else if (j > index) {
                    newA[i][j-1] = A[i][j];
                }
            }
        }
        return newA;
    }
    public double[] createNewC(double[] c, int index) {
        double[] newC = new double[c.length-1];
        for (int i = 0; i < c.length; i++) {
            if (i < index) {
                newC[i] = c[i];
            }else if (i > index) {
                newC[i-1] = c[i];
            }
        }
        return newC;
    }
    public double getOptimalValue() {
        return optimalValue;
    }
    public ArrayList<Double> getSolution() {
        return solution;
    }
}
