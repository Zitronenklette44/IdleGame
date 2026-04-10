package de.lemon.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.lemon.logic.render.SimpleSprite;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.enums.Upgrades;
import de.lemon.main.Main;
import de.lemon.mechanics.plants.Plant;
import de.lemon.mechanics.plants.PlantLogic;
import de.lemon.logic.enums.Plants;

import java.util.EnumSet;

public class GardenScreen extends CoreScreen{
    private AnimatedSprite background;
    private SimpleSprite pots;
    private PlantLogic[] plants = new PlantLogic[5];
    private Cell<Table> cell;
    private AnimatedSprite door;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.UI, ScreenFeatures.WORLD);
    }

    @Override
    public void init() {
        createPlants();
        super.init();
    }

    @Override
    protected void createComponents() {
//        setBackgroundColor(Color.YELLOW);
        Table main = new Table();
        main.setFillParent(true);
        stage.addActor(main);

        Table tools = new Table();
        main.bottom();
        cell = main.add(tools).expandX().fillX().height(50);
        tools.setBackground(Resources._instance.getAsset("skin", Skin.class).newDrawable("white", Color.valueOf("A0522D")));
        tools.setDebug(true);

    }

    @Override
    protected void createWorld() {
        background = new AnimatedSprite("garden", 512, 256, 0.1f, false, new Vector2());
        worldRenderer.addObject(background);

        pots = new SimpleSprite("pots",512, 256, false, new Vector2()){
            @Override
            public void onKeyDown(int keycode) {
                if(keycode == Input.Keys.U){
                    Main._instance.gameLogic.getGameState().addUpgradeLevel(Upgrades.GARDEN_POT_LEVEL);
                    Main._instance.switchScreen(Main.GAME_SCREEN);
                }
                super.onKeyDown(keycode);
            }
        };
        worldRenderer.addObject(pots);

//        for(PlantLogic plant : plants) worldRenderer.addObject(plant);
        addWorldObject(plants[0], 1.2f/10f, 8.2f/10f, 1.4f/5f, 1.4f/5f);

        door = new AnimatedSprite("door", 1, 72, 16, 0.1f, true, new Vector2()){
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.GAME_SCREEN);
            }
        };
        door.setClickable(true);
        door.setRotation(AnimatedSprite.CW_90);
        addWorldObject(door, 3.4f/100f, 1.2f/3f, 1/7.2f, 3/10f);

    }

    private void createPlants() {
        plants[0] = new PlantLogic(Plant.getNewPlant(Plants.BLATTRUBIN));
        plants[0].setClickable(true);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));
        pots.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));

        cell.height(stage.getViewport().getScreenHeight() / 5f);
    }

    @Override
    public void show() {
        super.show();
        pots.setFrame(Main._instance.gameLogic.getGameState().getLevelOf(Upgrades.GARDEN_POT_LEVEL));
    }
}
