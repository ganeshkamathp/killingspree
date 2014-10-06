package com.sillygames.killingSpree.managers.physics;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;

public class Ray {
    
    private final static Vector2 temp = new Vector2();

    public static Body findBody(World world, Body srcBody,
            Vector2 step, float length, boolean staticOnly) {
        Vector2 start = srcBody.getPosition();
        Body body = null;
        if (step.isZero()) {
            return null;
        }
        
        step.scl(2);
        
        float bestLength = 100000;
        length *= length;
        for (int i = 0; i < world.bodies.size(); i++) {
            if (staticOnly &&
                    world.bodies.get(i).bodyType != BodyType.StaticBody) {
                continue;
            }
            if (world.bodies.get(i) == srcBody) {
                continue;
            }
            temp .set(start);
            float currentLength = temp.dst2(start);
            while (currentLength < length && currentLength < bestLength) {
                if(world.bodies.get(i).rectangle.contains((temp.x + 
                        WorldRenderer.VIEWPORT_WIDTH) % 
                        WorldRenderer.VIEWPORT_WIDTH,
                        (temp.y + WorldRenderer.VIEWPORT_HEIGHT) %
                        WorldRenderer.VIEWPORT_HEIGHT)) {
                    body = world.bodies.get(i);
                    bestLength = currentLength;
                    continue;
                }
                temp.add(step);
                currentLength = temp.dst2(start);
            }
        }
        return body;
    }
    
    public static Body findBody(World world, Body srcBody,
            Vector2 step, float length) {
        return findBody(world, srcBody, step, length, false);
    }

}
