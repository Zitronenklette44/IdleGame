package de.lemon.mechanics.brewing.events;

public class BrewingEvent {

    private final String id;
    private final String title;
    private final String description;
    private final EventAction good;
    private final EventAction normal;
    private final EventAction negative;

    public BrewingEvent(String id, String title, String description, EventAction good, EventAction normal, EventAction negative){
        this.id = id;
        this.title = title;
        this.description = description;
        this.good = good;
        this.normal = normal;
        this.negative = negative;
    }


    @Override
    public String toString() {
        return "Event: " + "\n  id:" + id +
            "\n  title:" + title +
            "\n  description:" + description +
            "\n  good:" + good.toString() +
            "\n  normal:" + normal.toString() +
            "\n  bad:" + negative.toString();
    }
}
