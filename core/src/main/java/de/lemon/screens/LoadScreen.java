package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.Resources;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.main.Main;
import de.lemon.save.SaveManager;
import de.lemon.save.SavePreview;
import de.lemon.ui.STextButton;
import de.lemon.ui.TButton;

import java.util.EnumSet;
import java.util.List;

public class LoadScreen extends CoreScreen{
    private Table table;
    private ScrollPane scrollPane;
    private Cell<ScrollPane> cell;
    private Table buttonTable;
    private STextButton loadSelected;
    private STextButton deleteSelected;

    private SavePreview selectedSave = null;

    @Override
    protected EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.UI);
    }

    @Override
    protected void createComponents() {
        setBackgroundColor(Color.GRAY);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //table.add(createHeader()).padBottom(20).padTop(10).row();

        Table content = new Table();
        content.top();
        content.defaults().expandX().fillX().pad(10);

        Table wrapper = new Table();
        wrapper.top();
        wrapper.add(content).expand().fillX().top();

        scrollPane = new ScrollPane(wrapper);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setFlickScroll(true);
        scrollPane.setCancelTouchFocus(true);
        scrollPane.setTouchable(Touchable.enabled);
        cell = table.add(scrollPane).prefWidth(stage.getWidth() * 0.5f).expandY().fill().center();
        table.row();

        buttonTable = new Table();
        buttonTable.setVisible(false);
        TButton loadGame = new TButton("Load Game", Resources._instance.skin);
        TButton deleteGame = new TButton("Delete Game", Resources._instance.skin);
        TButton createGame = new TButton("Create Game", Resources._instance.skin);

        buttonTable.add(loadGame).pad(10);
        buttonTable.add(deleteGame).pad(10);
        buttonTable.add(createGame).pad(10);

        loadSelected = new STextButton("Load Game", Resources._instance.UI_Button, new Vector2(), new Vector2());
        loadSelected.setGlobalFontDecrease(4);

        deleteSelected = new STextButton("Delete Game", Resources._instance.UI_Button, new Vector2(), new Vector2());
        deleteSelected.setGlobalFontDecrease(4);

        STextButton createSelected = new STextButton("Create Game", Resources._instance.UI_Button, new Vector2(), new Vector2());
        createSelected.setGlobalFontDecrease(4);

        loadSelected.setOnClickAction(() -> {
            System.out.println("load clicked");
            Main._instance.switchScreen(Main.GAME_SCREEN);
            Main._instance.tick.start();
        });
        deleteSelected.setOnClickAction(() -> {
            System.out.println("del clicked");
            SaveManager.delete(selectedSave.getId());
//            Table wrapper = (Table) scrollPane.getActor();
//            Table content = (Table) wrapper.getChildren().first();
            content.removeActor(selectedSave);
            loadSelected.setEnabled(false);
            deleteSelected.setEnabled(false);
            rebuildSaves();
            selectedSave = null;
        });

        createSelected.setOnClickAction(()->{
            System.out.println("create clicked");
            showNameInputDialog();
        });

        addWorldObject(createSelected, 0.5f, 0.1f, 0.16f, 0.1f, Float.MAX_VALUE, 80);
        addWorldObject(loadSelected, 0.32f, 0.1f, 0.16f, 0.1f, Float.MAX_VALUE, 80);
        addWorldObject(deleteSelected, 0.68f, 0.1f, 0.16f, 0.1f, Float.MAX_VALUE, 80);

        table.add(buttonTable).center().padBottom(20);

        table.debugAll();
        addSaves();

        loadSelected.setEnabled(false);
        deleteSelected.setEnabled(false);
    }

    private Table createHeader() {
        Table header = new Table();
        header.defaults().pad(10);

        header.add(new Label("Name", Resources._instance.skin))
            .expandX().left();
        header.add(new Label("Playtime", Resources._instance.skin))
            .width(100).center();
        header.add(new Label("Last played", Resources._instance.skin))
            .width(150).right();

        return header;
    }

    public void showNameInputDialog() {
        Skin skin = Resources._instance.skin;

        TextField textField = new TextField("", skin);
        textField.setMessageText("Save Name");

        Label error = new Label("", skin);
        error.setText("Valid: a-z, A-Z, -_, 0-9\n ");

        Dialog dialog = new Dialog("Enter Save Name", skin) {
            @Override
            protected void result(Object object) {
                if(object == null){
                    hide();
                    return;
                }

                String name = textField.getText();
                if(name.isEmpty() || name.length() > 16){
                    cancel();
                    error.setText("Invalid length it has to be\nbetween 1 - 16");
                    textField.setText(textField.getText().subSequence(0, 16).toString());
                    return;
                }
                if(!name.matches("[a-zA-Z0-9 _-]{1,16}")){
                    cancel();
                    error.setText("invalid Characters\n ");
                    return;
                }

                Main._instance.currentGameStateId = SaveManager.getNewId();
                Main._instance.switchScreen(Main.GAME_SCREEN);
                Main._instance.gameLogic.getGameState().setName(name);
            }
        };

        dialog.getContentTable().add(textField).width(300).row();
        dialog.getContentTable().add(error).width(400);
        dialog.button("OK", true); // OK-Button
        dialog.button("Cancel", null); // Cancel-Button

        dialog.show(stage);
    }

    private void addSaves(){
        List<Integer> availableIds = SaveManager.getAvailableIds();
        if(availableIds.isEmpty()) return;

        Table wrapper = (Table) scrollPane.getActor();
        Table content = (Table) wrapper.getChildren().first();

        for (Integer id : availableIds) {
            SavePreview sP = new SavePreview(SaveManager.loadGameState(id), id);
            content.add(sP).row();

            sP.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
//                    event.stop();
                    if(selectedSave != null){
                        selectedSave.setSelected(false);
                    }
                    selectedSave = sP;
                    sP.setSelected(true);
                    Main._instance.currentGameStateId = sP.getId();
                    loadSelected.setEnabled(true);
                    deleteSelected.setEnabled(true);
                    super.clicked(event, x, y);
                }
            });
        }
//        table.debugAll();
    }

    private void rebuildSaves() {
        Table wrapper = (Table) scrollPane.getActor();
        Table content = (Table) wrapper.getChildren().first();

        content.clearChildren();

        List<Integer> ids = SaveManager.getAvailableIds();
        for (Integer id : ids) {
            SavePreview sP = new SavePreview(SaveManager.loadGameState(id), id);
            content.add(sP).row();

            sP.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (selectedSave != null) selectedSave.setSelected(false);
                    selectedSave = sP;
                    sP.setSelected(true);
                    Main._instance.currentGameStateId = sP.getId();
                    loadSelected.setEnabled(true);
                    deleteSelected.setEnabled(true);
                }
            });
        }

        content.invalidateHierarchy();
        scrollPane.layout();
    }

    @Override
    protected void createWorld() {}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        cell.prefWidth(stage.getWidth() * 0.5f);
    }
}
