package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ColoredSprite extends SimpleSprite{

    private Color color;

    public ColoredSprite(Color color) {
        super("tintableRect", 1, 1, true);
        this.color = color;
    }

    @Override
    public ColoredSprite cpy() {
        ColoredSprite returnValue = (ColoredSprite) super.cpy();
        returnValue.color = this.color;
        return returnValue;
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        batch.setColor(color);
        super.onSpriteRender(batch, delta);
        batch.setColor(Color.WHITE);
    }
}
