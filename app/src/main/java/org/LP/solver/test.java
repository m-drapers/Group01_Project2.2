package org.LP.solver;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

public class test {
    static {
        Loader.loadNativeLibraries();
    }

    public static void main(String[] args) {
        MPSolver solver = MPSolver.createSolver("CBC");

        // Create the variables x and y.
        MPVariable x1 = solver.makeIntVar(0.0, MPSolver.infinity(), "x1");
        MPVariable x2 = solver.makeIntVar(0.0, MPSolver.infinity(), "x2");

        // Create the objective function, 2 * x + y.
        MPObjective objective = solver.objective();
        objective.setCoefficient(x1, 2);
        objective.setCoefficient(x2, 1);
        objective.setMaximization();

        MPConstraint constraint1 = solver.makeConstraint(-solver.infinity(), 5);
        constraint1.setCoefficient(x1, 1);
        constraint1.setCoefficient(x2, 1);

        MPConstraint constraint2 = solver.makeConstraint(-solver.infinity(), 0);
        constraint2.setCoefficient(x1, -1);
        constraint2.setCoefficient(x2, 1);

        MPConstraint constraint3 = solver.makeConstraint(-solver.infinity(), 21);
        constraint3.setCoefficient(x1, 6);
        constraint3.setCoefficient(x2, 2);

        // Solve the problem.
        MPSolver.ResultStatus resultStatus = solver.solve();

        // Check that the problem has an optimal solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution:");
            System.out.println("Objective value = " + objective.value());
            System.out.println("x1 = " + x1.solutionValue());
            System.out.println("x2 = " + x2.solutionValue());
        }
    }
}
