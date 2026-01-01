package de.lemon.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import de.lemon.core.Resources;
import de.lemon.enums.ScreenMode;
import de.lemon.logic.GameLogic;
import de.lemon.logic.Tick;
import de.lemon.save.SaveManager;
import de.lemon.screens.*;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static final int SPLASH_SCREEN = 0;
    public static final int START_SCREEN = 1;
    public static final int LOAD_SCREEN = 2;
    public static final int GAME_SCREEN = 3;
    public static final int GARDEN_SCREEN = 4;

    public final ArrayList<CoreScreen> screens = new ArrayList<>();

    public static Main _instance;

    public ScreenMode screenMode = ScreenMode.WINDOWED;

    public Tick tick;
    public GameLogic gameLogic;
    public int currentGameStateId;
    public boolean played = false;


    public Main(){
        _instance = this;
        tick = new Tick();
        screens.add(new SplashScreen());
        screens.add(new StartScreen());
        screens.add(new LoadScreen());
        screens.add(new GameScreen());
        screens.add(new GardenScreen());
    }

    @Override
    public void create() {
        new Resources();
        SaveManager.init();
        setWindowsMode();
//        screens.add(new SplashScreen());
        switchScreen(SPLASH_SCREEN);
    }

    void setWindowsMode() {
        if (screenMode == ScreenMode.WINDOWED) {
            Gdx.graphics.setUndecorated(false);
            Gdx.graphics.setWindowedMode(1000, 600);
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
        if(played) {
            gameLogic.getGameState().setLastPlayed(System.currentTimeMillis());
            SaveManager.saveGameState(gameLogic.getGameState(), currentGameStateId);
        }
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
//        System.out.println("Current Width:" + width + " current Height:" + height);
        super.resize(width, height);
    }

    public void switchScreen(int id, boolean force){
        if(id < 0 || id >= screens.size()){
            System.out.println("[ERROR] invalid screen id (" + id + ")");
            return;
        }
        if(!force){
            if(getScreen() == screens.get(id)) return;
        }
        setScreen(screens.get(id));
    }

    public void switchScreen(int id){
        switchScreen(id, false);
    }

    public void initScreens(){
        for(CoreScreen cS : screens) cS.init();
    }
}
