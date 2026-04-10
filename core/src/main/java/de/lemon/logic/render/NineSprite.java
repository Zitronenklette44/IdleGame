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

    public NineSprite(String textureName, int left, int right, int top, int bottom, Vector2 pos, Vector2 size) {
        super(pos, size);
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.textureName = textureName;
        this.sprite = new NinePatch(Resources._instance.getTexture(textureName), left, right, top, bottom);
    }

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

    public void setText(String text, Color textColor) {
        this.text = text;
        this.textColor = textColor;
    }

    public void setText(String text) {
        setText(text, Color.WHITE);
    }

    @Override
    public void applyLayout(Viewport viewport) {
        size.set(viewport.getWorldWidth() * relSize.x, viewport.getWorldHeight() * relSize.y);
        pos.set(viewport.getWorldWidth() * relPos.x - size.x / 2f, viewport.getWorldHeight() * relPos.y - size.y / 2f);
    }

    @Override
    public Sprite cpy() {
        NineSprite copy = new NineSprite(textureName, left, right, top, bottom, pos, size);
        copy.text = text;
        copy.textColor = textColor;
        return copy;
    }

    public int left(){
        return left;
    }

    public int right() {
        return right;
    }

    public int top() {
        return top;
    }

    public int bottom() {
        return bottom;
    }

    public String getTextureName() {
        return textureName;
    }
}
