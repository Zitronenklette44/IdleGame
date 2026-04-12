package de.lemon.save.particle;

import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.render.*;

public class SpritePreset {

    public SpriteType type;

    // shared asset reference
    public String textureName;
    public int frameWidth = 16;
    public int frameHeight = 16;
    public boolean loop = true;

    // animated sprite
    public float frameDuration = 0.1f;
    public int row = 0;

    // controller sprite
    public int[] rows;
    public int delay = 0;

    // nine sprite
    public int left, right, top, bottom;

    public static Sprite generateFromPreset(SpritePreset preset) {
        SpriteType type = preset.type;
        switch (type) {
            case SIMPLE:
                return new SimpleSprite(preset.textureName, preset.frameWidth, preset.frameHeight, preset.loop, Vector2.Zero.cpy());
            case ANIMATED:
                return new AnimatedSprite(preset.textureName, preset.row, preset.frameWidth, preset.frameHeight, preset.frameDuration, preset.loop, Vector2.Zero.cpy());
            case CONTROLLER:
                return new AnimationController(preset.textureName, preset.rows, Vector2.Zero.cpy(), preset.frameWidth, preset.frameHeight, preset.frameDuration, preset.delay);
            case NINE:
                return new NineSprite(preset.textureName, preset.left, preset.right, preset.top, preset.bottom, Vector2.Zero.cpy(), Vector2.Zero.cpy());
        }
        System.out.println("Invalid SpriteType " + type);
        return null;
    }

    public enum SpriteType {
        SIMPLE,
        ANIMATED,
        CONTROLLER,
        NINE
    }

    public static SpritePreset generateFromSprite(Sprite sprite){
        SpritePreset returnValue = new SpritePreset();

        if(sprite instanceof SimpleSprite){
            SimpleSprite simple = (SimpleSprite) sprite;
            returnValue = fromSimpleSprite(simple);
        }else if(sprite instanceof AnimationController){
            AnimationController controller = (AnimationController) sprite;
            returnValue = fromController(controller);
        }else if(sprite instanceof AnimatedSprite) {
            AnimatedSprite animatedSprite = (AnimatedSprite) sprite;
            returnValue = fromAnimated(animatedSprite);
        } else if (sprite instanceof NineSprite) {
            NineSprite nineSprite = (NineSprite) sprite;
            returnValue = fromNine(nineSprite);
        }
        return returnValue;
    }

    private static SpritePreset fromSimpleSprite(SimpleSprite sprite){
        SpritePreset sp = new SpritePreset();
        sp.type = SpriteType.SIMPLE;
        sp.textureName = sprite.getTextureName();
        sp.frameWidth = sprite.getFrameWidth();
        sp.frameHeight = sprite.getFrameHeight();
        sp.loop = sprite.getLoop();
        return sp;
    }

    private static SpritePreset fromController(AnimationController controller){
        SpritePreset preset = new SpritePreset();
        preset.type = SpriteType.CONTROLLER;
        preset.textureName = controller.getTextureName();
        preset.frameWidth = controller.getFrameWidth();
        preset.frameHeight = controller.getFrameHeight();
        preset.frameDuration = controller.getFrameDuration();
        preset.delay = controller.getDelay();
        preset.rows = controller.getRows();
        return preset;
    }

    private static SpritePreset fromAnimated(AnimatedSprite sprite){
        SpritePreset preset = new SpritePreset();
        preset.type = SpriteType.ANIMATED;
        preset.textureName = sprite.getTextureName();
        preset.frameWidth = sprite.getFrameWidth();
        preset.frameHeight = sprite.getFrameHeight();
        preset.frameDuration = sprite.getFrameDuration();
        preset.row = sprite.getRow();
        preset.loop = sprite.getLoop();
        return preset;
    }

    private static SpritePreset fromNine(NineSprite sprite){
        SpritePreset preset = new SpritePreset();
        preset.type = SpriteType.NINE;
        preset.textureName = sprite.getTextureName();
        preset.left = sprite.left();
        preset.right = sprite.right();
        preset.top = sprite.top();
        preset.bottom = sprite.bottom();
        return preset;
    }
}
