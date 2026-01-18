package de.lemon.logic.render;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.lemon.core.GameObject;
import de.lemon.logic.GameLogic;
import de.lemon.logic.interfaces.Clickable;
import de.lemon.logic.interfaces.Hoverable;

import java.util.ArrayList;

public class WorldRenderer {
    private final ArrayList<GameObject> objects = new ArrayList<>();
    private final ArrayList<GameObject> lights = new ArrayList<>();
    private final OrthographicCamera camera;

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    SpriteBatch spriteBatch = new SpriteBatch();

    private Hoverable hoverable;

    public WorldRenderer(OrthographicCamera camera){
        this.camera = camera;
    }

    public void addObject(GameObject object){
        objects.add(object);
    }
    public void addLight(GameObject object){
        lights.add(object);
    }

    public void render(float delta){
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (GameObject o : objects) o.onShapeRender(shapeRenderer, delta);
        shapeRenderer.end();

        spriteBatch.begin();
        for (GameObject o : objects) o.onSpriteRender(spriteBatch, delta);
        spriteBatch.end();

        if(GameLogic.debug){
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (GameObject o : objects) o.onDebug(shapeRenderer, delta);
            shapeRenderer.end();
        }

        spriteBatch.setBlendFunction(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE
        );
        spriteBatch.begin();
        for (GameObject o : lights) o.onSpriteRender(spriteBatch, delta);
        spriteBatch.end();
        spriteBatch.setBlendFunction(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        );

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
                Vector3 world = new Vector3(screenX, screenY, 0);
                camera.unproject(world);

                for (GameObject o : objects) o.onTouchDown(screenX, screenY, button);
                for (int i = objects.size() - 1; i >= 0 ; i--) {
                    GameObject o = objects.get(i);
                    if(o instanceof Clickable){
                        Clickable c = (Clickable) o;
                        if(c.isClickable() && c.contains(world.x, world.y)) {
                            c.onClick(button);
                            break;
                        }
                    }
                }
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
                Vector3 world = new Vector3(screenX, screenY, 0);
                camera.unproject(world);

                for (GameObject o : objects) o.onMouseMoved(screenX, screenY);

                if(hoverable != null && !hoverable.contains(world.x, world.y)){
                    hoverable.onExit();
                    hoverable = null;
                }

                for (int i = objects.size() - 1; i >= 0 ; i--) {
                    GameObject o = objects.get(i);
                    if(o instanceof Hoverable){
                        Hoverable h = (Hoverable) o;
                        if(h.contains(world.x, world.y)){
                            if(hoverable == null){
                                hoverable = h;
                                h.onEnter();
                                break;
                            }
                            if(hoverable.equals(h)) break;
                            hoverable.onExit();
                            hoverable = h;
                            h.onEnter();
                        }
                    }
                }
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

    public ArrayList<GameObject> getObjects() {
        return objects;
    }
}
