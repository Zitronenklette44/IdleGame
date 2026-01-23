package de.lemon.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.interfaces.LayoutItem;

public abstract class GameObject implements LayoutItem {

    protected Vector2 pos;
    protected Vector2 size;
    protected Vector2 relPos = new Vector2();
    protected Vector2 relSize = new Vector2();
    protected Vector2 maxSize = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    protected Vector2 minSize = new Vector2();
    protected Vector2 desiredSize = new Vector2();
    protected boolean manualSize = false;

    public GameObject(Vector2 pos, Vector2 size){
        this.pos = pos;
        this.size = size;
    }

    public GameObject(float posX, float posY, int width, int height){
        pos = new Vector2(posX, posY);
        size = new Vector2(width, height);
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void onShapeRender(ShapeRenderer shapeRenderer, float delta){}
    public void onSpriteRender(Batch batch, float delta){}
    public void onDebug(ShapeRenderer shapeRenderer, float delta){}

    public void update(float delta){}

    public void onKeyDown(int keycode){}
    public void onKeyUp(int keycode){}
    public void onKeyTyped(char character){}
    public void onTouchDown(int screenX, int screenY, int button){}
    public void onTouchUp(int screenX, int screenY, int button){}
    public void onTouchDragged(int screenX, int screenY){}
    public void onMouseMoved(int screenX, int screenY){}
    public void onScrolled(float amountX, float amountY){}

    @Override
    public void applyLayout(Viewport viewport) {
        pos.set(viewport.getWorldWidth() * relPos.x, viewport.getWorldHeight() * relPos.y);
        desiredSize.set(viewport.getWorldWidth() * relSize.x, viewport.getWorldHeight() * relSize.y);
    }

    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        relPos.set(relX, relY);
        relSize.set(relWidth, relHeight);
    }

    @Override
    public void setMaxSize(float maxWidth, float maxHeight, float minWidth, float minHeight) {
        maxSize.set(maxWidth, maxHeight);
        minSize.set(minWidth, minHeight);
    }

    @Override
    public void applyMaxSize() {
        if(manualSize) return;
        size.x = MathUtils.clamp(desiredSize.x, minSize.x, maxSize.x);
        size.y = MathUtils.clamp(desiredSize.y, minSize.y, maxSize.y);
    }

    @Override
    public void finaliseLayout(){
        applyMaxSize();
    }
}
