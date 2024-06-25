package org.LP.solver;

public class SimplexSolverTest {
    private static double A[][] = { { 1,1,1,0,0 },
            { -1,1,0,1,0 },
            { 6,2,0,0,1 } };
    private static double b[] = { 5,0,21};
    private static double C[] = { 2,1,0,0,0};
    public static void main(String[] args) {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.SimplexSolver(A, C, b);
    }
}
