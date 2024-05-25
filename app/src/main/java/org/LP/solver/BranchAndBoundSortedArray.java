package org.LP.solver;

import java.util.Arrays;

public class BranchAndBoundSortedArray {

    private static double[][] students; // Sorted students matrix based on budget low to high, then on max distance low to high
    private static double[][] houses; // Sorted houses matrix based on price low to high, then distance low to high

    public double BranchAndBoundSortedArray(double[][] students, double[][] houses) {
        BranchAndBoundSortedArray.students = students;
        BranchAndBoundSortedArray.houses = houses;

        //Create a solution space
        int[] solution = new int[students.length];
        double[] costs = new double [students.length];
        int[] placesTaken = new int[houses.length];

        //fill the placesTaken array with zeros
        Arrays.fill(placesTaken, 0);

        ;
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
                System.out.println("There is no valid solution.");
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

                //Check if the student can be placed in an already full house
                if (newHouseNumber > 0) {
                    while (violationDistance & (!violationBudget | newHouseNumber < 0)) {
                        newHouseNumber--;
 //Think about this in the morning
                    }
                }

                while (violationDistance) {
                    newHouseNumber++;
                    while (placesTaken[newHouseNumber] >= houses[newHouseNumber][2]) {
                        newHouseNumber++;
                    }
                    if (students[i][1] >= houses[newHouseNumber][1]) {
                        violationDistance = false;
                    }
                }
                solution[i] = newHouseNumber;
                costs[i] = houses[newHouseNumber][0];
                placesTaken[newHouseNumber]++;
            }

            if (placesTaken[houseNumber] >= houses[houseNumber][2]) {
                houseNumber++;
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
