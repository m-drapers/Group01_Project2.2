package org.LP.generator;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;

public class StudentData {
    private int numOfStudnet;
    

    public StudentData( int numOfStudnet ) {
        this.numOfStudnet = numOfStudnet;
    }

    public int getNumOfStudnet(){
        return this.numOfStudnet;
    }

    public void setNumOfStudnet(int numOfStudnet){
        this.numOfStudnet = numOfStudnet;
    }


    // Method to generate random preferences based on given percentages
    public double generateRandomPreferencesPrice() {
        Random random = new Random();
        double pricePercentage = random.nextDouble() * 100;
        double maxPrice = 0;

        if (pricePercentage < 2.1) {
            maxPrice = 300;
        } else if (pricePercentage < 53.2) {
            maxPrice = 500;
        } else if (pricePercentage < 93.5) {
            maxPrice = 700;
        } else{
            maxPrice = 900;}
        return maxPrice;
    }
    // Method to generate random preferences based on given percentages
    public double generateRandomPreferencesDistance() {
        Random random = new Random();
        double distancePercentage = random.nextDouble() * 100;
        double maxDistance = 0;

        if (distancePercentage < 74.5) {
            maxDistance = 10;
        } else if (distancePercentage < 97.9) {
            maxDistance = 20;
        } else if (distancePercentage < 100) {
            maxDistance = 30;
        } else {
            maxDistance = Double.MAX_VALUE;
        }

        return maxDistance;
    }
}

