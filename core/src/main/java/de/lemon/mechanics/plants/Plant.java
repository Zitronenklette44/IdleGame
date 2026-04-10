package de.lemon.mechanics.plants;

import com.badlogic.gdx.graphics.Texture;
import de.lemon.core.Resources;
import de.lemon.logic.enums.Plants;

public class Plant {

    public String textureName;
    public int frameWidth;
    public int frameHeight;
    public int growTime;
    public int dropAmount;
    public Plants plantID;

    public Plant(String textureName, int frameWidth, int frameHeight, int growTime, int dropAmount, Plants plantID){
        this.textureName = textureName;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.growTime = growTime;
        this.dropAmount = dropAmount;
        this.plantID = plantID;
    }

    public static Plant getNewPlant(Plants plant){
        switch (plant){
            case BLATTRUBIN:
                return new Plant("plant_01", 32, 40, 15, 3, Plants.BLATTRUBIN);
        }

        return new Plant(null, 0, 0, 0, 0, null);
    }


}
