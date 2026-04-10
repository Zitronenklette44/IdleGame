package de.lemon.save.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import de.lemon.mechanics.particleSystem.GeneratorSettings;

public class JsonParser {

    private static Json json = new Json();
    private static boolean isInit = false;

    public static void init(){
        if(isInit) return;
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        isInit = true;
    }

    public static void saveSettings(GeneratorSettings settings, String name){
        init();
        String text = json.prettyPrint(settings);
        Gdx.files.local("particles/" + name + ".json").writeString(text, false);
    }



}
