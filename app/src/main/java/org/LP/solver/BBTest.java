package org.LP.solver;

public class BBTest {
    public static void main(String[] args) {
        double[][] A={
                {1,1},
                {-1,1},
                {6,2}
        };
        double[] b={5,0,21};
        double[] c={2,1};
        BB branchAndBound = new BB(A,b,c,null,null);
        branchAndBound.solve();
        System.out.println("Optimal value = "+branchAndBound.getOptimalValue());
        System.out.println("Solution:");
        for (int i = 0; i < branchAndBound.getSolution().size(); i++) {
            System.out.println("x" + (i + 1) + " = " + branchAndBound.getSolution().get(i));
        }
    }
}