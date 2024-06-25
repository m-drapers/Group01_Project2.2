package org.LP.solver;

import java.util.*;

public class GeneticSolverBudget {

    private static double[][] students; //Students matrix
    private static double[][] houses; //Houses matrix
    private static int firstGeneration; //firstGeneration must be an even number
    private static int numberOfGenerations; //The number of generations we want to generate

    public double GeneticSolverBudget(double[][] students, double[][] houses, int firstGeneration, int numberOfGenerations) {
        GeneticSolverBudget.students = students;
        GeneticSolverBudget.houses = houses;
        GeneticSolverBudget.firstGeneration = firstGeneration;
        GeneticSolverBudget.numberOfGenerations = numberOfGenerations;

        //Check if firstGeneration is even and update it if it is an odd number
        if (firstGeneration % 2 != 0) {
            firstGeneration++;
            System.out.println("firstGeneration must be an even number and has been updated to " + firstGeneration + ".");
        }

        //Check if there are more available room than the number of students to allocate
        int availableHouses = 0;

        for (double[] house : houses) {
            availableHouses += (int) house[2];
        }

        if (availableHouses < students.length) {
            System.out.println("There are more students than available houses.");
            System.out.println("There is no valid solution.");
            return -1;
        }

        if (availableHouses == students.length) {
            System.out.println("There are an equal amount of students as available houses.");
            System.out.println("This algorithm cannot find the solution.");
            return -1;
        }

        //Create a solution space
        double[][] solution = new double[firstGeneration][students.length+1];
        double[][] costs = new double[firstGeneration][students.length+1];
        double[][] exceededBudget = new double[firstGeneration][students.length + 1];
        double[][] placesTaken = new double[firstGeneration][houses.length+1];
        int[] houseNumber = new int[houses.length];

        // Generate a list of the house-numbers
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < houses.length; i++) {
            numbers.add(i);
        }

        //Create the first generation of random solutions
        //These solutions are distance valid and will place every student in a house within their maximum distance
        //These solutions are spaces valid and will place no more students in a house then there are available rooms
        //These solutions do not take into account the budget violations and the budget is used as the score function

        for (int i = 0; i < firstGeneration; i++) {

            // Shuffle the list of house-numbers to randomize the order
            Collections.shuffle(numbers);

            // Assign the random house-numbers to an array
            for (int j = 0; j < houseNumber.length; j++) {
                houseNumber[j] = numbers.get(j);
            }

            for (int j = 0; j < students.length; j++) {
                boolean violationDistance = true;

                //Check if the house is close enough to the University for the student
                int houseNumberCount = 0;
                while (violationDistance & (houseNumberCount < houseNumber.length)) {
                    if (students[j][1] >= houses[houseNumber[houseNumberCount]][1]) {
                        if (placesTaken[i][houseNumber[houseNumberCount]] < houses[houseNumber[houseNumberCount]][2]) {
                            violationDistance = false;

                            //If a available house if found for the student we add this as a solution to the solution arrays
                            solution[i][j] = houseNumber[houseNumberCount];
                            costs[i][j] = houses[houseNumber[houseNumberCount]][0];
                            double budgetViolation = 0;
                            if (houses[houseNumber[houseNumberCount]][0] > students[j][0]) {
                                budgetViolation = houses[houseNumber[houseNumberCount]][0] - students[j][0];
                            }
                            exceededBudget[i][j] = budgetViolation;
                            placesTaken[i][houseNumber[houseNumberCount]]++;
                        }
                    }
                    houseNumberCount++;
                }

                //If we find a student who cannot be placed into a house the attempt will be abandoned.
                //We restart the attempt to allocate the students.
                if (violationDistance) {
                    i--;

                    //Check if there is a possible solution without distance violations
                    if (i < 0) {
                        System.out.println("Student " + j + " cannot be placed into a house.");
                        System.out.println("There is no valid solution. (violation Distance)");
                        return -1;
                    }

                }
                else {
                    //If an available house if found for every student we add the total exceeded budget for this solution to the exceededBudget array
                    double budgetSum = 0;
                    for (int k = 0; k < students.length; k++) {
                        budgetSum = budgetSum + exceededBudget[i][k];
                    }
                    exceededBudget[i][students.length] = budgetSum;
                    solution[i][students.length] = budgetSum;
                    costs[i][students.length] = budgetSum;
                    placesTaken[i][houses.length] = budgetSum;
                }
            }
        }

        //Finally sort the solution, costs, exceededBudget and placesTaken array based on the best solution with the lowest budget violations
        Arrays.sort(exceededBudget, Comparator.comparingDouble(row -> row[students.length]));
        Arrays.sort(solution, Comparator.comparingDouble(row -> row[students.length]));
        Arrays.sort(costs, Comparator.comparingDouble(row -> row[students.length]));
        Arrays.sort(placesTaken, Comparator.comparingDouble(row -> row[houses.length]));

        //Now the first generation is known the strongest half of the solutions will be kept
        //Strongest means in this case the solutions with the smallest budget violations
        //After that we replace the discarded solutions with new solutions

        //For this, randomly swap a student to another house from a single solution
        //Either this house is not full, so check the distanceViolation and replace if possible
        //Or the new house is full, and replace with another random house

        //This process will be repeated until numberOfGenerations is reached

        //In this method the bottom half of the arrays (the weakest solutions) will be overwritten
        for (int i = 2; i <= numberOfGenerations; i++) {
            for (int j = firstGeneration / 2; j < firstGeneration; j++) {

                //Determine which solution we want to alternate
                int swapSolution = (int) (Math.random() * (firstGeneration / 2));

                //Override the previous weak solution
                System.arraycopy(exceededBudget[swapSolution], 0, exceededBudget[j], 0, exceededBudget[0].length);
                System.arraycopy(solution[swapSolution], 0, solution[j], 0, solution[0].length);
                System.arraycopy(costs[swapSolution], 0, costs[j], 0, costs[0].length);
                System.arraycopy(placesTaken[swapSolution], 0, placesTaken[j], 0, placesTaken[0].length);

                //Make the swap
                boolean swapCompleted = false;

                while (!swapCompleted) {
                    //Determine which student to swap with which house
                    int swapHouse = (int) (Math.random() * houses.length);
                    int swapStudent = (int) (Math.random() * students.length);

                    //Check if the chosen house if full
                    if (placesTaken[j][swapHouse] < houses[swapHouse][2]) {

                        //Check is the student is within its maximum biking distance from the new assigned house to the University
                        if (students[swapStudent][1] >= houses[swapHouse][1]) {
                            placesTaken[j][swapHouse]++;
                            placesTaken[j][(int) solution[j][swapStudent]]--; //House were the student comes from
                            if (houses[swapHouse][0] > students[swapStudent][0]) {
                                exceededBudget[j][swapStudent] = houses[swapHouse][0] - students[swapStudent][0];
                            }
                            else {
                                exceededBudget[j][swapStudent] = 0;
                            }
                            solution[j][swapStudent] = swapHouse;
                            costs[j][swapStudent] = students[swapStudent][1];
                            swapCompleted = true;
                        }
                    }

                }

                //If a swap was made for the solution add the total budget for the solution to the exceededBudget array
                double butgetSum = 0;
                for (int k = 0; k < students.length; k++) {
                    butgetSum = butgetSum + exceededBudget[j][k];
                }

                exceededBudget[j][students.length] = butgetSum;
                solution[j][students.length] = butgetSum;
                costs[j][students.length] = butgetSum;
                placesTaken[j][houses.length] = butgetSum;
            }

            //Finally sort the solution, costs, exceededBudget and placesTaken array based on the best solution with the lowest budget violations
            Arrays.sort(exceededBudget, Comparator.comparingDouble(row -> row[students.length]));
            Arrays.sort(solution, Comparator.comparingDouble(row -> row[students.length]));
            Arrays.sort(costs, Comparator.comparingDouble(row -> row[students.length]));
            Arrays.sort(placesTaken, Comparator.comparingDouble(row -> row[houses.length]));

            //When numberOfGenerations is reached, return the best found solution
            System.out.println("i: " + i);
            System.out.println("solution array: " + Arrays.toString(solution[0]));
            System.out.println("placesTaken array: " + Arrays.toString(placesTaken[0]));
            System.out.println("costs array: " + Arrays.toString(costs[0]));
            System.out.println("exceededBudget array: " + Arrays.toString(exceededBudget[0]));

            // Calculate the sum of all elements of the costs matrix;
            double result = 0;
            for (double value : costs[0]) {
                result += value;
            }

            System.out.println("result: " + result);
        }

        //When numberOfGenerations is reached, return the best found solution
        System.out.println("solution array: " + Arrays.toString(solution[0]));
        System.out.println("placesTaken array: " + Arrays.toString(placesTaken[0]));
        System.out.println("costs array: " + Arrays.toString(costs[0]));
        System.out.println("exceededBudget array: " + Arrays.toString(exceededBudget[0]));

        // Calculate the sum of all elements of the costs matrix;
        double result = 0;
        for (double value : costs[0]) {
            result += value;
        }

        System.out.println("result: " + result);

        return exceededBudget[0][students.length];
        //return result;
    }

}
