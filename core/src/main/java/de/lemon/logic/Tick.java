package de.lemon.logic;

import de.lemon.listeners.TickListener;
import de.lemon.logic.interfaces.Listenable;
import de.lemon.main.Main;

import java.util.concurrent.CopyOnWriteArrayList;

public class Tick implements Listenable<TickListener> {

    private float timeScale = 1f;
    private boolean running = false;

    CopyOnWriteArrayList<TickListener> listeners = new CopyOnWriteArrayList<>();

    public Tick(){}

    @Override
    public void addListener(TickListener listener){
        listeners.add(listener);
    }

    @Override
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
        Main._instance.initScreens();
        running = true;
    }

    public void stopp(){
        running = false;
    }
}
