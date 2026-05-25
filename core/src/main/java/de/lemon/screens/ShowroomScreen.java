package de.lemon.screens;

import de.lemon.listeners.DialogListener;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.mechanics.dialog.DialogSystem;
import de.lemon.utilities.DebugLogger;

import java.util.EnumSet;

public class ShowroomScreen extends CoreScreen{
    @Override
    public EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.DIALOG);
    }

    @Override
    protected void createComponents() {}

    @Override
    protected void createWorld() {

    }

    @Override
    public void show() {
        super.show();
        DialogListener listener = new DialogListener(){
            @Override
            public void onStart() {
                super.onStart();
                DebugLogger.printInfo("startDialog");
            }

            @Override
            public void onNext() {
                super.onNext();
                DebugLogger.printInfo("Dialog next");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DebugLogger.printInfo("Dialog finish");
            }

            @Override
            public void onUpdate() {
                super.onUpdate();
                DebugLogger.printInfo("Dialog update");
            }
        };
        DialogSystem._instance.startDialog("testLong");
    }
}
