package de.lemon.mechanics;

import de.lemon.mechanics.plants.Plants;

public class Inventory {
    public static Inventory _instance;

    public int BlattrubinAmount = 0;



    public Inventory(){
        _instance = this;
    }

    public void addPlant(Plants plants, int amount){
        switch (plants){
            case BLATTRUBIN:
                BlattrubinAmount += amount;
                break;
        }

    }
}
