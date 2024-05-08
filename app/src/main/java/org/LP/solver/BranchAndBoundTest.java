package org.LP.solver;

public class BranchAndBoundTest {
    private static double[][] P = {{ 1,0,1,0,0 },
            { 0,2,0,1,0 },
            { 3,2,0,0,1 } };
    private static double[] q = { 4,12,18};
    private static double[] R = { 3,2,0,0,0};
    public static void main(String[] args) {
        BranchAndBound branchAndBound = new BranchAndBound();
        branchAndBound.BranchAndBound(P, R, q);
    }
}