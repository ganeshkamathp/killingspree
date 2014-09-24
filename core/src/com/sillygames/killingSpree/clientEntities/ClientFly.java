package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerBlob;
import com.sillygames.killingSpree.serverEntities.ServerFly;

public class ClientFly extends ClientEntity {
    
    private Sprite sprite;
    boolean markForDispose;
    private Animation walk;
    private float walkDuration;
    private boolean previousXFlip;
    
    public ClientFly(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        Texture texture = AssetLoader.instance.getTexture("sprites/fly.png");
        sprite = new Sprite(texture);
        walk = new Animation(0.25f, TextureRegion.split(texture,
                texture.getWidth()/2, texture.getHeight())[0]);
        walk.setPlayMode(Animation.PlayMode.LOOP);
        sprite.setSize(ServerBlob.WIDTH + 5f, ServerFly.HEIGHT);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        walkDuration += delta;
        if (vX < -15f) {
            sprite.setRegion(walk.getKeyFrame(walkDuration));
            previousXFlip = false;
        } else if (vX > 15f){
            sprite.setRegion(walk.getKeyFrame(walkDuration));
            sprite.flip(true, false);
            previousXFlip = true;
        } else {
            sprite.setRegion(walk.getKeyFrame(walkDuration));
            sprite.flip(previousXFlip, false);
        }
        
        float x = position.x - sprite.getWidth() / 2 + 1f;
        float y = position.y - sprite.getHeight() / 2 + ServerBlob.YOFFSET;
        drawAll(sprite, batch, x, y);
    }
    
    @Override
    public void dispose() {
    }

}
