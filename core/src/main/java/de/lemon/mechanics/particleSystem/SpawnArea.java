package de.lemon.mechanics.particleSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.lemon.logic.enums.Direction;
import de.lemon.logic.enums.Geometric;

public class SpawnArea {
    public Vector2 pos = Vector2.Zero.cpy();
    public Vector2 size = pos.cpy();
    public Vector2 relSize = pos.cpy();

    public Geometric geometric;
    public Direction direction;

    public float rotation = 0;

    public SpawnArea() {}

    public Builder builder(){
        return new Builder(this);
    }

    public Vector2 getMovementDirection(Vector2 particlePos){
        Vector2 direction = new Vector2();
        switch(this.direction) {
            case FILLED:
                direction.set(MathUtils.random(-1f,1f), MathUtils.random(-1f,1f)).nor();
                break;
            case INWARDS:
                direction.set(pos).sub(particlePos).nor();
                break;
            case OUTWARDS:
                direction.set(MathUtils.random(-1f,1f), MathUtils.random(-1f,1f)).nor();
//                direction.set(particlePos).sub(pos).nor();
                break;
        }
        return direction;
//        switch (geometric){
//            case LINE:
//                return randomDirLINE(particlePos);
//            case RECTANGLE:
//                return randomDirRECT(particlePos);
//            case CIRCLE:
//                return randomDirCIRC(particlePos);
//        }
//        return null;
    }

    private Vector2 randomDirRECT(Vector2 particlePos){
        Vector2 direction = new Vector2();
        switch(this.direction) {
            case FILLED:
                direction.set(MathUtils.random(-1f,1f), MathUtils.random(-1f,1f)).nor();
                break;
            case INWARDS:
                direction.set(pos).sub(particlePos).nor();
                break;
            case OUTWARDS:
                direction.set(MathUtils.random(-1f,1f), MathUtils.random(-1f,1f)).nor();
//                direction.set(particlePos).sub(pos).nor();
                break;
        }
        return direction;
    }

    private Vector2 randomDirCIRC(Vector2 particlePos){
        return new Vector2();
    }

    private Vector2 randomDirLINE(Vector2 particlePos){
        return new Vector2();
    }

    public Vector2 getRandomParticlePos(){
        Vector2 returnValue = null;
        switch (geometric) {
            case LINE:
                returnValue = randomPosLINE();
                break;
            case RECTANGLE:
                returnValue = randomPosRECT();
                break;
            case CIRCLE:
                returnValue = randomPosCIRC();
                break;
        }
        return rotateAroundCenter(returnValue);
    }

    private Vector2 randomPosRECT(){
        Vector2 returnValue = new Vector2();
        float startX = pos.x - size.x / 2;
        float startY = pos.y - size.y / 2;
        float endX = pos.x + size.x / 2;
        float endY = pos.y + size.y / 2;

        float X, Y;

        switch (direction){
            case FILLED:
                X = MathUtils.random(startX, endX);
                Y = MathUtils.random(startY, endY);
                returnValue.set(X,Y);
                break;
            case INWARDS:
                boolean XAxis = MathUtils.randomBoolean();
                if(XAxis){
                    X = MathUtils.random(startX, endX);
                    boolean top = MathUtils.randomBoolean();
                    if(top) Y = endY;
                    else Y = startY;
                    returnValue.set(X, Y);
                } else {
                    Y = MathUtils.random(startY, endY);
                    boolean left = MathUtils.randomBoolean();
                    if(left) X = startX;
                    else X = endX;
                    returnValue.set(X, Y);
                }
                break;
            case OUTWARDS:
                returnValue.set(pos);
                break;
        }
        return returnValue;
    }

    private Vector2 randomPosCIRC(){
        Vector2 returnValue = pos.cpy();
        float rad = size.x;

        float angle = MathUtils.random(0f, 360f);;
        switch (direction){
            case FILLED:
                float r = rad * (float)Math.sqrt(MathUtils.random());
                returnValue.x += r;
                returnValue.rotateAroundDeg(pos, angle);
                if(rotation != 0) returnValue.rotateAroundDeg(pos, rotation);
                break;
            case INWARDS:
                returnValue.x = pos.x + rad;
                returnValue.rotateAroundDeg(pos, angle);
                break;
            case OUTWARDS:
                // use center as defined in the initialisation
                break;
        }
        return returnValue;
    }

    private Vector2 randomPosLINE(){
        Vector2 returnValue = new Vector2();

        float x1 = pos.x - size.x / 2;
        float x2 = pos.x + size.x / 2;
        float Y = pos.y;

        float X = 0;
        switch (direction) {
            case FILLED:
                X = MathUtils.random(x1, x2);
                break;
            case INWARDS:
                boolean useX1 = MathUtils.randomBoolean();
                if(useX1) X = x1;
                else X = x2;
                break;
            case OUTWARDS:
                X = pos.x;
                break;
        }
        returnValue.set(X, Y);

        return returnValue;
    }




    private Vector2 rotateAroundCenter(Vector2 point){
        if (rotation == 0) return point;

        Vector2 relative = point.cpy().sub(pos);
        relative.rotateDeg(rotation);
        return relative.add(pos);
    }

    public void onDebug(ShapeRenderer shapeRenderer, float delta) {
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        if (geometric == Geometric.RECTANGLE) {
            Vector2 p1 = rotateAroundCenter(new Vector2(pos.x - size.x / 2, pos.y - size.y / 2));
            Vector2 p2 = rotateAroundCenter(new Vector2(pos.x + size.x / 2, pos.y - size.y / 2));
            Vector2 p3 = rotateAroundCenter(new Vector2(pos.x + size.x / 2, pos.y + size.y / 2));
            Vector2 p4 = rotateAroundCenter(new Vector2(pos.x - size.x / 2, pos.y + size.y / 2));

            shapeRenderer.line(p1, p2);
            shapeRenderer.line(p2, p3);
            shapeRenderer.line(p3, p4);
            shapeRenderer.line(p4, p1);
        }else if (geometric == Geometric.LINE) {
            Vector2 a = rotateAroundCenter(new Vector2(pos.x - size.x / 2, pos.y));
            Vector2 b = rotateAroundCenter(new Vector2(pos.x + size.x / 2, pos.y));
            shapeRenderer.line(a, b);
        }else if (geometric == Geometric.CIRCLE) {
            shapeRenderer.circle(pos.x, pos.y, size.x);
        }
    }

    public static class Builder {

        SpawnArea spawnArea;

        public Builder(SpawnArea spawnArea) {
            this.spawnArea = spawnArea;
        }

        public Builder geometric(Geometric geometric){
            spawnArea.geometric = geometric;
            return this;
        }

        public Builder direction(Direction direction){
            spawnArea.direction = direction;
            return this;
        }

        public Builder rotation(float rotation){
            spawnArea.rotation = rotation;
            return this;
        }

        public SpawnArea build(){
            return spawnArea;
        }

    }
}
