package de.lemon.mechanics;

import de.lemon.core.Item;
import de.lemon.logic.enums.Plants;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
     public static Inventory _instance;

    public int BlattrubinAmount = 0;
    public int KarmintraubenAmount = 0;

    HashMap<String, Item> items = new HashMap<>();

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

    public boolean contains(Item item, int quantity) {
        Item i = items.get(item.name);
        if(i == null) return false;
        if(quantity == -1) return true;
        return i.quantity >= quantity;
    }

    public boolean contains(Item item){
        return contains(item, -1);
    }

    public void removeItems(ArrayList<Item> items) {
        for(Item i : items){
            Item item = this.items.get(i.name);
            if(item == null) {
                DebugLogger.printError("Item "+ i.name + "not found in inventory");
                continue;
            }
            item.quantity -= i.quantity;
            if (item.quantity <= 0) {
                if (item.quantity < 0) {
                    DebugLogger.printError(
                        "Item count of " + i.name + " got negative"
                    );
                }
                this.items.remove(item.name);
            }
        }
    }

    public void addItem(Item result) {
        Item item = items.get(result.name);
        if(item == null){
            items.put(result.name, result.cpy());
            DebugLogger.printInfo("item null");
        }
        else{
            DebugLogger.printInfo("Item found current quantity: " + item.quantity + " adding: " +result.quantity);
            item.quantity += result.quantity;
            DebugLogger.printInfo("Item found new quantity: " + item.quantity);
        }
//        System.out.println("ADD INSTANCE: " + System.identityHashCode(result));
//        System.out.println("MAP INSTANCE: " + System.identityHashCode(items.get(result.name)));
//        System.out.println("INVENTORY INSTANCE: " + System.identityHashCode(this));
//        Thread.dumpStack();
    }
}
