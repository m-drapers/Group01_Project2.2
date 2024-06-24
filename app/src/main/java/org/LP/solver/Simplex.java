package org.LP.solver;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

import java.util.ArrayList;

public class Simplex {
    static {
        Loader.loadNativeLibraries();
    }
    double[][] A;
    double[] b;
    double[] c;

    ArrayList<Double> solution=new ArrayList<>();
    double objectiveValue;
    public void Simplex(double[][] A, double[] b, double[] c) {
        this.A = A;
        this.b = b;
        this.c = c;
        MPSolver solver = MPSolver.createSolver("SCIP");


        // Create the variables.
        MPVariable[] x = new MPVariable[c.length];
        for (int i = 0; i < c.length; i++) {
            x[i] = solver.makeIntVar(0.0, MPSolver.infinity(), "x" + (i + 1));
        }

        // Create the objective function.
        MPObjective objective = solver.objective();
        for (int i = 0; i < c.length; i++) {
            objective.setCoefficient(x[i], c[i]);
        }
        objective.setMaximization();

        // Create the constraints.
        for (int i = 0; i < b.length; i++) {
            MPConstraint constraint = solver.makeConstraint(-solver.infinity(), b[i]);
            for (int j = 0; j < A[i].length; j++) {
                constraint.setCoefficient(x[j], A[i][j]);
            }
        }

        MPSolver.ResultStatus resultStatus = solver.solve();


        // Check that the problem has an optimal solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            this.objectiveValue = objective.value();
            for (int i = 0; i < c.length; i++) {
                this.solution.add(x[i].solutionValue());
            }
        }else {
            this.solution = null;
            objectiveValue = -1;
        }
    }
    public  ArrayList<Double> getSolution() {
        return solution;
    }
    public double getObjectiveValue() {
        return objectiveValue;
    }
}