package de.lemon.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import de.lemon.animation.SimpleSprite;
import de.lemon.animation.Sprite;
import de.lemon.core.Resources;
import de.lemon.enums.ScreenFeatures;
import de.lemon.enums.Upgrades;
import de.lemon.main.Main;
import de.lemon.mechanics.plants.Plant;
import de.lemon.mechanics.plants.PlantLogic;
import de.lemon.mechanics.plants.Plants;

import java.util.EnumSet;

public class GardenScreen extends CoreScreen{
    private Sprite background;
    private SimpleSprite pots;
    private PlantLogic[] plants = new PlantLogic[5];

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.UI, ScreenFeatures.WORLD);
    }

    @Override
    public void init() {
        createPlants();
        System.out.println("called");
        super.init();
    }

    @Override
    protected void createComponents() {
//        setBackgroundColor(Color.YELLOW);
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
    }

    @Override
    public void show() {
        super.show();
        pots.setFrame(Main._instance.gameLogic.getGameState().getLevelOf(Upgrades.GARDEN_POT_LEVEL));
    }
}
