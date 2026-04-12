package de.lemon.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import de.lemon.components.PreviewFrame;
import de.lemon.components.StatFrame;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ParticlePresets;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.sources.StaticParticleSource;
import de.lemon.screens.CoreScreen;

import java.util.EnumSet;

public class ParticleStartScreen extends CoreScreen{

    public static ParticleStartScreen _instance;
    public GeneratorSettings particleSettings;
    private PreviewFrame previewFrame;
    private StatFrame statFrame;

    public Skin skin;
    public StaticParticleSource source;

    public ParticleStartScreen(){
        setBackgroundColor(Color.DARK_GRAY);
        _instance = this;
    }

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.UI);
    }

    @Override
    protected void createComponents() {
        particleSettings = Resources._instance.getParticle(ParticlePresets.FIRE);
        skin = new Skin(Gdx.files.internal("skins/template.json"));
        Table main = new Table();
        main.setFillParent(true);

        previewFrame = new PreviewFrame();
        statFrame = new StatFrame();
        main.add(previewFrame).grow().pad(10).row();
        main.add(statFrame).height(80).expandX().fillX().pad(0,10,10,10);

        stage.addActor(main);

        getParticleManager().setSpeed(0);
    }

    @Override
    protected void createWorld() {
        statFrame.createWorld();
        previewFrame.createWorld();

        source = new StaticParticleSource(new Vector2(0, 0), getParticleManager(), particleSettings);
        addWorldObject(source, 0.5f, 0.5f, 0.1f, 0.1f);
    }

    public ParticleManager getParticleManager() {
        return worldRenderer.getParticleManager();
    }
}
