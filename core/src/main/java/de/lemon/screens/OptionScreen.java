package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.render.AnimationController;
import de.lemon.ui.SLabel;

import java.util.EnumSet;

public class OptionScreen extends CoreScreen{
    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD);
    }

    @Override
    protected void createComponents() {}

    @Override
    protected void createWorld() {
        setBackgroundColor(Color.LIGHT_GRAY);
        SLabel title = new SLabel("Options", new Vector2(), Color.BLACK);
        addWorldObject(title, 0.5f, 1, 0.3f, 0.1f);
    }
}
