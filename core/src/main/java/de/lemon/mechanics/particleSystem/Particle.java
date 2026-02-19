package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.render.Sprite;

public class Particle extends Sprite {

    private final float lifetime;
    private float age;
    private Vector2 velocity;
    private float friction;
    private TextureRegion texture;
    private float rotationSpeed = 10;
    private final boolean rotDirection;

    public Particle(Vector2 pos, Vector2 size, float lifetime, Vector2 velocity, Texture texture) {
        super(pos, size);
        this.lifetime = lifetime;
        this.velocity = velocity;
        this.texture = new TextureRegion(texture);
        origin = new Vector2(size.x / 2, size.y / 2);
        rotDirection = MathUtils.randomBoolean();
    }

    @Override
    public void update(float delta) {
        age += delta;
        pos.mulAdd(velocity, delta);
        velocity.scl(1f - friction * delta);
        if(rotDirection){
            rotation += rotationSpeed * delta;
        }else{
            rotation -= rotationSpeed * delta;
        }
        super.update(delta);
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public boolean isAlive(){
        return age < lifetime;
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        batch.draw(texture, pos.x, pos.y, origin.x, origin.y, size.x, size.y , scale.x, scale.y, rotation);
        super.onSpriteRender(batch, delta);
    }

    public void dispose() {
//        texture.dispose();
    }
}
