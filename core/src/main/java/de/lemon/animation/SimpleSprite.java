package de.lemon.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class SimpleSprite extends Sprite{
    public SimpleSprite(Texture texture, int frameWidth, int frameHeight, boolean loop, Vector2 pos) {
        super(texture, frameWidth, frameHeight, 1f, loop, pos);
        autoPlay = false;
    }

    public void nextFrame(){
        if(!isFinished()){
            addStateTime(1);
        }
    }

    public void setFrame(int index) {
        setStateTime(index);
    }
}
