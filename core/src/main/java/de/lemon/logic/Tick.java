package de.lemon.logic;

import de.lemon.listeners.TickListener;

import java.util.ArrayList;

public class Tick {

    private float timeScale = 1f;

    ArrayList<TickListener> listeners = new ArrayList<>();

    public Tick(){}

    public void addListener(TickListener listener){
        listeners.add(listener);
    }

    public void removeListener(TickListener listener){
        listeners.remove(listener);
    }

    public void update(float delta){
        for(TickListener l : listeners) l.onTick(delta * timeScale);
    }

    public float getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = Math.max(0, timeScale);
    }
}
