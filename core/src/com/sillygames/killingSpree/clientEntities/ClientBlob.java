package com.sillygames.killingSpree.clientEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ClientBlob extends ClientEntity {
    
    private Texture texture;
    private Sprite sprite;
    boolean markForDispose;
    
    public ClientBlob(float x, float y) {
        super(x, y);
        markForDispose = false;
        loadAssets();
    }
    
    @Override
    public void loadAssets() {
        texture = new Texture(Gdx.files.internal("sprites/player.png"));
        sprite = new Sprite(texture);
        sprite.setSize(2 * WorldRenderer.SCALE, 2 * WorldRenderer.SCALE);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.setPosition(position.x * WorldRenderer.SCALE - sprite.getWidth() / 2,
                position.y * WorldRenderer.SCALE - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}
