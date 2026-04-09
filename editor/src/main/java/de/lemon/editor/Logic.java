package de.lemon.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.ParticleSource;
import de.lemon.parameter.EditorNode;
import de.lemon.parameter.window.Window;

public class Logic {

    public static Logic _instance = new Logic();

    public void btnNew(){
//        ParticleEditor.startScreen.particleSettings.settings = new GeneratorSettings();
//        ParticleEditor.startScreen.getParticleManager().particles.clear();
    }


    public void btnAdd() {
    }

    public void btnSourcePos() {
    }

    public void btnSourceType() {
    }

    public void end() {
        Gdx.app.exit();
        Window.stop();
    }

    public void changeValue(EditorNode node, Object value){
        GeneratorSettings.Builder builder = ParticleStartScreen._instance.particleSettings.builder();
//        int intValue;
//        float floatValue;
//        if(node.getValueType() == EditorNode.ValueType.INTEGER) intValue = (int) value;
//        if(node.getValueType() == EditorNode.ValueType.FLOAT)floatValue = (float) value;

        switch (node) {
            case SIZE:
                builder.particleSize(new Vector2((Float) value, (Float) value));
                break;
            case START_SPEED:
                builder.startSpeed((Float) value);
                break;
            case LIFETIME:
                builder.lifetime((Float) value);
//                System.out.println("new Lifetime:" + value);
                break;
            case FRICTION:
                builder.friction((Float) value);
                break;
            case TINT:
                builder.color((Color) value);
                break;
            case ROTATION_SPEED:
                builder.rotationSpeed((Float) value);
                break;
            case SPAWN_MIN:
                int max = ParticleStartScreen._instance.particleSettings.maxGeneration;
                builder.generation((Integer) value, max);
                break;
            case SPAWN_MAX:
                int min = ParticleStartScreen._instance.particleSettings.minGeneration;
                builder.generation(min, (Integer) value);
                break;
            case SPAWN_INTERVAL:
                builder.interval((Float) value);
                break;
            case BURST_MIN:
                int maxB = ParticleStartScreen._instance.particleSettings.maxBurst;
                builder.burst((Integer) value, maxB);
                break;
            case BURST_MAX:
                int minB = ParticleStartScreen._instance.particleSettings.minBurst;
                builder.burst(minB, (Integer) value);
                break;
            case EMISSION_TYPE:
                builder.emissionType((ParticleEmissionType) value);
                break;
            case TTL:
                builder.TTL((Float) value);
                break;
            case TRACE_BACK:
                builder.traceBack((Boolean) value);
                break;
            case MOVEMENT_SPEED:
                builder.movementSpeed((Float) value);
                break;
            case MAX_EMISSIONS:
                builder.emissions((Integer) value);
                break;
        }
        ParticleStartScreen._instance.particleSettings = builder.build();
        updateSourceSettings();
    }

    public void updateSourceSettings(){
        ParticleStartScreen._instance.source.applySettings(ParticleStartScreen._instance.particleSettings);
//        System.out.println("Lifetime V2: " + ParticleStartScreen._instance.particleSettings.particleLifetime);
//        System.out.println("new settings for source:" + ParticleStartScreen._instance.source);
    }
}
