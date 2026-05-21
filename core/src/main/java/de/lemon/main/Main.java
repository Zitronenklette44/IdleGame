package de.lemon.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenMode;
import de.lemon.logic.GameLogic;
import de.lemon.logic.Tick;
import de.lemon.mechanics.Inventory;
import de.lemon.save.SaveManager;
import de.lemon.screens.*;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static final int SPLASH_SCREEN = 0;
    public static final int START_SCREEN = 1;
    public static final int LOAD_SCREEN = 2;
    public static final int GAME_SCREEN = 3;
    public static final int GARDEN_SCREEN = 4;
    public static final int OPTIONS_SCREEN = 5;

    public final ArrayList<CoreScreen> screens = new ArrayList<>();
    public final Deque<Integer> lastScreens = new ArrayDeque<>();
    private int currentIndex = -1;

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
        screens.add(new OptionScreen());
    }

    @Override
    public void create() {
        new Resources();
        SaveManager.init();
        setWindowsMode();
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
            gameLogic.getGameState().setInventory(Inventory._instance);
            gameLogic.getGameState().setLastPlayed(System.currentTimeMillis());
            SaveManager.saveGameState(gameLogic.getGameState(), currentGameStateId);
        }
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public void switchScreen(int id, boolean force){
        if(id < 0 || id >= screens.size()){
            DebugLogger.printError("Invalid screen id: " + id);
            return;
        }
        if(!force && currentIndex == id) return;
        if(currentIndex != -1) lastScreens.push(currentIndex);
        currentIndex = id;
        setScreen(screens.get(id));
    }

    public void switchScreen(int id){
        switchScreen(id, false);
    }

    public void initScreens(){
        for(CoreScreen cS : screens) cS.init();
    }

    public void popScreens(int amount) {
        if (lastScreens.isEmpty()) return;

        int target = -1;

        for (int i = 0; i < amount; i++) {
            if (lastScreens.isEmpty()) break;
            target = lastScreens.pop();
        }

        if (target != -1) {
            currentIndex = target;
            setScreen(screens.get(target));
        }
    }

    public void popScreens(){
        popScreens(1);
    }
}
