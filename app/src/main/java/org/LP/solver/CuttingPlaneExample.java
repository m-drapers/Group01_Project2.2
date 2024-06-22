package org.LP.solver;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

public class CuttingPlaneExample {
    static {
        Loader.loadNativeLibraries();
    }

    public static void main(String[] args) {
        MPSolver solver = MPSolver.createSolver("GLOP");

        // Create the variables x and y.
        MPVariable x1 = solver.makeIntVar(0.0, MPSolver.infinity(), "x1");
        MPVariable x2 = solver.makeIntVar(0.0, MPSolver.infinity(), "x2");


        // Create the objective function, 2 * x + y.
        MPObjective objective = solver.objective();
        objective.setCoefficient(x1, 2);
        objective.setCoefficient(x2, 1);
        objective.setMaximization();

        // Create a linear constraint, x + y <= 2.
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
            System.out.println("Coefficient of x1 in constraint1 = " + constraint1.getCoefficient(x1));
            System.out.println("Coefficient of x2 in constraint1 = " + constraint1.getCoefficient(x2));
            double slackConstraint1 = constraint1.ub() - (x1.solutionValue() * constraint1.getCoefficient(x1) + x2.solutionValue() * constraint1.getCoefficient(x2));
            double slackConstraint2 = constraint2.ub() - (x1.solutionValue() * constraint2.getCoefficient(x1) + x2.solutionValue() * constraint2.getCoefficient(x2));
            double slackConstraint3 = constraint3.ub() - (x1.solutionValue() * constraint3.getCoefficient(x1) + x2.solutionValue() * constraint3.getCoefficient(x2));
            System.out.println("Slack of constraint1 = " + slackConstraint1);
            System.out.println("Slack of constraint2 = " + slackConstraint2);
            System.out.println("Slack of constraint3 = " + slackConstraint3);

            // Add a cutting plane, x + y >= 1.
            MPConstraint cuttingPlane = solver.makeConstraint(Math.ceil(x1.solutionValue() + x2.solutionValue()), solver.infinity());
            cuttingPlane.setCoefficient(x1, 1);
            cuttingPlane.setCoefficient(x2, 1);

            // Solve the problem again.
            resultStatus = solver.solve();

            // Check that the problem has an optimal solution.
            if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
                System.out.println("Solution after adding cutting plane:");
                System.out.println("Objective value = " + objective.value());
                System.out.println("x = " + x1.solutionValue());
                System.out.println("y = " + x2.solutionValue());
            } else {
                System.err.println("The problem does not have an optimal solution.");
            }
        } else {
            System.err.println("The problem does not have an optimal solution.");
        }
    }
}