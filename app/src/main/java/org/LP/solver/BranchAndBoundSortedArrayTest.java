package org.LP.solver;

public class BranchAndBoundSortedArrayTest {

    public static void main(String[] args) {
        double[][] A = { { 300, 25 },
                { 300, 15 },
                {400, 30 },
                { 400, 5 }
        };
        double[][] B = {{275, 20, 2},
                {300, 10, 2},
                {400, 30, 1},
                {400, 5, 1}
        };

        BranchAndBoundSortedArray branchAndBoundSortedArray = new BranchAndBoundSortedArray();
        double result = branchAndBoundSortedArray.BranchAndBoundSortedArray(A, B);
    }
}

