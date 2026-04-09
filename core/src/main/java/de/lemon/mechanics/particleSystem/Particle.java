package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.Color;
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
    private Sprite sprite;
    private float rotationSpeed = 10;
    private final boolean rotDirection;
    private Color tintColor = null;
    private Vector2 killPoint = new Vector2(-1000, -1000);
    private float killDist = 0;

    public Particle(Vector2 pos, Vector2 size, float lifetime, Vector2 velocity, Texture texture) {
        super(pos, size);
        this.lifetime = lifetime;
        this.velocity = velocity;
        this.texture = new TextureRegion(texture);
        origin = new Vector2(size.x / 2, size.y / 2);
        rotDirection = MathUtils.randomBoolean();
    }

    public Particle(Vector2 pos, Vector2 size, float lifetime, Vector2 velocity, Sprite sprite) {
        super(pos, size);
        this.lifetime = lifetime;
        this.velocity = velocity;
        this.sprite = sprite;
        this.sprite.setSize(size);
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
        if(sprite != null) {
            sprite.setPos(pos.cpy());
            sprite.setRotation(rotation);
            sprite.update(delta);
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

    public boolean isDead(){
        return !(age < lifetime) || (killPoint != null && pos.dst(killPoint) <= killDist);
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        if(isDead()) return;
        if(tintColor != null) batch.setColor(tintColor);
        if(texture != null) batch.draw(texture, pos.x, pos.y, origin.x, origin.y, size.x, size.y , scale.x, scale.y, rotation);
        if(sprite != null) sprite.onSpriteRender(batch, delta);
        if(tintColor != null) batch.setColor(Color.WHITE);
        super.onSpriteRender(batch, delta);
    }

    public void dispose() {
//        texture.dispose();
    }

    @Override
    public Particle cpy() {
        Particle copy;

        if (sprite != null) {
            copy = new Particle(
                pos.cpy(),
                size.cpy(),
                lifetime,
                velocity.cpy(),
                sprite.cpy()
            );
        } else {
            copy = new Particle(
                pos.cpy(),
                size.cpy(),
                lifetime,
                velocity.cpy(),
                texture.getTexture()
            );
        }

        // base sprite data
        copy.rotation = this.rotation;
        copy.scale.set(this.scale);
        copy.origin.set(this.origin);

        // particle specific
        copy.age = this.age;
        copy.friction = this.friction;
        copy.rotationSpeed = this.rotationSpeed;

        copy.killPoint = this.killPoint;
        copy.killDist = this.killDist;

        return copy;
    }

    public void setTintColor(Color tintColor) {
        this.tintColor = tintColor;
    }

    public void setKillPoint(Vector2 killPoint, float killDist) {
        this.killPoint = killPoint;
        this.killDist = killDist;
//        System.out.println("killPos: " + killPoint + " killDist: " + killDist);
    }
}
