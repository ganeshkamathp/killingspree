package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.screens.helpers.Utils;

public class Player extends Entity{

    private Body body;
    Sprite sprite;
    
    public Player(float x, float y) {
        super(x, y);
        Texture texture = new Texture(Gdx.files.internal("sprites/player.png"));
        sprite = new Sprite(texture);
        sprite.setSize(2 * WorldRenderer.SCALE, 2 * WorldRenderer.SCALE);
    }
    
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
    
    @Override
    public void updateAndRender(float delta, SpriteBatch batch) {
        // Server
        if(body != null) {
            processPlayer();
            position = body.getPosition();
        }
        renderPlayer(batch);
        
    }

    private void renderPlayer(SpriteBatch batch) {
        sprite.setPosition(position.x * WorldRenderer.SCALE - sprite.getWidth() / 2,
                position.y * WorldRenderer.SCALE - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    private void processPlayer() {
        // TODO Auto-generated method stub
        
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
    
}
