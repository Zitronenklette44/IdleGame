package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.lemon.logic.render.AnimationController;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.render.NineSprite;
import de.lemon.main.Main;
import de.lemon.ui.STextButton;
import de.lemon.ui.TButton;

import java.util.EnumSet;

public class StartScreen extends CoreScreen{

    private AnimatedSprite name;
    private TButton startGame;
    private TButton options;
    private TButton quit;

    private STextButton testButton;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.UI);
    }

    @Override
    protected void createComponents() {
        Table table = new Table();
        table.setFillParent(true);
        table.defaults().width(200).height(60).pad(10);
//        stage.addActor(table);

        startGame = new TButton("Start Game",Resources._instance.skin);
        startGame.bindCell(table.add(startGame));
        table.row();

        options = new TButton("Options", Resources._instance.skin);
        options.bindCell(table.add(options));
        table.row();

        quit = new TButton("Quit Game", Resources._instance.skin);
        quit.bindCell(table.add(quit));
        table.row();

        addListeners();
    }

    private void addListeners(){
        startGame.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Main._instance.switchScreen(Main.LOAD_SCREEN);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

    }

    @Override
    protected void createWorld() {
        setBackgroundColor(Color.GRAY);
        name = new AnimationController(Resources._instance.startScreen_name,new int[]{0, 1, 2, 3},new Vector2(), 256, 48, 0.11f, 10);
        addWorldObject(name, 0.5f, 0.9f, 0.5f, 0.1f);

        testButton = new STextButton("Start Game",Resources._instance.UI_Button){
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.LOAD_SCREEN);
            }
        };
        addWorldObject(testButton, 1/2f, 1/2f, 2/10f, 1/10f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        startGame.autoResize(1/5f, 1/10f, stage.getViewport());
        options.autoResize(1/5f, 1/10f, stage.getViewport());
        quit.autoResize(1/5f, 1/10f, stage.getViewport());

    }
}
