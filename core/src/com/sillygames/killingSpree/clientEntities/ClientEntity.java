package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
    public boolean destroy;
    public boolean remove;
    protected float vX, vY;
    
    public ClientEntity(short id, float x, float y){
        position = new Vector2(x, y);
        previousPosition = new Vector2(x, y);
        currentState = new EntityState();
        this.id = id;
        destroy = false;
        remove = false;
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
        
        if (position.dst2(new Vector2(nextState.x, nextState.y)) > 60) {
            position.set(nextState.x, nextState.y);
        } else {
            position.lerp(new Vector2((float)nextState.x, (float)nextState.y), alpha);
        }
        if (nextState.angle > 0.001f || nextState.angle < -0.001f)
            angle = nextState.angle;
        vX = nextState.vX;
        vY = nextState.vY;
        
    }
    
    public void drawAll(Sprite sprite, SpriteBatch batch, float x, float y) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
        if (x > WorldRenderer.VIEWPORT_WIDTH / 2) {
            sprite.setPosition(x - WorldRenderer.VIEWPORT_WIDTH, y);
        } else {
            sprite.setPosition(x + WorldRenderer.VIEWPORT_WIDTH, y);
        }
        sprite.draw(batch);
        
        if (position.y > WorldRenderer.VIEWPORT_HEIGHT / 2) {
            sprite.setPosition(x, y - WorldRenderer.VIEWPORT_HEIGHT);
        } else {
            sprite.setPosition(x, y + WorldRenderer.VIEWPORT_HEIGHT);
        }
        sprite.draw(batch);
    }

}
