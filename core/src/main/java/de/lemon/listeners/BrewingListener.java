package de.lemon.listeners;

import de.lemon.mechanics.brewing.BrewingSystem;

public class BrewingListener {

    public BrewingListener(){
        BrewingSystem._instance.addListener(this);
    }

    public void onStart(){}
    public void onFinish(boolean success){}
    public void onUpdate(){}

    public void dispose(){
        BrewingSystem._instance.removeListener(this);
    }

}
