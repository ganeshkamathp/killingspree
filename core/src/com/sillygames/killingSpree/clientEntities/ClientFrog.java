package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerBlob;
import com.sillygames.killingSpree.serverEntities.ServerFrog;

public class ClientFrog extends ClientEntity {
    
    private Sprite sprite;
    boolean markForDispose;
    private Animation walk;
    private boolean previousXFlip;
    
    public ClientFrog(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        Texture texture = AssetLoader.instance.getTexture("sprites/frog.png");
        sprite = new Sprite(texture);
        walk = new Animation(0.25f, TextureRegion.split(texture,
                texture.getWidth()/2, texture.getHeight())[0]);
        walk.setPlayMode(Animation.PlayMode.LOOP);
        sprite.setSize(ServerFrog.WIDTH + 5f, ServerFrog.HEIGHT + 5f);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        if (vY <= 0) 
            sprite.setRegion(walk.getKeyFrame(0));
        else
            sprite.setRegion(walk.getKeyFrame(0.3f));

        if (vX < -15f) {
            previousXFlip = false;
        } else if (vX > 15f){
            previousXFlip = true;
        }
        sprite.flip(previousXFlip, false);
        
        float x = position.x - sprite.getWidth() / 2 + 1f;
        float y = position.y - sprite.getHeight() / 2 + ServerBlob.YOFFSET;
        drawAll(sprite, batch, x, y);
    }
    
    @Override
    public void dispose() {
    }

}
