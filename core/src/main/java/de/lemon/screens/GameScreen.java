package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.animation.Sprite;
import de.lemon.core.Resources;
import de.lemon.enums.ScreenFeatures;
import de.lemon.logic.GameLogic;
import de.lemon.main.Main;
import de.lemon.save.SaveManager;
import de.lemon.ui.Hitbox;

import java.util.EnumSet;

public class GameScreen extends CoreScreen{
    private Sprite background;
    private Hitbox door;


    public GameScreen(){
        Main._instance.gameLogic = new GameLogic(SaveManager.loadGameState(Main._instance.currentGameStateId));
        Main._instance.tick.addListener(Main._instance.gameLogic.getTickListener());
        setBackgroundColor(Color.DARK_GRAY);
    }

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.UI, ScreenFeatures.WORLD);
    }

    @Override
    protected void createComponents() {
        door = new Hitbox(){
            @Override
            public void onClick() {
//              System.out.println("door");
                Main._instance.setScreen(new GardenScreen());
            }
        };
        worldStage.addActor(door);


    }

    @Override
    protected void createWorld() {
        background = new Sprite(Resources._instance.gameScreen_background, 512, 320, 0.1f, true, new Vector2(0, 0));
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));
        worldRenderer.addObject(background);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));

        Vector2 size = background.getSize().cpy();
        door.autoresize(background, size.x / 4, 0, 1/7.2f, 1/20f);

    }
}
