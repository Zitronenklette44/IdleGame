package de.lemon.core;

import de.lemon.enums.Upgrades;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private int mana;
    private final Map<String, Integer> upgrades = new HashMap<>();


    public int getMana() {
        return mana;
    }
    public void setMana(int mana) {
        this.mana = Math.max(0, mana);
    }

    public boolean hasUpgrade(Upgrades upgrade){
        return  upgrades.containsKey(upgrade.name()) && upgrades.get(upgrade.name()) > 0;
    }
    public int getLevelOf(Upgrades upgrade){
        return  upgrades.getOrDefault(upgrade.name(), 0);
    }
    public void addUpgrade(Upgrades upgrade){
        upgrades.putIfAbsent(upgrade.name(), 1);
    }
    public void setUpgradeLevel(Upgrades upgrade, int level) {
        if(level <= 0){
            upgrades.remove(upgrade.name());
        }
        upgrades.put(upgrade.name(), level);
    }
    public void addUpgradeLevel(Upgrades upgrade, int level){
        int currentLevel = upgrades.get(upgrade.name());
        int newLevel = Math.max(1, currentLevel + level);
        upgrades.put(upgrade.name(), newLevel);
    }
    public void addUpgradeLevel(Upgrades upgrade){
        addUpgradeLevel(upgrade, 1);
    }

    public void addMana(int mana) {
        setMana(this.mana + mana);
    }
}
