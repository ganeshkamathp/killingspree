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
    protected short extra;
    protected WorldRenderer renderer;
    private Vector2 tempVector;
    
    public ClientEntity(short id, float x, float y, WorldRenderer renderer){
        position = new Vector2(x, y);
        previousPosition = new Vector2(x, y);
        tempVector = new Vector2();
        currentState = new EntityState();
        this.id = id;
        destroy = false;
        remove = false;
        this.renderer = renderer;
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
        
        tempVector.set((float)nextState.x, (float)nextState.y);
        
        if (position.dst2(tempVector) > 60) {
            position.set(tempVector);
        } else {
            position.lerp(tempVector, alpha);
        }
        angle = nextState.angle;
        vX = nextState.vX;
        vY = nextState.vY;
        extra = nextState.extra;
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
