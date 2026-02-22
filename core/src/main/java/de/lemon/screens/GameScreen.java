package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.ParticlePresent;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.GameLogic;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;
import de.lemon.mechanics.particleSystem.sources.MovingParticleSource;
import de.lemon.mechanics.particleSystem.sources.StaticParticleSource;
import de.lemon.save.SaveManager;

import java.util.EnumSet;

public class GameScreen extends CoreScreen{
    private AnimatedSprite background;
    private AnimatedSprite door;


    @Override
    public void init() {
        Main._instance.gameLogic = new GameLogic(SaveManager.loadGameState(Main._instance.currentGameStateId));
        Inventory._instance = Main._instance.gameLogic.getGameState().getInventory();
        Main._instance.tick.addListener(Main._instance.gameLogic.getTickListener());
        setBackgroundColor(Color.DARK_GRAY);
        super.init();
    }

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.UI, ScreenFeatures.WORLD);
    }

    @Override
    protected void createComponents() {
        Main._instance.played = true;
    }

    @Override
    protected void createWorld() {
        background = new AnimatedSprite(Resources._instance.gameScreen_background, 512, 320, 0.1f, true, new Vector2(0, 0));
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));
        worldRenderer.addObject(background);

        door = new AnimatedSprite(Resources._instance.door, 72, 16, 0.1f, true, new Vector2()){
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.GARDEN_SCREEN);
            }
        };
        door.setClickable(true);
        addWorldObject(door, 4 / 10f, 2.6f/100f, 1/7.2f, 3/10f);

        StaticParticleSource test = new StaticParticleSource(Vector2.Zero.cpy(), worldRenderer.getParticleManager(), Resources._instance.getParticle(ParticlePresent.SMOKE));
//        test.relTargetPos.set(0.8f, 0.3f);
        addWorldObject(test, 0.5f, 0.5f, 0, 0);
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
    }
}
