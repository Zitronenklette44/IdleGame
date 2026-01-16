package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.interfaces.Clickable;

public class AnimatedSprite extends Sprite implements Clickable {

    protected Animation<TextureRegion> animation;
    protected float stateTime;

    protected boolean autoPlay;
    protected boolean loop;
    protected int frameWidth;
    protected int frameHeight;

    protected AnimatedSprite(int frameWidth, int frameHeight){
        super(new Vector2(), new Vector2());
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public AnimatedSprite(Texture texture, int row, int frameWidth, int frameHeight, float frameDuration, boolean loop, Vector2 pos) {
        super(pos, new Vector2(frameWidth, frameHeight));
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        this.autoPlay = true;
        this.loop = loop;

        createAnimation(splitTexture(texture, row), frameDuration, loop);

        stateTime = 0f;
    }
    public AnimatedSprite(Texture texture, int frameWidth, int frameHeight, float frameDuration, boolean loop, Vector2 pos){
        this(texture, 0, frameWidth, frameHeight, frameDuration, loop, pos);
    }
    public AnimatedSprite(Texture texture, int frameWidth, int frameHeight, boolean loop, Vector2 pos){
        this(texture, 0, frameWidth, frameHeight, 0, loop, pos);
    }

    protected TextureRegion[] splitTexture(Texture texture, int row){
        TextureRegion[][] regions = TextureRegion.split(texture, frameWidth, frameHeight);
        return regions[row];
    }

    protected void createAnimation(TextureRegion[] frames, float frameDuration, boolean loop){
        if(frameDuration <= 0){
            frameDuration = 1f / frames.length;
        }

        animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (autoPlay) {
            stateTime += delta;
        }
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        TextureRegion frame = animation.getKeyFrame(stateTime, loop);

        batch.draw(frame, pos.x, pos.y,origin.x, origin.y, size.x, size.y, scale.x, scale.y, rotation);
        //System.out.println("drawn ->" + pos.toString() + " size: " + size.toString());
    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
            if(shapeRenderer.getCurrentType() != ShapeRenderer.ShapeType.Line) shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(pos.x, pos.y, origin.x, origin.y, size.x, size.y, scale.x, scale.y, rotation);
    }

    public void reset() {
        stateTime = 0f;
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(stateTime);
    }

    public void setPlayMode(Animation.PlayMode playMode){
        animation.setPlayMode(playMode);
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
    }

    public void addStateTime(float time){
        float newTime = stateTime + time;
        stateTime = MathUtils.clamp(
            newTime,
            0f,
            animation.getAnimationDuration()
        );
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void autoResize(float relX, float relY, float relWidth, float relHeight, Viewport viewport){
        scaleToContain(viewport.getWorldWidth() * relWidth, viewport.getWorldHeight() * relHeight);

        pos.set(viewport.getWorldWidth() * relX - size.x / 2,
            viewport.getWorldHeight() * relY - size.y / 2);

    }

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

    public void scaleToFit(float width, float height){
        scaleToFit(new Vector2(width, height));
    }

    public void scaleToContain(float targetW, float targetH) {
        float scaleX = targetW / frameWidth;
        float scaleY = targetH / frameHeight;
        float scale  = Math.min(scaleX, scaleY);

        size.set(frameWidth * scale, frameHeight * scale);
    }

    public void scaleToContain(Vector2 size){
        scaleToContain(size.x, size.y);
    }

    public void scale(float factor){
        size = new Vector2(frameWidth * factor, frameHeight * factor);
    }
}
