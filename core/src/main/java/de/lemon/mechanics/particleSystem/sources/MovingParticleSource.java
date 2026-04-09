package de.lemon.mechanics.particleSystem.sources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.logic.enums.ParticleEmissionType;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;

public class MovingParticleSource extends ParticleSource {
    boolean targetGoal = true;
    boolean reachedEndOfMovement = false;

    private final Vector2 startPos = Vector2.Zero.cpy();
    private final Vector2 targetPos = Vector2.Zero.cpy();
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

            if (settings.traceBack && emissionType != ParticleEmissionType.LIMITED) {
                targetGoal = !targetGoal;
            } else {
                reachedEndOfMovement = true;
            }
            return;
        }

        pos.mulAdd(toGoal.nor(), move);
    }

    @Override
    public boolean canEmitParticles() {
        return emissionType != ParticleEmissionType.LIMITED || !reachedEndOfMovement;
    }

    @Override
    public void onApplySettings() {}

    @Override
    public void reset() {
        super.reset();
        pos.set(startPos);
        reachedEndOfMovement = false;
        targetGoal = true;
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
//        System.out.println("relStar:" + relStartPos.toString() + " startPos: "+ startPos.toString());
    }
}
