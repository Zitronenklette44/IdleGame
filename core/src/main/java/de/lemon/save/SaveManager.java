package de.lemon.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import de.lemon.core.GameState;

public final class SaveManager {

    // Debug
    private static final boolean DEBUG_PRINT = true;

    // Save config
    private static final String SAVE_DIR = "saves";
    private static final String FILE_PREFIX = "SaveGame";
    private static final String FILE_EXTENSION = ".json";

    private static final Json json = new Json();

    private SaveManager() {}

    public static void init(){
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        json.setElementType(GameState.class, "upgrades", Integer.class);
    }

    public static GameState loadGameState(int id) {
        FileHandle file = getSaveFile(id);

        if (!file.exists()) {
            return new GameState();
        }

        String text = file.readString();

        if (text.isEmpty()) {
            return new GameState();
        }

        return json.fromJson(GameState.class, text);
    }

    public static void saveGameState(GameState gameState, int id) {
        FileHandle dir = Gdx.files.local(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileHandle file = getSaveFile(id);

        String text = DEBUG_PRINT
            ? json.prettyPrint(gameState)
            : json.toJson(gameState);

        file.writeString(text, false);
    }

    /**
     * Counts the number of save files in the save directory.
     */
    public static int getNumberOfSaveGames() {
        FileHandle dir = Gdx.files.local(SAVE_DIR);

        if (!dir.exists() || !dir.isDirectory()) {
            return 0;
        }

        int count = 0;

        for (FileHandle file : dir.list()) {
            String name = file.name();
            if (!file.isDirectory()
                && name.startsWith(FILE_PREFIX)
                && name.endsWith(FILE_EXTENSION)) {
                count++;
            }
        }

        return count;
    }

    private static FileHandle getSaveFile(int id) {
        return Gdx.files.local(
            SAVE_DIR + "/" + FILE_PREFIX + id + FILE_EXTENSION
        );
    }

    private static String getSaveName(int id){
        GameState temp = loadGameState(id);
        String name = temp.getName();
        temp.dispose();
        temp = null;
        return name;
    }
}
