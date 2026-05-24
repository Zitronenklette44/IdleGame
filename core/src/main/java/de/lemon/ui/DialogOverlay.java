package de.lemon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.render.Sprite;
import de.lemon.mechanics.dialog.Dialog;

public class DialogOverlay extends Sprite {

    Dialog dialog;

    public DialogOverlay(Dialog dialog) {
        super(new Vector2(), new Vector2());
        this.dialog = dialog;
        setRelLayout(.5f, .3f, .8f, .3f);
    }

    @Override
    public Sprite cpy() {
        return null;
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);

        BitmapFont font = FontCache.getFont(12, Color.WHITE);

    }
}
