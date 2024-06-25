package org.LP.solver;

public class GeneticSolverBudgetTest {

    public static void main(String[] args) {
        double[][] A = { {258.59,26.98},
                {322.17,29.20},
                {341.54,26.62},
                {349.39,26.49},
                {352.85,28.84},
                {357.06,25.28},
                {362.47,29.77},
                {434.60,27.36},
                {448.46,28.72},
                {455.10,26.10},
                {475.28,27.82},
                {491.35,25.65},
                {493.21,29.73},
                {494.72,25.96},
                {501.13,26.59},
                {509.17,26.52},
                {512.32,27.85},
                {520.35,29.46},
                {557.98,18.59},
                {572.13,23.87},
                {575.71,26.83},
                {639.64,21.73 },
                {654.02,29.98},
                {663.59,26.66},
                {877.72,24.59}
        };
        double[][] B = {{576.78,14.89,3},
                {578.68,15.73,3},
                {597.30,16.56,3},
                {629.66,16.96,2},
                {658.71,17.71,3},
                {677.71,18.44,3},
                {708.21,18.44,3},
                {710.81,18.46,3},
                {743.67,18.99,2},
                {816.14,19.17,3},
                {839.95,19.63,2},
                {896.94,20.31,3},
                {934.52,20.64,3},
                {946.03,20.72,3}
        };

        double[][] C = { {258.59,16.98},
                {322.17,19.20},
                {341.54,16.62},
                {349.39,16.49},
                {352.85,18.84},
                {357.06,15.28},
                {362.47,19.77},
                {434.60,17.36},
                {448.46,18.72},
                {455.10,16.10},
                {475.28,17.82},
                {491.35,15.65},
                {493.21,19.73},
                {494.72,15.96},
                {501.13,16.59},
                {509.17,16.52},
                {512.32,17.85},
                {520.35,19.46},
                {557.98,18.59},
                {572.13,13.87},
                {575.71,16.83},
                {639.64,11.73 },
                {654.02,0.98},
                {663.59,0.66},
                {877.72,0.59}
        };
        double[][] D = {{176.78,14.89,3},
                {178.68,15.73,3},
                {197.30,12.56,3},
                {229.66,16.96,2},
                {258.71,13.71,3},
                {277.71,14.44,3},
                {308.21,16.44,3},
                {310.81,10.46,3},
                {343.67,13.99,2},
                {416.14,18.17,3},
                {439.95,16.63,2},
                {496.94,15.31,3},
                {534.52,13.64,3},
                {546.03,13.72,3}
        };

        GeneticSolverBudget geneticSolverBudget = new GeneticSolverBudget();
        double result = geneticSolverBudget.GeneticSolverBudget(A, B, 149, 30);
    }
}