package de.lemon.mechanics.plants;

import com.badlogic.gdx.graphics.Texture;
import de.lemon.core.Resources;

public class Plant {

    public Texture texture;
    public int frameWidth;
    public int frameHeight;
    public int growTime;
    public int dropAmount;
    public Plants plantID;

    public Plant(Texture texture, int frameWidth, int frameHeight, int growTime, int dropAmount, Plants plantID){
        this.texture = texture;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.growTime = growTime;
        this.dropAmount = dropAmount;
        this.plantID = plantID;
    }

    public static Plant getNewPlant(Plants plant){
        switch (plant){
            case BLATTRUBIN:
                return new Plant(Resources._instance.plants_01, 64, 64, 15, 3, Plants.BLATTRUBIN);
        }

        return new Plant(null, 0, 0, 0, 0, null);
    }


}
