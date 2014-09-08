package com.sillygames.killingSpree.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.InputController;

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
    
}
