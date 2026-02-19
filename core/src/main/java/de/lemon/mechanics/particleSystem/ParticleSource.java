package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.lemon.core.GameObject;
import de.lemon.logic.enums.ParticleEmissionType;

public abstract class ParticleSource extends GameObject  {

    protected final ParticleManager particleManager;
    protected float particleGenerationTime;
    protected final GeneratorSettings settings;
    protected float lastParticleGeneration;
    protected ParticleEmissionType emissionType;
    protected int maxParticleGeneration;
    protected  int minParticleGeneration;
    protected boolean alive = true;
    protected float survivedTime = 0;

    public ParticleSource(Vector2 pos, ParticleManager particleManager, GeneratorSettings settings) {
        super(pos, Vector2.Zero.cpy());
        this.particleManager = particleManager;
        emissionType = settings.emissionType;
        minParticleGeneration = settings.minGeneration;
        maxParticleGeneration = settings.maxGeneration;
        particleGenerationTime = settings.generationInterval;
        this.settings = settings;
        particleManager.sources.add(this);
    }

    @Override
    public void update(float delta) {
        if(!alive) return;
        lastParticleGeneration += delta;
        if(lastParticleGeneration >= particleGenerationTime && emissionType != ParticleEmissionType.ONCE){
            generateParticles();
            lastParticleGeneration = 0;
        }

        survivedTime += delta;
        if(settings.TTL != -1 && survivedTime >= settings.TTL) dispose();
//        System.out.println("State: lPG:" + lastParticleGeneration + " pGT: " + particleGenerationTime + " emT: " + emissionType);
        super.update(delta);
    }

    protected abstract void generateParticles();

    public void dispose(){
        alive = false;
        particleManager.sources.remove(this);
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void onSpriteRender(Batch batch, float delta) {
        if(!alive) return;
        super.onSpriteRender(batch, delta);
    }


}
