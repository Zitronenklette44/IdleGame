package de.lemon.logic.interfaces;

public interface Hoverable {
    void onEnter();
    boolean contains(float x, float y);
    void onExit();
}
