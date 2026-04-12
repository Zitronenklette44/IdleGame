package de.lemon.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.parameter.EditorNode;
import de.lemon.parameter.window.Window;
import de.lemon.save.particle.JsonParser;

import javax.swing.*;
import java.io.File;

public class Logic {

    public static Logic _instance = new Logic();

    public void btnNew(){
        ParticleStartScreen._instance.particleSettings = new GeneratorSettings();
        ParticleStartScreen._instance.getParticleManager().particles.clear();
        updateSourceSettings();
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

        switch (node) {
            case SIZE:
                builder.particleSize((Float) value);
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
    }

    public void btnLoad() {
    }

    public void btnCreateCode() {
        String name = JOptionPane.showInputDialog(null, "Namen der Einstellungen festlegen", "Name festlegen", JOptionPane.QUESTION_MESSAGE);
        File repoRoot = new File(System.getProperty("user.dir")).getParentFile();

        FileHandle file = Gdx.files.absolute(
            new File(repoRoot,
                "core/src/main/java/de/lemon/logic/enums/ParticlePresets.java"
            ).getAbsolutePath()
        );

        String content = file.readString();
        int startPos = content.indexOf("<EDITOR - PRESETS>");

        if (content.contains(name.toUpperCase() + ",")) {
            JOptionPane.showMessageDialog(null, "Enum already exists!");
            return;
        }
        JsonParser.saveSettings(ParticleStartScreen._instance.particleSettings, name);

        StringBuilder builder = new StringBuilder(content);
        builder.insert(startPos + 18, "\n    " + name.toUpperCase() + ",");

        file.writeString(builder.toString(), false);

        Object[] options = {"Editor schließen", "Weiterarbeiten"};
        int n = JOptionPane.showOptionDialog(null,
            "Preset '" + name + "' wurde zu ParticlePresets.java hinzugefügt.\n" +
                "Starte den Editor neu, um das neue Enum im Code zu verwenden.",
            "Erfolgreich gespeichert",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);

        if (n == JOptionPane.YES_OPTION) {
            Gdx.app.exit();
            Window.stop();
            System.exit(0);
        }
    }

}
