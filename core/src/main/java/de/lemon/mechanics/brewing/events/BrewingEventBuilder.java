package de.lemon.mechanics.brewing.events;

public class BrewingEventBuilder {
    private String id;
    private String title;
    private String description;
    private EventAction good;
    private EventAction normal;
    private EventAction negative;

    public BrewingEventBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public BrewingEventBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public BrewingEventBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public BrewingEventBuilder setGood(EventAction good) {
        this.good = good;
        return this;
    }

    public BrewingEventBuilder setNormal(EventAction normal) {
        this.normal = normal;
        return this;
    }

    public BrewingEventBuilder setNegative(EventAction negative) {
        this.negative = negative;
        return this;
    }

    public BrewingEvent createBrewingEvent() {
        return new BrewingEvent(id, title, description, good, normal, negative);
    }
}
