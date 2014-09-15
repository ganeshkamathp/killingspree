package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.messages.EntityState;

public abstract class ClientEntity {
    
    protected final Vector2 position;
    protected EntityState currentState;
    
    public ClientEntity(float x, float y){
        position = new Vector2(x, y);
        currentState = new EntityState();
    }

    public abstract void loadAssets();
    
    public abstract void render(float delta, SpriteBatch batch);
    
    public abstract void dispose();

    public void processState(EntityState previousState, EntityState nextState, float alpha) {
        
        
        position.set(previousState.x, previousState.y);
        
        if (previousState.x - nextState.x > 2000) {
            position.x -= WorldRenderer.VIEWPORT_WIDTH * 10;
        }
        else if (previousState.x - nextState.x < -2000) {
            position.x += WorldRenderer.VIEWPORT_WIDTH * 10;
        }
        
        if (previousState.y - nextState.y > 2000) {
            position.y -= WorldRenderer.VIEWPORT_HEIGHT * 10;
        }
        else if (previousState.y - nextState.y < -2000) {
            position.y += WorldRenderer.VIEWPORT_HEIGHT * 10;
        }
        
        
        position.lerp(new Vector2((float)nextState.x, (float)nextState.y), alpha);
        position.scl(0.01f);
        
        
//        Gdx.app.log("Alpha", Float.toString(alpha));
//        Gdx.app.log("previous postion", (new Vector2(previousState.x, previousState.y).toString()));
//        Gdx.app.log("current postion", (new Vector2(position.x, position.y).toString()));
//        Gdx.app.log("next postion", (new Vector2(nextState.x, nextState.y).toString()));
        
    }

}
