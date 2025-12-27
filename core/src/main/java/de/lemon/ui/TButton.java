package de.lemon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TButton extends TextButton {


    private Cell<TButton> cell;

    public TButton(String text, Skin skin) {
        super(text, skin);
    }

    public void bindCell(Cell<TButton> cell){
        this.cell = cell;
    }

    public void autoResize(float relWidth, float relHeight, Viewport viewport){
        if(cell == null) {
            System.out.println("Cell null");
            return;
        }
        cell.width(viewport.getWorldWidth() * relWidth).height(viewport.getWorldHeight() * relHeight);
        cell.getTable().layout();

        Gdx.app.postRunnable(() -> {
            int fontSize = FontCache.calculateFontSize(getWidth());
            TextButtonStyle style = new TextButtonStyle(getStyle());
            style.font = FontCache.getFont(fontSize);
            setStyle(style);

//            System.out.println("Resized to: " + cell.getActorWidth() + ", " + cell.getActorHeight() + " Viewport:" + viewport.getWorldWidth() + ", " + viewport.getWorldHeight());
        });

    }


}
