package de.lemon.mechanics.customer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.logic.render.Sprite;
import de.lemon.utilities.DebugLogger;

public class CustomerDisplay extends Sprite {

    private final String textureName;
    private final int frameWidth;
    private final int frameHeight;
    private final Customer customer;
    AnimatedSprite sprite;

    public CustomerDisplay(String textureName, int frameWidth, int frameHeight, Customer customer) {
        super(new Vector2(), new Vector2());
        this.textureName = textureName;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.customer = customer;
        manualSize = false;
        setClickable(true);
        createSprite();
    }

    private void createSprite(){
        sprite = new AnimatedSprite(textureName, frameWidth, frameHeight, 0.1f, true);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        sprite.update(delta);
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
        sprite.onSpriteRender(batch, delta);
    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(pos.x - size.x / 2, pos.y - size.y / 2, size.x ,size.y);
        sprite.onDebug(shapeRenderer, delta);
    }

    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        super.setRelLayout(relX, relY, relWidth, relHeight);
        sprite.setRelLayout(relX, relY, relWidth, relHeight);
    }

    @Override
    public void applyLayout(Viewport viewport) {
        super.applyLayout(viewport);
        sprite.applyLayout(viewport);
    }

    @Override
    public void finaliseLayout() {
        super.finaliseLayout();
        sprite.finaliseLayout();
    }

    @Override
    public Sprite cpy() {
        return null;
    }

    @Override
    public void onClick(int button) {
        super.onClick(button);
        customer.startDialog();
    }

    @Override
    public boolean contains(float x, float y) {
        return sprite.contains(x, y);
    }
}
