package de.lemon.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.GameLogic;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.logic.render.AnimationController;
import de.lemon.logic.render.Sprite;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;
import de.lemon.save.SaveManager;
import de.lemon.utilities.DebugLogger;

import java.util.EnumSet;

public class GameScreen extends CoreScreen{
    private AnimatedSprite background;

    @Override
    public void init() {
        super.init();
        DebugLogger.printInfo(" loaded id: " + Main._instance.currentGameStateId);
        Main._instance.gameLogic = new GameLogic(SaveManager.loadGameState(Main._instance.currentGameStateId));
        if(Main._instance.gameLogic.getGameState().getInventory() == null) Main._instance.gameLogic.getGameState().setInventory(new Inventory());
        Inventory._instance = Main._instance.gameLogic.getGameState().getInventory();
        Main._instance.tick.addListener(Main._instance.gameLogic.getTickListener());
        setBackgroundColor(Color.DARK_GRAY);
    }

    @Override
    public EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.UI, ScreenFeatures.WORLD, ScreenFeatures.DIALOG);
    }

    @Override
    protected void createComponents() {
        Main._instance.played = true;
    }

    @Override
    protected void createWorld() {
        background = new AnimatedSprite("gameScreen", 512, 320, 0.1f, true);
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));
        worldRenderer.addObject(background);

        AnimatedSprite door = new AnimatedSprite("door", 72, 16, 0.1f, true) {
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.GARDEN_SCREEN);
            }
        };
        door.setClickable(true);
        addWorldObject(door, 4 / 10f, 2.6f/100f, 1/7.2f, 3/10f);

        AnimatedSprite doorShowroom = new AnimatedSprite("door", 1, 72, 16, 0.1f, true) {
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.SHOWROOM_SCREEN);
            }
        };
        doorShowroom.setClickable(true);
        doorShowroom.setRotation(Sprite.CW_90);
        addWorldObject(doorShowroom, .035f, .15f, 1/7.2f, 3/10f);

        AnimationController cauldron = new AnimationController("cauldron", new int[] {0,1,2}, 80, 112, 0.1f, 5){
            @Override
            public void onClick(int button) {
                super.onClick(button);
                if(button == Input.Buttons.LEFT){
                    Main._instance.switchScreen(Main.BREWING_SCREEN);
                }
            }
        };
        cauldron.setClickable(true);
        addWorldObject(cauldron, 0.1f, 0.7f, 0.3f, 0.3f);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));
    }
}
