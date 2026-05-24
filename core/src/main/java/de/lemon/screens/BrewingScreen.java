package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.core.Item;
import de.lemon.core.Resources;
import de.lemon.listeners.BrewingListener;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.render.*;
import de.lemon.main.Main;
import de.lemon.mechanics.brewing.BrewingSystem;
import de.lemon.mechanics.brewing.potions.Recipe;
import de.lemon.ui.InventorySelect;
import de.lemon.ui.STextButton;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;
import java.util.EnumSet;

public class BrewingScreen extends CoreScreen{

    Item input1;
    Item input2;
    Item input3;
    Item input4;
    Item output1;

    SimpleSprite brewingArrow;
    InventorySelect inv;
    int selectingSlot = -1;

    BrewingListener brewingListener = new BrewingListener(){
        @Override
        public void onFinish(boolean success) {
            super.onFinish(success);
            output1.set(Resources._instance.getItem("empty"));
        }
    };

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD);
    }

    @Override
    protected void createComponents() {}

    @Override
    protected void createWorld() {
         input1 = Resources._instance.getItem("empty");
         input2 = Resources._instance.getItem("empty");
         input3 = Resources._instance.getItem("empty");
         input4 = Resources._instance.getItem("empty");
         this.output1 = Resources._instance.getItem("empty");
        setBackgroundColor(Color.FOREST);

        AnimationController cauldron = new AnimationController("cauldron",new int[]{0,1,2},80, 112, 0.1f, 8);
        addWorldObject(cauldron, 0.25f, 0.25f, 1f, 1.5f);

        ColoredSprite overlay = new ColoredSprite(new Color(0, 0, 0, .7f));
        addWorldObject(overlay, .5f, .5f, 3, 3);

        NineSprite recipeBackground = new NineSprite("recipeBackground", 16, 16, 16, 16, new Vector2());
        addWorldObject(recipeBackground, 0.7f, .5f, .5f, .95f);

        SimpleSprite slot1 = new SimpleSprite("recipeSlot", 64, 64, true){
            @Override
            public void onClick(int button) {
                super.onClick(button);
                selectingSlot = 0;
                inv.setVisible(true);
                inv.refreshItems(viewport);
            }
        };;
        slot1.setClickable(true);
        addWorldObject(slot1, .34f, .8f, .1f, .1f);
        addWorldObject(input1, .34f, .8f, .1f, .1f);

        SimpleSprite slot2 = new SimpleSprite("recipeSlot", 64, 64, true){
            @Override
            public void onClick(int button) {
                super.onClick(button);
                selectingSlot = 1;
                inv.setVisible(true);
                inv.refreshItems(viewport);
            }
        };
        slot2.setClickable(true);
        addWorldObject(slot2, .26f, .85f, .1f, .1f);
        addWorldObject(input2, .26f, .85f, .1f, .1f);

        SimpleSprite slot3 = new SimpleSprite("recipeSlot", 64, 64, true){
            @Override
            public void onClick(int button) {
                super.onClick(button);
                selectingSlot = 2;
                inv.setVisible(true);
                inv.refreshItems(viewport);
            }
        };
        slot3.setClickable(true);;
        addWorldObject(slot3, .18f, .85f, .1f, .1f);
        addWorldObject(input3, .18f, .85f, .1f, .1f);

        SimpleSprite slot4 = new SimpleSprite("recipeSlot", 64, 64, true){
            @Override
            public void onClick(int button) {
                super.onClick(button);
                selectingSlot = 3;
                inv.setVisible(true);
                inv.refreshItems(viewport);
            }
        };
        slot4.setClickable(true);;
        addWorldObject(slot4, .1f, .8f, .1f, .1f);
        addWorldObject(input4, .1f, .8f, .1f, .1f);

        SimpleSprite outputSlot = new SimpleSprite("recipeSlot", 64, 64, true);
        addWorldObject(outputSlot, .22f, .3f, .1f, .1f);
        addWorldObject(output1, .22f, .3f, .1f, .1f);

        brewingArrow = new SimpleSprite("brewingArrow", 32, 32, true){
            @Override
            public void onClick(int button) {
                super.onClick(button);
                Recipe r = BrewingSystem._instance.findRecipe(getItems());
                BrewingSystem._instance.brewRecipe(r);
                output1.set(r.getResult());
                clearItems();
                layout.resize(viewport);
            }
        };
        brewingArrow.setClickable(true);
        addWorldObject(brewingArrow, .22f, .6f, .1f, .15f);

        STextButton returnButton = new STextButton("Zurück", Resources._instance.UI_Button, new Vector2(), new Vector2()){
            @Override
            public void onClick(int button) {
                super.onClick(button);
                Main._instance.switchScreen(Main.GAME_SCREEN);
            }
        };
        addWorldObject(returnButton, .05f, .95f, .1f, .09f);


        inv = new InventorySelect(this::selectedItem);
        inv.setVisible(false);
        addWorldObject(inv, .5f, .5f, .8f, .8f);

    }

    private void selectedItem(){
        inv.setVisible(false);
        if(selectingSlot == 0){
            input1.set(inv.selected);
        }
        if(selectingSlot == 1){
            input2.set(inv.selected);
        }
        if(selectingSlot == 2){
            input3.set(inv.selected);
        }
        if(selectingSlot == 3){
            input4.set(inv.selected);
        }
        layout.resize(viewport);
    }

    private void clearItems(){
        input1.set(Resources._instance.getItem("empty"));
        input2.set(Resources._instance.getItem("empty"));
        input3.set(Resources._instance.getItem("empty"));
        input4.set(Resources._instance.getItem("empty"));
    }

    private Item[] getItems(){
        ArrayList<Item> items = new ArrayList<>();
        if(!input1.name.equals("empty")) items.add(input1);
        if(!input2.name.equals("empty")) items.add(input2);
        if(!input3.name.equals("empty")) items.add(input3);
        if(!input4.name.equals("empty")) items.add(input4);
        return items.toArray(new Item[0]);
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
//        BrewingSystem._instance.brewRecipe(RecipeData.purpurWater);
        DebugLogger.printInfo("Search for Recipe resulted in " + BrewingSystem._instance.findRecipe(new Item[] {Resources._instance.getItem("purpurWater"), Resources._instance.getItem("blattRubin")}));
    }
}
