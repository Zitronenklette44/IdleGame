package de.lemon.save.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.logic.enums.ParticlePresets;
import de.lemon.mechanics.particleSystem.GeneratorSettings;

public class JsonParser {

    private static final Json json = new Json();
    private static final JsonReader reader = new JsonReader();
    private static boolean isInit = false;

    public static void init(){
        if(isInit) return;
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        isInit = true;
    }

    public static void saveSettings(GeneratorSettings settings, String name){
        init();

        JsonValue root = new JsonValue(JsonValue.ValueType.object);

        root.addChild("particlePreset", new JsonValue(name.toUpperCase()));
        root.addChild("particleSize", new JsonValue(settings.particleSize));
        root.addChild("startSpeed", new JsonValue(settings.particleStartSpeed));
        root.addChild("lifetime", new JsonValue(settings.particleLifetime));
        root.addChild("interval", new JsonValue(settings.generationInterval));
        root.addChild("friction", new JsonValue(settings.particleFriction));
        root.addChild("TTL", new JsonValue(settings.TTL));
        root.addChild("movementSpeed", new JsonValue(settings.movementSpeed));
        root.addChild("rotationSpeed", new JsonValue(settings.particleRotationSpeed));

        root.addChild("generationMin", new JsonValue(settings.minGeneration));
        root.addChild("generationMax", new JsonValue(settings.maxGeneration));

        root.addChild("burstMin", new JsonValue(settings.minBurst));
        root.addChild("burstMax", new JsonValue(settings.maxBurst));

        root.addChild("emissions", new JsonValue(settings.maxEmissions));
        root.addChild("traceBack", new JsonValue(settings.traceBack));

        root.addChild("emissionType", new JsonValue(settings.emissionType.name()));

        if(settings.textureName != null)
            root.addChild("texture", new JsonValue(settings.textureName));

        if(settings.particleTint!= null)
            root.addChild("color", new JsonValue(settings.particleTint.toString()));

        if(settings.particleSprite != null){
            JsonValue spriteJson = reader.parse(json.toJson(SpritePreset.generateFromSprite(settings.particleSprite)));
            root.addChild("sprite", spriteJson);
        }

        String text = root.prettyPrint(JsonWriter.OutputType.json, 1);
        Gdx.files.local("particles/" + name + ".json").writeString(text, false);
    }


    public static ParticleLoader loadParticle(FileHandle file) {
        JsonValue root = reader.parse(file);

        ParticleLoader result = new ParticleLoader();

        if (root.has("particlePreset")) {
            result.preset = ParticlePresets.valueOf(root.getString("particlePreset"));
        }

        result.settings = parseSettings(root);

        return result;
    }

    private static GeneratorSettings parseSettings(JsonValue root) {
        init();
        GeneratorSettings.Builder builder = new GeneratorSettings().builder();

        if(root.has("particleSize")) builder.particleSize(root.getFloat("particleSize"));
        if(root.has("startSpeed")) builder.startSpeed(root.getFloat("startSpeed"));
        if(root.has("lifetime")) builder.lifetime(root.getFloat("lifetime"));
        if(root.has("generationMin") && root.has("generationMax")) builder.generation(root.getInt("generationMin"), root.getInt("generationMax"));
        if(root.has("emissionType")) builder.emissionType(ParticleEmissionType.valueOf(root.getString("emissionType")));
        if(root.has("interval")) builder.interval(root.getFloat("interval"));
        if(root.has("friction")) builder.friction(root.getFloat("friction"));
        if(root.has("TTL")) builder.TTL(root.getFloat("TTL"));
        if(root.has("movementSpeed")) builder.movementSpeed(root.getFloat("movementSpeed"));
        if(root.has("traceBack")) builder.traceBack(root.getBoolean("traceBack"));
        if(root.has("color")) builder.color(Color.valueOf(root.getString("color")));
        if(root.has("rotationSpeed")) builder.rotationSpeed(root.getFloat("rotationSpeed"));
        if(root.has("burstMin") && root.has("burstMax")) builder.burst(root.getInt("burstMin"), root.getInt("burstMax"));
        if(root.has("emissions")) builder.emissions(root.getInt("emissions"));

        if(root.has("texture")) builder.texture(root.getString("texture"));
        if (root.has("sprite")) {
            JsonValue spriteValue = root.get("sprite");

            SpritePreset preset = mapSpritePreset(spriteValue);

            builder.sprite(SpritePreset.generateFromPreset(preset));
        }

        return builder.build();
    }

    private static SpritePreset mapSpritePreset(JsonValue json) {
        SpritePreset preset = new SpritePreset();

        if (json.has("type")) {
            preset.type = SpritePreset.SpriteType.valueOf(json.getString("type"));
        }else{
            System.out.println("Error no type conversion could be made");
        }

        if (json.has("textureName")) preset.textureName = json.getString("textureName");
        if (json.has("frameWidth")) preset.frameWidth = json.getInt("frameWidth");
        if (json.has("frameHeight")) preset.frameHeight = json.getInt("frameHeight");
        if (json.has("loop")) preset.loop = json.getBoolean("loop");

        if (json.has("frameDuration")) preset.frameDuration = json.getFloat("frameDuration");
        if (json.has("row")) preset.row = json.getInt("row");

        if (json.has("rows")) {
            JsonValue rowsJson = json.get("rows");
            if (rowsJson.isArray()) {
                preset.rows = new int[rowsJson.size];
                int i = 0;
                for (JsonValue v = rowsJson.child; v != null; v = v.next) {
                    preset.rows[i++] = v.asInt();
                }
            }
        }

        if (json.has("delay")) preset.delay = json.getInt("delay");

        if (json.has("left")) preset.left = json.getInt("left");
        if (json.has("right")) preset.right = json.getInt("right");
        if (json.has("top")) preset.top = json.getInt("top");
        if (json.has("bottom")) preset.bottom = json.getInt("bottom");

        return preset;
    }
}
