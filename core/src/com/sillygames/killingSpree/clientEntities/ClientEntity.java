package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.messages.EntityState;

public abstract class ClientEntity {
    
    public short id;
    protected final Vector2 previousPosition;
    protected final Vector2 position;
    protected EntityState currentState;
    protected float angle;
    
    public ClientEntity(short id, float x, float y){
        position = new Vector2(x, y);
        previousPosition = new Vector2(x, y);
        currentState = new EntityState();
        this.id = id;
    }

    public abstract void render(float delta, SpriteBatch batch);
    
    public abstract void dispose();

    public void processState(EntityState nextState, float alpha) {
        
        previousPosition.set(position);
        if (position.x - nextState.x > 20) {
            position.x -= WorldRenderer.VIEWPORT_WIDTH / 10;
        }
        else if (position.x - nextState.x < -20) {
            position.x += WorldRenderer.VIEWPORT_WIDTH / 10;
        }
        
        if (position.y - nextState.y > 20) {
            position.y -= WorldRenderer.VIEWPORT_HEIGHT / 10;
        }
        else if (position.y - nextState.y < -20) {
            position.y += WorldRenderer.VIEWPORT_HEIGHT / 10;
        }
        
        position.lerp(new Vector2((float)nextState.x, (float)nextState.y), alpha);
        
//        Gdx.app.log("Alpha", Float.toString(alpha));
//        Gdx.app.log("current postion", (new Vector2(position.x, position.y).toString()));
//        Gdx.app.log("next postion", (new Vector2(nextState.x, nextState.y).toString()));
        if (nextState.angle > 0.001f || nextState.angle < -0.001f)
            angle = nextState.angle;
        
    }

}
