package de.lemon.parameter;

import com.badlogic.gdx.graphics.Color;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.mechanics.particleSystem.GeneratorSettings;

import java.util.EnumSet;

public enum EditorNode {

    SIZE(
        "Size",
        "Particle size",
        ValueType.FLOAT,
        -Integer.MAX_VALUE + 0f, 10, Integer.MAX_VALUE + 0f
    ),

    START_SPEED(
        "Start Speed",
        "Initial particle velocity",
        ValueType.FLOAT,
        -Float.MAX_VALUE, 0f, Float.MAX_VALUE
    ),

    LIFETIME(
        "Lifetime",
        "Time the Particle is visible until it gets removed",
        ValueType.FLOAT,
        -1f, 5f, Float.MAX_VALUE
    ),
    FRICTION(
        "Friction",
        "How fast the Particle Slows down",
        ValueType.FLOAT,
        -Float.MAX_VALUE, 3f, Float.MAX_VALUE
    ),
    TINT(
        "Tint",
        "Particle color",
        ValueType.COLOR,
        null, Color.WHITE, null
    ),
    ROTATION_SPEED(
        "Rotation Speed",
        "How fast the Particle spins around his own axis",
        ValueType.FLOAT,
        -Float.MAX_VALUE, 3f, Float.MAX_VALUE
    ),

    SPAWN_MIN(
        "Min",
        "Mindest number of particle spawns",
        ValueType.INTEGER,
        0f, 3f, Integer.MAX_VALUE + 0f
    ),
    SPAWN_MAX(
        "Max",
        "Maximum number of particle spawns",
        ValueType.INTEGER,
        0f, 5f, Integer.MAX_VALUE + 0f
    ),
    SPAWN_INTERVAL(
        "Interval",
        "Time between spawns",
        ValueType.FLOAT,
        0f, 3f, Float.MAX_VALUE
    ),

    BURST_MIN(
        "Min",
        "Mindest number of Particle Spawn during the Burst",
        ValueType.INTEGER,
        0f, 10f, Integer.MAX_VALUE + 0f),
    BURST_MAX(
        "Max",
        "Maximum number of Particle Spawn during the Burst",
        ValueType.INTEGER,
        0f, 15f, Integer.MAX_VALUE + 0f
    ),

    EMISSION_TYPE(
        "Emission Type",
        "How the Source generates Particles",
        ValueType.ENUM,
        ParticleEmissionType.class
    ),

    TTL(
        "TTL",
        "How long the Source may exist",
        ValueType.FLOAT,
        -1f, -1f, Float.MAX_VALUE
    ),
    TRACE_BACK(
        "Trace Back",
        "Particles follow emitter movement",
        ValueType.BOOLEAN,
        null, true, null
    ),
    MOVEMENT_SPEED(
        "Movement Speed",
        "How fast the Source moves",
        ValueType.FLOAT,
        0f, 0f, 500f
    ),
    MAX_EMISSIONS(
        "Max Emissions",
        "How many times the Source can emit particles. The emitted Particle have no impact on this value.",
        ValueType.INTEGER,
        -1f, 0f, Integer.MAX_VALUE + 0f

    );


    private final String displayName;
    private final String description;

    private final ValueType valueType;

    private Object defaultValue;
    private final Float min;
    private final Float max;

    private Class<? extends Enum<?>> enumClass;

    EditorNode(String displayName,
               String description,
               ValueType valueType,
               Float min,
               Object defaultValue,
               Float max) {

        this.displayName = displayName;
        this.description = description;
        this.valueType = valueType;

        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;

        enumClass = null;
    }

    EditorNode(String displayName, String description, ValueType valueType, Class<? extends Enum<?>> enumClass) {
        this(displayName, description, valueType, null, null, null);
        this.enumClass = enumClass;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }

    public ValueType getValueType() { return valueType; }

    public Object getDefaultValue() { return defaultValue; }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Float getMin() { return min; }
    public Float getMax() { return max; }

    @Override
    public String toString() {
        return displayName;
    }

    public static EnumSet<EditorNode> getParticleEditorNodes(){
        return EnumSet.of(SIZE, START_SPEED, LIFETIME, FRICTION, TINT, ROTATION_SPEED);
    }

    public static EnumSet<EditorNode> getSpawnEditorNodes(){
        return EnumSet.of(SPAWN_MIN, SPAWN_MAX, SPAWN_INTERVAL);
    }

    public static EnumSet<EditorNode> getBurstEditorNodes(){
        return EnumSet.of(BURST_MIN, BURST_MAX);
    }

    public static EnumSet<EditorNode> getGenerationEditorNodes(){
        return EnumSet.of(EMISSION_TYPE);
    }

    public static EnumSet<EditorNode> getOtherEditorNodes(){
        return EnumSet.of(TTL, MOVEMENT_SPEED, TRACE_BACK, MAX_EMISSIONS);
    }

    public Enum<?>[] getEnumValues() {
        if (enumClass == null) return new Enum<?>[0];
        return enumClass.getEnumConstants();
    }

    public static EditorNode fromString(String v){
        switch (v){
            case "Size": return SIZE;
            case "Start Speed": return START_SPEED;
            case "Lifetime": return LIFETIME;
            case "Friction": return FRICTION;
            case "Tint": return TINT;
            case "Rotation Speed": return ROTATION_SPEED;
            case "Spawn Min": return SPAWN_MIN;
            case "Spawn Max": return SPAWN_MAX;
            case "Spawn Interval": return SPAWN_INTERVAL;
            case "Burst Min": return BURST_MIN;
            case "Burst Max": return BURST_MAX;
            case "Emission Type": return EMISSION_TYPE;
            case "TTL": return TTL;
            case "Trace Back": return  TRACE_BACK;
            case "Movement Speed": return MOVEMENT_SPEED;
            case "Max Emissions": return MAX_EMISSIONS;
            default: throw new IllegalArgumentException("unknown Value: " + v);
        }
    }

    public enum ValueType {
        FLOAT,
        INTEGER,
        BOOLEAN,
        COLOR,
        ENUM
    }

    public static void setDefaultValues(GeneratorSettings settings){
        SIZE.setDefaultValue(settings.particleSize);
        START_SPEED.setDefaultValue(settings.particleStartSpeed);
        LIFETIME.setDefaultValue(settings.particleLifetime);
        FRICTION.setDefaultValue(settings.particleFriction);
        TINT.setDefaultValue(settings.particleTint);
        ROTATION_SPEED.setDefaultValue(settings.particleRotationSpeed);
        SPAWN_MIN.setDefaultValue(settings.minGeneration);
        SPAWN_MAX.setDefaultValue(settings.maxGeneration);
        SPAWN_INTERVAL.setDefaultValue(settings.generationInterval);
        BURST_MIN.setDefaultValue(settings.minBurst);
        BURST_MAX.setDefaultValue(settings.maxBurst);
        EMISSION_TYPE.setDefaultValue(settings.emissionType);
        TTL.setDefaultValue(settings.TTL);
        TRACE_BACK.setDefaultValue(settings.traceBack);
        MOVEMENT_SPEED.setDefaultValue(settings.movementSpeed);
        MAX_EMISSIONS.setDefaultValue(settings.maxEmissions);
        MOVEMENT_SPEED.setDefaultValue(settings.movementSpeed);
//        SPRITE.setDefaultValue(settings.particleSprite);
//        TEXTURE.setDefaultValue(settings.particleTexture);
    }

}
