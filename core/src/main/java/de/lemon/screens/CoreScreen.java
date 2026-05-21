package de.lemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameObject;
import de.lemon.logic.render.WorldRenderer;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.mechanics.particleSystem.ParticleSource;
import de.lemon.mechanics.particleSystem.sources.MovingParticleSource;
import de.lemon.ui.LayoutManager;

import java.util.EnumSet;

/**
 * Core Template for all Screens
 */
public abstract class CoreScreen implements Screen {
    protected Color backgroundColor = Color.BLACK;
    private GameObject background;

    protected Stage stage;
    protected Stage worldStage;
    protected WorldRenderer worldRenderer;
    protected LayoutManager layout = new LayoutManager();

    OrthographicCamera camera = new OrthographicCamera();
    protected Viewport viewport = new ExtendViewport(800, 480,camera);

    InputMultiplexer inputMultiplexer = new InputMultiplexer();

    protected abstract EnumSet<ScreenFeatures> getFeatures();

    public void init(){}

    @Override
    public void show() {
        if (inputMultiplexer != null) inputMultiplexer.clear();
        if(getFeatures().contains(ScreenFeatures.LOADING) || getFeatures().contains(ScreenFeatures.UI)){
            stage = new Stage(new ScreenViewport());
            inputMultiplexer.addProcessor(stage);
        }
        if(getFeatures().contains(ScreenFeatures.WORLD)){
            worldStage = new Stage(viewport);
            worldRenderer = new WorldRenderer(camera);
            inputMultiplexer.addProcessor(worldStage);
            inputMultiplexer.addProcessor(worldRenderer.getInputProcessor());
        }
        Gdx.input.setInputProcessor(inputMultiplexer);

        background = new GameObject(new Vector2(0, 0), new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight())) {
            @Override
            public void onShapeRender(ShapeRenderer shapeRenderer, float delta) {
                super.onShapeRender(shapeRenderer, delta);
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(backgroundColor);
                shapeRenderer.rect(pos.x, pos.y, size.x, size.y);

            }
        };
        addWorldObject(background, 0, 0, 1, 1);

        createComponents();
        createWorld();
    }

    protected abstract void createComponents();
    protected abstract void createWorld();

    @Override
    public void render(float delta) {
        //reset Colors
        Gdx.gl.glClearColor(
            backgroundColor.r,
            backgroundColor.g,
            backgroundColor.b,
            backgroundColor.a
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        if(worldRenderer != null){
            worldRenderer.update(delta);
            worldRenderer.render(delta);
        }
        if(stage != null) {
            stage.act(delta);
            stage.draw();
        }
        if(worldStage != null){
            worldStage.act(delta);
            worldStage.draw();
        }
    }

    public void addWorldObject(GameObject item, float relX, float relY, float relWidth, float relHeight){
        addWorldObject(item, relX, relY, relWidth, relHeight, Float.MAX_VALUE, Float.MAX_VALUE);
    }

    public void addWorldObject(GameObject item, float relX, float relY, float relWidth, float relHeight, float maxWidth, float maxHeight){
        addWorldObject(item, relX, relY, relWidth, relHeight, maxWidth, maxHeight, 0, 0);
    }

    public void addWorldObject(GameObject item, float relX, float relY, float relWidth, float relHeight, float maxWidth, float maxHeight, float minWidth, float minHeight){
        item.setRelLayout(relX, relY, relWidth, relHeight);
        item.setMaxSize(maxWidth, maxHeight, minWidth, minHeight);
        layout.add(item);
        if(item instanceof ParticleSource){
            addParticleSource((ParticleSource) item, relX, relY);
            return;
        }
        if (worldRenderer != null){
            worldRenderer.addObject(item);
        }
    }

    private void addParticleSource(ParticleSource item, float relX, float relY) {
        if(item instanceof MovingParticleSource){
            ((MovingParticleSource) item).relStartPos.set(relX, relY);
        }
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        camera.position.set(
            viewport.getWorldWidth() / 2f,
            viewport.getWorldHeight() / 2f,
            0
        );
        camera.update();

        if(stage != null) stage.getViewport().update(width, height, true);
        if(worldStage != null) worldStage.getViewport().update(width, height, true);
        if(worldRenderer != null) worldRenderer.resize(viewport);
        layout.resize(viewport);

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (worldRenderer != null) worldRenderer.dispose();
        if (worldStage != null) worldStage.dispose();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
