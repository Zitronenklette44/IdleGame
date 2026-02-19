package de.lemon.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.logic.enums.ParticlePresent;
import de.lemon.mechanics.particleSystem.GeneratorSettings;

public class Resources {

    public static Resources _instance;
    private final AssetManager assetManager;

    public Texture splashScreen_loadingBar;
    public Texture door;
    public Texture redParticle;

    public Texture gameScreen_background;

    public Texture gardenScreen_background;
    public Texture gardenScreen_pots;

    public Texture plants_01;

    public Texture startScreen_name;
    public Skin skin;
    public FileHandle font1;

    public NinePatch UI_Button;

    private GeneratorSettings particle_fire;
    private GeneratorSettings particle_fire_mov;


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
        assetManager.load("sprites/door.png", Texture.class);

        assetManager.load("sprites/gameScreen.png", Texture.class);
        assetManager.load("sprites/garden.png", Texture.class);
        assetManager.load("sprites/pots.png", Texture.class);

        assetManager.load("sprites/plants/plant_01.png", Texture.class);

        assetManager.load("sprites/ui/button1.png", Texture.class);

        assetManager.load("sprites/red_particle.png", Texture.class);


        //skin
        assetManager.load("skins/template.json", Skin.class);

        assetManager.load("skins/customSkin/skin.atlas", TextureAtlas.class);
        SkinLoader.SkinParameter skinParam = new SkinLoader.SkinParameter("skins/customSkin/skin.atlas");
        assetManager.load("skins/customSkin/skin.json", Skin.class, skinParam);

        //fonts
        font1 = Gdx.files.internal("fonts/font1.ttf");
    }

    boolean loadedAll = false;
    public void update() {
        if (assetManager.update()) { // true, if all loaded
            if (door == null) door = assetManager.get("sprites/door.png", Texture.class);

            if (gameScreen_background == null) gameScreen_background = assetManager.get("sprites/gameScreen.png", Texture.class);
            if (gardenScreen_background == null) gardenScreen_background = assetManager.get("sprites/garden.png", Texture.class);
            if (gardenScreen_pots == null) gardenScreen_pots = assetManager.get("sprites/pots.png", Texture.class);

            if (plants_01 == null) plants_01 = assetManager.get("sprites/plants/plant_01.png", Texture.class);

            if (redParticle == null) redParticle = assetManager.get("sprites/red_particle.png", Texture.class);

            if (UI_Button == null) UI_Button = new NinePatch(assetManager.get("sprites/ui/button1.png", Texture.class), 16, 16, 16, 16);

            if (skin == null) skin = assetManager.get("skins/template.json", Skin.class);
//            if (skin == null)  skin = assetManager.get("skins/customSkin/skin.json", Skin.class);
            loadedAll = true;
        }
        if(loadedAll){
            createParticleSheets();
        }
    }

    private void createParticleSheets() {
        particle_fire = new GeneratorSettings().builder()
            .texture(redParticle)
            .particleSize(new Vector2(10, 10))
            .startSpeed(10f)
            .lifetime(5)
            .friction(-3.0f)
            .generation(12, 20)
            .emissionType(ParticleEmissionType.CONTINUOUS)
            .interval(0.2f)
            .build();

        particle_fire_mov = new GeneratorSettings().builder()
            .texture(redParticle)
            .particleSize(new Vector2(10, 10))
            .startSpeed(10f)
            .lifetime(5)
            .friction(-3.0f)
            .generation(12, 20)
            .emissionType(ParticleEmissionType.CONTINUOUS)
            .interval(0.2f)
            .movementSpeed(100f)
            .traceBack(true)
            .build();

    }

    public boolean isAllLoaded(){
        return assetManager.isFinished() && loadedAll;
    }

    public void dispose(){
        assetManager.dispose();
    }

    public GeneratorSettings getParticle(ParticlePresent particlePresent) {
        switch (particlePresent) {
            case FIRE:
                return particle_fire.cpy();
            case SMOKE:
                break;
            case SPARK:
                return particle_fire_mov.cpy();
            case GROWTH:
                break;
            case SPLASH:
                break;
        }
        return null;
    }
}
