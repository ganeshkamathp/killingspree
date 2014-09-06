package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.sillygames.killingSpree.InputController;

public class Player extends Entity{

    private Body body;
    
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Player(float x, float y) {
        super(x, y);
    }
    
    @Override
    public void update() {
        position = body.getPosition();
        if(InputController.instance.axisUp()){
            body.setTransform(position.x, position.y, 
                    MathUtils.degreesToRadians * 90);
            body.applyLinearImpulse(0, 20, 0, 0, true);
        } else if(InputController.instance.axisDown()){
            body.setTransform(position.x, position.y, 
                    MathUtils.degreesToRadians * 270);
            body.applyLinearImpulse(0, -20, 0, 0, true);
        } else if(InputController.instance.axisLeft()){
            body.setTransform(position.x, position.y, 
                    MathUtils.degreesToRadians * 180);
            body.applyLinearImpulse(-20, 0, 0, 0, true);
        } else if(InputController.instance.axisRight()){
            body.setTransform(position.x, position.y, 
                    MathUtils.degreesToRadians * 0);
            body.applyLinearImpulse(20, 0, 0, 0, true);
        }
    }
    
}
