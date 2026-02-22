package de.lemon.mechanics.particleSystem.sources;

import com.badlogic.gdx.math.Vector2;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;

public class GeometricParticleSource extends ParticleSource {
    public GeometricParticleSource(Vector2 pos, ParticleManager particleManager, GeneratorSettings settings) {
        super(pos, particleManager, settings);
    }

    @Override
    protected void generateParticles(boolean burst) {

    }
}
