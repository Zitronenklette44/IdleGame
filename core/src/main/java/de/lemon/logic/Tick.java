package de.lemon.logic;

import de.lemon.listeners.TickListener;

import java.util.concurrent.CopyOnWriteArrayList;

public class Tick {

    private float timeScale = 1f;
    private boolean running = false;

    CopyOnWriteArrayList<TickListener> listeners = new CopyOnWriteArrayList<>();

    public Tick(){}

    public void addListener(TickListener listener){
        listeners.add(listener);
    }

    public void removeListener(TickListener listener){
        listeners.remove(listener);
    }

    public void update(float delta){
        if(!running) return;
        for(TickListener l : listeners) l.onTick(delta * timeScale);
    }

    public float getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = Math.max(0, timeScale);
    }

    public void start(){
        running = true;
    }

    public void stopp(){
        running = false;
    }
}
