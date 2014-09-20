package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerBlob;

public class ClientBlob extends ClientEntity {
    
    private Sprite sprite;
    boolean markForDispose;
    
    public ClientBlob(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        sprite = new Sprite(AssetLoader.instance.getTexture("sprites/blob.png"));
        sprite.setSize(ServerBlob.WIDTH + 5, 
                ServerBlob.HEIGHT);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(x - sprite.getWidth() / 2,
                y - sprite.getHeight() / 2);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {

        float x = position.x - sprite.getWidth() / 2;
        float y = position.y - sprite.getHeight() / 2;
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
    public void dispose() {
    }

}
