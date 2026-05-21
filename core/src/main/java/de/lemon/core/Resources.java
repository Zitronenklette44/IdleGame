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
import de.lemon.utilities.DebugLogger;

import java.util.*;

public class Resources {

    public static Resources _instance;
    private final AssetManager assetManager;

    public FileHandle font1;

    public NinePatch UI_Button;

    private SpawnArea testArea;

    private final Map<String, String> assetRegistry = new HashMap<>();
    private final Map<ParticlePresets, GeneratorSettings> particleRegistry = new HashMap<>();

    public Resources(){
        _instance = this;

        assetManager = new AssetManager();
        registerAsset("loadingBar", "sprites/loadingBar.png", Texture.class);
        registerAsset("gameName", "sprites/gameName.png", Texture.class);

        assetManager.finishLoading();
    }

    /**
     * Registers an asset in the AssetManager and stores its logical name mapping.
     *
     * @param name logical name used to retrieve the asset later
     * @param path file path of the asset
     * @param clazz type of the asset (e.g. Texture.class)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registerAsset(String name, String path, Class clazz){
        assetManager.load(path, clazz);
        assetRegistry.put(name, path);
    }
    /**
     * Starts loading additional assets dynamically from the sprites folder.
     * Skips already registered core assets.
     */
    public void startLoading(){
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
    /**
     * Updates the AssetManager loading process.
     * When loading is complete, initializes UI and particle systems.
     */
    public void update() {
        if (assetManager.update()) { // true, if all loaded
            if(getTexture("button_01") != null) UI_Button = new NinePatch(assetManager.get("sprites/ui/button_01.png", Texture.class), 16, 16, 16, 16);
        }
        if(assetManager.isFinished()){
            createParticleSheets();
        }
    }
    /**
     * Retrieves a Texture by its registered logical name.
     *
     * @param name logical asset name
     * @return Texture instance or null if not found
     */
    public Texture getTexture(String name){
        try {
            return assetManager.get(getPath(name), Texture.class);
        }catch (GdxRuntimeException e){
            DebugLogger.printError("Error getting Asset: " + name , e);
        }
        return null;
    }

    /**
     * Retrieves a generic asset by its registered logical name and type.
     *
     * @param name logical asset name
     * @param clazz asset class type
     * @return loaded asset instance or null if not found
     */
    public <T> T getAsset(String name, Class<T> clazz){
        try {
            return assetManager.get(getPath(name), clazz);
        }catch (GdxRuntimeException e){
            DebugLogger.printError("Error getting Asset: " + name, e);
        }
        return null;
    }
    /**
     * Converts a logical asset name into its registered file path.
     *
     * @param name logical asset name
     * @return file path of the asset
     * @throws RuntimeException if the asset is not registered
     */
    private String getPath(String name){
        String path = assetRegistry.get(name);
        if(path == null)
            throw new RuntimeException("Asset not registered: " + name);
        return path;
    }
    /**
     * Loads all particle definitions from the particles folder
     * and registers them into the particle system.
     * Also initializes predefined spawn areas.
     */
    private void createParticleSheets() {
        ArrayList<FileHandle> particles = getAllFiles(Gdx.files.internal("particles"), "json");
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

    }
    /**
     * Checks whether all assets are fully loaded.
     *
     * @return true if AssetManager finished loading all assets
     */
    public boolean isAllLoaded(){
        return assetManager.isFinished();
    }
    /**
     * Disposes all managed assets and frees memory.
     */
    public void dispose(){
        assetManager.dispose();
    }
    /**
     * Returns a particle configuration for a given preset.
     *
     * @param particlePreset particle preset type
     * @return copied GeneratorSettings or null if not found
     */
    public GeneratorSettings getParticle(ParticlePresets particlePreset) {
        GeneratorSettings settings = particleRegistry.get(particlePreset);
        if (settings == null) return null;
        return settings.cpy();
    }
    /**
     * Returns a predefined spawn area based on an ID.
     *
     * @param id spawn area identifier
     * @return SpawnArea instance or null if unknown
     */
    public SpawnArea getSpawnAreaPresent(int id){
        switch (id){
            case 0: return testArea;
        }
        return null;
    }
    /**
     * Recursively collects all files with a specific extension in a folder.
     *
     * @param folder root folder to search in
     * @param extension required file extension (null for all files)
     * @return list of matching files
     */
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
