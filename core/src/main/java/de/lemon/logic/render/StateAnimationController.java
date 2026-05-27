package de.lemon.logic.render;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StateAnimationController extends AnimationController{
    public StateAnimationController(String textureName, int[] rows, int frameWidth, int frameHeight, float frameDuration) {
        super(textureName, rows, frameWidth, frameHeight, frameDuration, 0);
        for(Animation<TextureRegion> a : animationList){
            a.setPlayMode(Animation.PlayMode.LOOP);
        }
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
        Animation<TextureRegion> anim = animationList.get(currentAnimation);
        TextureRegion frame = anim.getKeyFrame(stateTime, false);
        batch.draw(frame, pos.x, pos.y, size.x, size.y);
    }

    public void setAnimationToState(int state){
        currentAnimation = Math.clamp(state, 0, animationList.size() - 1);
    }

    @Override
    protected int nextAnimation() {return currentAnimation;}
}
