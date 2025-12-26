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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameObject;
import de.lemon.core.WorldRenderer;
import de.lemon.enums.ScreenFeatures;

import java.util.EnumSet;

/**
 * Core Template for all Screens
 */
public abstract class CoreScreen implements Screen {
    private GameObject background;
    protected Color backgroundColor = Color.BLACK;

    protected Stage stage;
    protected WorldRenderer worldRenderer;

    OrthographicCamera camera = new OrthographicCamera();
    Viewport viewport = new ExtendViewport(800, 480,camera);

    InputMultiplexer inputMultiplexer = new InputMultiplexer();

    protected abstract EnumSet<ScreenFeatures> getFeatures();

    @Override
    public void show() {
        if(getFeatures().contains(ScreenFeatures.LOADING) || getFeatures().contains(ScreenFeatures.UI)){
            stage = new Stage(new ScreenViewport());
            inputMultiplexer.addProcessor(stage);
        }
        if(getFeatures().contains(ScreenFeatures.WORLD)){
            worldRenderer = new WorldRenderer();
            inputMultiplexer.addProcessor(worldRenderer.getInputProcessor());
        }

        Gdx.input.setInputProcessor(inputMultiplexer);

        background = new GameObject(new Vector2(0, 0), new Vector2(viewport.getWorldWidth(),viewport.getWorldHeight())) {
            @Override
            protected void onShapeRender(ShapeRenderer shapeRenderer, float delta) {
                super.onShapeRender(shapeRenderer, delta);
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(backgroundColor);
                shapeRenderer.rect(pos.x,pos.y, size.x, size.y);

            }
        };
        worldRenderer.addObject(background);

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
        //System.out.println("RENDER->" + getClass().getSimpleName());
        if(worldRenderer != null){
            worldRenderer.update(delta);
            worldRenderer.render(delta, camera);
        }
        if(stage != null) {
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        camera.position.set(
            viewport.getWorldWidth() / 2f,
            viewport.getWorldHeight() / 2f,
            0
        );
        camera.update();

        if(stage != null){
            stage.getViewport().update(width, height, true);
        }

    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        if (stage != null) stage.dispose();
        if ( worldRenderer != null) worldRenderer.dispose();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
