package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class ParticleManager {

    SpriteBatch spriteBatch;

    public ArrayList<Particle> particles = new ArrayList<>();

    public ArrayList<ParticleSource> sources = new ArrayList<>();

    public ParticleManager(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    private float particleSpeedModifier = 1f;
    private float sourceSpeedModifier = 1f;

    public void update(float delta){
        for (int i = sources.size() - 1; i >= 0; i--) {
            ParticleSource s = sources.get(i);
            s.update(delta * sourceSpeedModifier);
        }
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update(delta * particleSpeedModifier);
            if (p.isDead()) {
                p.dispose();
                particles.remove(i);
            }
        }
    }

    public void render(float delta){
        for(Particle p : particles) p.onSpriteRender(spriteBatch, delta);
    }

    public void onDebug(ShapeRenderer sR, float delta){
        for(ParticleSource s : sources) s.onDebug(sR, delta);
    }

    public void setParticleSpeedModifier(float particleSpeedModifier) {
        this.particleSpeedModifier = particleSpeedModifier;
    }

    public void setSourceSpeedModifier(float sourceSpeedModifier) {
        this.sourceSpeedModifier = sourceSpeedModifier;
    }

    public void setSpeed(float speed){
        particleSpeedModifier = speed;
        sourceSpeedModifier = speed;
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public Vector2 getSpeed(){
        return new Vector2(particleSpeedModifier, sourceSpeedModifier);
    }

    public void resize(Viewport viewport) {
        for (ParticleSource s: sources) {
            s.applyLayout(viewport);
            s.finaliseLayout();
        }
    }
}
