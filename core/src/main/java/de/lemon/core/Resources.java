package de.lemon.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Resources {

    public static Resources _instance;
    private final AssetManager assetManager;

    public Texture splashScreen_loadingBar;
    public Texture gameScreen_background;
    public Texture startScreen_name;



    public Resources(){
        _instance = this;

        assetManager = new AssetManager();
        assetManager.load("sprites/loadingBar.png", Texture.class);

        assetManager.finishLoading();
        splashScreen_loadingBar = assetManager.get("sprites/loadingBar.png", Texture.class);

    }

    public void startLoading(){
        assetManager.load("sprites/gameScreen.png", Texture.class);
        assetManager.load("sprites/name.png", Texture.class);
    }

    boolean loadedAll = false;
    public void update() {
        if (assetManager.update()) { // true, wenn alles fertig
            if (gameScreen_background == null) gameScreen_background = assetManager.get("sprites/gameScreen.png", Texture.class);
            if (startScreen_name == null) startScreen_name = assetManager.get("sprites/name.png", Texture.class);

            loadedAll = true;
        }
    }
    int counter = 0;
    public boolean isAllLoaded(){
        if(counter < 100) {
            counter++;
            return false;
        }
        return assetManager.isFinished() && loadedAll;
    }

    public void dispose(){
        assetManager.dispose();
    }

}
