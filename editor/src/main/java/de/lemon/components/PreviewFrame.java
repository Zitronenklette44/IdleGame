package de.lemon.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.lemon.editor.ParticleStartScreen;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;
import de.lemon.mechanics.particleSystem.SpawnArea;
import de.lemon.mechanics.particleSystem.sources.GeometricParticleSource;
import de.lemon.mechanics.particleSystem.sources.MovingParticleSource;
import de.lemon.mechanics.particleSystem.sources.StaticParticleSource;

public class PreviewFrame extends Component{

    private ParticleSource particleSource;
    private ParticleManager particleManager;
    private SourceType sourceType;

    @Override
    public void init() {
//        setColor(Color.GRAY);
        setColor(Color.CLEAR);
        particleManager = ParticleStartScreen._instance.getParticleManager();
    }

    @Override
    public void createComponents() {
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("source Pos: " + particleSource.getPos());

            }
        });
    }

    @Override
    public void createWorld() {}

    private ParticleSource createParticleSource(){
        switch (sourceType) {
            case MOVING:
                return new MovingParticleSource(particleSource.getPos(),particleManager, ParticleStartScreen._instance.particleSettings);
            case GEOMETRIC:
                return new GeometricParticleSource(particleManager, ParticleStartScreen._instance.particleSettings, new SpawnArea());
            case STATIC:
            default: return new StaticParticleSource(particleSource.getPos(),particleManager, ParticleStartScreen._instance.particleSettings);
        }
    }

    public enum SourceType{
        MOVING,
        GEOMETRIC,
        STATIC
    }
}

