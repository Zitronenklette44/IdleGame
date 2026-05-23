package de.lemon.mechanics.plants;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.lemon.core.Resources;
import de.lemon.logic.enums.Plants;
import de.lemon.logic.render.SimpleSprite;
import de.lemon.listeners.TickListener;
import de.lemon.main.Main;
import de.lemon.mechanics.Inventory;

public class PlantLogic extends SimpleSprite {

    private int stage = 0;
    private float growTime;
    private int dropAmount;
    private Plants plantID;
    private float currentGrowthTime;
    private boolean fullyGrown = false;

    private TickListener tickListener;

    public PlantLogic(String textureName, int frameWidth, int frameHeight, float growTime, int dropAmount, Plants plantID) {
        super(textureName, frameWidth, frameHeight, false);
        this.growTime = growTime;
        this.dropAmount = dropAmount;
        this.plantID = plantID;
        addListener();
        fullyGrown = false;
    }

    private void addListener(){
        tickListener = new TickListener(){
            @Override
            protected void onSecond() {
                PlantLogic.this.onTick();
                super.onSecond();
            }
        };
        Main._instance.tick.addListener(tickListener);
    }

    public PlantLogic(Plant plant){
        this(plant.textureName, plant.frameWidth, plant.frameHeight ,plant.growTime, plant.dropAmount, plant.plantID);
    }

    public void changePlant(String textureName, int frameWidth, int frameHeight, float growTime, int dropAmount, Plants plantID){
        this.dropAmount = dropAmount;
        this.plantID = plantID;
        this.growTime = growTime;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        createAnimation(splitTexture(Resources._instance.getTexture(textureName), 0), 1, false);
        stage = 0;
        tickListener.dispose();
        addListener();
        fullyGrown = false;
    }

    public void changePlant(Plant plant){
        changePlant(plant.textureName, plant.frameWidth, plant.frameHeight, plant.growTime, plant.dropAmount, plant.plantID);
    }

    public void onTick() {
        currentGrowthTime++;

        float progress = currentGrowthTime / growTime;
        progress = Math.min(progress, 1f);

        int frames = animation.getKeyFrames().length;
        stage = (int) (progress * (frames - 1));

        setFrame(stage);

        if(progress >= 1f){
            Main._instance.tick.removeListener(tickListener);
            fullyGrown = true;
        }
    }

    @Override
    public void onClick(int button) {
        if(button == Input.Buttons.LEFT){
            if(fullyGrown){
                Inventory._instance.addPlant(plantID, dropAmount);
                fullyGrown = false;
                currentGrowthTime = 0;
                stateTime = 0;
                tickListener.dispose();
                addListener();
            }
        }
    }
}
