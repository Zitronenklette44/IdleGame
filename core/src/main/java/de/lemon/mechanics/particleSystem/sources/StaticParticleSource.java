package de.lemon.mechanics.particleSystem.sources;

import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;

public class StaticParticleSource extends ParticleSource {

    int currentEmissions = 0;
    int maxEmissions = -1;

    public StaticParticleSource(Vector2 pos, ParticleManager particleManager, GeneratorSettings settings) {
        super(pos, particleManager, settings);
        if(emissionType == ParticleEmissionType.LIMITED) maxEmissions = settings.maxEmissions;
    }

    @Override
    protected void generateParticles(boolean burst) {
        super.generateParticles(burst);
        currentEmissions++;
    }

    @Override
    public boolean canEmitParticles() {
        return maxEmissions == -1 || currentEmissions < maxEmissions;
    }
}
