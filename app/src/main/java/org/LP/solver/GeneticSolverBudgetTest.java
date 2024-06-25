package org.LP.solver;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticSolverBudgetTest {

    public static void main(String[] args) {

        double[][] students = new double[50][2];
        double[][] houses = new double[35][3];

        double meanBudget = 600;
        double stdDevBudget = 100;
        double meanMaxDistance = 10;
        double stdDevMaxDistance = 1;

        for (double[] student : students) {
            student[0] = meanBudget + stdDevBudget * ThreadLocalRandom.current().nextGaussian();
            student[1] = meanMaxDistance + stdDevMaxDistance * ThreadLocalRandom.current().nextGaussian();
        }

        for (double[] doubles : students) {
            System.out.println(doubles[0] + " --- " + doubles[1]);
        }

        double meanPrice = 800;
        double stdDevPrice = 50;
        double meanDistance = 3;
        double stdDevDistance = 0.5;

        for (double[] house : houses) {
            house[0] = meanPrice + stdDevPrice * ThreadLocalRandom.current().nextGaussian();
            house[1] = meanDistance + stdDevDistance * ThreadLocalRandom.current().nextGaussian();
            house[2] = 1 + (int) (Math.random() * 4);
        }

        for (double[] house : houses) {
            System.out.println(house[0] + " --- " + house[1] + " --- " + house[2]);
        }

        GeneticSolverBudget geneticSolverBudget = new GeneticSolverBudget();

        int testAmount = 100;
        double[] testResults = new double[testAmount + 1];

        for (int i = 0; i < testAmount; i++) {
            testResults[i] = geneticSolverBudget.GeneticSolverBudget(students, houses, 200, 10);
        }

        double sum = 0;
        for (double testResult : testResults) {
            sum += testResult;
        }

        testResults[testAmount] = sum / testAmount;
        System.out.println("budgetViolation array: " + Arrays.toString(testResults));
        System.out.println("Average violation: " + testResults[testAmount]);
    }
}
