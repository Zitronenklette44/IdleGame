package de.lemon.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.lemon.editor.ParticleEditor;
import de.lemon.parameter.window.Window;

public class EditorLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Particle Editor");
        config.setWindowedMode(1100, 850);
        config.setWindowPosition(810, 160);

        Window.start();

        new Lwjgl3Application(new ParticleEditor(), config);
    }
}
