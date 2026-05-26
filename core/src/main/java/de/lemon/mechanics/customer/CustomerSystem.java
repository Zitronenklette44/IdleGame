package de.lemon.mechanics.customer;

import com.badlogic.gdx.math.MathUtils;
import de.lemon.core.Item;
import de.lemon.listeners.CustomerListener;
import de.lemon.listeners.TickListener;
import de.lemon.logic.interfaces.Listenable;
import de.lemon.main.Main;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomerSystem implements Listenable<CustomerListener> {
    public static CustomerSystem _instance = new CustomerSystem();

    Customer[] customers = new Customer[2];
//    float spawnChance = 0.02f;
    float spawnChance = 0.2f;
    public ArrayList<CustomerListener> listeners = new ArrayList<>();
    boolean needUpdate = false;

     private CustomerSystem(){
         TickListener listener = new TickListener(){
             @Override
             protected void onSecond() {
                 super.onSecond();
                 spawnCustomer();
             }
         };
         Main._instance.tick.addListener(listener);
     }

     private void spawnCustomer(){
         for (int i = 0; i < customers.length; i++) {
             if(customers[i] == null){
                 float value = MathUtils.random(0, 100);
                 if(value <= 100 * spawnChance){
                     Item desiredItem = new Item("healingPotion", MathUtils.random(1, 5), 32, 32);
                     customers[i] = new Customer("unknownSpeaker", 32, 32, desiredItem, MathUtils.random(5, 10), "villagerPotionBuy");
                     for(CustomerListener l : listeners) l.onNewSpawn();
                 }
             }
         }
     }

     public void removeCustomer(Customer c){
         for (int i = 0; i < customers.length; i++) {
             if(customers[i] != null && customers[i] == c) {
                 customers[i] = null;
                 needUpdate = true;
                 return;
             }
         }
     }

    public Customer[] getCustomers() {
        return Arrays.copyOf(customers, 2);
    }

    @Override
    public void addListener(CustomerListener customerListener) {
        listeners.add(customerListener);
    }
    @Override
    public void removeListener(CustomerListener customerListener) {
         listeners.remove(customerListener);
    }

    public boolean needsUpdate() {
        return needUpdate;
    }

    public void setNeedsUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
}
