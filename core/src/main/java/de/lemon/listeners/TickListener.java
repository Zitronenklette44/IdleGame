package de.lemon.listeners;

import de.lemon.main.Main;

public class TickListener {
    private float time;

    public void onTick(float delta){
        time+=delta;

        if(time >= 1){
            onSecond();
            time -= 1;
        }
    }

    protected void onSecond(){}

    public void dispose() {
        Main._instance.tick.removeListener(this);
    }
}
