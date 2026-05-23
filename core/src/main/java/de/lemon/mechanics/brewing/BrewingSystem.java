package de.lemon.mechanics.brewing;

import de.lemon.listeners.TickListener;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;
import de.lemon.mechanics.brewing.potions.Recipe;
import de.lemon.utilities.DebugLogger;

public class BrewingSystem {
    public static BrewingSystem _instance = new BrewingSystem();

    boolean currentlyBrewing = false;

    private TickListener tickListener;

    int brewTime = 0;
    private Recipe recipe;
    int currentBrewTime = 0;

    public BrewingSystem(){
        tickListener = new TickListener(){
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
    }

    public void update(){
        if(!currentlyBrewing) return;
        DebugLogger.printInfo("brewing: " + currentBrewTime + " / " + brewTime);
        currentBrewTime++;
        if(currentBrewTime >= brewTime) finishBrewing(true);
    }

    private void finishBrewing(boolean success){
        currentlyBrewing = false;
        currentBrewTime = 0;
        if(success){
            Inventory._instance.addItem(recipe.getResult());
            DebugLogger.printInfo("Finished with success added Item" + recipe.getResult());
        }
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
}
