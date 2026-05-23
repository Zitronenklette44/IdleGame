package de.lemon.mechanics.brewing;

import de.lemon.core.Resources;
import de.lemon.mechanics.brewing.potions.Recipe;

public class RecipeData {

    public static Recipe purpurWater = new Recipe(5, Resources._instance.getItem("purpurWater"), Resources._instance.getItem("KarminTrauben"));
    public static Recipe healingPotion = new Recipe(15, Resources._instance.getItem("healingPotion"), Resources._instance.getItem("purpurWater"), Resources._instance.getItem("KarminTrauben"));

}
