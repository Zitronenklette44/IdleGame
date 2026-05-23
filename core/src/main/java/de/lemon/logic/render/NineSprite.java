package de.lemon.logic.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.Resources;
import de.lemon.ui.FontCache;

public class NineSprite extends Sprite {
    private final NinePatch sprite;
    private String text;
    private Color textColor;

    private final String textureName;
    private final int left,right,top,bottom;

    public NineSprite(String textureName, int left, int right, int top, int bottom, Vector2 size) {
        super(new Vector2(), size);
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.textureName = textureName;
        this.sprite = new NinePatch(Resources._instance.getTexture(textureName), left, right, top, bottom);
    }
    /**
     * Renders the NinePatch sprite and optional centered text.
     *
     * @param batch batch used for rendering
     * @param delta time since last frame
     */
    @Override
    public void onSpriteRender(Batch batch, float delta) {
        sprite.draw(batch, pos.x, pos.y, origin.x, origin.y, size.x, size.y, scale.x, scale.y, rotation);
        if(text != null && !text.isEmpty()){
            BitmapFont font = FontCache.getFont((int) (size.y * 0.4f), textColor);

            GlyphLayout layout = new GlyphLayout();
            layout.setText(font, text);

            float textX = pos.x + (size.x - layout.width) / 2f;
            float textY = pos.y + (size.y + layout.height) / 2f;

            assert font != null;
            font.draw(batch, layout, textX, textY);
        }
    }
    /**
     * Sets the displayed text and its color.
     *
     * @param text text to display on the sprite
     * @param textColor color of the text
     */
    public void setText(String text, Color textColor) {
        this.text = text;
        this.textColor = textColor;
    }
    /**
     * Sets the displayed text with default white color.
     *
     * @param text text to display
     */
    public void setText(String text) {
        setText(text, Color.WHITE);
    }
    /**
     * Applies viewport-based layout scaling and positioning.
     *
     * @param viewport current viewport used for world scaling
     */
    @Override
    public void applyLayout(Viewport viewport) {
        size.set(viewport.getWorldWidth() * relSize.x, viewport.getWorldHeight() * relSize.y);
        pos.set(viewport.getWorldWidth() * relPos.x - size.x / 2f, viewport.getWorldHeight() * relPos.y - size.y / 2f);
    }
    /**
     * Creates a deep copy of this sprite including visual configuration and text state.
     *
     * @return copied NineSprite instance
     */
    @Override
    public Sprite cpy() {
        NineSprite copy = new NineSprite(textureName, left, right, top, bottom, size);
        copy.text = text;
        copy.textColor = textColor;
        return copy;
    }
    /**
     * @return left border size of the NinePatch
     */
    public int left(){
        return left;
    }
    /**
     * @return right border size of the NinePatch
     */
    public int right() {
        return right;
    }
    /**
     * @return top border size of the NinePatch
     */
    public int top() {
        return top;
    }
    /**
     * @return bottom border size of the NinePatch
     */
    public int bottom() {
        return bottom;
    }
    /**
     * @return texture name used to build this NinePatch
     */
    public String getTextureName() {
        return textureName;
    }
}
