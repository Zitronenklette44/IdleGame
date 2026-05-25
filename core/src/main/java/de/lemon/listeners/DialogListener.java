package de.lemon.listeners;

import de.lemon.mechanics.dialog.DialogSystem;

public class DialogListener {
    public  DialogListener(){
        DialogSystem._instance.add(this);
    }

    public void dispose() {
        DialogSystem._instance.remove(this);
    }

    public void onStart(){}
    public void onUpdate(){}
    public void onNext(){}
    public void onFinish(){}
    public void onCancel() {}
}
