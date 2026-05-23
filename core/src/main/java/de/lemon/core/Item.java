package de.lemon.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.interfaces.Copyable;
import de.lemon.logic.render.AnimatedSprite;

public class Item extends GameObject implements Copyable<Item> {

    public String name;
    public int quantity;
    public final int frameWidth;
    public final int frameHeight;
    private AnimatedSprite sprite;

    public Item(String name, int quantity, int frameWidth, int frameHeight) {
        super(0, 0, 0, 0);
        this.name = name;
        this.quantity = quantity;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        sprite = new AnimatedSprite(Resources._instance.getItemTexture(name), frameWidth, frameHeight, true);
    }

    @Override
    public void setPos(Vector2 pos) {
        super.setPos(pos);
        sprite.setPos(pos.cpy());
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
        sprite.onSpriteRender(batch, delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        sprite.update(delta);
    }

    @Override
    public Item cpy() {
        Item item = new Item(name, quantity, frameWidth, frameHeight);
        item.relPos = relPos;
        item.relSize = relSize;
        item.maxSize = maxSize;
        item.minSize = minSize;
        item.sprite = sprite.cpy();
        return item;
    }
}
