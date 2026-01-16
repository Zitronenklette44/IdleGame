package de.lemon.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.lemon.logic.animation.Sprite;
import de.lemon.core.Resources;
import de.lemon.logic.GameLogic;

public class Hitbox extends Button {

    private boolean absorbFirstHit;

    public Hitbox(boolean absorbFirstHit){
        super(Resources._instance.skin);
        this.absorbFirstHit = absorbFirstHit;
        setColor( 1, 1, 1, 0);
        setDebug(GameLogic.debug);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                System.out.println("fired");
                if(Hitbox.this.absorbFirstHit){
                    Hitbox.this.absorbFirstHit = false;
//                    System.out.println("absorbed");
                }else{
                    onClick();
                }
                super.clicked(event, x, y);
            }
        });
    }

    public Hitbox(){
        this(false);
    }

    public void onClick() {}

    public void autoresize(Sprite reference, float addX, float addY, float relWidth, float relHeight){
        Vector2 pos = reference.getPos().cpy();
        Vector2 size = reference.getSize().cpy();

        setPosition(pos.x + addX, pos.y + addY);
        setSize(size.x * relWidth, size.y * relHeight);
    }

}
