package de.lemon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameObject;
import de.lemon.logic.interfaces.Clickable;
import de.lemon.logic.interfaces.Hoverable;
import de.lemon.logic.render.StateAnimationController;

public class AnimatedButton extends GameObject implements Hoverable, Clickable {
    public static final int IDLE_STATE = 0;
    public static final int HOVER_STATE = 1;
    public static final int CLICKED_STATE = 2;

    StateAnimationController animationController;

    private boolean enabled = true;

    Runnable action;

    public AnimatedButton(String textureName, int[] rows, int frameWidth, int frameHeight) {
        super(new Vector2(), new Vector2());
        animationController = new StateAnimationController(textureName, rows, frameWidth, frameHeight,0.1f);
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
        animationController.onSpriteRender(batch, delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        animationController.update(delta);
    }

    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        super.setRelLayout(relX, relY, relWidth, relHeight);
        animationController.setRelLayout(relX, relY, relWidth, relHeight);
    }

    @Override
    public void applyLayout(Viewport viewport) {
        super.applyLayout(viewport);
        animationController.applyLayout(viewport);
    }

    @Override
    public void finaliseLayout() {
        super.finaliseLayout();
        animationController.finaliseLayout();
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public void onTouchDown(float worldX, float worldY, int screenX, int screenY, int button) {
        super.onTouchDown(worldX, worldY, screenX, screenY, button);
        if(contains(worldX, worldY) && enabled) animationController.setAnimationToState(CLICKED_STATE);
        else animationController.setAnimationToState(IDLE_STATE);
    }

    @Override
    public void onTouchUp(float worldX, float worldY, int screenX, int screenY, int button) {
        super.onTouchUp(worldX, worldY, screenX, screenY, button);
        if(contains(worldX, worldY) && enabled) action.run();
        else animationController.setAnimationToState(IDLE_STATE);
    }

    @Override
    public void onClick(int button) {}

    @Override
    public void onClickChildren(float x, float y, int button) {}

    @Override
    public void onEnter() {
        if(enabled) animationController.setAnimationToState(HOVER_STATE);
    }

    @Override
    public void onExit() {
        animationController.setAnimationToState(IDLE_STATE);
    }

    @Override
    public boolean contains(float x, float y) {
        Rectangle rec = new Rectangle(pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
        return rec.contains(x, y);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
        super.onDebug(shapeRenderer, delta);
    }
}
