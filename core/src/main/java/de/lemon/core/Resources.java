package de.lemon.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import de.lemon.logic.enums.Direction;
import de.lemon.logic.enums.Geometric;
import de.lemon.logic.enums.ParticlePresets;
import de.lemon.mechanics.dialog.DialogData;
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
    private final Map<String, String> itemNameTexture = new HashMap<>();
    private final Map<String, Item> items = new HashMap<>();
    private final HashMap<String, DialogData> dialogs = new HashMap<>();
    private final HashMap<String, String> icons = new HashMap<>();

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
            if(name.equals("loadingBar") || name.equals("gameName")) continue;
            String path = f.path();
            registerAsset(name, path, Texture.class);
//            DebugLogger.printInfo("loadedFile: " + path + " as " + name);
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
            storeItemNameToTexture();
            createItems();
            parseDialogs();
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

    /**
     * soring Item names with the corresponding texture name
     */
    private void storeItemNameToTexture(){
        itemNameTexture.put("blattRubin", "BlattRubin");
        icons.put("blattRubin", "blattRubinIcon");
        itemNameTexture.put("grapes", "Grapes");
        icons.put("grapes", "grapesIcon");
        itemNameTexture.put("purpurWater", "PurpurWater");
        icons.put("purpurWater", "purpurWaterIcon");
        itemNameTexture.put("healingPotion", "HealingPotion");
        icons.put("healingPotion", "healingPotionIcon");
        itemNameTexture.put("empty", "emptyItem");
    }

    /**
     * get the Texture corresponding to the item name
     * @param itemName name of the item
     * @return texture name
     */
    public String getItemTexture(String itemName){
        String name = itemNameTexture.get(itemName);
        if(name == null)
            throw new RuntimeException("ItemTexture not registered: " + itemName);
        return name;
    }

    /**
     * creates default items
     */
    private void createItems(){
        items.put("empty", new Item("empty", 1, 32, 32, 0));
        items.put("blattRubin", new Item("blattRubin", 1, 32, 32, 1));
        items.put("grapes", new Item("grapes", 1, 32, 32, 2));
        items.put("purpurWater", new Item("purpurWater", 1, 32, 32, 3));
        items.put("healingPotion", new Item("healingPotion", 1, 32,32, 4));
    }

    /**
     * returns a copy of the default item with the given name
     * @param itemName name of the item
     * @return item copy
     */
    public Item getItem(String itemName) {
        Item item = items.get(itemName);
        if(item == null)
            throw new RuntimeException("Item not registered: " + itemName);
        return item.cpy();
    }

    /**
     * creates DialogData out of dialogs.json file
     */
    private void parseDialogs() {
        FileHandle file = Gdx.files.local("dialogs.json");      //read File

        JsonValue json = new JsonReader().parse(file.readString());
        JsonValue dialogsJson = json.get("dialogs");    //get dialog object

        for (JsonValue dialog = dialogsJson.child; dialog != null; dialog = dialog.next) { //iterate ofer collection
            DialogData data = new DialogData();

            data.name = dialog.name;
            data.speaker = dialog.getString("speaker");
            data.title = dialog.getString("title");
            JsonValue linesJson = dialog.get("lines");
            for (JsonValue line = linesJson.child; line != null; line = line.next) {    //store all lines in a List
                data.lines.add(line.asString());
            }
            JsonValue texture = dialog.get("texture");
            if(texture != null){
                data.textureName = texture.getString("name");
                data.frameWidth = texture.getInt("frameWidth");
                data.frameHeight = texture.getInt("frameHeight");
            }else{
                data.textureName = "unknownSpeaker";
                data.frameWidth = 32;
                data.frameHeight = 32;
            }
            dialogs.put(data.name, data);
//            DebugLogger.printInfo(data.toString());
        }
    }

    /**
     * Gives the DialogData to the given name
     * @param name name of the dialog
     * @return DialogData
     */
    public DialogData getDialogData(String name){
        DialogData item = dialogs.get(name).cpy();
        if(item == null)
            throw new RuntimeException("Dialog not found: " + name);
        return item;
    }

    public TextureRegion getIcon(String value) {
        return new TextureRegion(getTexture(icons.get(value)));
    }
}
