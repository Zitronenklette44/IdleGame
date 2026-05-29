package de.lemon.mechanics.brewing;

import com.badlogic.gdx.math.MathUtils;
import de.lemon.core.Item;
import de.lemon.listeners.BrewingListener;
import de.lemon.listeners.TickListener;
import de.lemon.logic.enums.Impact;
import de.lemon.logic.interfaces.Listenable;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;
import de.lemon.mechanics.brewing.events.BrewingEvent;
import de.lemon.mechanics.brewing.events.EventManager;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BrewingSystem implements Listenable<BrewingListener> {
    public static BrewingSystem _instance = new BrewingSystem();
    private final ArrayList<BrewingListener> listeners = new ArrayList<>();

    boolean currentlyBrewing = false;

    int brewTime = 0;
    private Recipe recipe;
    int currentBrewTime = 0;
    float currentRiskValue = 0;

    float baseEventChance = 0.3f;
    float currentEventChance = 0;

    float[] weights = new float[4];
    Impact[] impacts = new Impact[4];
    int currentNumberOfEvents = 0;
    int maxNumberOfEvents = 2;

    boolean testing = false;
    TickListener tickListener;

    private BrewingSystem(){
        tickListener = new TickListener() {
            @Override
            protected void onSecond() {
                super.onSecond();
                update();
            }
        };
        Main._instance.tick.addListener(tickListener);
        impacts[0] = Impact.LOW;
        impacts[1] = Impact.NORMAL;
        impacts[2] = Impact.HIGH;
        impacts[3] = Impact.CRITICAL;
        Arrays.fill(weights, 0);
    }

    public void brewRecipe(Recipe recipe){
        if(recipe == null || !recipe.canBeBrewed() || currentlyBrewing) return;
        Inventory._instance.removeItems(recipe.getItems());
        startBrewing(recipe);
    }

    private void startBrewing(Recipe recipe){
        this.recipe = recipe;
        brewTime = recipe.getBrewingTime();
        currentlyBrewing = true;
        currentRiskValue = recipe.riskValue;

        // chance visualisation https://www.geogebra.org/calculator/yfswnnwb

        currentEventChance = baseEventChance * currentRiskValue;

        float p = currentRiskValue;
        weights[0] = (float) Math.pow((1 - p), 3);
        weights[1] = (float) Math.exp(-Math.pow((p - 0.5), 2) / 0.04);
        weights[2] = p < 0.1 ? 0 : (float) (1.2345679012f * Math.pow((p - .1), 2));
        weights[3] = (float) Math.max(0, Math.pow((p -.3), 3));

        currentNumberOfEvents = 0;

        DebugLogger.printInfo("event Chance: " + currentEventChance);

        for(BrewingListener l : listeners) l.onStart();
    }

    public void update(){
        if(!currentlyBrewing) return;
//        DebugLogger.printInfo("brewing: " + currentBrewTime + " / " + brewTime);
//        DebugLogger.printInfo("CurrentEventNumber: " + currentNumberOfEvents);
        if(currentNumberOfEvents < maxNumberOfEvents && MathUtils.random() < currentEventChance){
            DebugLogger.printInfo("event triggert");
            Impact impact = rollImpact();
            DebugLogger.printInfo("event: " + impact.name());
            BrewingEvent event = EventManager._instance.getRandomEvent(impact);
            DebugLogger.printInfo(event.toString());
            currentNumberOfEvents++;
        }
        currentBrewTime++;
        if(currentBrewTime >= brewTime) finishBrewing(true);
        for(BrewingListener l : listeners) l.onUpdate();
    }

    private void finishBrewing(boolean success){
        currentlyBrewing = false;
        currentBrewTime = 0;
        if(success){
            Inventory._instance.addItem(recipe.getResult());
//            DebugLogger.printInfo("Finished with success added Item" + recipe.getResult());
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
        ArrayList<Recipe> recipes = RecipeData.getAll();
        for (Recipe recipe : recipes) {
            if (recipe.getItems().size() != items.length) continue;

            ArrayList<Item> remainingItems = new ArrayList<>(recipe.getItems());
            boolean success = true;
            for (Item inputItem : items) {
                boolean found = false;
                for (Item rItem : remainingItems) {
                    if (rItem.id == inputItem.id) {
                        remainingItems.remove(rItem);
                        found = true;
                        break;
                    }
                }
                if(!found){
                    success = false;
                    break;
                }
            }
            if (success) return recipe;
        }
        return null;
    }

    @Override
    public void removeListener(BrewingListener brewingListener) {
        listeners.remove(brewingListener);
    }

    @Override
    public void addListener(BrewingListener brewingListener) {
        listeners.add(brewingListener);
    }

    private Impact rollImpact() {
        float sum = weights[0] + weights[1] + weights[2] + weights[3];
        if(sum <= 0){
            DebugLogger.printError("eventRoll was <= 0 with risk: " + currentRiskValue);
            DebugLogger.printArray("weights: ", weights);
            return Impact.LOW;
        }

        float roll = MathUtils.random();
        float cumulative = 0;

        cumulative += weights[0] / sum;
        if (roll < cumulative) return Impact.LOW;

        cumulative += weights[1] / sum;
        if (roll < cumulative) return Impact.NORMAL;

        cumulative += weights[2] / sum;
        if (roll < cumulative) return Impact.HIGH;

        return Impact.CRITICAL;
    }

//    public void BrewEventTest(Recipe recipe){
//        testing = true;
//        DebugLogger.printInfo("started Brewing Test");
//        Main._instance.tick.removeListener(tickListener);
//        DebugLogger.printInfo("removed TickListener");
//        ArrayList<BrewingListener> brewListeners = new ArrayList<>(listeners);
//        DebugLogger.printInfo("saved brewingListeners");
//        listeners.clear();
//        tickListener = new TickListener(){
//            @Override
//            public void onTick(float delta) {
//                super.onTick(delta);
//                update();
//            }
//        };
//        Main._instance.tick.addListener(tickListener);
//        DebugLogger.printInfo("created new TickListener");
//        float[] weights = new float[4];
//        float p = recipe.riskValue;
//        weights[0] = (float) Math.pow((1 - p), 3);
//        weights[1] = (float) Math.exp(-Math.pow((p - 0.5), 2) / 0.04);
//        weights[2] = p < 0.1 ? 0 : (float) (1.2345679012f * Math.pow((p - .1), 2));
//        weights[3] = (float) Math.max(0, Math.pow((p -.3), 3));
//        DebugLogger.printArray("weights: ", weights);
//        final int[] recipeBrewing = {1000};
//        BrewingListener brewingListener = new BrewingListener(){
//            @Override
//            public void onFinish(boolean success) {
//                super.onFinish(success);
//                DebugLogger.printInfo("finished run " + (1000 - recipeBrewing[0]) + " out of " + 1000);
//                recipeBrewing[0]--;
//                if(recipeBrewing[0] > 0) startBrewing(recipe);
//                else{
//                    DebugLogger.printInfo("test finished");
//                    listeners.clear();
//                    Main._instance.tick.removeListener(tickListener);
//                    DebugLogger.printInfo("removed Test listener");
//                    _instance = new BrewingSystem();
//                    DebugLogger.printInfo("created new instance");
//                    listeners.addAll(brewListeners);
//                    DebugLogger.printInfo("restored Listener");
//                }
//            }
//        };
//        startBrewing(recipe);
//    }
}
