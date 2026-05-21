package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.render.AnimationController;
import de.lemon.logic.render.SimpleSprite;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.main.Main;
import de.lemon.ui.FontCache;

import java.util.EnumSet;

public class SplashScreen extends CoreScreen{

    private SimpleSprite simpleSprite;
    private AnimatedSprite name;
    private final boolean[] loadProgress = {false, false, false};
    private final int MAX_LOADING_FRAMES = 20;
    private final int TRANSITION_FRAMES = 30;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.LOADING, ScreenFeatures.WORLD);
    }

    @Override
    public void show() {
        super.show();
        Resources._instance.startLoading();
        simpleSprite.setFrame(0);
        setBackgroundColor(Color.GRAY);
    }

    @Override
    protected void createComponents() {}

    @Override
    protected void createWorld() {
        simpleSprite = new SimpleSprite("loadingBar", 128, 16, false, new Vector2());
        addWorldObject(simpleSprite, 0.5f, 0.5f, 1f/3, 1);

        name = new AnimationController("gameName", new int[]{0, 1, 2, 3}, new Vector2(), 256, 48, 0.1f, 5);
        addWorldObject(name, 0.5f, 0.8f, 0.5f, 0.1f);
    }

    int frameCounter = -1;
    @Override
    public void render(float delta) {
        if(!loadProgress[0]) Resources._instance.update();
        checkProgress();
        if(amountLoaded() == loadProgress.length) {
            if(frameCounter == -1) frameCounter = 0;
            if(frameCounter >= TRANSITION_FRAMES){
                Main._instance.switchScreen(Main.START_SCREEN);
                return;
            }
        }
        if(frameCounter != -1) frameCounter++;
        super.render(delta);
    }

    private void checkProgress() {
        if(!loadProgress[0] && Resources._instance.isAllLoaded()){
            loadProgress[0] = true;
        }

        if(loadProgress[0]){
            if(!FontCache.isLoaded) FontCache.init(Resources._instance.font1);
            if(!loadProgress[1]) loadProgress[1] = FontCache.isLoaded;

            if(!loadProgress[2]){
                Main._instance.initScreens();
                loadProgress[2] = true;
            }


        }
        setState((MAX_LOADING_FRAMES / loadProgress.length) * amountLoaded());
    }

    public int amountLoaded(){
        int counter = 0;
        for (boolean b : loadProgress) if (b) counter++;
        return counter;
    }

    public void setState(int newState){
        simpleSprite.setFrame(newState);
    }

    public void increaseState(){
        simpleSprite.nextFrame();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
