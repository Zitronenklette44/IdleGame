package de.lemon.mechanics.dialog;

import de.lemon.core.Resources;
import de.lemon.listeners.DialogListener;
import de.lemon.listeners.TickListener;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.main.Main;
import de.lemon.screens.CoreScreen;
import de.lemon.ui.DialogOverlay;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;

public class DialogSystem {

    public static DialogSystem _instance = new DialogSystem();

    private TickListener tickListener;
    private DialogOverlay overlay;
    private Dialog currentDialog;
    private int visibleChars = 0;
    private final float charDelay = 0.03f;
    private boolean active = false;
    private final ArrayList<DialogListener> listeners = new ArrayList<>();

    public DialogSystem(){
        tickListener = new TickListener(){
            float counter = 0;
            @Override
            public void onTick(float delta) {
                super.onTick(delta);
                counter += delta;
                while(counter >= charDelay) {
                    update();
                    counter-= charDelay;
                }

            }
        };
    }

    private void update() {
        String text = currentDialog.getChars(visibleChars);
        overlay.showText(text);
        if(!currentDialog.allVisible(visibleChars)) visibleChars++;
        for(DialogListener l : listeners) l.onUpdate();
    }

    public void startDialog(String name){
        Main._instance.tick.addListener(tickListener);
        active = true;
        DialogData data = Resources._instance.getDialogData(name);
        CoreScreen screen = (CoreScreen) Main._instance.getScreen();
        if(!screen.getFeatures().contains(ScreenFeatures.DIALOG)){
            DebugLogger.printError("unable to show Dialog due to screen not supporting");
            return;
        }
        overlay = screen.getDialogOverlay();
        overlay.showName(data.speaker);
        overlay.showTitle(data.title);
        overlay.showPortrait(data.textureName, data.frameWidth, data.frameHeight);
        screen.revalidateLayout();
        currentDialog = new Dialog(data);
        visibleChars = 0;
        for(DialogListener l : listeners) l.onStart();
    }

    public void cancel(){
        if(!active) return;
        Main._instance.tick.removeListener(tickListener);
        close();
        for(DialogListener l : listeners) l.onCancel();
    }

    public void handleInput() {
        if(currentDialog.isLastLine() && currentDialog.allVisible(visibleChars)) {
            close();
            return;
        }
        if(!currentDialog.allVisible(visibleChars)) visibleChars = currentDialog.getMaxChars();
        else {
            currentDialog.next();
            for(DialogListener l : listeners) l.onNext();
            visibleChars = 0;
        }
    }

    public void close(){
        overlay.setVisible(false);
        Main._instance.tick.removeListener(tickListener);
        active = false;
        for(DialogListener l : listeners) l.onFinish();
    }

    public boolean isAllVisible() {
        return currentDialog.allVisible(visibleChars);
    }

    public boolean isActive() {
        return active;
    }

    public void remove(DialogListener dialogListener) {

    }

    public void add(DialogListener dialogListener) {
    }
}
