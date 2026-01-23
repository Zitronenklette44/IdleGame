package de.lemon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameObject;
import de.lemon.logic.interfaces.Clickable;
import de.lemon.logic.interfaces.Hoverable;

public class STextButton extends GameObject implements Clickable, Hoverable {

    private String text;
    private final NinePatch sprite;
    private Color textColor = Color.WHITE;
    private final GlyphLayout layout = new GlyphLayout();
    private BitmapFont font;
    private final Vector2 textPos = new Vector2();
    private int fontAddition = 0;
    private int maxFontAddition = 5;


    public STextButton(String text, NinePatch sprite, Vector2 pos, Vector2 size) {
        super(pos, size);
        this.text = text;
        this.sprite = sprite;
        recalculateFont();
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        sprite.draw(batch, pos.x, pos.y, size.x, size.y);

        font.draw(batch, layout, textPos.x, textPos.y);
    }


    public STextButton(String text, NinePatch sprite){
        this(text, sprite, new Vector2(), new Vector2());
    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public void onEnter() {
        fontAddition = maxFontAddition;
        recalculateFont();
    }

    @Override
    public void onExit() {
        fontAddition = 0;
        recalculateFont();
    }

    @Override
    public boolean contains(float x, float y) {
        return x >= pos.x && x <= pos.x + size.x &&
            y >= pos.y && y <= pos.y + size.y;
    }

    @Override
    public void onClick(int button) {}

    public void setText(String text) {
        this.text = text;
        recalculateFont();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        recalculateFont();
    }

    @Override
    public void applyLayout(Viewport viewport) {
        Vector2 newSize = new Vector2(viewport.getWorldWidth() * relSize.x, viewport.getWorldHeight() * relSize.y);
        Vector2 newPos = new Vector2(viewport.getWorldWidth() * relPos.x - newSize.x / 2, viewport.getWorldHeight() * relPos.y - newSize.y / 2);

        if(!newSize.equals(size) || !newPos.equals(pos)) {
            desiredSize.set(newSize);
            pos.set(newPos);
            recalculateFont();
        }
    }

    private void recalculateFont(){
        int fontSize = FontCache.calculateFontSize(size.x - fontAddition * 10) + fontAddition;
        System.out.println("FontAddition: " + fontAddition + " width: " + size.x);
        font = FontCache.getFont(fontSize, textColor);

        float paddingX = size.x * 0.1f;

        float textWidth = size.x - paddingX * 2f;

        layout.setText(font, text, textColor, textWidth, Align.center, true );

        textPos.x = pos.x + paddingX;
        textPos.y = pos.y + (size.y + layout.height) / 2f;
    }

    @Override
    public void applyMaxSize() {
        super.applyMaxSize();
        recalculateFont();
    }

    public void setMaxFontAddition(int maxFontAddition) {
        if(maxFontAddition < 0) return;
        this.maxFontAddition = maxFontAddition;
    }
}
