package org.LP.solver;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

public class BranchAndCut {
    static {
        Loader.loadNativeLibraries();
    }
    double[][] A1; // coefficient of a in <= constraints
    double[] b1; // coefficient of b in <= constraints
    double[] c1; // coefficient of c in <= constraints
    double[][] A2; // coefficient of a in >= constraints
    double[][] b2; // coefficient of b in >= constraints
    double[] c2; // coefficient of c in >= constraints

    public void BranchAndCut(double[][] A1, double[] b1, double[] c1) {
        this.A1 = A1;
        this.b1 = b1;
        this.c1 = c1;
        MPSolver solver = MPSolver.createSolver("CBC");

        // Create the variables.
        MPVariable[] x = new MPVariable[c1.length];
        for (int i = 0; i < c1.length; i++) {
            x[i] = solver.makeIntVar(0.0, MPSolver.infinity(), "x" + (i + 1));
        }

        // Create the objective function.
        MPObjective objective = solver.objective();
        for (int i = 0; i < c1.length; i++) {
            objective.setCoefficient(x[i], c1[i]);
        }
        objective.setMaximization();

        // Create the constraints.
        for (int i = 0; i < b1.length; i++) {
            MPConstraint constraint = solver.makeConstraint(-solver.infinity(), b1[i]);
            for (int j = 0; j < A1[i].length; j++) {
                constraint.setCoefficient(x[j], A1[i][j]);
            }
        }
        MPSolver.ResultStatus resultStatus = solver.solve();

        // Check that the problem has an optimal solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution:");
            System.out.println("Objective value = " + objective.value());
            for (int i = 0; i < c1.length; i++) {
                System.out.println("x" + (i + 1) + " = " + x[i].solutionValue());
            }
        }else {
            System.out.println("No feasible solution");
        }
    }
}
