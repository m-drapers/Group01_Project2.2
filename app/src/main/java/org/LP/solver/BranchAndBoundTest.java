package org.LP.solver;

public class BranchAndBoundTest {
    static double[][] P = { { 0.3,0.1,1,0,0,0 },
            { 0.5,0.5,0,1,0,0 },
            {0.5,0.5,0,0,-1,0},
            { 0.6,0.4,0,0,0,-1 }
    };
    static double[] q = { 2.7,6,6,6};
    static double[] R = { 0.4,0.5,0,0,0,0};
    static int[] bigMConstraint ={3,4};
    public static void main(String[] args) {
        BranchAndBound branchAndBound = new BranchAndBound();
        branchAndBound.BranchAndBound(P, R, q, 2, bigMConstraint);
    }
}
