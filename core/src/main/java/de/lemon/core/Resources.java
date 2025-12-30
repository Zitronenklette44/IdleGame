package de.lemon.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Resources {

    public static Resources _instance;
    private final AssetManager assetManager;

    public Texture splashScreen_loadingBar;
    public Texture gameScreen_background;
    public Texture gardenScreen_background;
    public Texture startScreen_name;
    public Skin skin;
    public FileHandle font1;


    public Resources(){
        _instance = this;

        assetManager = new AssetManager();
        assetManager.load("sprites/loadingBar.png", Texture.class);
        assetManager.load("sprites/idlePotions.png", Texture.class);

        assetManager.finishLoading();
        splashScreen_loadingBar = assetManager.get("sprites/loadingBar.png", Texture.class);
        startScreen_name = assetManager.get("sprites/idlePotions.png", Texture.class);

    }

    public void startLoading(){
        assetManager.load("sprites/gameScreen.png", Texture.class);
        assetManager.load("sprites/garden.png", Texture.class);


        assetManager.load("skins/template.json", Skin.class);

        font1 = Gdx.files.internal("fonts/font1.ttf");
    }

    boolean loadedAll = false;
    public void update() {
        if (assetManager.update()) { // true, wenn alles fertig
            if (gameScreen_background == null) gameScreen_background = assetManager.get("sprites/gameScreen.png", Texture.class);
            if (gardenScreen_background == null) gardenScreen_background = assetManager.get("sprites/garden.png", Texture.class);

            if (skin == null) skin = assetManager.get("skins/template.json", Skin.class);
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
