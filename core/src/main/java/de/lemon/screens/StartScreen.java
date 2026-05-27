package de.lemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import de.lemon.logic.render.AnimationController;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.main.Main;
import de.lemon.ui.AnimatedButton;

import java.util.EnumSet;

public class StartScreen extends CoreScreen{

    @Override
    public EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD);
    }

    @Override
    protected void createComponents() {}

    @Override
    protected void createWorld() {
        setBackgroundColor(Color.GRAY);
        AnimatedSprite name = new AnimationController("gameName", new int[]{0, 1, 2, 3}, 256, 48, 0.11f, 10);
        addWorldObject(name, 0.5f, 0.9f, 0.5f, 0.1f);

        AnimatedButton bStart = new AnimatedButton("buttons", new int[]{3, 4, 5}, 96, 32);
        bStart.setAction(()-> Main._instance.switchScreen(Main.LOAD_SCREEN));
        addWorldObject(bStart, .5f, .61f, .2f, .1f);

        AnimatedButton bOptions = new AnimatedButton("buttons", new int[]{6, 7, 8}, 96, 32);
        bOptions.setAction(()-> Main._instance.switchScreen(Main.OPTIONS_SCREEN));
        addWorldObject(bOptions, .5f, .5f, .2f, .1f);

        AnimatedButton bQuit = new AnimatedButton("buttons", new int[]{9, 10, 11}, 96, 32);
        bQuit.setAction(()-> Gdx.app.exit());
        addWorldObject(bQuit, .5f, .39f, .2f, .1f);
    }

}
