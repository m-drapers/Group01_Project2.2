package org.LP.solver;

import java.util.Arrays;

public class LogicBasedSolver {

    private static double[][] students; // Sorted students matrix based on budget low to high, then on max distance low to high
    private static double[][] houses; // Sorted houses matrix based on price low to high, then distance low to high

    public double LogicBasedSolver(double[][] students, double[][] houses) {
        LogicBasedSolver.students = students;
        LogicBasedSolver.houses = houses;

        //Create a solution space
        int[] solution = new int[students.length];
        double[] costs = new double[students.length];
        int[] placesTaken = new int[houses.length];

        //fill the placesTaken array with zeros
        Arrays.fill(placesTaken, 0);

        int houseNumber = 0;

        //Create the solution
        for (int i = 0; i < students.length; i++) {
            boolean violationBudget = true;
            boolean violationDistance = true;

            if (students[i][0] >= houses[houseNumber][0]) {
                violationBudget = false;
            }

            //Since the array is sorted, if the budget is violated the student cannot be placed, since all the students who are already placed have at maximum this budget
            if (violationBudget) {
                System.out.println("Student " + i + " cannot be placed into a house.");
                System.out.println("There is no valid solution. (violation Budget)");
                return -1;
            }

            if (students[i][1] >= houses[houseNumber][1]) {
                violationDistance = false;
            }

            if (!violationDistance) {
                solution[i] = houseNumber;
                costs[i] = houses[houseNumber][0];
                placesTaken[houseNumber]++;
            }

            else {
                int newHouseNumber = houseNumber;

                //Check if the student can be placed in a house which is already full
                //Then we have to reallocate some other student(s)
                for (int j = 0; j < i; j++) {
                    if (students[j][0] >= houses[solution[houseNumber]][0] & students[j][1] >= houses[solution[j]][1] & violationDistance) {
                        newHouseNumber = solution[j];
                        solution[j] = houseNumber;
                        violationDistance = false;
                    }
                }

                //Check if the student can be placed in a more expensive house
                while (violationDistance) {
                    while (placesTaken[newHouseNumber] >= houses[newHouseNumber][2]) {
                        newHouseNumber++;
                        if (newHouseNumber >= houses.length) {
                            System.out.println("Student " + i + " cannot be placed into a house.");
                            System.out.println("There is no valid solution. (violation Distance)");
                            return -1;
                        }
                        if (students[i][1] >= houses[newHouseNumber][1]) {
                            violationDistance = false;
                        }
                        //Houses become more expensive after this one, so if the found one is not affordable all the upcoming wil also not be affordable
                        if (students[i][0] < houses[newHouseNumber][0]) {
                            System.out.println("Student " + i + " cannot be placed into a house.");
                            System.out.println("There is no valid solution. (violation Distance --> violation Budget)");
                            return -1;
                        }
                    }

                }

                solution[i] = newHouseNumber;
                costs[i] = houses[newHouseNumber][0];
                placesTaken[newHouseNumber]++;

            }
        }

        System.out.println("solution array: " + Arrays.toString(solution));
        System.out.println("placesTaken array: " + Arrays.toString(placesTaken));
        System.out.println("costs array: " + Arrays.toString(costs));

        // Calculate the sum of all elements of the costs matrix;
        double result = 0;
        for (double value : costs) {
            result += value;
        }

        System.out.println("result: " + result);
        return result;
    }

}
