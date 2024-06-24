package org.LP;

import org.junit.Test;
import static org.junit.Assert.*;

import org.LP.generator.LPProblemPreparer;

public class LPProblemPreparerTest {

    @Test
    public void testAddSlackSurplusArtificial() {
        double[][] A = {
            {1, 2},
            {3, 4}
        };
        double[] b = {5, 6};
        String[] constraintTypes = {"<=", ">="};

        double[][] newA = LPProblemPreparer.addSlackSurplusArtificial(A, b, constraintTypes);

        double[][] expectedA = {
            {1, 2, 1, 0, 0},
            {3, 4, 0, -1, 1}
        };

        assertArrayEquals(expectedA, newA);
    }
}
