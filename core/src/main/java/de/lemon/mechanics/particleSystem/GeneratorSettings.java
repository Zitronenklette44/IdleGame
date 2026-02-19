package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.logic.interfaces.Copyable;

public class GeneratorSettings implements Copyable<GeneratorSettings> {
    // Particles
    public Texture particleTexture;
    public Vector2 particleSize = Vector2.Zero.cpy();
    public float particleStartSpeed;
    public float particleLifetime;
    public float particleFriction;

    // Generation
    public int minGeneration;
    public int maxGeneration;
    public ParticleEmissionType emissionType;
    public float generationInterval;

    //optional
    public float TTL = -1;

    public float movementSpeed;
    public boolean traceBack;

    public Builder builder(){
        return new Builder(this);
    }

    @Override
    public GeneratorSettings cpy() {
        GeneratorSettings copy = new GeneratorSettings();
        copy.particleTexture = this.particleTexture;
        copy.particleSize = this.particleSize.cpy();
        copy.particleStartSpeed = this.particleStartSpeed;
        copy.particleLifetime = this.particleLifetime;
        copy.particleFriction = this.particleFriction;

        copy.minGeneration = this.minGeneration;
        copy.maxGeneration = this.maxGeneration;
        copy.emissionType = this.emissionType;
        copy.generationInterval = this.generationInterval;

        copy.TTL = this.TTL;

        copy.movementSpeed = this.movementSpeed;
        copy.traceBack = this.traceBack;
        return copy;
    }

    public static class Builder {

        private final GeneratorSettings settings;

        public Builder(GeneratorSettings generatorSettings) {
            settings = generatorSettings;
        }


        public Builder texture(Texture texture) {
            settings.particleTexture = texture;
            return this;
        }

        public Builder particleSize(Vector2 size) {
            settings.particleSize.set(size);
            return this;
        }

        public Builder startSpeed(float speed) {
            settings.particleStartSpeed = speed;
            return this;
        }

        public Builder lifetime(float lifetime) {
            settings.particleLifetime = lifetime;
            return this;
        }

        public Builder generation(int min, int max) {
            settings.minGeneration = min;
            settings.maxGeneration = max;
            return this;
        }

        public Builder emissionType(ParticleEmissionType type) {
            settings.emissionType = type;
            return this;
        }

        public Builder interval(float interval) {
            settings.generationInterval = interval;
            return this;
        }

        public Builder friction(float friction){
            settings.particleFriction = friction;
            return this;
        }

        public Builder TTL(float TTl){
            settings.TTL = TTl;
            return this;
        }

        public Builder movementSpeed(float movementSpeed){
            settings.movementSpeed = movementSpeed;
            return this;
        }

        public Builder traceBack(boolean traceBack){
            settings.traceBack = traceBack;
            return this;
        }

        public GeneratorSettings build() {
            return settings;
        }
    }
}
