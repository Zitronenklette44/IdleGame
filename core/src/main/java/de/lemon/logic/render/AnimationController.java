package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.lemon.core.Resources;

import java.util.ArrayList;

public class AnimationController extends AnimatedSprite {

    private int[] rows;
    private int delay;
    private final ArrayList<Animation<TextureRegion>> animationList = new ArrayList<>();
    private int currentAnimation = 0;
    /**
     * Creates a deep copy of this AnimationController.
     * Copies transformation state and animation list reference data.
     *
     * @return copied AnimationController instance
     */
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
        copy.textureName = this.textureName;

        return copy;
    }

    private AnimationController(int frameWidth, int frameHeight){
        super(frameWidth, frameHeight);
    }

    public AnimationController(String textureName, int[] rows, int frameWidth, int frameHeight, float frameDuration, int delay){
        super(frameWidth, frameHeight);
        this.rows = rows;
        this.delay = delay;
        this.textureName = textureName;
        currentAnimation = MathUtils.random(rows.length - 1);

        setPos(pos);
        Texture texture = Resources._instance.getTexture(textureName);
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
    /**
     * Updates the internal animation state time.
     *
     * @param delta time since last frame
     */
    @Override
    public void update(float delta) {
        stateTime += delta;
    }

    float currentDelay = 0;
    /**
     * Renders the current animation frame.
     * Handles animation looping, delay at end frame, and switching animations.
     *
     * @param batch rendering batch
     * @param delta time since last frame
     */
    @Override
    public void onSpriteRender(Batch batch, float delta) {
        if(!isVisible) return;
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
    /**
     * Switches to a different animation and resets timing state.
     *
     * @param currentAnimation index of the animation in the list
     */
    public void setCurrentAnimation(int currentAnimation) {
        stateTime = 0;
        currentDelay = 0;
        this.currentAnimation = currentAnimation;
    }
    /**
     * @return delay time applied after animation finishes
     */
    public int getDelay() {
        return delay;
    }
    /**
     * @return row indices used to build animations from texture sheet
     */
    public int[] getRows() {
        return rows;
    }
}
