package org.LP.solver;

public class SimplexSolverTest {
    private static double[][] A = { { -1, 2, -2, 1, 0, 0 },
            { 2, -2, 2, 0, 1, 0 },
            { -3, 3, 2, 0, 0, 1 } };
    private static double[] b = { 40, 30 ,25};
    private static double[] C = { 4, 3, 3, 0, 0, 0 };
    public static void main(String[] args) {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.SimplexSolver(A, C, b);
    }
}
