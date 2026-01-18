package de.lemon.logic.interfaces;

public interface Hoverable {
    void onEnter();
    void onExit();
    boolean contains(float x, float y);
}
