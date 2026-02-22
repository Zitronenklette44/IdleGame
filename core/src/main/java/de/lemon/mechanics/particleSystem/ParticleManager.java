package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class ParticleManager {

    SpriteBatch spriteBatch;

    public ArrayList<Particle> particles = new ArrayList<>();

    public ArrayList<ParticleSource> sources = new ArrayList<>();

    public ParticleManager(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public void update(float delta){
//        System.out.println("Particle Sources: " + sources.size());
        for (int i = sources.size() - 1; i >= 0; i--) {
            ParticleSource s = sources.get(i);
            s.update(delta);
        }
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update(delta);
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
}
