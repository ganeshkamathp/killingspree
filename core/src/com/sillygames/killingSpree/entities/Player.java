package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.screens.helpers.Utils;

public class Player extends Entity{

    private Body body;
    private Sprite sprite;
    private ControlsMessage currentControls;
    private boolean markForDispose;
    private Texture texture;
    
    public Player(float x, float y) {
        super(x, y);
        markForDispose = false;
        currentControls = new ControlsMessage();
    }
    
    @Override
    public void loadAssets() {
        toLoadAssets = false;
        texture = new Texture(Gdx.files.internal("sprites/player.png"));
        sprite = new Sprite(texture);
        sprite.setSize(2 * WorldRenderer.SCALE, 2 * WorldRenderer.SCALE);
    }
    
    public void createBody(WorldManager worldManager) {
        body = worldManager.addBox(0.7f, 1f, position.x, position.y,
                BodyType.DynamicBody);
    }
    
    @Override
    public void update(float delta) {
        if (markForDispose) {
            dispose();
            return;
        }
        processPlayer();
        position.set(body.getPosition());
        
    }
    
    public void markForDispose() {
        markForDispose = true;
    }
    
    @Override
    public void render(float delta, SpriteBatch batch) {
        if (markForDispose) {
            dispose();
            return;
        } else if (toLoadAssets) {
            loadAssets();
        }
        // Server
        if(body != null) {
            processPlayer();
            position.set(body.getPosition());
        }
        renderPlayer(batch);
        
    }
    private void renderPlayer(SpriteBatch batch) {
        sprite.setPosition(position.x * WorldRenderer.SCALE - sprite.getWidth() / 2,
                position.y * WorldRenderer.SCALE - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    private void processPlayer() {
        processControls(currentControls);
    }

    public void processControls(ControlsMessage controls) {
        Vector2 direction = Utils.parseDirections(controls);
        Vector2 velocity = body.getLinearVelocity();
        Vector2 position = body.getPosition();

        if (velocity.y < -20f) {
            velocity.y = -20f;
        }

        if (direction.x!=0){
            velocity.x = Math.signum(direction.x) * 10;
        } else {
            velocity.x += -0.1f * velocity.x;
        }
        
        body.setLinearVelocity(velocity);

        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }

        if (controls.action > 1 && velocity.y == 0) {
            body.applyLinearImpulse(0, 100f, 0, 0, true);
        }
    }

    public void setCurrentControls(ControlsMessage currentControls) {
        this.currentControls = currentControls;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
    
}
