package de.lemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.render.AnimationController;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.main.Main;
import de.lemon.ui.STextButton;

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

        STextButton startButton = new STextButton("Start Game", Resources._instance.UI_Button) {
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.LOAD_SCREEN);
            }
        };
        addWorldObject(startButton, 0.5f, 0.61f, 0.2f, 0.1f);

        STextButton optionsButton = new STextButton("Options", Resources._instance.UI_Button){
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.OPTIONS_SCREEN);
            }
        };
        addWorldObject(optionsButton, 0.5f, 0.5f, 0.2f, 0.1f);

        STextButton quitButton = new STextButton("Quit Game", Resources._instance.UI_Button){
            @Override
            public void onClick(int button) {
                Gdx.app.exit();
            }
        };
        addWorldObject(quitButton, 0.5f, 0.39f, 0.2f, 0.1f);
    }

}
