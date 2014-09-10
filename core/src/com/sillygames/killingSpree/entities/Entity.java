package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    
    Vector2 position;
    
    public Entity(float x, float y){
        position = new Vector2(x, y);
    }
    
    public void setPosition(float x, float y){
        position.x = x;
        position.y = y;
    }
    
    public Vector2 getPosition(){
        return position;
    }
    
    public abstract void updateAndRender(float delta, SpriteBatch batch);

}
