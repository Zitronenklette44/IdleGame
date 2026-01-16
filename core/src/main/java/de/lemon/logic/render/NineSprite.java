package de.lemon.logic.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;

public class NineSprite extends Sprite {
    NinePatch sprite;

    public NineSprite(NinePatch sprite, Vector2 pos, Vector2 size) {
        super(pos, size);
    }


    @Override
    public void onSpriteRender(Batch batch, float delta) {
        sprite.draw(batch, pos.x, pos.y, origin.x, origin.y, size.x, size.y, scale.x, scale.y, rotation);
    }
}
