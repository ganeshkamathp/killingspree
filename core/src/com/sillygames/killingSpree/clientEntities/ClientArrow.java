package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.pool.AssetLoader;
import com.sillygames.killingSpree.serverEntities.ServerArrow;

public class ClientArrow extends ClientEntity {
    
    private Sprite sprite;
    boolean markForDispose;
    
    public ClientArrow(short id, float x, float y) {
        super(id, x, y);
        markForDispose = false;
        sprite = new Sprite(AssetLoader.instance.getTexture("sprites/arrow.png"));
        sprite.setSize(ServerArrow.RADIUS * 10 , 
                ServerArrow.RADIUS * 2.5f);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(x  - sprite.getWidth() / 2,
                y  - sprite.getHeight() / 2);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.setRotation(angle * MathUtils.radiansToDegrees);

        float x = position.x - sprite.getWidth() / 2;
        float y = position.y - sprite.getHeight() / 2;
        drawAll(sprite, batch, x, y);
    }

    @Override
    public void dispose() {
    }

}
