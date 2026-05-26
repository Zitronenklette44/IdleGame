package de.lemon.ui;

import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameObject;
import de.lemon.logic.interfaces.LayoutItem;

import java.util.ArrayList;

public class LayoutManager {

    ArrayList<LayoutItem> items = new ArrayList<>();

    public void resize(Viewport viewport){
        for(LayoutItem i : items){
            i.applyLayout(viewport);
            i.finaliseLayout();
        }
    }

    public void add(LayoutItem item){
        items.add(item);
    }

    public void remove(LayoutItem item) {
        items.remove(item);
    }
}
