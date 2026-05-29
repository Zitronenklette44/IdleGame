package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.lemon.core.GameState;
import de.lemon.core.Resources;
import de.lemon.logic.GameLogic;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;
import de.lemon.save.SaveManager;
import de.lemon.save.SavePreview;
import de.lemon.ui.AnimatedButton;
import de.lemon.ui.STextButton;
import de.lemon.ui.TButton;
import de.lemon.utilities.DebugLogger;

import java.util.EnumSet;
import java.util.List;

public class LoadScreen extends CoreScreen{

    private AnimatedButton bDeleteSelected;
    private AnimatedButton bLoadSelected;

    private SavePreview selectedSave = null;
    private SavePreview sP;
    private SavePreview sP1;
    private SavePreview sP2;

    @Override
    public EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.UI);
    }

    @Override
    protected void createComponents() {
        setBackgroundColor(Color.GRAY);

        bLoadSelected = new AnimatedButton("buttons", new int[] {12, 13, 14}, 96, 32);
        bLoadSelected.setAction(() -> {
            if(!selectedSave.isValid()){
                showNameInputDialog();
                return;
            }
            Main._instance.tick.start();
            Main._instance.switchScreen(Main.GAME_SCREEN);
        });
        addWorldObject(bLoadSelected, 0.32f, 0.1f, 0.16f, 0.1f);

//        AnimatedButton bCreateSelected = new AnimatedButton("buttons", new int[] {15, 16, 17}, 96, 32);
//        bCreateSelected.setAction(this::showNameInputDialog);
//        addWorldObject(bCreateSelected, 0.5f, 0.1f, 0.16f, 0.1f);

        bDeleteSelected = new AnimatedButton("buttons", new int[] {18, 19, 20}, 96, 32);
        bDeleteSelected.setAction(() -> {
            if(!selectedSave.isValid()) return;
            SaveManager.delete(selectedSave.getId());
            refreshSaves(false);
            bLoadSelected.setEnabled(false);
            bDeleteSelected.setEnabled(false);
        });
        addWorldObject(bDeleteSelected, 0.68f, 0.1f, 0.16f, 0.1f);

        bLoadSelected.setEnabled(false);
        bDeleteSelected.setEnabled(false);

        refreshSaves(true);
    }

    private void refreshSaves(boolean first){
        if(!first) {
            removeWordObject(sP);
            removeWordObject(sP1);
            removeWordObject(sP2);
        }
        selectedSave = null;

        sP = new SavePreview(SaveManager.loadGameState(0), 0);
        sP.setAction(()->{
            if (selectedSave != null) selectedSave.setSelected(false);
            selectedSave = sP;
            sP.setSelected(true);
            Main._instance.currentGameStateId = sP.getId();
            DebugLogger.printInfo("set Id to: " + sP.getId());
            bLoadSelected.setEnabled(true);
            bDeleteSelected.setEnabled(true);
        });
        addWorldObject(sP, .18f, .57f, .3f, .8f);

        sP1 = new SavePreview(SaveManager.loadGameState(1), 1);
        sP1.setAction(()->{
            if (selectedSave != null) selectedSave.setSelected(false);
            selectedSave = sP1;
            sP1.setSelected(true);
            Main._instance.currentGameStateId = sP1.getId();
            DebugLogger.printInfo("set Id to: " + sP1.getId());
            bLoadSelected.setEnabled(true);
            bDeleteSelected.setEnabled(true);
        });
        addWorldObject(sP1, .5f, .57f, .3f, .8f);

        sP2 = new SavePreview(SaveManager.loadGameState(2), 2);
        sP2.setAction(()->{
            if (selectedSave != null) selectedSave.setSelected(false);
            selectedSave = sP2;
            sP2.setSelected(true);
            Main._instance.currentGameStateId = sP2.getId();
            DebugLogger.printInfo("set Id to: " + sP2.getId());
            bLoadSelected.setEnabled(true);
            bDeleteSelected.setEnabled(true);
        });
        addWorldObject(sP2, .82f, .57f, .3f, .8f);
        revalidateLayout();
    }

    public void showNameInputDialog() {
        Skin skin = Resources._instance.getAsset("skin", Skin.class);

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

                Main._instance.tick.start();
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

    @Override
    protected void createWorld() {}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
        if(!GameLogic.fastLoad) return;
        Main._instance.currentGameStateId = 3;
        Main._instance.tick.start();
        Main._instance.switchScreen(Main.GAME_SCREEN);
    }
}
