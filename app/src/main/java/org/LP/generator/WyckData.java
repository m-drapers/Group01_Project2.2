package org.LP.generator;


    import java.sql.*;

    public class WyckData {
        private String name;
        private double minPrice;
        private double maxPrice;
        private double minDista;
        private double maxDista;
    
        public WyckData(String name, double minPrice, double maxPrice, double minDista, double maxDista) {
            this.name = name;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.minDista = minDista;
            this.maxDista = maxDista;
        }
    
        // Getters and Setters
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public double getMinPrice() {
            return minPrice;
        }
    
        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }
    
        public double getMaxPrice() {
            return maxPrice;
        }
    
        public void setMaxPrice(double maxPrice) {
            this.maxPrice = maxPrice;
        }
    
        public double getMinDista() {
            return minDista;
        }
    
        public void setMinDista(double minDista) {
            this.minDista = minDista;
        }
    
        public double getMaxDista() {
            return maxDista;
        }
    
        public void setMaxDista(double maxDista) {
            this.maxDista = maxDista;
        }
    
        // Method to generate random data within the specified ranges
        public double generateRandomPrice() {
            double randomPrice = minPrice + (maxPrice - minPrice) * Math.random();
            return Math.round(randomPrice * 100.0) / 100.0;
        }
        
        public double generateRandomDistance() {
            double randomDistance = minDista + (maxDista - minDista) * Math.random();
            return Math.round(randomDistance * 100.0) / 100.0;
        }
        

        public int generateNumberOfUnits(){
            return (int) (1 + Math.random() * 5); // Assuming a house can have 1 to 5 places
        }
    

        @Override
        public String toString() {
            return "Wyck{" +
                    "name='" + name + '\'' +
                    ", minPrice=" + minPrice +
                    ", maxPrice=" + maxPrice +
                    ", minDista=" + minDista +
                    ", maxDista=" + maxDista +
                    '}';
        }
    }
    

