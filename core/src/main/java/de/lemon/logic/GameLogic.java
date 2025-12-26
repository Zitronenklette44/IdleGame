package de.lemon.logic;

import de.lemon.core.GameState;
import de.lemon.listeners.TickListener;
import de.lemon.mechanics.Mana;

public class GameLogic {
    private final GameState gameState;
    private final TickListener tickListener;
    private final Mana mana;

    public GameLogic(GameState gameState){
        this.gameState = gameState;
        mana = new Mana(gameState);
        tickListener = new TickListener(){
            @Override
            public void onTick(float delta) {
                super.onTick(delta);
            }

            @Override
            protected void onSecond() {
                onSecondAction();
                super.onSecond();
            }
        };
    }

    private void onSecondAction(){
        // ===== mana =====
        int addMana = mana.getManaPerSecond();
        int currentMana = gameState.getMana();
        int newMana = currentMana + addMana;
        int manaCapacity = mana.getManaCapacity();
        gameState.setMana(Math.min(manaCapacity, newMana));


    }



    public TickListener getTickListener() {
        return tickListener;
    }
    public GameState getGameState() {
        return gameState;
    }
}
