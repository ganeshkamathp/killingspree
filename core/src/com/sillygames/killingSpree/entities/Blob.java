package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.screens.helpers.Utils;

public class Blob extends Entity {
    
    private Body body;
    private Sprite sprite;
    boolean markForDispose;
    private Texture texture;
    private float velocity = 5f;
    
    public Blob(float x, float y) {
        super(x, y);
        markForDispose = false;
    }
    
    @Override
    public void loadAssets() {
        texture = new Texture(Gdx.files.internal("sprites/player.png"));
        sprite = new Sprite(texture);
        sprite.setSize(2 * WorldRenderer.SCALE, 2 * WorldRenderer.SCALE);
    }

    public void createBody(WorldManager worldManager) {
        body = worldManager.addBox(0.7f, 1f, position.x, position.y,
                BodyType.DynamicBody);
        body.setLinearVelocity(velocity, 0);
    }

    @Override
    public void update(float delta) {
        if(Math.abs(body.getLinearVelocity().x) < 2f) {
            velocity *= -1;
            body.setLinearVelocity(velocity, 0);
        }
        position.set(body.getPosition());
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }       
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        if (toLoadAssets) {
            loadAssets();
        }
        sprite.setPosition(position.x * WorldRenderer.SCALE - sprite.getWidth() / 2,
                position.y * WorldRenderer.SCALE - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }


}
