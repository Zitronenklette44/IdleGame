package de.lemon.listeners;

import de.lemon.mechanics.dialog.DialogSystem;

public class DialogListener {
    public  DialogListener(){
        DialogSystem._instance.addListener(this);
    }

    public void dispose() {
        DialogSystem._instance.removeListener(this);
    }

    public void onStart(){}
    public void onUpdate(){}
    public void onNext(){}
    public void onFinish(){}
    public void onCancel() {}
}
