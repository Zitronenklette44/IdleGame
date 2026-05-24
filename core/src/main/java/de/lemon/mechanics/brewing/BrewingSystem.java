package de.lemon.mechanics.brewing;

import de.lemon.core.Item;
import de.lemon.listeners.BrewingListener;
import de.lemon.listeners.TickListener;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;
import de.lemon.mechanics.brewing.potions.Recipe;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;

public class BrewingSystem {
    public static BrewingSystem _instance = new BrewingSystem();
    private final ArrayList<BrewingListener> listeners = new ArrayList<>();

    boolean currentlyBrewing = false;

    int brewTime = 0;
    private Recipe recipe;
    int currentBrewTime = 0;

    public BrewingSystem(){
        TickListener tickListener = new TickListener() {
            @Override
            protected void onSecond() {
                super.onSecond();
                update();
            }
        };
        Main._instance.tick.addListener(tickListener);
    }

    public void brewRecipe(Recipe recipe){
        if(!recipe.canBeBrewed() || currentlyBrewing) return;
        Inventory._instance.removeItems(recipe.getItems());
        startBrewing(recipe);
    }

    private void startBrewing(Recipe recipe){
        this.recipe = recipe;
        brewTime = recipe.getBrewingTime();
        currentlyBrewing = true;
        for(BrewingListener l : listeners) l.onStart();
    }

    public void update(){
        if(!currentlyBrewing) return;
        DebugLogger.printInfo("brewing: " + currentBrewTime + " / " + brewTime);
        currentBrewTime++;
        if(currentBrewTime >= brewTime) finishBrewing(true);
        for(BrewingListener l : listeners) l.onUpdate();
    }

    private void finishBrewing(boolean success){
        currentlyBrewing = false;
        currentBrewTime = 0;
        if(success){
            Inventory._instance.addItem(recipe.getResult());
            DebugLogger.printInfo("Finished with success added Item" + recipe.getResult());
        }
        for(BrewingListener l : listeners) l.onFinish(success);
    }

    public void cancelBrewing(){
        finishBrewing(false);
    }

    public boolean isCurrentlyBrewing() {
        return currentlyBrewing;
    }

    public float progress(){
        return (float) currentBrewTime / brewTime;
    }

    public Recipe findRecipe(Item[] items) {
        StringBuilder itemsText = new StringBuilder();
        for(Item item : items) itemsText.append(item.name);
        DebugLogger.printInfo("given Items: " + itemsText);
        ArrayList<Recipe> recipes = RecipeData.allRecipes;
        for (Recipe recipe : recipes) {
            if (recipe.getItems().size() != items.length) continue;

            ArrayList<Item> remainingItems = new ArrayList<>(recipe.getItems());
            boolean success = true;
            for (Item inputItem : items) {
                if (!remainingItems.remove(inputItem)) {
                    success = false;
                    break;
                }
            }
            if (success) return recipe;
        }
        return null;
    }

    public void removeListener(BrewingListener brewingListener) {
        listeners.remove(brewingListener);
    }

    public void addListener(BrewingListener brewingListener) {
        listeners.add(brewingListener);
    }
}
