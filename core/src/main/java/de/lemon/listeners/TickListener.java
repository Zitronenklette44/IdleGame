package de.lemon.listeners;

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

}
