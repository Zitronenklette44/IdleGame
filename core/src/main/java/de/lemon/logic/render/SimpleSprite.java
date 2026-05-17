package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class SimpleSprite extends AnimatedSprite {
    public SimpleSprite(String textureName, int frameWidth, int frameHeight, boolean loop, Vector2 pos) {
        super(textureName, frameWidth, frameHeight, 1f, loop, pos);
        autoPlay = false;
    }
    /**
     * Advances the animation by one step if it is not finished.
     * Increases internal state time by 1 frame.
     */
    public void nextFrame(){
        if(!isFinished()){
            addStateTime(1);
        }
    }
    /**
     * Sets the animation to a specific frame index.
     *
     * @param index frame index to display
     */
    public void setFrame(int index) {
        setStateTime(index);
    }
}
