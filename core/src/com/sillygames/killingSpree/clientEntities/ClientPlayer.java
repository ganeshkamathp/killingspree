package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.Gdx;
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

        if (vX < -1f) {
            sprite.setRegion(walk.getKeyFrame(walkDuration));
            previousXFlip = true;
        } else if (vX > 1f){
            sprite.setRegion(walk.getKeyFrame(walkDuration));
            previousXFlip = false;
        } else {
            sprite.setRegion(walk.getKeyFrame(0.49f));
        }
        sprite.flip(previousXFlip, false);
        
        sprite.setSize(ServerPlayer.WIDTH + 6f, 
                ServerPlayer.HEIGHT + 1f);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        
        float x = position.x - sprite.getWidth() / 2;
        float y = position.y - sprite.getHeight() / 2 + ServerPlayer.YOFFSET;
       
        drawAll(sprite, batch, x, y);
    }

//    @Override
//    public void processState(EntityState nextState, float alpha) {
//        super.processState(nextState, alpha);
//    }
    
    @Override
    public void dispose() {
    }

    
}
