package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.Resources;
import de.lemon.logic.interfaces.Clickable;

public class AnimatedSprite extends Sprite implements Clickable {

    protected Animation<TextureRegion> animation;
    protected float stateTime;

    protected boolean autoPlay;
    private boolean loop;
    private int row; // needed to save Object
    protected int frameWidth;
    protected int frameHeight;
    private float frameDuration; //needed to save Object
    protected String textureName;

    protected AnimatedSprite(int frameWidth, int frameHeight){
        super(new Vector2(), new Vector2());
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public AnimatedSprite(String textureName, int row, int frameWidth, int frameHeight, float frameDuration, boolean loop, Vector2 pos) {
        super(pos, new Vector2(frameWidth, frameHeight));
        this.textureName = textureName;
        this.row = row;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameDuration = frameDuration;

        this.autoPlay = true;
        this.loop = loop;
        Texture texture = Resources._instance.getTexture(textureName);

        createAnimation(splitTexture(texture, row), frameDuration, loop);

        stateTime = 0f;
    }
    public AnimatedSprite(String textureName, int frameWidth, int frameHeight, float frameDuration, boolean loop, Vector2 pos){
        this(textureName, 0, frameWidth, frameHeight, frameDuration, loop, pos);
    }
    public AnimatedSprite(String textureName, int frameWidth, int frameHeight, boolean loop, Vector2 pos){
        this(textureName, 0, frameWidth, frameHeight, 0, loop, pos);
    }
    /**
     * Splits a texture into frames based on frameWidth and frameHeight.
     *
     * @param texture source texture containing animation sheet
     * @param row row index to extract frames from
     * @return array of TextureRegion frames for the given row
     */
    protected TextureRegion[] splitTexture(Texture texture, int row){
        TextureRegion[][] regions = TextureRegion.split(texture, frameWidth, frameHeight);
        return regions[row];
    }
    /**
     * Creates an Animation from given frames and frame duration.
     * If frameDuration <= 0, a default duration is calculated automatically.
     *
     * @param frames animation frames
     * @param frameDuration time per frame in seconds
     * @param loop whether the animation should loop
     */
    protected void createAnimation(TextureRegion[] frames, float frameDuration, boolean loop){
        if(frameDuration <= 0){
            frameDuration = 1f / frames.length;
        }

        animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
    }
    /**
     * Updates the animation state.
     * Advances state time if autoPlay is enabled.
     *
     * @param delta time since last frame
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        if (autoPlay) {
            stateTime += delta;
        }
    }
    /**
     * Renders the current animation frame.
     *
     * @param batch rendering batch
     * @param delta time since last frame
     */
    @Override
    public void onSpriteRender(Batch batch, float delta) {
        TextureRegion frame = animation.getKeyFrame(stateTime, loop);

        batch.draw(frame, pos.x, pos.y,origin.x, origin.y, size.x, size.y, scale.x, scale.y, rotation);
        //System.out.println("drawn ->" + pos.toString() + " size: " + size.toString());
    }
    /**
     * Resets the animation playback to the start.
     */

    public void reset() {
        stateTime = 0f;
    }
    /**
     * @return true if the animation has reached its end (only relevant in non-loop mode)
     */
    public boolean isFinished() {
        return animation.isAnimationFinished(stateTime);
    }
    /**
     * Changes the play mode of the animation (e.g. LOOP or NORMAL).
     *
     * @param playMode new animation play mode
     */
    public void setPlayMode(Animation.PlayMode playMode){
        animation.setPlayMode(playMode);
    }
    /**
     * Enables or disables automatic animation playback.
     *
     * @param autoPlay true to let the animation advance automatically
     */
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }
    /**
     * Enables or disables looping and updates the internal play mode accordingly.
     *
     * @param loop true for looping animation
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
    }
    /**
     * Adds time to the current animation state time.
     * The value is clamped to the animation duration.
     *
     * @param time time to add
     */
    public void addStateTime(float time){
        float newTime = stateTime + time;
        stateTime = MathUtils.clamp(
            newTime,
            0f,
            animation.getAnimationDuration()
        );
    }
    /**
     * Sets the current animation state time.
     *
     * @param stateTime new state time
     */
    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }
    /**
     * Applies viewport-based layout scaling and positioning.
     *
     * @param viewport current viewport used for world scaling
     */
    @Override
    public void applyLayout(Viewport viewport) {
        scaleToContain(viewport.getWorldWidth() * relSize.x, viewport.getWorldHeight() * relSize.y);

        pos.set(viewport.getWorldWidth() * relPos.x - size.x / 2,
            viewport.getWorldHeight() * relPos.y - size.y / 2);

    }
    /**
     * Scales the sprite so it fits within the target size while preserving aspect ratio.
     *
     * @param targetSize target size vector
     */
    public void scaleToFit(Vector2 targetSize) {
        //System.out.println(targetSize.toString());
        float scaleX = targetSize.x / frameWidth;
        float scaleY = targetSize.y / frameHeight;

        float scale = Math.max(scaleX, scaleY);

        float scaledWidth  = frameWidth * scale;
        float scaledHeight = frameHeight * scale;

        //System.out.println("new Size->" + scaledWidth + ", " + scaledHeight);
        size.set(scaledWidth, scaledHeight);
    }
    /**
     * Scales the sprite so it fits within the given width and height.
     *
     * @param width target width
     * @param height target height
     */
    public void scaleToFit(float width, float height){
        scaleToFit(new Vector2(width, height));
    }
    /**
     * Scales the sprite so it completely fits inside the target area (no overflow).
     *
     * @param targetW target width
     * @param targetH target height
     */
    public void scaleToContain(float targetW, float targetH) {
        float scaleX = targetW / frameWidth;
        float scaleY = targetH / frameHeight;
        float scale  = Math.min(scaleX, scaleY);

        size.set(frameWidth * scale, frameHeight * scale);
    }
    /**
     * Scales the sprite so it fits inside the given size vector.
     *
     * @param size target size
     */
    public void scaleToContain(Vector2 size){
        scaleToContain(size.x, size.y);
    }
    /**
     * Applies a uniform scale factor based on the original frame size.
     *
     * @param factor scaling factor
     */
    public void scale(float factor){
        size = new Vector2(frameWidth * factor, frameHeight * factor);
    }
    /**
     * Creates a shallow copy of this AnimatedSprite.
     * Note: animation reference is shared.
     *
     * @return copied AnimatedSprite instance
     */
    @Override
    public AnimatedSprite cpy() {
        AnimatedSprite copy = new AnimatedSprite(frameWidth, frameHeight);

        copy.pos.set(this.pos);
        copy.size.set(this.size);

        copy.rotation = this.rotation;
        copy.scale.set(this.scale);
        copy.origin.set(this.origin);

        copy.animation = this.animation;
        copy.autoPlay = this.autoPlay;
        copy.loop = this.loop;
        copy.clickable = this.clickable;
        copy.textureName = this.textureName;

        return copy;
    }
    /**
     * @return height of a single animation frame
     */
    public int getFrameHeight() {
        return frameHeight;
    }
    /**
     * @return width of a single animation frame
     */
    public int getFrameWidth() {
        return frameWidth;
    }
    /**
     * @return texture row index used for this animation
     */
    public int getRow() {
        return row;
    }
    /**
     * @return texture name used for this animation
     */
    public String getTextureName() {
        return textureName;
    }
    /**
     * @return true if animation is looping
     */
    public boolean getLoop() {
        return loop;
    }
    /**
     * @return duration of one frame in seconds
     */
    public float getFrameDuration() {
        return frameDuration;
    }
}
