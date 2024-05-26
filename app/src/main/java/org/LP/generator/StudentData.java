package org.LP.generator;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentData {
    private int numOfStudnet;

    private  int[] priceRangesLower;
    private  int[] priceRangesHigher;
    private   double[] priceRatios;

    private  int[] maxDisRangesLower = {5, 10, 20, 30, 40};
    private  int[] maxDisRangesHigher = {10, 20, 30, 40, 50};
    private double[] maxDisRatios = {74.5, 23.4, 2.1, 0, 0};
    

    public StudentData(int numOfStudnet, int[] priceRangesHigher, int[] priceRangesLower, double[] priceRatios, int[] maxDisRangesHigher, int[] maxDisRangesLower, double[] maxDisRatios ) {
        this.numOfStudnet = numOfStudnet;

        this.priceRangesHigher = priceRangesHigher;
        this.priceRangesLower = priceRangesLower;
        this.priceRatios = priceRatios;

        this.maxDisRangesHigher = maxDisRangesHigher;
        this.maxDisRangesLower = maxDisRangesLower;
        this.maxDisRatios = maxDisRatios;
    }

    public int getNumOfStudnet(){
        return this.numOfStudnet;
    }

    public void setNumOfStudnet(int numOfStudnet){
        this.numOfStudnet = numOfStudnet;
    }


    // Method to generate random preferences based on given percentages
    public List<Double> generateRandomPreferencesPrice() {
        return generateData(numOfStudnet, priceRangesLower, priceRangesHigher, priceRatios);

    }
    // Method to generate random preferences based on given percentages
    public List<Double> generateRandomPreferencesDistance() {
      return generateData(numOfStudnet, maxDisRangesLower, maxDisRangesHigher, maxDisRatios);
    }

    private static List<Double> generateData(int numSamples, int[] lowerRange, int[] upperRange, double[] ratios) {
        Random random = new Random();
        List<Double> generatedData = new ArrayList<>();
        int totalSamplesAllocated = 0;

        // Calculate the number of samples for each range
        int[] rangeSamples = new int[lowerRange.length];
        for (int i = 0; i < lowerRange.length; i++) {
            double rangeRatio = ratios[i] / 100.0; // Convert percentage to ratio
            rangeSamples[i] = (int) Math.round(numSamples * rangeRatio);
            totalSamplesAllocated += rangeSamples[i];
        }

        // Adjust the last range to ensure the total number of samples is exactly numSamples
        int difference = numSamples - totalSamplesAllocated;
        if (difference != 0) {
            rangeSamples[rangeSamples.length - 1] += difference;
        }

        // Generate random numbers for each range
        for (int i = 0; i < lowerRange.length; i++) {
            for (int j = 0; j < rangeSamples[i]; j++) {
                double lowerBound = lowerRange[i];
                double upperBound = upperRange[i];

                double randomNum = lowerBound + (upperBound - lowerBound) * random.nextDouble();
                randomNum = Math.round(randomNum * 100.0) / 100.0;
                generatedData.add(randomNum);
            }
        }
        return generatedData;
    }
}

