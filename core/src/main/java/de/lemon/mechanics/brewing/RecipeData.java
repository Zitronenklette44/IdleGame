package de.lemon.mechanics.brewing;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import de.lemon.core.Item;
import de.lemon.core.Resources;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeData {

    private static HashMap<String, Recipe> recipes = new HashMap<>();

    public static void init(FileHandle file){
        JsonValue json = new JsonReader().parse(file.readString());
        JsonValue origin = json.get("recipes");
        for(JsonValue recipe = origin.child; recipe != null; recipe = recipe.next){
            String recipeName = recipe.name;
            int brewingTime = recipe.getInt("brewingTime");
            float riskValue = recipe.getFloat("riskValue");
            Item result = Resources._instance.getItem(recipe.getString("resultName"));
            JsonValue ingredients = recipe.get("ingredients");
            ArrayList<Item> items = new ArrayList<>();
            for(JsonValue item = ingredients.child; item != null; item = item.next){
                items.add(Resources._instance.getItem(item.asString()));
            }
            recipes.put(recipeName, new Recipe(brewingTime, riskValue, result, items));
        }
    }

    public static Recipe get(String name){
        Recipe r = recipes.get(name);
        if(r == null) DebugLogger.printError("unknown Recipe name: "+ name, new IllegalArgumentException());
        return r;
    }

    public static ArrayList<Recipe> getAll() {
        return new ArrayList<>(recipes.values());
    }
}
