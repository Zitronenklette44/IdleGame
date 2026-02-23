package de.lemon.mechanics.particleSystem.sources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.mechanics.particleSystem.*;

public class GeometricParticleSource extends ParticleSource {
    private final SpawnArea spawnArea;

    public GeometricParticleSource(ParticleManager particleManager, GeneratorSettings settings, SpawnArea spawnArea) {
        super(Vector2.Zero.cpy(), particleManager, settings);
        this.spawnArea = spawnArea;
    }

    @Override
    protected void generateParticles(boolean burst) {
        Vector2 position = spawnArea.getRandomParticlePos();
        Vector2 velocity = spawnArea.getMovementDirection(position).scl(settings.particleStartSpeed);
//        System.out.println("scalar: " + settings.particleStartSpeed + " velocity: " + spawnArea);

        Particle newParticle;
        if(settings.particleTexture != null) newParticle = new Particle(position.cpy(), settings.particleSize, settings.particleLifetime, velocity, settings.particleTexture);
        else if (settings.particleSprite != null) newParticle = new Particle(position.cpy(), settings.particleSize, settings.particleLifetime, velocity, settings.particleSprite.cpy());
        else throw new IllegalArgumentException("No Texture or Sprite declared for Particle");
        newParticle.setFriction(settings.particleFriction);
        newParticle.setTintColor(settings.particleTint);
        newParticle.setRotationSpeed(settings.particleRotationSpeed);
        newParticle.setKillPoint(pos.cpy(), 0.5f);
        particleManager.particles.add(newParticle);


    }

    @Override
    public void applyLayout(Viewport viewport) {
        super.applyLayout(viewport);
        spawnArea.pos = pos.cpy();
        spawnArea.size.set(viewport.getWorldWidth() * spawnArea.relSize.x, viewport.getWorldHeight() * spawnArea.relSize.y);
    }

    @Override
    public void setRelLayout(float relX, float relY, float relWidth, float relHeight) {
        super.setRelLayout(relX, relY, relWidth, relHeight);
        spawnArea.relSize.set(relWidth, relHeight);
    }


    int counter = 0;
    Vector2 testPoint = Vector2.Zero.cpy();
    Vector2 testDirection = Vector2.Zero.cpy();
    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        super.onDebug(shapeRenderer, delta);
        spawnArea.onDebug(shapeRenderer, delta);
        shapeRenderer.setColor(Color.RED);
        if(counter >= 50) {
            testPoint = spawnArea.getRandomParticlePos();
            testDirection = spawnArea.getMovementDirection(testPoint);
            counter = 0;
        }
        shapeRenderer.circle(testPoint.x, testPoint.y,5);
        float dirLength = 50;
        Vector2 lineEnd = new Vector2(testPoint).add(new Vector2(testDirection).scl(dirLength));
        shapeRenderer.line(testPoint.x, testPoint.y, lineEnd.x, lineEnd.y);
        counter++;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
