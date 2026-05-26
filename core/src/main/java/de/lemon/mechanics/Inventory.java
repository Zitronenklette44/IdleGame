package de.lemon.mechanics;

import de.lemon.core.Item;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Inventory {
     public static Inventory _instance;

    HashMap<String, Item> items = new HashMap<>();

    public Inventory(){}

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
        for(Item i : items) removeItem(i);
    }

    public void addItem(Item result) {
        Item item = items.get(result.name);
        if(item == null) items.put(result.name, result.cpy());
        else item.quantity += result.quantity;
    }

    public ArrayList<Item> getAllItems(){
        return items.values().stream().map(Item::cpy).collect(Collectors.toCollection(ArrayList::new));
    }

    public void removeItem(Item item) {
        Item i = this.items.get(item.name);
        if(i == null) {
            DebugLogger.printError("Item "+ item.name + "not found in inventory");
            return;
        }
        i.quantity -= item.quantity;
        if (i.quantity <= 0) {
            if (i.quantity < 0) {
                DebugLogger.printError(
                    "Item count of " + item.name + " got negative"
                );
            }
            this.items.remove(i.name);
        }
    }
}
