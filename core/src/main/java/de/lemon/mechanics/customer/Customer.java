package de.lemon.mechanics.customer;

import de.lemon.core.Item;
import de.lemon.listeners.DialogListener;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;
import de.lemon.mechanics.dialog.DialogSystem;
import de.lemon.utilities.DebugLogger;

public class Customer {

    Item desiredItem;
    int rewardGold;

    String dialogName;

    CustomerDisplay display;

    boolean transaktionCompleted = false;
    DialogListener listener;

    public Customer(String textureName, int frameWidth, int frameHeight, Item desiredItem, int rewardGold, String dialogName){
        this.desiredItem = desiredItem;
        this.rewardGold = rewardGold;
        this.dialogName = dialogName;
        display = new CustomerDisplay(textureName, frameWidth, frameHeight, this);
    }

    public CustomerDisplay getDisplay() {
        return display;
    }

    public void startDialog() {
        DebugLogger.printInfo("trying to start");
        if(DialogSystem._instance.isActive()) return;
        if(listener != null) listener.dispose();
        listener = new DialogListener(){
            @Override
            public void onFinish() {
                super.onFinish();
                completeTransaction();
            }
        };
        DebugLogger.printInfo("init listener");
        DebugLogger.printInfo("trying to start dialog currentState:" + DialogSystem._instance.isActive());
        DialogSystem._instance.startDialog(dialogName);
        DialogSystem._instance.getCurrentDialog().replaceVariableWithValue("quantity", desiredItem.quantity + "");
        DialogSystem._instance.getCurrentDialog().replaceVariableWithValue("amount", rewardGold + "");
        DebugLogger.printInfo("started Dialog:" + DialogSystem._instance.isActive());
    }

    private void completeTransaction() {
        if(Inventory._instance.contains(desiredItem, desiredItem.quantity)){
            Inventory._instance.removeItem(desiredItem);
            Main._instance.gameLogic.getGameState().addMoney(rewardGold);
            transaktionCompleted = true;
            CustomerSystem._instance.removeCustomer(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Customer)) return false;
        Customer c = (Customer) obj;
        return display.sprite.getTextureName().equals(c.display.sprite.getTextureName()) &&
            desiredItem.equals(c.desiredItem) &&
            desiredItem.quantity == c.desiredItem.quantity &&
            rewardGold == c.rewardGold &&
            dialogName.equals(c.dialogName);
    }

    public boolean isTransaktionCompleted() {
        return transaktionCompleted;
    }
}
