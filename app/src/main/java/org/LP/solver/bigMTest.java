package org.LP.solver;

public class BigMTest {
    public static void main(String[] args) {
        double A[][] = { { 0.3,0.1,1,0,0,0 },
                { 0.5,0.5,0,1,0,0 },
                {0.5,0.5,0,0,-1,0},
                { 0.6,0.4,0,0,0,-1 }
        };
        double b[] = { 2.7,6,6,6};
        double C[] = { 0.4,0.5,0,0,0,0};
        int bigMConstraint[] ={3,4};
        BigM simplexSolver = new BigM();
        double result =simplexSolver.SimplexSolver(A, C, b, 2,bigMConstraint);
    }
}
