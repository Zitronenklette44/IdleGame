package de.lemon.mechanics.brewing.events;

public class EventAction {

    private final String action;
    private final float factor;

    public EventAction(String action, float factor){
        this.action = action;
        this.factor = factor;
    }

    @Override
    public String toString() {
        return "Action: {"+action + " | " + factor + "}";
    }
}
