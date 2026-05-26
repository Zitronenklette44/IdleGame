package de.lemon.core;

import de.lemon.logic.enums.Upgrades;
import de.lemon.mechanics.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private int mana;
    private Map<String, Integer> upgrades = new HashMap<>();
    private String name;
    private long lastPlayed;
    private long playtime = 0;
    private Inventory inventory;
    private float money = 0;

    public GameState(){}

    /**
     * @return current mana value. Never negative.
     */
    public int getMana() {
        return mana;
    }
    /**
     * Sets the mana value. Negative values are clamped to 0.
     *
     * @param mana new mana value
     */
    public void setMana(int mana) {
        this.mana = Math.max(0, mana);
    }

    /**
     * Checks whether a specific upgrade is owned and has a level greater than 0.
     *
     * @param upgrade upgrade to check
     * @return true if the upgrade exists and has at least level 1
     */
    public boolean hasUpgrade(Upgrades upgrade){
        return  upgrades.containsKey(upgrade.name()) && upgrades.get(upgrade.name()) > 0;
    }
    /**
     * Returns the current level of an upgrade.
     *
     * @param upgrade upgrade to query
     * @return level of the upgrade or 0 if not owned
     */
    public int getLevelOf(Upgrades upgrade){
        return  upgrades.getOrDefault(upgrade.name(), 0);
    }
    /**
     * Adds an upgrade with level 1 if it does not already exist.
     *
     * @param upgrade upgrade to add
     */
    public void addUpgrade(Upgrades upgrade){
        upgrades.putIfAbsent(upgrade.name(), 1);
    }
    /**
     * Sets the level of an upgrade. If the level is 0 or lower,
     * the upgrade will be removed.
     *
     * @param upgrade upgrade to modify
     * @param level new level of the upgrade
     */
    public void setUpgradeLevel(Upgrades upgrade, int level) {
        if(level <= 0){
            upgrades.remove(upgrade.name());
            return;
        }
        upgrades.put(upgrade.name(), level);
    }
    /**
     * Increases or decreases the level of an upgrade.
     * The resulting level will never be lower than 1.
     * If the upgrade does not exist yet, it will be created.
     *
     * @param upgrade upgrade to modify
     * @param level amount to add to the current level
     */
    public void addUpgradeLevel(Upgrades upgrade, int level){
        addUpgrade(upgrade);
        int currentLevel = upgrades.get(upgrade.name());
        int newLevel = Math.max(1, currentLevel + level);
        upgrades.put(upgrade.name(), newLevel);
    }
    /**
     * Increases the level of an upgrade by 1.
     *
     * @param upgrade upgrade to level up
     */
    public void addUpgradeLevel(Upgrades upgrade){
        addUpgradeLevel(upgrade, 1);
    }
    /**
     * Adds mana to the current mana pool.
     * Resulting mana will never be negative.
     *
     * @param mana amount of mana to add (can be negative)
     */
    public void addMana(int mana) {
        setMana(this.mana + mana);
    }
    /**
     * @return save File name
     */

    public String getName() {
        return name;
    }
    /**
     * Sets the Save File name.
     *
     * @param name new save File name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Clears important runtime data.
     * Used when disposing the game state.
     */
    public void dispose() {
        mana = 0;
        upgrades = null;
        name = null;
    }

    /**
     * @return total playtime in milliseconds
     */
    public long getPlaytime() {
        return playtime;
    }

    /**
     * Sets the total playtime.
     *
     * @param playtime playtime in milliseconds
     */
    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    /**
     * @return timestamp of the last played session
     */
    public long getLastPlayed() {
        return lastPlayed;
    }
    /**
     * Sets the timestamp of the last played session.
     *
     * @param lastPlayed timestamp in milliseconds
     */
    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
    /**
     * @return player's inventory instance
     */
    public Inventory getInventory() {
        return inventory;
    }
    /**
     * Replaces the current inventory.
     *
     * @param inventory new inventory instance
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void addMoney(float money){
        this.money += money;
    }
}
