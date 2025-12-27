package de.lemon.screens;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import de.lemon.core.Resources;
import de.lemon.enums.ScreenFeatures;
import de.lemon.ui.TButton;

import java.util.EnumSet;

public class LoadScreen extends CoreScreen{
    private Table table;
    private ScrollPane scrollPane;
    private Cell<ScrollPane> cell;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.UI);
    }

    @Override
    protected void createComponents() {
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        scrollPane = new ScrollPane(null);
        cell = table.add(scrollPane).prefWidth(stage.getWidth() * 0.5f).expandY().fill().center();
        table.row();

        Table buttonTable = new Table();
        TButton loadGame = new TButton("Load Game", Resources._instance.skin);
        TButton deleteGame = new TButton("Delete Game", Resources._instance.skin);
        TButton createGame = new TButton("Create Game", Resources._instance.skin);

        buttonTable.add(loadGame).pad(10);
        buttonTable.add(deleteGame).pad(10);
        buttonTable.add(createGame).pad(10);

        table.add(buttonTable).center().padBottom(20);

        table.debugAll();

        float value = loadGame.getMinWidth() + deleteGame.getMinWidth() + createGame.getMinWidth() + 60;
//        System.out.println("Value: "+ value);
        cell.minWidth(value);
    }


    @Override
    protected void createWorld() {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        cell.prefWidth(stage.getWidth() * 0.5f);
//        System.out.println("ValueMax: "+ stage.getWidth() * 0.5f);
//        System.out.println("ValueTrue: "+cell.getActorWidth());

    }
}
