package de.lemon.mechanics.dialog;

import de.lemon.core.Resources;
import de.lemon.listeners.DialogListener;
import de.lemon.listeners.TickListener;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.interfaces.Listenable;
import de.lemon.main.Main;
import de.lemon.screens.CoreScreen;
import de.lemon.ui.DialogOverlay;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;

public class DialogSystem implements Listenable<DialogListener> {

    public static DialogSystem _instance = new DialogSystem();

    private TickListener tickListener;
    private DialogOverlay overlay;
    private Dialog currentDialog;

    private boolean active = false;

    private final ArrayList<DialogListener> listeners = new ArrayList<>();

    // Token typing state
    private int visibleTokenIndex = 0;
    private int visibleCharIndex = 0;

    private final float charDelay = 0.03f;

    public DialogSystem() {

        tickListener = new TickListener() {
            float counter = 0;

            @Override
            public void onTick(float delta) {
                super.onTick(delta);

                counter += delta;

                while (counter >= charDelay) {
                    update();
                    counter -= charDelay;
                }
            }
        };
    }

    private void update() {

        if (currentDialog == null) return;

        String text = currentDialog.getVisibleText(visibleTokenIndex, visibleCharIndex);
        overlay.showText(text);
        if (currentDialog.allVisible(visibleTokenIndex, visibleCharIndex)) {
            for (DialogListener l : listeners) l.onUpdate();
            return;
        }
        visibleCharIndex++;

        if (visibleTokenIndex < currentDialog.getTokensSize()) {
            int tokenLength = currentDialog.getCurrentTokenLength(visibleTokenIndex);
            if (visibleCharIndex >= tokenLength) {
                visibleTokenIndex++;
                visibleCharIndex = 0;
            }
        }

        if (visibleTokenIndex >= currentDialog.getTokensSize()) {
            visibleTokenIndex = currentDialog.getTokensSize() - 1;
            visibleCharIndex = currentDialog.getLastTokenLength();
        }

        for (DialogListener l : listeners) l.onUpdate();
    }

    public void startDialog(String name) {
        if (active) {
            DebugLogger.printError("Can not start a new Dialog because one is currently active");
            return;
        }
        CoreScreen screen = (CoreScreen) Main._instance.getScreen();
        if (!screen.getFeatures().contains(ScreenFeatures.DIALOG)) {
            DebugLogger.printError("unable to show Dialog due to screen not supporting");
            return;
        }

        Main._instance.tick.addListener(tickListener);
        active = true;
        DialogData data = Resources._instance.getDialogData(name);
        overlay = screen.getDialogOverlay();
        overlay.showName(data.speaker);
        overlay.showTitle(data.title);
        overlay.showPortrait(data.textureName, data.frameWidth, data.frameHeight);
        overlay.setVisible(true);
        screen.revalidateLayout();
        currentDialog = new Dialog(data);

        visibleTokenIndex = 0;
        visibleCharIndex = 0;

        for (DialogListener l : listeners) {
            l.onStart();
        }
    }

    public void handleInput() {
        if (currentDialog == null) return;
        if (currentDialog.allVisible(visibleTokenIndex, visibleCharIndex)) {
            if (currentDialog.isLastLine()) {
                close();
                return;
            }
            currentDialog.next();
            visibleTokenIndex = 0;
            visibleCharIndex = 0;

            for (DialogListener l : listeners) {
                l.onNext();
            }

        } else {
            visibleTokenIndex = currentDialog.getTokensSize() - 1;
            visibleCharIndex = currentDialog.getLastTokenLength();
        }
    }

    public void cancel() {
        if (!active) return;
        close();
        for (DialogListener l : listeners) {
            l.onCancel();
        }
    }

    public void close() {
        if (!active) return;

        overlay.setVisible(false);

        Main._instance.tick.removeListener(tickListener);

        active = false;
        currentDialog = null;

        visibleTokenIndex = 0;
        visibleCharIndex = 0;

        for (DialogListener l : listeners) {
            l.onFinish();
        }
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void addListener(DialogListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(DialogListener listener) {
        listeners.remove(listener);
    }

    public Dialog getCurrentDialog() {
        return currentDialog;
    }

    public boolean isAllVisible() {
        return currentDialog.allVisible(visibleTokenIndex, visibleCharIndex);
    }

    public DialogRenderState getRenderState() {
        if (currentDialog == null) return null;
        return new DialogRenderState(
            currentDialog,
            visibleTokenIndex,
            visibleCharIndex
        );
    }
}
