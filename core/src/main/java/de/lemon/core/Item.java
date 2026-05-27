package de.lemon.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.interfaces.Copyable;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.ui.FontCache;

import java.util.UUID;

public class Item extends GameObject implements Copyable<Item> {

    public String name;
    public int quantity;
    public int frameWidth;
    public int frameHeight;
    private AnimatedSprite sprite;
    private UUID uuid;
    public int id;

    public Item(String name, int quantity, int frameWidth, int frameHeight, int id) {
        super(0, 0, 0, 0);
        this.name = name;
        this.quantity = quantity;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.id = id;
        loadSprite();

        uuid = UUID.randomUUID();
    }

    private void loadSprite(){
        sprite = null;
        sprite = new AnimatedSprite(Resources._instance.getItemTexture(name), frameWidth, frameHeight, 0.1f, true);
    }

    @Override
    public void setPos(Vector2 pos) {
        super.setPos(pos);
        sprite.setPos(pos.cpy());
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        if(!isVisible) return;
        super.onSpriteRender(batch, delta);
        sprite.onSpriteRender(batch, delta);
        if(name.equals("empty")) return;
        BitmapFont font = FontCache.getFont(12, Color.WHITE);
        assert font != null;
        font.draw(batch,quantity + "", pos.x + size.x/4.5f, pos.y - size.y / 4.5f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        sprite.update(delta);
    }

    @Override
    public Item cpy() {
        Item item = new Item(name, quantity, frameWidth, frameHeight, id);
        item.relPos = relPos.cpy();
        item.relSize = relSize.cpy();
        item.maxSize = maxSize.cpy();
        item.minSize = minSize.cpy();
        item.sprite = sprite.cpy();
        item.id = id;
        return item;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Item)) return false;
        Item objI = (Item) obj;
        return objI.uuid == uuid;
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
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        super.onDebug(shapeRenderer, delta);
        sprite.onDebug(shapeRenderer, delta);
    }

    public void set(Item newItem) {
        name = newItem.name;
        quantity = newItem.quantity;
        frameWidth = newItem.frameWidth;
        frameHeight = newItem.frameHeight;
        id = newItem.id;
        loadSprite();
        sprite.relPos.set(relPos.cpy());
        sprite.relSize.set(relSize.cpy());
    }

    public AnimatedSprite getSprite() {
        return sprite;
    }

    public UUID getUuid() {
        return uuid;
    }
}
