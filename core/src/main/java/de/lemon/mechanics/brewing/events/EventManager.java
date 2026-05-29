package de.lemon.mechanics.brewing.events;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import de.lemon.logic.enums.Impact;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;

public class EventManager {
    public static EventManager _instance = new EventManager();

    private final ArrayList<BrewingEvent> lowEvents = new ArrayList<>();
    private final ArrayList<BrewingEvent> normalEvents = new ArrayList<>();
    private final ArrayList<BrewingEvent> highEvents = new ArrayList<>();
    private final ArrayList<BrewingEvent> criticalEvents = new ArrayList<>();

    private EventManager(){}

    public void init(FileHandle file){
        JsonValue json = new JsonReader().parse(file.readString());
        JsonValue events = json.get("events");
        splitEvents(lowEvents, events.get("low"));
        splitEvents(normalEvents, events.get("normal"));
        splitEvents(highEvents, events.get("high"));
        splitEvents(criticalEvents, events.get("critical"));
    }

    private void splitEvents(ArrayList<BrewingEvent> list, JsonValue impact){
        for(JsonValue event = impact.child; event != null; event = event.next){
            BrewingEventBuilder builder = new BrewingEventBuilder();
            builder.setId(event.getString("id"));
            builder.setTitle(event.getString("title"));
            builder.setDescription(event.getString("description"));

            JsonValue actions = event.get("options");
            builder.setGood(castActions(actions.get("good")));
            builder.setNormal(castActions(actions.get("neutral")));
            builder.setNegative(castActions(actions.get("bad")));
            list.add(builder.createBrewingEvent());
        }
    }
    private EventAction castActions(JsonValue option){
        String action = option.getString("action");
        float factor = option.getFloat("factor");
        return new EventAction(action, factor);
    }

    public BrewingEvent getRandomEvent(Impact impact){
        switch (impact){
            case LOW -> {
                return lowEvents.get(MathUtils.random(lowEvents.size() - 1));
            }
            case NORMAL -> {
                return normalEvents.get(MathUtils.random(normalEvents.size() - 1));
            }
            case HIGH -> {
                return highEvents.get(MathUtils.random(highEvents.size() - 1));
            }
            case CRITICAL -> {
                return criticalEvents.get(MathUtils.random(criticalEvents.size() - 1));
            }
        }
        DebugLogger.printError("Invalid Impact: " + impact);
        return null;
    }
}
