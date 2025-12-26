package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.lemon.animation.AnimationController;
import de.lemon.animation.SimpleSprite;
import de.lemon.animation.Sprite;
import de.lemon.core.Resources;
import de.lemon.enums.ScreenFeatures;

import java.util.EnumSet;

public class StartScreen extends CoreScreen{

    private Sprite name;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.UI);
    }

    @Override
    protected void createComponents() {

    }

    @Override
    protected void createWorld() {
        setBackgroundColor(Color.GRAY);
        name = new AnimationController(Resources._instance.startScreen_name,new int[]{0, 1},new Vector2(), 256, 48, 0.11f, 10);
        worldRenderer.addObject(name);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        name.autoResize(0.5f, 0.9f, 0.5f, 0.1f, viewport);
    }
}
