package de.lemon.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import de.lemon.Utilities;
import de.lemon.editor.Logic;
import de.lemon.editor.ParticleStartScreen;

public class StatFrame extends Component{
    private Label valueFps;
    private Label valueParticleNum;

    @Override
    public void init() {

    }

    @Override
    public void createComponents() {
        Table main = new Table();
        main.setFillParent(true);

        Table labels = new Table();
//        labels.setFillParent(true);

        Label fps = new Label("FPS: ", ParticleStartScreen._instance.skin);
        Label particleNum = new Label("Particle Number: ", ParticleStartScreen._instance.skin);

        valueFps = new Label("60", ParticleStartScreen._instance.skin);
        valueParticleNum = new Label("0", ParticleStartScreen._instance.skin);

        labels.add(fps, valueFps).align(Align.topLeft).row();
        labels.add(particleNum, valueParticleNum).align(Align.topLeft).row();

        Table buttons = new Table();
        buttons.defaults().pad(5);

        Button start = new Button(Utilities.getDefaultSkin());
        start.add("start");
        start.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ParticleStartScreen._instance.getParticleManager().setSpeed(1);
            }
        });
        buttons.add(start);

        Button pause = new Button(Utilities.getDefaultSkin());
        pause.add("pause");
        pause.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ParticleStartScreen._instance.getParticleManager().setSpeed(0);
            }
        });
        buttons.add(pause);

        Button clear = new Button(Utilities.getDefaultSkin());
        clear.add("clear");
        clear.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ParticleStartScreen._instance.getParticleManager().particles.clear();
            }
        });
        buttons.add(clear);

        main.add(labels)
            .expandX()
            .left()
            .pad(10);

        main.add(buttons)
            .expandX()
            .right()
            .pad(10)
            .row();
        addActor(main);
    }

    @Override
    public void createWorld() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        valueFps.setText("" + Gdx.graphics.getFramesPerSecond());
        valueParticleNum.setText(ParticleStartScreen._instance.getParticleManager().particles.size());
    }
}
