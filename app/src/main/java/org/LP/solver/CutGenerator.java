package org.LP.solver;

import java.util.ArrayList;
import java.util.List;

public class CutGenerator {

    private static final double EPSILON = 1e-9;

    public static List<String> generateGomoryCuts(double[][] A, double[] b) {
        List<String> cuts = new ArrayList<>();

        int numRows = A.length;
        int numCols = A[0].length;

        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            double rhs = b[rowIndex];
            double fractionalRHS = rhs - Math.floor(rhs);

            // Only generate a cut if the RHS is fractional
            if (fractionalRHS > EPSILON && fractionalRHS < 1 - EPSILON) {
                StringBuilder cut = new StringBuilder();
                boolean hasFractionalCoeff = false;
                for (int colIndex = 0; colIndex < numCols; colIndex++) {
                    double coeff = A[rowIndex][colIndex];
                    double fractionalCoeff = coeff - Math.floor(coeff);
                    if (fractionalCoeff > EPSILON && fractionalCoeff < 1 - EPSILON) {
                        cut.append(String.format("%.2f x%d + ", fractionalCoeff, colIndex + 1));
                        hasFractionalCoeff = true;
                    }
                }
                // Only add the cut if there are fractional coefficients
                if (hasFractionalCoeff) {
                    // Remove the last " + " and add the right-hand side
                    if (cut.length() > 3) {
                        cut.setLength(cut.length() - 3);
                    }
                    cut.append(" >= ").append(String.format("%.2f", fractionalRHS));
                    cuts.add(cut.toString());
                }
            }
        }

        return cuts;
    }

    public static void main(String[] args) {
            double[][] A = {
                {1.0, 2.5, 3.5}, // Coefficients of the constraints
                {0.0, 1.5, 2.3},
                {0.0, 0.0, 1.0}
            };
            double[] b = {
                4.0, // Right-hand side values
                3.2,
                1.5
            };
            
        List<String> cuts = generateGomoryCuts(A, b);
        for (String cut : cuts) {
            System.out.println(cut);
        }
    }
}


