package de.lemon.editor;

import com.badlogic.gdx.Game;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ParticlePresent;
import de.lemon.main.EditorLauncher;
import de.lemon.parameter.EditorNode;

public class ParticleEditor extends Game {

    @Override
    public void create() {

        new Resources();
        Resources._instance.startLoading();
        System.out.println("Started loading ...");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        Logic._instance.end();
    }

    boolean loaded = false;
    @Override
    public void render() {
        Resources._instance.update();
        if(Resources._instance.isAllLoaded()) {
            super.render();
            finishedLoading();
        }
    }

    void finishedLoading(){
        if(loaded) return;
        new ParticleStartScreen();

        setScreen(ParticleStartScreen._instance);
        ParticleStartScreen._instance.particleSettings = Resources._instance.getParticle(ParticlePresent.FIRE);
        EditorNode.setDefaultValues(ParticleStartScreen._instance.particleSettings);
        Logic._instance.updateSourceSettings();
        System.out.println("Finished loading!");
        loaded = true;
    }
}
