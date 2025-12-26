package de.lemon.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

    protected Vector2 pos;
    protected Vector2 size;

    public GameObject(Vector2 pos, Vector2 size){
        this.pos = pos;
        this.size = size;
    }

    public  GameObject(float posX, float posY, int width, int height){
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

    protected void onShapeRender(ShapeRenderer shapeRenderer, float delta){}
    protected void onSpriteRender(Batch batch, float delta){}

    protected void update(float delta){}

    protected void onKeyDown(int keycode){}
    protected void onKeyUp(int keycode){}
    protected void onKeyTyped(char character){}
    protected void onTouchDown(int screenX, int screenY, int button){}
    protected void onTouchUp(int screenX, int screenY, int button){}
    protected void onTouchDragged(int screenX, int screenY){}
    protected void onMouseMoved(int screenX, int screenY){}
    protected void onScrolled(float amountX, float amountY){}
}
