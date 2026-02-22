package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class AnimationController extends AnimatedSprite {

    private int delay;
    private final ArrayList<Animation<TextureRegion>> animationList = new ArrayList<>();
    private int currentAnimation = 0;

    @Override
    public AnimationController cpy() {
        AnimationController copy = new AnimationController(frameWidth, frameHeight);
        copy.pos.set(this.pos);
        copy.size.set(this.size);
        copy.rotation = this.rotation;
        copy.scale.set(this.scale);
        copy.origin.set(this.origin);

        copy.animationList.addAll(this.animationList);
        copy.delay = this.delay;

        return copy;
    }

    private AnimationController(int frameWidth, int frameHeight){
        super(frameWidth, frameHeight);
    }

    public AnimationController(Texture texture, int[] rows, Vector2 pos, int frameWidth, int frameHeight, float frameDuration, int delay){
        super(frameWidth, frameHeight);
        this.delay = delay;
        currentAnimation = MathUtils.random(rows.length - 1);

        setPos(pos);

        TextureRegion[][] regions = TextureRegion.split(texture, frameWidth, frameHeight);

        for (int row : rows) {
            if (row < 0 || row >= regions.length) {
                continue;
            }
            TextureRegion[] frames = regions[row];
            Animation<TextureRegion> a = new Animation<>(frameDuration, frames);
            a.setPlayMode(Animation.PlayMode.NORMAL);
            animationList.add(a);
        }
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
    }

    float currentDelay = 0;
    @Override
    public void onSpriteRender(Batch batch, float delta) {
        Animation<TextureRegion> anim = animationList.get(currentAnimation);

        if(anim.isAnimationFinished(stateTime)){
            stateTime = anim.getAnimationDuration();
            if(currentDelay < delay){
                currentDelay += delta;
                batch.draw(anim.getKeyFrames()[anim.getKeyFrames().length - 1], pos.x, pos.y, size.x, size.y);
                return;
            }else{
                setCurrentAnimation(MathUtils.random(animationList.size() - 1));
            }
        }

        TextureRegion frame = anim.getKeyFrame(stateTime, false);
        batch.draw(frame, pos.x, pos.y, size.x, size.y);
    }

    public void setCurrentAnimation(int currentAnimation) {
        stateTime = 0;
        currentDelay = 0;
        this.currentAnimation = currentAnimation;
    }
}
