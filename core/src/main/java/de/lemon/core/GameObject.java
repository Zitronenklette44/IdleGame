package de.lemon.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.interfaces.LayoutItem;

/**
 * Source object for most important classes that appear on screen
 */
public abstract class GameObject implements LayoutItem {
    /**
     * Position of the object
     */
    protected Vector2 pos;
    /**
     * size of the object
     */
    protected Vector2 size;
    /**
     * relative Position of the object on the window
     */
    protected Vector2 relPos = new Vector2();
    /**
     * relative Size of the object on the window
     */
    protected Vector2 relSize = new Vector2();
    /**
     * maximum Size the object kan habe if not overwritten set to the max float value
     */
    protected Vector2 maxSize = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    /**
     * minimum Size the Object can have
     */
    protected Vector2 minSize = new Vector2();
    /**
     * the size the object wants to have bevor the Layout is applied and min or max size is applied
     */
    protected Vector2 desiredSize = new Vector2();
    /**
     * Decides if the size may be overwritten by the autoLayout
     */
    protected boolean manualSize = false;

    /**
     * Standard constructor for the object using Vector2 class
     * @param pos position of the Object
     * @param size size of the Object
     */
    public GameObject(Vector2 pos, Vector2 size){
        this.pos = pos;
        this.size = size;
    }

    /**
     * Alternative Constructor using floats values instead of Vector2 class
     * @param posX x position of the object
     * @param posY y position of the object
     * @param width width of the object
     * @param height height of the object
     * @see #GameObject(Vector2 pos, Vector2 size)
     */
    public GameObject(float posX, float posY, int width, int height){
        pos = new Vector2(posX, posY);
        size = new Vector2(width, height);
    }

    /**
     * Getter for the position
     * @return position of the object
     */
    public Vector2 getPos() {
        return pos;
    }

    /**
     * Getter for the size of the object
     * @return size of the object
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * Setter for the position
     * @param pos new position
     */
    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    /**
     * Setter for the size
     * @param size new size
     */
    public void setSize(Vector2 size) {
        this.size = size;
    }

    /**
     * Called on each frame to render Shapes
     * @param shapeRenderer libGDX ShapeRenderer renderer
     * @param delta time since last frame
     * @see com.badlogic.gdx.graphics.glutils.ShapeRenderer
     */
    public void onShapeRender(ShapeRenderer shapeRenderer, float delta){}

    /**
     * Called on each Frame to render Sprites
     * @param batch LibGDX Batch
     * @param delta
     * @see com.badlogic.gdx.graphics.g2d.Batch
     */
    public void onSpriteRender(Batch batch, float delta){}

    /**
     * Only called in the debugState of the application used to render additional information
     * @param shapeRenderer shapeRenderer libGDX ShapeRenderer renderer
     * @param delta time between frames
     * @see com.badlogic.gdx.graphics.glutils.ShapeRenderer
     */
    public void onDebug(ShapeRenderer shapeRenderer, float delta){}

    /**
     * Called on each frame to update values. Can be used for things like movement
     * @param delta time between frames
     */
    public void update(float delta){}

    /**
     * Listener for Key presses
     * @param keycode LibGDX keyCode
     * @see com.badlogic.gdx.InputProcessor
     */
    public void onKeyDown(int keycode){}
    /**
     * Listener for Key release
     * @param keycode LibGDX keyCode
     * @see com.badlogic.gdx.InputProcessor
     */
    public void onKeyUp(int keycode){}
    /**
     * Listener for Key typed
     * @param character LibGDX keyCode
     * @see com.badlogic.gdx.InputProcessor
     */
    public void onKeyTyped(char character){}
    public void onTouchDown(int screenX, int screenY, int button){}
    public void onTouchUp(int screenX, int screenY, int button){}
    public void onTouchDragged(int screenX, int screenY){}
    public void onMouseMoved(int screenX, int screenY){}
    public void onScrolled(float amountX, float amountY){}

    /**
     * aApplies position with the viewport an the relative position
     * Sets the desired Size with the viewport and the relative Size
     * @param viewport viewport of the window
     */
    @Override
    public void applyLayout(Viewport viewport) {
        pos.set(viewport.getWorldWidth() * relPos.x, viewport.getWorldHeight() * relPos.y);
        desiredSize.set(viewport.getWorldWidth() * relSize.x, viewport.getWorldHeight() * relSize.y);
    }

    /**
     * Sets the relative Sizes of this Object that are later used to calculate the position aswell as the size
     * @param relX x factor 0..1
     * @param relY y factor 0..1
     * @param relWidth with factor 0..1
     * @param relHeight height factor 0..1
     */
    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        relPos.set(relX, relY);
        relSize.set(relWidth, relHeight);
    }

    /**
     * Setts the bounding values of the object NOT relative to the viewport
     * If not necessary set to the +/- Float max value
     * @param maxWidth maximum value for the width
     * @param maxHeight maximum value for the height
     * @param minWidth minimum value for the width
     * @param minHeight minimum value for the height
     */
    @Override
    public void setMaxSize(float maxWidth, float maxHeight, float minWidth, float minHeight) {
        maxSize.set(maxWidth, maxHeight);
        minSize.set(minWidth, minHeight);
    }

    /**
     * Called to apply the desired Size if not overwritten by manualSize sets Limits to min and max Size
     * @see #manualSize
     */
    @Override
    public void applyMaxSize() {
        if(manualSize) return;
        size.x = MathUtils.clamp(desiredSize.x, minSize.x, maxSize.x);
        size.y = MathUtils.clamp(desiredSize.y, minSize.y, maxSize.y);
    }

    /**
     * Finalises Layout bevor it gets applied to the object
     */
    @Override
    public void finaliseLayout(){
        applyMaxSize();
    }
}
