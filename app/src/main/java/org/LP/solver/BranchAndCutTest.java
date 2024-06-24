package org.LP.solver;

public class BranchAndCutTest {
    public static void main(String[] args) {
        double[][] A={
                {1,1},
                {-1,1},
                {6,2}
        };
        double[] b={5,0,21};
        double[] c={2,1};
        BranchAndCut branchAndCut = new BranchAndCut();
        branchAndCut.BranchAndCut(A,b,c);
    }
}
