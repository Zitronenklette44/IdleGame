package de.lemon.screens;

import com.badlogic.gdx.graphics.Color;
import de.lemon.listeners.CustomerListener;
import de.lemon.logic.enums.ScreenFeatures;
import de.lemon.logic.render.AnimatedSprite;
import de.lemon.main.Main;
import de.lemon.mechanics.customer.Customer;
import de.lemon.mechanics.customer.CustomerSystem;
import de.lemon.utilities.DebugLogger;

import java.util.EnumSet;

public class ShowroomScreen extends CoreScreen{

    Customer[] customers = CustomerSystem._instance.getCustomers();
    CustomerListener listener;

    @Override
    public EnumSet<ScreenFeatures> getFeatures() {
        return EnumSet.of(ScreenFeatures.WORLD, ScreenFeatures.DIALOG);
    }

    @Override
    protected void createComponents() {}

    @Override
    protected void createWorld() {
        AnimatedSprite door = new AnimatedSprite("door", 1, 72, 16, 0.1f, true) {
            @Override
            public void onClick(int button) {
                Main._instance.switchScreen(Main.GAME_SCREEN);
            }
        };
        door.setClickable(true);
        addWorldObject(door, 4 / 10f, 2.6f/100f, 1/7.2f, 3/10f);
    }

    @Override
    public void show() {
        super.show();
        listener = new CustomerListener(){
            @Override
            public void onNewSpawn() {
                super.onNewSpawn();
                refreshCustomers();
                DebugLogger.printInfo("new Spawn");
            }
        };
        refreshCustomers();
        backgroundColor = Color.GRAY;
    }

    public void refreshCustomers(){
        for(Customer c : customers){
            if(c != null){
                removeWordObject(c.getDisplay());
            }
        }
        customers = CustomerSystem._instance.getCustomers();
        for (int i = 0; i < customers.length; i++) {
            Customer c = customers[i];
            if(c != null) addWorldObject(c.getDisplay(), .5f + 0.15f * i, .5f, .2f, .2f);
        }
        revalidateLayout();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(CustomerSystem._instance.needsUpdate()){
            refreshCustomers();
            CustomerSystem._instance.setNeedsUpdate(false);
        }
    }

    @Override
    public void hide() {
        super.hide();
        listener.dispose();
    }
}
