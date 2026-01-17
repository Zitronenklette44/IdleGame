package de.lemon.logic.interfaces;

import com.badlogic.gdx.utils.viewport.Viewport;

public interface LayoutItem {
    void applyLayout(Viewport viewport);
    void setRelLayout(float relX, float relY, float relWidth, float relHeight);

}
