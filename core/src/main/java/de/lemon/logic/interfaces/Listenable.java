package de.lemon.logic.interfaces;

public interface Listenable<T> {
    void addListener(T listener);
    void removeListener(T listener);
}
