package org.LP.solver;

public class LogicBasedSolverTest {

    public static void main(String[] args) {
        double[][] A = { { 300, 15 },
                { 300, 25 },
                {400, 6 },
                { 400, 35 }
        };
        double[][] B = {{275, 20, 2},
                {300, 10, 2},
                {400, 6, 1},
                {400, 30, 1}
        };

        LogicBasedSolver logicBasedSolver = new LogicBasedSolver();
        double result = logicBasedSolver.LogicBasedSolver(A, B);
    }
}

