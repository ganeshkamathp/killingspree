package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    
    protected boolean toLoadAssets;
    protected final Vector2 position;
    
    public Entity(float x, float y){
        position = new Vector2(x, y);
        toLoadAssets = true;
    }

    public abstract void loadAssets();
    
    public abstract void update(float delta);

    public abstract void render(float delta, SpriteBatch batch);
    
    public abstract void dispose();

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

}
