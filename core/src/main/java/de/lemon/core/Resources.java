package de.lemon.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.lemon.logic.enums.Direction;
import de.lemon.logic.enums.Geometric;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.logic.enums.ParticlePresent;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.SpawnArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Resources {

    public static Resources _instance;
    private final AssetManager assetManager;

    public FileHandle font1;

    public NinePatch UI_Button;

    private GeneratorSettings particle_fire;
    private GeneratorSettings particle_smoke;

    private SpawnArea testArea;
    // <globalDeclaration>
    // </globalDeclaration>

    private final Map<String, String> assetRegistry = new HashMap<>();

    public Resources(){
        _instance = this;

        assetManager = new AssetManager();
        registerAsset("loadingBar", "sprites/loadingBar.png", Texture.class);
        registerAsset("gameName", "sprites/idlePotions.png", Texture.class);

        assetManager.finishLoading();
    }

    private void registerAsset(String name, String path, Class clazz){
        assetManager.load(path, clazz);
        assetRegistry.put(name, path);
    }

    public void startLoading(){
        registerAsset("door", "sprites/door.png", Texture.class);
        registerAsset("gameScreen", "sprites/gameScreen.png", Texture.class);
        registerAsset("garden", "sprites/garden.png", Texture.class);
        registerAsset("pots", "sprites/pots.png", Texture.class);
        registerAsset("plant_01", "sprites/plants/plant_01.png", Texture.class);
        registerAsset("button_01", "sprites/ui/button1.png", Texture.class);
        registerAsset("red_particle", "sprites/particle/red_particle.png", Texture.class);
        registerAsset("animated_particle", "sprites/particle/animatedParticle.png", Texture.class);
        registerAsset("tintable_particle", "sprites/particle/tintableParticle.png", Texture.class);
        registerAsset("smoke_particle", "sprites/particle/smoke_particle.png", Texture.class);
        registerAsset("skin", "skins/template.json", Skin.class);

        //fonts
        font1 = Gdx.files.internal("fonts/font1.ttf");
    }

    boolean loadedAll = false;
    public void update() {
        if (assetManager.update()) { // true, if all loaded
            if(getTexture("button_01") != null) UI_Button = new NinePatch(assetManager.get("sprites/ui/button1.png", Texture.class), 16, 16, 16, 16);;
            loadedAll = true;
        }
        if(loadedAll){
            createParticleSheets();
        }
    }

    public Texture getTexture(String name){
        try {
            return assetManager.get(getPath(name), Texture.class);
        }catch (GdxRuntimeException e){
            System.out.println("Error getting Asset: " + name + " Error:" + Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    public <T> T getAsset(String name, Class<T> clazz){
        try {
            return assetManager.get(getPath(name), clazz);
        }catch (GdxRuntimeException e){
            System.out.println("Error getting Asset: " + name + " Error:" + Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    private String getPath(String name){
        String path = assetRegistry.get(name);
        if(path == null)
            throw new RuntimeException("Asset not registered: " + name);
        return path;
    }

    private void createParticleSheets() {
        particle_fire = new GeneratorSettings().builder()
            .texture(getTexture("tintable_particle"))
            .particleSize(new Vector2(10, 10))
            .startSpeed(30f)
            .lifetime(5)
            .friction(0.2f)
            .generation(0, 8)
            .emissionType(ParticleEmissionType.CONTINUOUS)
            .interval(0.2f)
            .color(Color.WHITE)
            .build();

        particle_smoke = new GeneratorSettings().builder()
            .sprite(new AnimatedSprite("smoke_particle", 32, 32, 0.2f, false, Vector2.Zero.cpy()))
            .particleSize(new Vector2(50, 50))
            .startSpeed(15f)
            .lifetime(10)
            .friction(0.2f)
            .generation(1, 1)
            .emissionType(ParticleEmissionType.BURST)
            .interval(0.5f)
            .burst(50, 75)
            .color(Color.GRAY)
            .rotationSpeed(0)
            .build();

        testArea = new SpawnArea().builder()
            .geometric(Geometric.RECTANGLE)
            .direction(Direction.INWARDS)
            .rotation(0)
            .build();

        // <particlePresent>
        // </particlePresent>

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
                return particle_smoke.cpy();
            case SPARK:
                break;
            case GROWTH:
                break;
            case SPLASH:
                break;
        }
        return null;
    }

    public SpawnArea getSpawnAreaPresent(int id){
        switch (id){
            case 0: return testArea;
        }
        return null;
    }
}
