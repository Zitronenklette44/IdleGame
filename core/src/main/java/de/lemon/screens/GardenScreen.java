package de.lemon.screens;

import com.badlogic.gdx.math.Vector2;
import de.lemon.animation.SimpleSprite;
import de.lemon.animation.Sprite;
import de.lemon.core.Resources;
import de.lemon.enums.ScreenFeatures;

import java.util.EnumSet;

public class GardenScreen extends CoreScreen{
    private Sprite background;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.UI, ScreenFeatures.WORLD);
    }

    @Override
    protected void createComponents() {

    }

    @Override
    protected void createWorld() {
        background = new Sprite(Resources._instance.gardenScreen_background, 512, 256, 0.1f, false, new Vector2());
        worldRenderer.addObject(background);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.scaleToFit(new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight()));
    }
}
