package de.lemon.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.components.PreviewFrame;
import de.lemon.components.StatFrame;
import de.lemon.core.Resources;
import de.lemon.logic.enums.Direction;
import de.lemon.logic.enums.Geometric;
import de.lemon.logic.enums.ParticlePresets;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.render.WorldRenderer;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;
import de.lemon.mechanics.particleSystem.SpawnArea;
import de.lemon.mechanics.particleSystem.sources.StaticParticleSource;
import de.lemon.screens.CoreScreen;

import java.util.EnumSet;

public class ParticleStartScreen extends CoreScreen{

    public static ParticleStartScreen _instance;
    public GeneratorSettings particleSettings;
    public SpawnArea spawnArea;
    private PreviewFrame previewFrame;
    private StatFrame statFrame;

    public Skin skin;
    public ParticleSource source;
    public int width;
    public int height;

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
        particleSettings = Resources._instance.getParticle(ParticlePresets.FIRE).builder().movementSpeed(10).build();
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

        spawnArea = new SpawnArea().builder().direction(Direction.INWARDS).geometric(Geometric.CIRCLE).build();

        source = new StaticParticleSource(new Vector2(0, 0), getParticleManager(), particleSettings);
        addWorldObject(source, 0.5f, 0.5f, 0.1f, 0.1f);

    }

    public ParticleManager getParticleManager() {
        return worldRenderer.getParticleManager();
    }

    public Stage getWorldStage(){
        return worldStage;
    }

    public void refreshLayout(){
        if(worldRenderer != null) worldRenderer.resize(viewport);
        layout.resize(viewport);
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        super.resize(width, height);
    }
}
