package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.logic.interfaces.Copyable;
import de.lemon.logic.render.Sprite;

public class GeneratorSettings implements Copyable<GeneratorSettings> {
    // Particles
    public Texture particleTexture;
    public Sprite particleSprite;
    public Vector2 particleSize = Vector2.Zero.cpy();
    public float particleStartSpeed = 1;
    public float particleLifetime = 5;
    public float particleFriction = 1f;
    public Color particleTint = null;
    public float particleRotationSpeed = 10;

    // Generation
    public int minGeneration = 0;
    public int maxGeneration = 5;
    public int minBurst = 10;
    public int maxBurst = 25;
    public ParticleEmissionType emissionType;
    public float generationInterval = 0.5f;

    //optional
    public float TTL = -1;

    public float movementSpeed = 0;
    public boolean traceBack = false;
    public int maxEmissions = -1;

    public Builder builder(){
        return new Builder(this);
    }

    @Override
    public GeneratorSettings cpy() {
        GeneratorSettings copy = new GeneratorSettings();
        copy.particleTexture = this.particleTexture;
        if(particleSprite != null) copy.particleSprite = this.particleSprite.cpy();
        copy.particleSize = this.particleSize.cpy();
        copy.particleStartSpeed = this.particleStartSpeed;
        copy.particleLifetime = this.particleLifetime;
        copy.particleFriction = this.particleFriction;
        copy.particleTint = this.particleTint;
        copy.particleRotationSpeed = this.particleRotationSpeed;

        copy.minGeneration = this.minGeneration;
        copy.maxGeneration = this.maxGeneration;
        copy.minBurst = this.minBurst;
        copy.maxBurst = this.maxBurst;
        copy.emissionType = this.emissionType;
        copy.generationInterval = this.generationInterval;

        copy.TTL = this.TTL;

        copy.movementSpeed = this.movementSpeed;
        copy.traceBack = this.traceBack;
        copy.maxEmissions = this.maxEmissions;
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

        public Builder sprite(Sprite sprite) {
            settings.particleSprite = sprite;
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

        public Builder color(Color color){
            settings.particleTint = color;
            return this;
        }

        public Builder rotationSpeed(float rotationSpeed){
            settings.particleRotationSpeed = rotationSpeed;
            return this;
        }

        public Builder burst(int min, int max){
            settings.minBurst = min;
            settings.maxBurst = max;
            return this;
        }

        public Builder emissions(int emissions){
            settings.maxEmissions = emissions;
            return this;
        }

        public GeneratorSettings build() {
            return settings;
        }
    }
}
