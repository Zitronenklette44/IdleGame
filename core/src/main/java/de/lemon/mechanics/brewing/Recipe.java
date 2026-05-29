package de.lemon.mechanics.brewing;

import de.lemon.core.Item;
import de.lemon.mechanics.Inventory;

import java.util.ArrayList;

public class Recipe {

    ArrayList<Item> items = new ArrayList<>();
    int brewingTime;
    Item result;
    public float riskValue;

    public Recipe(int brewingTime, float riskValue, Item result, ArrayList<Item> items) {
        this.brewingTime = brewingTime;
        this.riskValue = Math.clamp(riskValue, 0, 1);
        this.result = result;
        this.items.addAll(items);
    }

    public boolean canBeBrewed(){
        for(Item i : items){
            if(!Inventory._instance.contains(i, i.quantity)){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public Item getResult() {
        return result;
    }
}
