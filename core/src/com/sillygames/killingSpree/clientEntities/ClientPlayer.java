package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class ClientPlayer extends ClientEntity{

    private Sprite sprite;
    private boolean markForDispose;
    private Animation walk;
    private float walkDuration;
    private float vX, vY;
    private boolean previousXFlip;
    
    public ClientPlayer(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        Texture texture = AssetLoader.instance.getTexture("sprites/player.png");
        sprite = new Sprite(texture);
        
        walk = new Animation(0.05f, TextureRegion.split(texture,
                texture.getWidth()/10, texture.getHeight())[0]);
        walk.setPlayMode(Animation.PlayMode.LOOP);
        
        walkDuration = 0;
    }
    
    @Override
    public void render(float delta, SpriteBatch batch) {
        walkDuration += delta;
        if (markForDispose) {
            dispose();
            return;
        }
        renderPlayer(batch);
        
    }
    private void renderPlayer(SpriteBatch batch) {
        
        if (vY !=0) {
            walkDuration = 0.49f;
        }

        if (vX < -0.5f) {
            sprite.setRegion(walk.getKeyFrame(walkDuration));
            previousXFlip = true;
        } else if (vX > 0.5f){
            sprite.setRegion(walk.getKeyFrame(walkDuration));
            previousXFlip = false;
        } else {
            sprite.setRegion(walk.getKeyFrame(0.49f));
        }
        sprite.flip(previousXFlip, false);
        
        sprite.setSize(ServerPlayer.WIDTH + 3f, 
                ServerPlayer.HEIGHT + 1f);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        
        float x = position.x - sprite.getWidth() / 2;
        float y = position.y - sprite.getHeight() / 2 + ServerPlayer.YOFFSET;
        sprite.setPosition(x, y);
        sprite.draw(batch);
        if (position.x > WorldRenderer.VIEWPORT_WIDTH / 2) {
            x -= WorldRenderer.VIEWPORT_WIDTH;
        } else {
            x += WorldRenderer.VIEWPORT_WIDTH;
        }
        sprite.setPosition(x, y);
        sprite.draw(batch);
        
        if (position.y > WorldRenderer.VIEWPORT_HEIGHT / 2) {
            y -= WorldRenderer.VIEWPORT_HEIGHT;
        } else {
            y += WorldRenderer.VIEWPORT_HEIGHT;
        }
        x = position.x - sprite.getWidth() / 2;
        sprite.setPosition(x, y);
        sprite.draw(batch);
        
    }
    
    @Override
    public void processState(EntityState nextState, float alpha) {
        super.processState(nextState, alpha);
        vX = nextState.vX;
        vY = nextState.vY;
    }
    
    @Override
    public void dispose() {
    }

    
}
