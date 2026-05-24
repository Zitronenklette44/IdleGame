package de.lemon.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.Item;
import de.lemon.logic.render.ColoredSprite;
import de.lemon.logic.render.SimpleSprite;
import de.lemon.logic.render.Sprite;
import de.lemon.mechanics.Inventory;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;

public class InventorySprite extends Sprite {

    ArrayList<SimpleSprite> slots = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();
    int slotsPerRow = 8;
    ColoredSprite backgroundOverlay;

    public Item selected;

    public InventorySprite() {
        super(new Vector2(), new Vector2());
        backgroundOverlay = new ColoredSprite(Color.DARK_GRAY);
        createSlots(false);
        manualSize = false;
    }

    protected void createSlots(boolean selectable){
        slots.clear();
        items = Inventory._instance.getAllItems();
        for (int i = 0; i < items.size(); i++) {
            slots.add(new SimpleSprite("inventorySlot", 16, 16, true){
                @Override
                public void onClick(int button) {
                    super.onClick(button);
                    if(button == Input.Buttons.LEFT) clickedSlot(this);
                }
            });
        }
    }

    protected void clickedSlot(SimpleSprite slot){}

    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        super.setRelLayout(relX, relY, relWidth, relHeight);

        backgroundOverlay.setRelLayout(relX, relY, relWidth, relHeight);

        int itemCount = items.size();

        float slotSizeX = relWidth / slotsPerRow;
        float slotSizeY = slotSizeX;

        for (int i = 0; i < itemCount; i++) {

            int col = i % slotsPerRow;
            int row = i / slotsPerRow;

            float slotX = relX - relWidth / 2 + col * slotSizeX + slotSizeX / 2;
            float slotY = relY + relHeight / 2 + row * slotSizeY - slotSizeY / 1.25f;

            slots.get(i).setRelLayout(slotX, slotY, slotSizeX, slotSizeY);
            items.get(i).setRelLayout(slotX, slotY, slotSizeX, slotSizeY);
        }
    }



    @Override
    public void onSpriteRender(Batch batch, float delta) {
        if(!isVisible) return;
//        DebugLogger.printInfo("inv pos:" + pos + " size:" + size);
        super.onSpriteRender(batch, delta);
        backgroundOverlay.onSpriteRender(batch, delta);
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).onSpriteRender(batch, delta);
            items.get(i).onSpriteRender(batch, delta);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        backgroundOverlay.update(delta);
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).update(delta);
            items.get(i).update(delta);
        }
    }

    @Override
    public void applyLayout(Viewport viewport) {
        super.applyLayout(viewport);
        backgroundOverlay.applyLayout(viewport);
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).applyLayout(viewport);
            items.get(i).applyLayout(viewport);
        }
    }

    @Override
    public void finaliseLayout() {
        super.finaliseLayout();
        backgroundOverlay.finaliseLayout();
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).finaliseLayout();
            items.get(i).finaliseLayout();
        }
    }

    @Override
    public Sprite cpy() {
        return null;
    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        super.onDebug(shapeRenderer, delta);
        shapeRenderer.setColor(Color.VIOLET);
        shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
        backgroundOverlay.onDebug(shapeRenderer, delta);
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).onDebug(shapeRenderer, delta);
            items.get(i).onDebug(shapeRenderer, delta);
        }
    }

    @Override
    public void onClickChildren(float x, float y, int button) {
        super.onClickChildren(x, y, button);
        for(SimpleSprite s : slots){
            if(s.contains(x, y)) {
//                DebugLogger.printInfo("slot pressed");
                s.onClick(button);
                break;
            }
        }
    }

    @Override
    public boolean contains(float x, float y) {
        Rectangle bound = new Rectangle(pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
//        DebugLogger.printInfo("contains: " + bound.contains(x, y));
        return bound.contains(x,y);
    }

    public void refreshItems(Viewport viewport){
        createSlots(slots.get(0).isClickable());
        setRelLayout(relPos.x, relPos.y, relSize.x, relSize.y);
        applyLayout(viewport);
        finaliseLayout();
    }
}
