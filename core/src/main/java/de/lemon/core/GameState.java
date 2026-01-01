package de.lemon.core;

import de.lemon.enums.Upgrades;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private int mana;
    private Map<String, Integer> upgrades = new HashMap<>();
    private String name;
    private long lastPlayed;
    private long playtime = 0;


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
            return;
        }
        upgrades.put(upgrade.name(), level);
    }
    public void addUpgradeLevel(Upgrades upgrade, int level){
        addUpgrade(upgrade);
        int currentLevel = upgrades.get(upgrade.name());
        int newLevel = Math.max(1, currentLevel + level);
        upgrades.put(upgrade.name(), newLevel);
//        System.out.println("upgrade: "+ upgrade.name() + " currentLevel: " + currentLevel + " newLevel: " + newLevel + " savedLevel: " + upgrades.get(upgrade.name()));
    }
    public void addUpgradeLevel(Upgrades upgrade){
        addUpgradeLevel(upgrade, 1);
    }

    public void addMana(int mana) {
        setMana(this.mana + mana);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void dispose() {
        mana = 0;
        upgrades = null;
        name = null;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
}
