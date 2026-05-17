package de.lemon.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    private ParticleManager particleManager;

    @Override
    public void init() {
//        setColor(Color.GRAY);
        setColor(Color.CLEAR);
        particleManager = ParticleStartScreen._instance.getParticleManager();
    }

    boolean startEndSwitch = true;
    @Override
    public void createComponents() {
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                System.out.println("source Pos: " + ParticleStartScreen._instance.source.getPos() + " clickPos: ("+x + "|" + y +")");
                Vector2 stageCoords = localToStageCoordinates(new Vector2(x, y));
                float screenX = stageCoords.x;
                float screenY = getStage().getViewport().getScreenHeight() - stageCoords.y;

                Vector3 worldCoords3 = ParticleStartScreen._instance.getWorldStage()
                    .getViewport()
                    .unproject(new Vector3(screenX, screenY, 0));

                Vector2 worldCoords = new Vector2(worldCoords3.x, worldCoords3.y);
                float relX = worldCoords.x / getWidth();
                float relY = worldCoords.y / getHeight();
//                float relX = worldCoords.x / ParticleStartScreen._instance.width;
//                float relY = worldCoords.y / ParticleStartScreen._instance.height;
                ParticleStartScreen._instance.source.setRelLayout(relX, relY, 0.1f, 0.1f); // needs to be relative so the layout kan be correctly applied later
                if(ParticleStartScreen._instance.source instanceof MovingParticleSource){
                    System.out.println("called for movingSource and set startPoint: " + startEndSwitch);
                    MovingParticleSource movSource = (MovingParticleSource) ParticleStartScreen._instance.source;
                    startEndSwitch = !startEndSwitch;
                    if(startEndSwitch) movSource.relStartPos.set(relX, relY);
                    else movSource.relTargetPos.set(relX, relY);
                    ParticleStartScreen._instance.refreshLayout();
                }else {
                    ParticleStartScreen._instance.source.setPos(worldCoords);
                }
            }
        });
    }

    @Override
    public void createWorld() {}

    private ParticleSource createParticleSource(){
        SourceType sourceType = getSourceType(ParticleStartScreen._instance.source);
        switch (sourceType) {
            case MOVING:
                return new MovingParticleSource(ParticleStartScreen._instance.source.getPos(),particleManager, ParticleStartScreen._instance.particleSettings);
            case GEOMETRIC:
                return new GeometricParticleSource(particleManager, ParticleStartScreen._instance.particleSettings, new SpawnArea());
            case STATIC:
            default: return new StaticParticleSource(ParticleStartScreen._instance.source.getPos(),particleManager, ParticleStartScreen._instance.particleSettings);
        }
    }

    public SourceType getSourceType(ParticleSource source){
        if(source instanceof StaticParticleSource) return SourceType.STATIC;
        if(source instanceof MovingParticleSource) return SourceType.MOVING;
        if(source instanceof GeometricParticleSource) return SourceType.GEOMETRIC;
        return null;
    }

    public enum SourceType{
        MOVING,
        GEOMETRIC,
        STATIC
    }
}

