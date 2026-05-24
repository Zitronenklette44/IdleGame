package de.lemon.save.serializer;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import de.lemon.core.Item;

public class ItemSerializer implements Json.Serializer<Item> {
    @Override
    public void write(Json json, Item object, Class knownType) {
        json.writeObjectStart();
        json.writeValue("name", object.name);
        json.writeValue("quantity", object.quantity);
        json.writeValue("frameWidth", object.frameWidth);
        json.writeValue("frameHeight", object.frameHeight);
        json.writeObjectEnd();
    }

    @Override
    public Item read(Json json, JsonValue jsonData, Class type) {
        String name = jsonData.getString("name");
        int quantity = jsonData.getInt("quantity");
        int frameWidth = jsonData.getInt("frameWidth");
        int frameHeight = jsonData.getInt("frameHeight");
        return new Item(name, quantity, frameWidth, frameHeight);
    }
}
