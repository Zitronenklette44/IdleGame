package de.lemon.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.lemon.logic.enums.Direction;
import de.lemon.logic.enums.Geometric;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.logic.enums.ParticlePresets;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.SpawnArea;
import de.lemon.save.particle.JsonParser;
import de.lemon.save.particle.ParticleLoader;

import java.util.*;

public class Resources {

    public static Resources _instance;
    private final AssetManager assetManager;

    public FileHandle font1;

    public NinePatch UI_Button;


    private SpawnArea testArea;
    // <globalDeclaration>
    // </globalDeclaration>

    private final Map<String, String> assetRegistry = new HashMap<>();
    private final Map<ParticlePresets, GeneratorSettings> particleRegistry = new HashMap<>();

    public Resources(){
        _instance = this;

        assetManager = new AssetManager();
        registerAsset("loadingBar", "sprites/loadingBar.png", Texture.class);
        registerAsset("gameName", "sprites/gameName.png", Texture.class);

        assetManager.finishLoading();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registerAsset(String name, String path, Class clazz){
        assetManager.load(path, clazz);
        assetRegistry.put(name, path);
    }

    public void startLoading(){
//        registerAsset("door", "sprites/door.png", Texture.class);
//        registerAsset("gameScreen", "sprites/gameScreen.png", Texture.class);
//        registerAsset("garden", "sprites/garden.png", Texture.class);
//        registerAsset("pots", "sprites/pots.png", Texture.class);
//        registerAsset("plant_01", "sprites/plants/plant_01.png", Texture.class);
//        registerAsset("button_01", "sprites/ui/button1.png", Texture.class);
//        registerAsset("red_particle", "sprites/particle/red_particle.png", Texture.class);
//        registerAsset("animated_particle", "sprites/particle/animatedParticle.png", Texture.class);
//        registerAsset("tintable_particle", "sprites/particle/tintableParticle.png", Texture.class);
//        registerAsset("smoke_particle", "sprites/particle/smoke_particle.png", Texture.class);

        ArrayList<FileHandle> textures = getAllFiles(Gdx.files.internal("sprites"), "png");
        for (FileHandle f : textures){
            String name = f.nameWithoutExtension();
            if(name.equals("loadingBar") || name.equalsIgnoreCase("gameName")) continue;
            String path = f.path();
            registerAsset(name, path, Texture.class);
        }
        //skin
        registerAsset("skin", "skins/template.json", Skin.class);

        //fonts
        font1 = Gdx.files.internal("fonts/font1.ttf");
    }

    public void update() {
        if (assetManager.update()) { // true, if all loaded
            if(getTexture("button_01") != null) UI_Button = new NinePatch(assetManager.get("sprites/ui/button_01.png", Texture.class), 16, 16, 16, 16);
        }
        if(assetManager.isFinished()){
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
        ArrayList<FileHandle> particles = getAllFiles(Gdx.files.internal("particles"), "json");
//        System.out.println("number of files: " + particles.size());
        for (FileHandle f : particles) {
            ParticleLoader loader = JsonParser.loadParticle(f);
            if (loader.settings == null || loader.preset == null) continue;
            particleRegistry.put(loader.preset, loader.settings);
        }


        testArea = new SpawnArea().builder()
            .geometric(Geometric.RECTANGLE)
            .direction(Direction.INWARDS)
            .rotation(0)
            .build();

        // <particlePresent>
        // </particlePresent>

    }

    public boolean isAllLoaded(){
        return assetManager.isFinished();
    }

    public void dispose(){
        assetManager.dispose();
    }

    public GeneratorSettings getParticle(ParticlePresets particlePreset) {
        GeneratorSettings settings = particleRegistry.get(particlePreset);
        if (settings == null) return null;
        return settings.cpy();
    }

    public SpawnArea getSpawnAreaPresent(int id){
        switch (id){
            case 0: return testArea;
        }
        return null;
    }

    private ArrayList<FileHandle> getAllFiles(FileHandle folder, String extension) {
        ArrayList<FileHandle> result = new ArrayList<>();

        if (!folder.exists() || !folder.isDirectory()) return result;

        for (FileHandle file : folder.list()) {
            if (file.isDirectory()) {
                result.addAll(getAllFiles(file, extension));
            } else if (extension == null || file.extension().equalsIgnoreCase(extension)) {
                result.add(file);
            }
        }

        return result;
    }
}
