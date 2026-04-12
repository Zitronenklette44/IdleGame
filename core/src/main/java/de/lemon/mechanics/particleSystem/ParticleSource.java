package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.lemon.core.GameObject;
import de.lemon.logic.enums.ParticleEmissionType;

public abstract class ParticleSource extends GameObject  {

    protected final ParticleManager particleManager;
    protected float particleGenerationTime;
    protected GeneratorSettings settings;
    protected float lastParticleGeneration;
    protected ParticleEmissionType emissionType;
    protected int maxParticleGeneration;
    protected  int minParticleGeneration;
    protected boolean alive = true;
    protected float survivedTime = 0;
    protected int minParticleBurst;
    protected int maxParticleBurst;

    public ParticleSource(Vector2 pos, ParticleManager particleManager, GeneratorSettings settings) {
        super(pos, Vector2.Zero.cpy());
        this.particleManager = particleManager;

        applySettings(settings);
//        System.out.println("created Source: " + this);
        particleManager.sources.add(this);
    }

    @Override
    public void update(float delta) {
        if(!alive) return;
        lastParticleGeneration += delta;
        if(lastParticleGeneration >= particleGenerationTime){
            generateParticles(emissionType == ParticleEmissionType.BURST);
            if(emissionType == ParticleEmissionType.ONCE || emissionType == ParticleEmissionType.BURST) dispose();
            lastParticleGeneration -= particleGenerationTime;
        }

        survivedTime += delta;
        if(settings.TTL != -1 && survivedTime >= settings.TTL) dispose();
//        System.out.println("Delta : " + delta);
//        System.out.println("Particle Manager Source: " + particleManager);
        super.update(delta);
    }

    protected void generateParticles(boolean burst){
        if(!canEmitParticles()) return;
        int generationNumber;
        if(burst) generationNumber = MathUtils.random(minParticleBurst, maxParticleBurst);
        else generationNumber = MathUtils.random(minParticleGeneration, maxParticleGeneration);
        for (int i = 0; i < generationNumber; i++) {
            Vector2 velocity = new Vector2(1, 0).setToRandomDirection().scl(settings.particleStartSpeed);
            Particle newParticle;
            if(settings.particleTexture != null) newParticle = new Particle(pos.cpy(), new Vector2(settings.particleSize, settings.particleSize), settings.particleLifetime, velocity, settings.particleTexture);
            else if (settings.particleSprite != null) newParticle = new Particle(pos.cpy(), new Vector2(settings.particleSize, settings.particleSize), settings.particleLifetime, velocity, settings.particleSprite.cpy());
            else throw new IllegalArgumentException("No Texture or Sprite declared for Particle");
            newParticle.setFriction(settings.particleFriction);
            newParticle.setTintColor(settings.particleTint);
            newParticle.setRotationSpeed(settings.particleRotationSpeed);
            particleManager.particles.add(newParticle);
        }
    }

    public boolean canEmitParticles(){
        return true;
    }

    public void dispose(){
        alive = false;
        particleManager.sources.remove(this);
    }

    public boolean isAlive() {
        return alive;
    }

    public void applySettings(GeneratorSettings settings){
        this.settings = settings;

        emissionType = settings.emissionType;
        minParticleGeneration = settings.minGeneration;
        maxParticleGeneration = settings.maxGeneration;
        minParticleBurst = settings.minBurst;
        maxParticleBurst = settings.maxBurst;
        particleGenerationTime = settings.generationInterval;

        lastParticleGeneration = 0f;
        onApplySettings();
    }

    public void onApplySettings(){}
    public void reset(){
        survivedTime = 0;
    }

}
