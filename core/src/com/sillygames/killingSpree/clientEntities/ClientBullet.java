package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerArrow;

public class ClientBullet extends ClientEntity {
    
    private Sprite sprite;
    boolean markForDispose;
    
    public ClientBullet(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        sprite = new Sprite(AssetLoader.instance.getTexture("sprites/bullet.png"));
        sprite.setSize(ServerArrow.RADIUS * 4 , 
                ServerArrow.RADIUS * 1.5f);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(x  - sprite.getWidth() / 2,
                y  - sprite.getHeight() / 2);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.setRotation(angle * MathUtils.radiansToDegrees);

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
