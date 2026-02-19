package de.lemon.mechanics.particleSystem.sources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.Particle;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;

import java.util.ArrayList;

public class MovingParticleSource extends ParticleSource {
    boolean targetGoal = true;
    boolean reachedEndOfMovement = false;

    private Vector2 startPos = Vector2.Zero.cpy();
    private Vector2 targetPos = Vector2.Zero.cpy();
    public Vector2 relTargetPos = Vector2.Zero.cpy();
    public Vector2 relStartPos = Vector2.Zero.cpy();

    public MovingParticleSource(Vector2 pos, ParticleManager particleManager, GeneratorSettings settings) {
        super(pos, particleManager, settings);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (reachedEndOfMovement) return;

        Vector2 goal = targetGoal ? targetPos : startPos;

        Vector2 toGoal = goal.cpy().sub(pos);
        float move = settings.movementSpeed * delta;

        if (toGoal.len2() <= move * move) {
            pos.set(goal);

            if (settings.traceBack) {
                targetGoal = !targetGoal;
            } else {
                reachedEndOfMovement = true;
            }
            return;
        }

        pos.mulAdd(toGoal.nor(), move);
    }


    @Override
    protected void generateParticles() {
        if(emissionType == ParticleEmissionType.LIMITED && reachedEndOfMovement) return;
//        ArrayList<Particle> newParticles = new ArrayList<>();
        int generationNumber = MathUtils.random(minParticleGeneration, maxParticleGeneration);
        for (int i = 0; i < generationNumber; i++) {
            Vector2 velocity = new Vector2(1, 0).setToRandomDirection().scl(settings.particleStartSpeed);
            Particle newParticle =  new Particle(pos.cpy(), settings.particleSize, settings.particleLifetime, velocity, settings.particleTexture);
            newParticle.setFriction(settings.particleFriction);
//            newParticles.add(newParticle);
            particleManager.particles.add(newParticle);
        }
    }

    @Override
    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        super.onDebug(shapeRenderer, delta);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(pos.x, pos.y , 2);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(startPos.x, startPos.y, 2);

        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.circle(targetPos.x, targetPos.y, 2);

        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.line(startPos, targetPos);
    }

    @Override
    public void applyLayout(Viewport viewport) {
        super.applyLayout(viewport);
        startPos.set(viewport.getWorldWidth() * relStartPos.x, viewport.getWorldHeight() * relStartPos.y);
        targetPos.set(viewport.getWorldWidth() * relTargetPos.x, viewport.getWorldHeight() * relTargetPos.y);
    }
}
