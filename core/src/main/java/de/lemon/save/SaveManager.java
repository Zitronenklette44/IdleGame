package de.lemon.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import de.lemon.core.GameState;
import de.lemon.core.Item;
import de.lemon.save.serializer.ItemSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        json.setSerializer(Item.class, new ItemSerializer());
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
    public static List<Integer> getAvailableIds() {
        FileHandle dir = Gdx.files.local(SAVE_DIR);

        if (!dir.exists() || !dir.isDirectory()) {
            return new ArrayList<>();
        }

        List<Integer> availableIds = new ArrayList<>();

        for (FileHandle file : dir.list()) {
            String name = file.name();
            if (!file.isDirectory()
                && name.startsWith(FILE_PREFIX)
                && name.endsWith(FILE_EXTENSION)) {
                int id = Integer.parseInt(name.replace(FILE_PREFIX, "").replace(FILE_EXTENSION, ""));
                if (!availableIds.contains(id)) {
                    availableIds.add(id);
                } else {
                    file.delete();
                }

                Collections.sort(availableIds);

            }
        }

        return availableIds;
    }

    private static FileHandle getSaveFile(int id) {
        return Gdx.files.local(
            SAVE_DIR + "/" + FILE_PREFIX + id + FILE_EXTENSION
        );
    }

    public static void delete(int id) {
        FileHandle file = Gdx.files.local(SAVE_DIR + "/" + FILE_PREFIX + id + FILE_EXTENSION);
        file.delete();
    }

    public static int getNewId(){
        List<Integer> availableIds = getAvailableIds();
        if(availableIds.isEmpty()) return 0;
        int lastId = availableIds.get(availableIds.size() - 1);
        return lastId + 1;
    }
}

