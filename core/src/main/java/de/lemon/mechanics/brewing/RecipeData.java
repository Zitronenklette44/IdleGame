package de.lemon.mechanics.brewing;

import de.lemon.core.Resources;
import de.lemon.mechanics.brewing.potions.Recipe;

import java.util.ArrayList;

public class RecipeData {

    public static final ArrayList<Recipe> allRecipes = new ArrayList<>();;

    public static Recipe purpurWater = new Recipe(5, Resources._instance.getItem("purpurWater"), Resources._instance.getItem("grapes"));
    public static Recipe healingPotion = new Recipe(15, Resources._instance.getItem("healingPotion"), Resources._instance.getItem("purpurWater"), Resources._instance.getItem("blattRubin"));

    static {
        allRecipes.add(purpurWater);
        allRecipes.add(healingPotion);
    }
}
