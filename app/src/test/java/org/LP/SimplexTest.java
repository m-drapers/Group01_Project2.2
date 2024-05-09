package org.LP;

import org.LP.solver.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTest {
    @Test public void test() {
        double A[][] = { { 1,0,1,0,0 },
                { 0,2,0,1,0 },
                { 3,2,0,0,1 } };
        double b[] = { 4,12,18};
        double C[] = { 3,2,0,0,0};
        SimplexSolver simplexSolver = new SimplexSolver();
        double result=simplexSolver.SimplexSolver(A, C, b);
    }
}
