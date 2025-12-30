package de.lemon.save;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import de.lemon.core.GameState;
import de.lemon.core.Resources;

import java.text.DateFormat;
import java.util.Date;

public class SavePreview extends Table {

    private GameState gameState;
    private final int id;

    private Label left;
    private Label middle;
    private Label right;

    private boolean selected = false;
    private final Drawable selectedBg;
    private final Drawable normalBg;

    public SavePreview(GameState file, int id){
        this.gameState = file;
        this.id = id;
        createPreview();

        normalBg = Resources._instance.skin.newDrawable(
            "white",
            0.8f, 0.8f, 0.8f, 1f
        );

        selectedBg = Resources._instance.skin.newDrawable(
            "white",
            0.4f, 0.4f, 0.4f, 1f
        );

        setBackground(normalBg);
        setTouchable(Touchable.enabled);

        gameState = null;
    }

    private void createPreview() {
        left = new Label(gameState.getName(), Resources._instance.skin);
        middle = new Label(gameState.getPlaytime() + "", Resources._instance.skin);
        String date = DateFormat.getInstance().format(new Date(gameState.getLastPlayed()));

        right = new Label(date, Resources._instance.skin);
        add(left).expandX().left();
        add(middle).width(100).center();
        add(right).width(150).right();
        pad(10);
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        setBackground(selected ? selectedBg : normalBg);
        this.selected = selected;
    }

    public int getId() {
        return id;
    }
}
