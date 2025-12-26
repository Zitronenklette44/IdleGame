package de.lemon.core;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class WorldRenderer {
    ArrayList<GameObject> objects = new ArrayList<>();

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    SpriteBatch spriteBatch = new SpriteBatch();

    public WorldRenderer(){

    }

    public void addObject(GameObject object){
        objects.add(object);
    }

    public void render(float delta, OrthographicCamera camera){
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        for (GameObject o : objects) o.onShapeRender(shapeRenderer, delta);
        shapeRenderer.end();

        spriteBatch.begin();
        for (GameObject o : objects) o.onSpriteRender(spriteBatch, delta);
        spriteBatch.end();

    }

    public InputProcessor getInputProcessor() {
        return new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                for (GameObject o : objects) o.onKeyDown(keycode);
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                for (GameObject o : objects) o.onKeyUp(keycode);
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                for (GameObject o : objects) o.onKeyTyped(character);
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                for (GameObject o : objects) o.onTouchDown(screenX, screenY, button);
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                for (GameObject o : objects) o.onTouchUp(screenX, screenY, button);
                return false;
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                for (GameObject o : objects) o.onTouchDragged(screenX, screenY);
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                for (GameObject o : objects) o.onMouseMoved(screenX, screenY);
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                for (GameObject o : objects) o.onScrolled(amountX, amountY);
                return false;
            }
        };
    }

    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        objects.clear();
    }

    public void update(float delta){
        for (GameObject gO : objects){
            gO.update(delta);
        }
    }
}
