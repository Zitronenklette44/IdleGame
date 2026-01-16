package de.lemon.mechanics.plants;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.animation.SimpleSprite;
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

    public PlantLogic(Texture texture, int frameWidth, int frameHeight, Vector2 pos, float growTime, int dropAmount, Plants plantID) {
        super(texture, frameWidth, frameHeight, false, pos);
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
        this(plant.texture, plant.frameWidth, plant.frameHeight, new Vector2() ,plant.growTime, plant.dropAmount, plant.plantID);
    }

    public void changePlant(Texture texture, int frameWidth, int frameHeight, float growTime, int dropAmount, Plants plantID){
        this.dropAmount = dropAmount;
        this.plantID = plantID;
        this.growTime = growTime;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        createAnimation(splitTexture(texture, 0), 1, false);
        stage = 0;
        tickListener.dispose();
        addListener();
        fullyGrown = false;
    }

    public void changePlant(Plant plant){
        changePlant(plant.texture, plant.frameWidth, plant.frameHeight, plant.growTime, plant.dropAmount, plant.plantID);
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
    protected void onTouchDown(int screenX, int screenY, int button) {
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
        super.onTouchDown(screenX, screenY, button);
    }
}
