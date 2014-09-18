package com.sillygames.killingSpree.managers.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;

public class World {
    
    public final Vector2 gravity;
    public final ArrayList<Body> staticBodies;
    public final ArrayList<Body> dynamicBodies;

    public World(Vector2 gravity) {
        this.gravity = gravity;
        staticBodies = new ArrayList<Body>();
        dynamicBodies = new ArrayList<Body>();
    }

    public void step(float delta, int i, int j) {
        for (Body body: dynamicBodies) {
            body.update(delta);
        }
        
    }
    
    public void addBody(Body body) {
        if (body.bodyType == BodyType.StaticBody) {
            staticBodies.add(body);
        } else if (body.bodyType == BodyType.DynamicBody) {
            dynamicBodies.add(body);
        }
    }

}
