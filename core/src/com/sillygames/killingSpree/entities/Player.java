package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.screens.managers.WorldRenderer;

public class Player extends Entity{

    private Body body;
    
    public Player(float x, float y) {
        super(x, y);
    }
    
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
    
    @Override
    public void update() {
        
    }

    public void processControls(ControlsMessage controls) {
        byte direction = controls.direction;
        float x = 0, y = 0;
        switch (direction) {
            case 1:
                x = 0; y = 1f;
                break;
            case 2:
                x = 0.71f; y = 0.71f;
                break;
            case 3:
                x = 1f; y = 0;
                break;
            case 4:
                x = 0.71f; y = -0.71f;
                break;
            case 5:
                x = 0; y = -1f;
                break;
            case 6:
                x = -0.71f; y = -0.71f;
                break;
            case 7:
                x = -1f; y = 0f;
                break;
            case 8:
                x = -0.71f; y = 0.71f;
                break;
        }
        if (controls.action > 1 && body.getLinearVelocity().y == 0) {
            body.applyLinearImpulse(0, 100f, 0, 0, true);
        }
        if (x!=0 || y!=0){
            Gdx.app.log(Float.toString(x), Float.toString(y));
            body.setLinearVelocity(x * 10, body.getLinearVelocity().y);
        } else {
            Vector2 velocity = body.getLinearVelocity();
            velocity.x += -0.1f * velocity.x;
            body.setLinearVelocity(velocity);
        }
        Vector2 position = body.getPosition();
        boolean wrap = false;
        if (position.x > 600/WorldRenderer.SCALE) {
            wrap = true;
            position.x -= 600/WorldRenderer.SCALE;
        } else if (position.x < 0) {
            wrap = true;
            position.x += 600/WorldRenderer.SCALE;
        }
        if (position.y > 450/WorldRenderer.SCALE) {
            wrap = true;
            position.y -= 450/WorldRenderer.SCALE;
        } else if (position.y < 0) {
            wrap = true;
            position.y += 450/WorldRenderer.SCALE;
        }
        if (wrap) {
            body.setTransform(position, 0);
        }
        
        
    }
    
}
