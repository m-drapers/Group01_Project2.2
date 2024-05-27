package org.LP.solver;

public class LogicBasedSolverTest {

    public static void main(String[] args) {
        double[][] A = { { 300, 25 },
                { 325, 40 },
                {400, 7 },
                { 400, 35 },
                { 450, 5 },
                {450, 4 },
                { 450, 30 },
        };
        double[][] B = {{275, 10, 1},
                {300, 20, 2},
                {400, 5, 1},
                {400, 6, 3}
        };

        LogicBasedSolver logicBasedSolver = new LogicBasedSolver();
        double result = logicBasedSolver.LogicBasedSolver(A, B);
    }
}

