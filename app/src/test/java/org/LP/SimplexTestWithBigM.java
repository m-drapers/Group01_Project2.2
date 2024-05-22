package org.LP;

import org.LP.solver.BigM;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimplexTestWithBigM {
    @Test public void test() {
        double A[][] = {{0.3, 0.1, 1, 0, 0, 0},
                {0.5, 0.5, 0, 1, 0, 0},
                {0.5, 0.5, 0, 0, -1, 0},
                {0.6, 0.4, 0, 0, 0, -1}
        };
        double b[] = {2.7, 6, 6, 6};
        double C[] = {0.4, 0.5, 0, 0, 0, 0};
        int bigMConstraint[] = {3, 4};
        BigM simplexSolver = new BigM();
        assertEquals(5.4, simplexSolver.SimplexSolver(A, C, b, 2, bigMConstraint), 0.0001);
    }
}

