package de.lemon.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import de.lemon.Utilities;

public abstract class Component extends Group {

    public Component(){
        init();
        createComponents();
    }

    public abstract void init();
    public abstract void createComponents();

    public abstract void createWorld();

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color oldColor = batch.getColor();

        batch.setColor(
            getColor().r,
            getColor().g,
            getColor().b,
            getColor().a * parentAlpha
        );

        batch.draw(
            Utilities.getWhitePixel(),
            getX(),
            getY(),
            getWidth(),
            getHeight()
        );

        batch.setColor(oldColor);
        super.draw(batch, parentAlpha);
    }
}
