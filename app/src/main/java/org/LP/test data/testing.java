import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.LP.generator.WyckData;

public class testing {


    static final WyckData amby = new WyckData("Amby", 400.0, 750.0, 6.2, 7.0);
    static final WyckData biesland = new WyckData("Biesland", 430.0, 700.0, 4.7, 5.5);
    static final WyckData binnenstad = new WyckData("Binnenstad", 430.0, 800.0, 3.9, 4.6);
    static final WyckData borgharen = new WyckData("Borgharen", 400.0, 750.0, 8.1, 9.0);
    static final WyckData boschpoort = new WyckData("Boschpoort", 400.0, 800.0, 6.1, 6.9);
    static final WyckData boschstraatkwartier = new WyckData("Boschstraatkwartier", 430.0, 800.0, 4.7, 5.7);
    static WyckData heer = new WyckData("Heer", 430.0, 850.0, 2.4, 2.8);
    static WyckData itteren = new WyckData("Itteren", 400.0, 700.0, 10.0, 10.7);
    static WyckData jekerkwartier = new WyckData("Jekerkwartier", 450.0, 850.0, 3.3, 4.1);
    static WyckData sintPieter = new WyckData("Sint Pieter", 400.0, 800.0, 5.5, 6.1);
    static WyckData villapark = new WyckData("Villapark", 450.0, 850, 3.7, 4.0);
    static WyckData wyck = new WyckData("Wyck", 400.0, 900.0, 3.5, 4.1);
    static WyckData[] wycks = {amby, biesland, binnenstad, borgharen, boschpoort,boschstraatkwartier, heer, itteren, jekerkwartier, sintPieter, villapark, wyck};

    // Generate random numbers within each range based on ratios
    static int total = 0;
    static final int numOfHouses = 20; 

    
    
    public static List<String> generateHouseData() {
        int totalGeneratedUnits = 0;
        int numberOfWycks = wycks.length;
        List<String> insertQueries = new ArrayList<>();
        
        for (int i = 0; i < numberOfWycks; i++) {
            WyckData current = wycks[i];
            int unitsToGenerate = numOfHouses / numberOfWycks;
            
            // For the last iteration, ensure we cover any remaining units
            if (i == numberOfWycks - 1) {
                unitsToGenerate = numOfHouses - totalGeneratedUnits;
            }
            
            int count = 0;
            while (count < unitsToGenerate) {
                double price = current.generateRandomPrice();
                double distance = current.generateRandomDistance();
                
                // Generate a unit number that does not exceed unitsToGenerate
                int units = current.generateNumberOfUnits();
                if (count + units > unitsToGenerate) {
                    units = unitsToGenerate - count;
                }
                
                String insertQuery = "INSERT INTO Houses (Price, Distance, Places) VALUES (" + price + "," + distance + "," + units + ")";
                insertQueries.add(insertQuery);
                count += units;
                totalGeneratedUnits += units;
                
                // Break if we have generated the required number of houses
                if (totalGeneratedUnits >= numOfHouses) {
                    break;
                }
            }
        }
        total = totalGeneratedUnits;
        return insertQueries;
    }
    
    // Testing the method
    public static void main(String[] args) {
        List<String> houseData = generateHouseData();
        System.out.println(total);

        for (String query : houseData) {
            System.out.println(query);
        }
    }
    
}
