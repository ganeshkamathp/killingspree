package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerArrow;
import com.sillygames.killingSpree.serverEntities.ServerBlob;

public class ClientArrow extends ClientEntity {
    
    private Sprite sprite;
    boolean markForDispose;
    
    public ClientArrow(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        loadAssets();
    }
    
    @Override
    public void loadAssets() {
        sprite = new Sprite(AssetLoader.instance.getTexture("sprites/blob.png"));
        sprite.setSize(ServerArrow.RADIUS * 2 * WorldRenderer.SCALE, 
                ServerArrow.RADIUS * 2 * WorldRenderer.SCALE);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(position.x * WorldRenderer.SCALE - sprite.getWidth() / 2,
                position.y * WorldRenderer.SCALE - sprite.getHeight() / 2);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.setPosition(position.x * WorldRenderer.SCALE - sprite.getWidth() / 2,
                position.y * WorldRenderer.SCALE - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
    }

}
