package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.lemon.core.GameObject;
import de.lemon.logic.interfaces.Clickable;

public abstract class Sprite extends GameObject implements Clickable {

    public static final float CW_180 = 180;
    public static final float CW_90 = -90;
    public static final float CCW_90 = 90;
    public static final float CCW_45 = 45;
    public static final float CW_45 = -45;
    public static final float CW_135 = -135;
    public static final float CCW_135 = 135;

    protected float rotation = 0;
    protected Vector2 origin = new Vector2();
    protected Vector2 scale = new Vector2(1f,1f);

    protected boolean clickable = false;


    public Sprite(Vector2 pos, Vector2 size) {
        super(pos, size);
        origin.x = size.x / 2f;
        origin.y = size.y / 2f;
        manualSize = true;
    }

    @Override
    public boolean isClickable() {
        return clickable;
    }

    @Override
    public boolean contains(float x, float y) {
        // move point into local sprite space
        float cx = pos.x + origin.x;
        float cy = pos.y + origin.y;

        float dx = x - cx;
        float dy = y - cy;

        // rotate point back
        float rad = -rotation * MathUtils.degreesToRadians;
        float cos = MathUtils.cos(rad);
        float sin = MathUtils.sin(rad);

        float localX = dx * cos - dy * sin;
        float localY = dx * sin + dy * cos;

        // check bounds
        return localX >= -origin.x &&
            localX <= (size.x - origin.x) &&
            localY >= -origin.y &&
            localY <= (size.y - origin.y);
    }

    @Override
    public void onClick(int button) {}

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        if(shapeRenderer.getCurrentType() != ShapeRenderer.ShapeType.Line) shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(pos.x, pos.y, origin.x, origin.y, size.x, size.y, scale.x, scale.y, rotation);
    }
}
