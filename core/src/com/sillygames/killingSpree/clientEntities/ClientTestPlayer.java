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

public class ClientTestPlayer extends ClientEntity{

    private Sprite sprite;
    private boolean markForDispose;
    private Animation walk;
    private float walkDuration;
    private boolean previousXFlip;
    
    public ClientTestPlayer(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        Texture texture = AssetLoader.instance.getTexture("sprites/explosion.png");
        sprite = new Sprite(texture);
        
        walk = new Animation(0.05f, TextureRegion.split(texture,
                texture.getWidth()/7, texture.getHeight())[0]);
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

            sprite.setRegion(walk.getKeyFrame(walkDuration));
        sprite.flip(previousXFlip, false);
        
        sprite.setSize(ServerPlayer.WIDTH + 50, 
                ServerPlayer.WIDTH + 50);
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
