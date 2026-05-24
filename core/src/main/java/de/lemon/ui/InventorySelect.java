package de.lemon.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.lemon.logic.render.SimpleSprite;

public class InventorySelect extends InventorySprite{
    Runnable action;

    public InventorySelect(Runnable onFound){
        action = onFound;
        clickable = true;
    }

    @Override
    public void onTouchDown(float worldX, float worldY, int screenX, int screenY, int button) {
        super.onTouchDown(worldX, worldY, screenX, screenY, button);
        if(!contains(worldX, worldY)) setVisible(false);
    }

    @Override
    protected void createSlots(boolean selectable) {
        super.createSlots(true);
        for(SimpleSprite s : slots) s.setClickable(true);
    }

    @Override
    protected void clickedSlot(SimpleSprite slot) {
        super.clickedSlot(slot);
        int index = slots.indexOf(slot);
        selected = items.get(index).cpy();
        action.run();
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        super.onSpriteRender(batch, delta);
    }
}
