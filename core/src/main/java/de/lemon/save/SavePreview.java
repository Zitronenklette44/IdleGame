package de.lemon.save;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameObject;
import de.lemon.core.GameState;
import de.lemon.logic.interfaces.Clickable;
import de.lemon.logic.render.ColoredSprite;
import de.lemon.logic.render.NineSprite;
import de.lemon.ui.FontCache;

import java.text.DateFormat;
import java.util.Date;

public class SavePreview extends GameObject implements Clickable {

    private GameState gameState;
    private Integer id;
    private NineSprite bg;
    private Runnable action;
    private ColoredSprite selectedOverlay;
    private boolean valid = true;

    public SavePreview(GameState gameState, Integer id) {
        super(new Vector2(), new Vector2());
        if(gameState.getName() == null){
            gameState.setName("Empty");
            valid = false;
        }
        this.gameState = gameState;
        this.id = id;
        bg = new NineSprite("savePreviewBg", 16);
        selectedOverlay = new ColoredSprite(new Color(1, 1, 1, .5f));
        selectedOverlay.setVisible(false);
    }

    public SavePreview() {
        super(new Vector2(), new Vector2());
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
        bg.onSpriteRender(batch, delta);
        selectedOverlay.onSpriteRender(batch, delta);
        BitmapFont font = FontCache.getFont(24, Color.WHITE);

        Vector2 size = new Vector2(bg.getSize());
        Vector2 pos = new Vector2(bg.getPos().x - size.x / 2, bg.getPos().y - size.y / 2);
        float padding = 10f;

        String title = gameState.getName();
        font.setColor(Color.GOLD);
        font.draw(batch, title, pos.x + size.x / 2 + padding, pos.y + size.y * 1.45f - padding, size.x - padding * 2, Align.center, true);

        font = FontCache.getFont(18, Color.WHITE);
        long playtime = gameState.getPlaytime();

        String lastPlayed = DateFormat.getInstance().format(new Date(gameState.getLastPlayed()));
        String formattedPlaytime = formatePlaytime(playtime);

        font.draw(batch, "Playtime:\n" + formattedPlaytime, pos.x + size.x / 2 + padding, pos.y + size.y * 1.2f - padding, size.x - padding * 2, Align.left, true);
        font.draw(batch, "Last Played:\n" + lastPlayed, pos.x + size.x / 2 + padding, pos.y + size.y * 1.05f - padding, size.x - padding * 2, Align.left, true);

    }

    private String formatePlaytime(long time){
        time = time / 1000;//into millis
        time = time / 60;//into seconds
        long minutes = time % 60;
        time = time / 60;
        long hours = time;
        return String.format("%02d:%02d h", hours, minutes);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bg.update(delta);
        selectedOverlay.update(delta);
    }

    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        super.setRelLayout(relX, relY, relWidth, relHeight);
        bg.setRelLayout(relX, relY, relWidth, relHeight);
        selectedOverlay.setRelLayout(relX, relY, relWidth, relHeight);
    }

    @Override
    public void applyLayout(Viewport viewport) {
        super.applyLayout(viewport);
        bg.applyLayout(viewport);
        selectedOverlay.applyLayout(viewport);
    }

    @Override
    public void finaliseLayout() {
        super.finaliseLayout();
        bg.finaliseLayout();
        selectedOverlay.finaliseLayout();
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public boolean contains(float x, float y) {
        Rectangle rec = new Rectangle(pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
        return rec.contains(x, y);
    }

    @Override
    public void onClick(int button) {
        action.run();
    }

    @Override
    public void onClickChildren(float x, float y, int button) {

    }

    public int getId() {
        return id;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void setSelected(boolean selected) {
        selectedOverlay.setVisible(selected);
    }

    public boolean isValid() {
        return valid;
    }
}
