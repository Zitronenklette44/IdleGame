package de.lemon.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.lemon.logic.animation.SimpleSprite;
import de.lemon.logic.animation.Sprite;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.enums.Upgrades;
import de.lemon.main.Main;
import de.lemon.mechanics.plants.Plant;
import de.lemon.mechanics.plants.PlantLogic;
import de.lemon.mechanics.plants.Plants;

import java.util.EnumSet;

public class GardenScreen extends CoreScreen{
    private Sprite background;
    private SimpleSprite pots;
    private PlantLogic[] plants = new PlantLogic[5];
    private Cell<Table> cell;
    private Sprite door;

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
        tools.setBackground(Resources._instance.skin.newDrawable("white", Color.valueOf("A0522D")));
        tools.setDebug(true);

    }

    @Override
    protected void createWorld() {
        background = new Sprite(Resources._instance.gardenScreen_background, 512, 256, 0.1f, false, new Vector2());
        worldRenderer.addObject(background);

        pots = new SimpleSprite(Resources._instance.gardenScreen_pots,512, 256, false, new Vector2()){
            @Override
            protected void onKeyDown(int keycode) {
                if(keycode == Input.Keys.U){
                    Main._instance.gameLogic.getGameState().addUpgradeLevel(Upgrades.GARDEN_POT_LEVEL);
                    Main._instance.switchScreen(Main.GAME_SCREEN);
                }
                super.onKeyDown(keycode);
            }
        };
        worldRenderer.addObject(pots);

//        for(PlantLogic plant : plants) worldRenderer.addObject(plant);
        worldRenderer.addObject(plants[0]);

        door = new Sprite(Resources._instance.door, 1, 72, 16, 0.1f, true, new Vector2()){
            @Override
            public void onClick() {
                Main._instance.switchScreen(Main.GAME_SCREEN);
            }
        };
        door.setClickable(true);
        door.setRotation(Sprite.CW_90);
        worldRenderer.addObject(door);

    }

    private void createPlants() {
        plants[0] = new PlantLogic(Plant.getNewPlant(Plants.BLATTRUBIN));

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));
        pots.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));

        plants[0].autoResize(1.2f/10f, 7.25f/10f, 2/5f, 2/5f, viewport);

        cell.height(stage.getViewport().getScreenHeight() / 5f);

        door.autoResize(3.4f/100f, 1.2f/3f, 1/7.2f, 3/10f, viewport);
    }

    @Override
    public void show() {
        super.show();
        pots.setFrame(Main._instance.gameLogic.getGameState().getLevelOf(Upgrades.GARDEN_POT_LEVEL));
    }
}
