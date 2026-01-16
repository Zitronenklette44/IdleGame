package de.lemon.logic.interfaces;

public interface Clickable {
    boolean isClickable();
    boolean contains(float x, float y);
    void onClick(int button);
}
