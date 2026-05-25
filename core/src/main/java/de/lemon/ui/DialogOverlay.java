package de.lemon.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.logic.render.ColoredSprite;
import de.lemon.logic.render.NineSprite;
import de.lemon.logic.render.Sprite;
import de.lemon.mechanics.dialog.DialogSystem;
import de.lemon.utilities.DebugLogger;

public class DialogOverlay extends Sprite {

    NineSprite border;
    NineSprite profile;
    ColoredSprite background;
    AnimatedSprite spaceBar;
    private String name;
    private String title;
    private String text;
    AnimatedSprite speaker;

    public DialogOverlay() {
        super(new Vector2(), new Vector2());
        createSprites();
        manualSize = false;
        setClickable(true);
//        setVisible(false);
    }

    public void showName(String name){
        this.name = name;
    }
    public void showText(String text){
        this.text = text;
    }
    public void showTitle(String title) {
        this.title = title;
    }
    public void showPortrait(String textureName, int frameWidth, int frameHeight){
        speaker = new AnimatedSprite(textureName, frameWidth, frameHeight, 0.3f, true);
        speaker.setRelLayout(relPos.x - relSize.x / 2.6f, relPos.y - relSize.y / 6.5f, relSize.x / 3.5f, relSize.x / 3.5f);
    }

    private void createSprites(){
        border = new NineSprite("dialogBorder",16, 16, 16, 16);
        profile = new NineSprite("dialogProfile",16, 16, 16, 16);
        background = new ColoredSprite(new Color(0, 0, 0, .7f));
        spaceBar = new AnimatedSprite("spaceBar", 64, 32, 0.2f, true);
    }

    @Override
    public Sprite cpy() {
        return null;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        background.update(delta);
        border.update(delta);
        profile.update(delta);
        spaceBar.update(delta);
        if(speaker != null) speaker.update(delta);
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
        background.onSpriteRender(batch, delta);
        border.onSpriteRender(batch, delta);
        if(speaker != null) speaker.onSpriteRender(batch, delta);
        profile.onSpriteRender(batch, delta);
        if(DialogSystem._instance.isAllVisible()) spaceBar.onSpriteRender(batch, delta);

        BitmapFont font = FontCache.getFont(18, Color.WHITE);
        float padding = 20f;

//        float boxX = getPos().x - getSize().x / 2;
        float boxY = getPos().y - getSize().y / 2;
        float boxWidth = getSize().x;
        float boxHeight = getSize().y;

        float profileSize = profile.getSize().x;
        float profileX = profile.getPos().x;

        float textX = profileX + profileSize + padding;
        float textY = boxY + profileSize + padding;

        float textWidth = boxWidth - profileSize - padding * 2;

        GlyphLayout layout = new GlyphLayout();

        assert font != null;
        // NAME (oben links beim Portrait)
        if(name != null) {
            float nameX = profileX;
            float nameY = boxY + boxHeight - padding;

            layout.setText(font, name);
            font.draw(batch, layout, nameX, nameY);
        }

        if(title != null){
            float titleX = profileX + profileSize + padding;
            float titleY = boxY + boxHeight - padding;

            layout.setText(font, title);
            font.draw(batch, layout, titleX, titleY);
        }

        if(text != null) {
            layout.setText(font, text, Color.WHITE, textWidth, Align.topLeft, true);

            font.draw(batch, layout, textX, textY - 20);
        }

    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
//        super.onDebug(shapeRenderer, delta);
        border.onDebug(shapeRenderer, delta);
        profile.onDebug(shapeRenderer, delta);
        background.onDebug(shapeRenderer, delta);
        spaceBar.onDebug(shapeRenderer, delta);
        if(speaker != null) speaker.onDebug(shapeRenderer, delta);
    }

    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        super.setRelLayout(relX, relY, relWidth, relHeight);
        border.setRelLayout(relX, relY, relWidth, relHeight);
        profile.setRelLayout(relX - relWidth / 2.6f, relY - relHeight / 4, relWidth / 5, relWidth / 5);
        background.setRelLayout(relX, relY, relWidth, relHeight);
        spaceBar.setRelLayout(relX + relWidth / 2.2f, relY - relHeight / 2.4f, relWidth / 10, relHeight / 8);
        if(speaker != null) speaker.setRelLayout(relX - relWidth / 2.6f, relY - relHeight / 6.5f, relWidth / 3.5f, relWidth / 3.5f);
    }

    @Override
    public void applyLayout(Viewport viewport) {
        super.applyLayout(viewport);
        border.applyLayout(viewport);
        profile.applyLayout(viewport);
        profile.setSize(new Vector2(profile.getSize().x, profile.getSize().x));
        background.applyLayout(viewport);
        spaceBar.applyLayout(viewport);
        if(speaker != null){
            speaker.applyLayout(viewport);
            DebugLogger.printInfo("applied");
        }
    }

    @Override
    public void finaliseLayout() {
        super.finaliseLayout();
        border.finaliseLayout();
        profile.finaliseLayout();
        background.finaliseLayout();
        spaceBar.finaliseLayout();
        if(speaker != null) {
            speaker.finaliseLayout();
            DebugLogger.printInfo("finalised");
        }
    }

    @Override
    public void onKeyDown(int keycode) {
        super.onKeyDown(keycode);
        if(keycode == Input.Keys.SPACE && DialogSystem._instance.isActive()) DialogSystem._instance.handleInput();
    }

    @Override
    public void onClick(int button) {
        super.onClick(button);
        if(DialogSystem._instance.isActive()) DialogSystem._instance.handleInput();
    }

    @Override
    public boolean contains(float x, float y) {
        Rectangle rec = new Rectangle(pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
        return rec.contains(x,y);
    }
}
