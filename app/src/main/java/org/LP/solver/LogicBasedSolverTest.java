package org.LP.solver;

public class LogicBasedSolverTest {

    public static void main(String[] args) {
        double[][] A = { { 300, 25 },
                { 325, 40 },
                {400, 6 },
                { 400, 35 }
        };
        double[][] B = {{275, 10, 1},
                {300, 20, 2},
                {400, 6, 1},
                {400, 30, 1}
        };

        LogicBasedSolver logicBasedSolver = new LogicBasedSolver();
        double result = logicBasedSolver.LogicBasedSolver(A, B);
    }
}

