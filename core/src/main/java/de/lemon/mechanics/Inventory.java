package de.lemon.mechanics;

import de.lemon.logic.enums.Plants;

public class Inventory {
     public static Inventory _instance;

    public int BlattrubinAmount = 0;
    public int KarmintraubenAmount = 0;

    public Inventory(){}

    public void addPlant(Plants plants, int amount){
        switch (plants){
            case BLATTRUBIN:
                BlattrubinAmount += amount;
                break;
            case KARMINTRAUBEN:
                KarmintraubenAmount += amount;
                break;
        }

    }
}
