package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.core.Item;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.render.*;
import de.lemon.mechanics.brewing.BrewingSystem;
import de.lemon.mechanics.brewing.potions.Recipe;

import java.util.EnumSet;

public class BrewingScreen extends CoreScreen{

    SimpleSprite brewingArrow;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD);
    }

    @Override
    protected void createComponents() {}

    @Override
    protected void createWorld() {
        setBackgroundColor(Color.FOREST);

        AnimationController cauldron = new AnimationController("cauldron",new int[]{0,1,2},80, 112, 0.1f, 8);
        addWorldObject(cauldron, 0.25f, 0.25f, 1f, 1.5f);

        ColoredSprite overlay = new ColoredSprite(new Color(0, 0, 0, .7f));
        addWorldObject(overlay, .5f, .5f, 3, 3);

        NineSprite recipeBackground = new NineSprite("recipeBackground", 16, 16, 16, 16, new Vector2());
        addWorldObject(recipeBackground, 0.7f, .5f, .5f, .95f);

        SimpleSprite slot1 = new SimpleSprite("recipeSlot", 64, 64, true);
        addWorldObject(slot1, .34f, .8f, .1f, .1f);

        SimpleSprite slot2 = new SimpleSprite("recipeSlot", 64, 64, true);
        addWorldObject(slot2, .26f, .85f, .1f, .1f);

        SimpleSprite slot3 = new SimpleSprite("recipeSlot", 64, 64, true);
        addWorldObject(slot3, .18f, .85f, .1f, .1f);

        SimpleSprite slot4 = new SimpleSprite("recipeSlot", 64, 64, true);
        addWorldObject(slot4, .1f, .8f, .1f, .1f);

        SimpleSprite output1 = new SimpleSprite("recipeSlot", 64, 64, true);
        addWorldObject(output1, .22f, .3f, .1f, .1f);

        brewingArrow = new SimpleSprite("brewingArrow", 32, 32, true);
        addWorldObject(brewingArrow, .22f, .6f, .1f, .15f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(BrewingSystem._instance.isCurrentlyBrewing()){
            int currentFrame = (int) (brewingArrow.getMaxFrames() * BrewingSystem._instance.progress());
            brewingArrow.setFrame(currentFrame);
        }else {
            brewingArrow.setFrame(0);
        }
    }

    @Override
    public void show() {
        super.show();
        BrewingSystem._instance.brewRecipe(new Recipe(10, new Item("Potion", 1, 32, 32), new Item("Potion", 0, 32, 32)));
    }
}
