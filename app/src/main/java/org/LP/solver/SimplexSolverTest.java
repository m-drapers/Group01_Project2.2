package org.LP.solver;

public class SimplexSolverTest {
    private static double A[][] = { { 1,0,1,0,0 },
            { 0,2,0,1,0 },
            { 3,2,0,0,1 } };
    private static double b[] = { 4,12,18};
    private static double C[] = { 3,2,0,0,0};
    public static void main(String[] args) {
        simplexSolverCP simplexSolver = new simplexSolverCP();
        System.out.println(simplexSolver.solver(A, C, b));
    }
}
