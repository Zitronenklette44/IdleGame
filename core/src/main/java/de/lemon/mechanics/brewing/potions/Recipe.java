package de.lemon.mechanics.brewing.potions;

import de.lemon.core.GameState;
import de.lemon.core.Item;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

public class Recipe {

    ArrayList<Item> items = new ArrayList<>();
    int brewingTime;
    Item result;

    public Recipe(int brewTimeSeconds, Item result, Item... items){
        this.brewingTime = brewTimeSeconds;
        this.result = result;
        this.items.addAll(Arrays.asList(items));
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
