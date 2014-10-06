package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class ClientPlayer extends ClientEntity{

    private Sprite sprite;
    private Sprite gunSprite;
    private boolean markForDispose;
    private Animation walk;
    private float walkDuration;
    private boolean previousXFlip;
    
    public ClientPlayer(short id, float x, float y, WorldRenderer renderer) {
        super(id, x, y, renderer);
        markForDispose = false;
        Texture texture = AssetLoader.instance.getTexture("sprites/player.png");
        sprite = new Sprite(texture);
        gunSprite = new Sprite(AssetLoader.instance.getTexture("sprites/arrow.png"));
        gunSprite.setOrigin(gunSprite.getWidth()/2, gunSprite.getHeight()/2);
        walk = new Animation(0.05f, TextureRegion.split(texture,
                texture.getWidth()/10, texture.getHeight())[0]);
        walk.setPlayMode(Animation.PlayMode.LOOP);
        walkDuration = 0;
        gunSprite.setSize(40, 6);
        gunSprite.setOrigin(gunSprite.getWidth()/2, gunSprite.getHeight()/2);
        gunSprite.setAlpha(0.7f);
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
        angle *= MathUtils.radiansToDegrees;
        
        if (vY !=0) {
            walkDuration = 0.49f;
        }
        if (Math.abs(vX)> 0.4f) {
            sprite.setRegion(walk.getKeyFrame(walkDuration));
        }
        else {
            sprite.setRegion(walk.getKeyFrame(0.49f));
        }
        if ((extra & 0x1) == 0) {
            sprite.setAlpha(0.5f);
        } else {
            sprite.setAlpha(1);
        }
        
        if (angle < -90.1f || angle > 90.1f ) {
            previousXFlip = true;
//            Gdx.app.log("true", Float.toString(angle));
        } else if (angle > -89.9f && angle < 89.9f ){
//            Gdx.app.log("false", Float.toString(angle));
            previousXFlip = false;
        }
//        Gdx.app.log("angle", Float.toString(angle));
        sprite.flip(previousXFlip, false);
        
        sprite.setSize(ServerPlayer.WIDTH + 6f, 
                ServerPlayer.HEIGHT + 1f);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        
        float x = position.x - sprite.getWidth() / 2;
        float y = position.y - sprite.getHeight() / 2 + ServerPlayer.YOFFSET;
       
        drawAll(sprite, batch, x, y);
        
        renderer.hudRenderer.render(batch, x, y, extra, renderer.stateProcessor.
                playerNames.players.get(id));
        
        x = position.x - gunSprite.getWidth() / 2;
        y = position.y - gunSprite.getHeight() / 2 + ServerPlayer.YOFFSET;
        gunSprite.setRotation(angle);
        drawAll(gunSprite, batch, x, y);
    }

//    @Override
//    public void processState(EntityState nextState, float alpha) {
//        super.processState(nextState, alpha);
//    }
    
    @Override
    public void dispose() {
    }

    
}
