package de.lemon.listeners;

import de.lemon.mechanics.customer.CustomerSystem;

public class CustomerListener {

    public CustomerListener(){
        CustomerSystem._instance.addListener(this);
    }

    public void onNewSpawn(){}


    public void dispose(){
        CustomerSystem._instance.removeListener(this);
    }
}
