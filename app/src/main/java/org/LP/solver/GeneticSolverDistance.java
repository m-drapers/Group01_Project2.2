package org.LP.solver;

import java.util.*;

public class GeneticSolverDistance {

    private static double[][] students; //Sorted students matrix based on budget low to high, then on max distance low to high
    private static double[][] houses; //Houses matrix
    private static int firstGeneration; //firstGeneration must be an even number
    private static int numberOfGenerations; //The number of generations we want to generate

    public double GeneticSolverDistance(double[][] students, double[][] houses, int firstGeneration, int numberOfGenerations) {
        GeneticSolverDistance.students = students;
        GeneticSolverDistance.houses = houses;
        GeneticSolverDistance.firstGeneration = firstGeneration;
        GeneticSolverDistance.numberOfGenerations = numberOfGenerations;

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
        double[][] distance = new double[firstGeneration][students.length + 1];
        double[][] placesTaken = new double[firstGeneration][houses.length+1];
        int[] houseNumber = new int[houses.length];

        //Fill the placesTaken array with zeros
        for (int i = 0; i < placesTaken[0].length; i++) {
            Arrays.fill(placesTaken[i], 0);
        }

        // Generate a list of the house-numbers
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < houses.length; i++) {
            numbers.add(i);
        }

        //Create the first generation of random solutions
        //These solutions are budget valid and will place every student in an affordable house
        //These solutions are spaces valid and will place no more students in a house then there are available rooms
        //These solutions do not take into account the distance violations and the distance is used as the score function

        for (int i = 0; i < firstGeneration; i++) {

            // Shuffle the list of house-numbers to randomize the order
            Collections.shuffle(numbers);

            // Assign the random house-numbers to an array
            for (int j = 0; j < houseNumber.length; j++) {
                houseNumber[j] = numbers.get(j);
            }

            for (int j = 0; j < students.length; j++) {
                boolean violationBudget = true;

                //Check if a student can afford the house
                int houseNumberCount = 0;
                while (violationBudget & (houseNumberCount < houseNumber.length)) {
                    if (students[j][0] >= houses[houseNumber[houseNumberCount]][0]) {
                        if (placesTaken[i][houseNumber[houseNumberCount]] < houses[houseNumber[houseNumberCount]][2]) {
                            violationBudget = false;

                            //If a available house if found for the student we add this as a solution to the solution arrays
                            solution[i][j] = houseNumber[houseNumberCount];
                            costs[i][j] = houses[houseNumber[houseNumberCount]][0];
                            double distanceViolation = 0;
                            if (houses[houseNumber[houseNumberCount]][1] > students[j][1]) {
                                distanceViolation = houses[houseNumber[houseNumberCount]][1] - students[j][1];
                            }
                            distance[i][j] = distanceViolation;
                            placesTaken[i][houseNumber[houseNumberCount]]++;
                        }
                    }
                    houseNumberCount++;
                }

                //If we find a student who cannot be placed into a house the attempt will be abandoned.
                //We restart the attempt to allocate the students.
                if (violationBudget) {
                    i--;

                    //Check if there is a possible solution without distance violations
                    if (i < 0) {
                        System.out.println("Student " + j + " cannot be placed into a house.");
                        System.out.println("There is no valid solution. (violation Budget)");
                        return -1;
                    }
                }
                else {
                    //If an available house if found for every student we add the total distance for this solution to the distance array
                    double distanceSum = 0;
                    for (int k = 0; k < students.length; k++) {
                        distanceSum = distanceSum + distance[i][k];
                    }
                    distance[i][students.length] = distanceSum;
                    solution[i][students.length] = distanceSum;
                    costs[i][students.length] = distanceSum;
                    placesTaken[i][houses.length] = distanceSum;
                }
            }
        }

        //Finally sort the solution, costs, distance and placesTaken array based on the best solution with the lowest distance violations
        Arrays.sort(distance, Comparator.comparingDouble(row -> row[students.length]));
        Arrays.sort(solution, Comparator.comparingDouble(row -> row[students.length]));
        Arrays.sort(costs, Comparator.comparingDouble(row -> row[students.length]));
        Arrays.sort(placesTaken, Comparator.comparingDouble(row -> row[houses.length]));

        //Now the first generation is known the strongest half of the solutions will be kept
        //Strongest means in this case the solutions with the smallest distance violations
        //After that we replace the discarded solutions with new solutions

        //For this, randomly swap a student to another house from a single solution
        //Either this house is not full, so check the budgetViolation and replace if possible
        //Or the new house is full, and replace with another random house

        //This process will be repeated until numberOfGenerations is reached

        //In this method the bottom half of the arrays (the weakest solutions) will be overwritten
        for (int i = 2; i <= numberOfGenerations; i++) {
            for (int j = firstGeneration / 2; j < firstGeneration; j++) {

                //Determine which solution we want to alternate
                int swapSolution = (int) (Math.random() * (firstGeneration / 2));

                //Override the previous weak solution
                System.arraycopy(distance[swapSolution], 0, distance[j], 0, distance[0].length);
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

                        //Check is the student can afford the new assigned house
                        if (students[swapStudent][0] >= houses[swapHouse][0]) {
                            placesTaken[j][swapHouse]++;
                            placesTaken[j][(int) solution[j][swapStudent]]--; //House were the student comes from
                            if (houses[swapHouse][1] > students[swapStudent][1]) {
                                distance[j][swapStudent] = houses[swapHouse][1] - students[swapStudent][1];
                            }
                            else {
                                distance[j][swapStudent] = 0;
                            }
                            solution[j][swapStudent] = swapHouse;
                            costs[j][swapStudent] = students[swapStudent][0];
                            swapCompleted = true;
                        }
                    }

                }

                //If a swap was made for the solution add the total distance for the solution to the distance array
                double distanceSum = 0;
                for (int k = 0; k < students.length; k++) {
                    distanceSum = distanceSum + distance[j][k];
                }

                distance[j][students.length] = distanceSum;
                solution[j][students.length] = distanceSum;
                costs[j][students.length] = distanceSum;
                placesTaken[j][houses.length] = distanceSum;
            }

            //Finally sort the solution, costs, distance and placesTaken array based on the best solution with the lowest distance violations
            Arrays.sort(distance, Comparator.comparingDouble(row -> row[students.length]));
            Arrays.sort(solution, Comparator.comparingDouble(row -> row[students.length]));
            Arrays.sort(costs, Comparator.comparingDouble(row -> row[students.length]));
            Arrays.sort(placesTaken, Comparator.comparingDouble(row -> row[houses.length]));

            //When numberOfGenerations is reached, return the best found solution
            System.out.println("i: " + i);
            System.out.println("solution array: " + Arrays.toString(solution[0]));
            System.out.println("placesTaken array: " + Arrays.toString(placesTaken[0]));
            System.out.println("costs array: " + Arrays.toString(costs[0]));
            System.out.println("distance array: " + Arrays.toString(distance[0]));

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
        System.out.println("distance array: " + Arrays.toString(distance[0]));

        // Calculate the sum of all elements of the costs matrix;
        double result = 0;
        for (double value : costs[0]) {
            result += value;
        }

        System.out.println("result: " + result);
        return result;
    }

}