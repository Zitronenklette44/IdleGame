package de.lemon.mechanics;

import de.lemon.core.Item;
import de.lemon.logic.enums.Plants;
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
        if(item == null) items.put(result.name, result.cpy());
        else item.quantity += result.quantity;
    }

    public ArrayList<Item> getAllItems(){
        return items.values().stream().map(Item::cpy).collect(Collectors.toCollection(ArrayList::new));
    }
}
