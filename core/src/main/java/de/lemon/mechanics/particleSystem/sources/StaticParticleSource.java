package de.lemon.mechanics.particleSystem.sources;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.Particle;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;

import java.util.ArrayList;

public class StaticParticleSource extends ParticleSource {
    public StaticParticleSource(Vector2 pos, ParticleManager particleManager, GeneratorSettings settings) {
        super(pos, particleManager, settings);
    }

    @Override
    protected void generateParticles() {
//        ArrayList<Particle> newParticles = new ArrayList<>();
        int generationNumber = MathUtils.random(minParticleGeneration, maxParticleGeneration);
        for (int i = 0; i < generationNumber; i++) {
            Vector2 velocity = new Vector2(1, 0).setToRandomDirection().scl(settings.particleStartSpeed);
            Particle newParticle =  new Particle(pos.cpy(), settings.particleSize, settings.particleLifetime, velocity, settings.particleTexture);
            newParticle.setFriction(settings.particleFriction);
//            newParticles.add(newParticle);
            particleManager.particles.add(newParticle);
        }
//        System.out.println("new Particles: " + newParticles.size() +" currentPos:" + pos);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

}
