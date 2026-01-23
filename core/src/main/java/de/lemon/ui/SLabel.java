package de.lemon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameObject;

public class SLabel extends GameObject {

    private String text;
    private Color textColor;
    private final GlyphLayout layout = new GlyphLayout();
    private BitmapFont font;
    private final Vector2 textPos = new Vector2();

    public SLabel(String text, Vector2 pos, Color textColor){
        super(pos, new Vector2());
        this.text = text;
        this.textColor = textColor;
        recalculateFont();
    }

    public SLabel(String text, Vector2 pos) {
        this(text, pos, Color.WHITE);
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
        font.draw(batch, layout, textPos.x, textPos.y);
    }

    public void setText(String text) {
        this.text = text;
        recalculateFont();
        size.set(layout.width, layout.height);
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public void applyLayout(Viewport viewport) {
        Vector2 newSize = new Vector2(viewport.getWorldWidth() * relSize.x, viewport.getWorldHeight() * relSize.y);
        Vector2 newPos = new Vector2(viewport.getWorldWidth() * relPos.x - newSize.x / 2, viewport.getWorldHeight() * relPos.y - newSize.y);

        if(!newSize.equals(size) || !newPos.equals(pos)) {
            desiredSize.set(newSize);
            pos.set(newPos);
            recalculateFont();
        }
    }

    private void recalculateFont(){
        int fontSize = FontCache.calculateFontSize(size.x);
        font = FontCache.getFont(fontSize, textColor);

        float paddingX = size.x * 0.1f;

        float textWidth = size.x - paddingX * 2f;

        layout.setText(font, text, textColor, textWidth, Align.center, true );

        textPos.x = pos.x + paddingX;
        textPos.y = pos.y + (size.y + layout.height) / 2f;
    }
}
