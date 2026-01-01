package de.lemon.mechanics.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.lemon.animation.SimpleSprite;
import de.lemon.listeners.TickListener;
import de.lemon.main.Main;

public class PlantLogic extends SimpleSprite {

    private int stage = 0;
    private float growTime;
    private float currentGrowthTime;

    private TickListener tickListener;

    public PlantLogic(Texture texture, int frameWidth, int frameHeight, Vector2 pos, float growTime) {
        super(texture, frameWidth, frameHeight, false, pos);
        this.growTime = growTime;
        addListener();
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
        this(plant.texture, plant.frameWidth, plant.frameHeight, new Vector2() ,plant.growTime);
    }

    public void changePlant(Texture texture, int frameWidth, int frameHeight, float growTime){
        this.growTime = growTime;

        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        createAnimation(splitTexture(texture, 0), 1, false);
        stage = 0;
        tickListener.dispose();
        addListener();
    }

    public void changePlant(Plant plant){
        changePlant(plant.texture, plant.frameWidth, plant.frameHeight, plant.growTime);
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
        }
    }

}
