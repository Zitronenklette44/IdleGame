package de.lemon.mechanics.brewing.potions;

import de.lemon.core.Item;

public class Potion extends Item {
    public Potion(String name, int quantity, int frameWidth, int frameHeight) {
        super(name, quantity, 1, frameWidth, frameHeight);
    }
}
