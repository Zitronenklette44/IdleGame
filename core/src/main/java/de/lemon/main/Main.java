package de.lemon.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import de.lemon.core.Resources;
import de.lemon.enums.ScreenMode;
import de.lemon.logic.GameLogic;
import de.lemon.logic.Tick;
import de.lemon.save.SaveManager;
import de.lemon.screens.SplashScreen;

import java.util.Date;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static Main _instance;

    public ScreenMode screenMode = ScreenMode.WINDOWED;

    public Tick tick;
    public GameLogic gameLogic;
    public int currentGameStateId;


    public Main(){
        _instance = this;
        tick = new Tick();
    }

    @Override
    public void create() {
        new Resources();
        SaveManager.init();
        setWindowsMode();
        setScreen(new SplashScreen());
    }

    void setWindowsMode() {
        if (screenMode == ScreenMode.WINDOWED) {
            Gdx.graphics.setUndecorated(false);
            Gdx.graphics.setWindowedMode(640, 480);
        }

        if (screenMode == ScreenMode.FULLSCREEN) {
            Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(mode);
        }

        if (screenMode == ScreenMode.BORDERLESS) {
            Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setWindowedMode(mode.width, mode.height);
        }
    }

    @Override
    public void render() {
        super.render();
        float delta = Gdx.graphics.getDeltaTime();
        tick.update(delta);

    }

    @Override
    public void dispose() {
        if(gameLogic != null) {
            gameLogic.getGameState().setLastPlayed(System.currentTimeMillis());
            SaveManager.saveGameState(gameLogic.getGameState(), currentGameStateId);
        }
        super.dispose();
    }
}
