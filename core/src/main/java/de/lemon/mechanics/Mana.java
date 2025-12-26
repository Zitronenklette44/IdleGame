package de.lemon.mechanics;

import de.lemon.core.GameState;
import de.lemon.enums.Upgrades;

public class Mana {

    private final GameState gameState;

    public Mana(GameState gameState){
        this.gameState = gameState;
    }

    public int getManaPerSecond(){
        int base = 0;

        if(gameState.hasUpgrade(Upgrades.MANA_GENERATION_PER_TICK)){
            base += gameState.getLevelOf(Upgrades.MANA_GENERATION_PER_TICK) * 2;
        }


        int multipier = 1;
        if(gameState.hasUpgrade(Upgrades.MANA_MULTIPLIER)){
            multipier += gameState.getLevelOf(Upgrades.MANA_MULTIPLIER);
        }

        if(gameState.hasUpgrade(Upgrades.GLOBAL_MULTIPLIER)){
            multipier += gameState.getLevelOf(Upgrades.GLOBAL_MULTIPLIER);
        }

        return base * multipier;
    }

    public int getManaCapacity(){
        int base = 100;
        int multiplier = 1;

        if(gameState.hasUpgrade(Upgrades.MANA_CAPACITY)){
            base += gameState.getLevelOf(Upgrades.MANA_CAPACITY) * 10;
        }

        return base * multiplier;
    }

}
