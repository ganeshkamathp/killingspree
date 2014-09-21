package com.sillygames.killingSpree.managers.physics;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.WorldRenderer;

public class Ray {
    
    public static Body findBody(World world, Body srcBody,
            Vector2 step, float length) {
        Vector2 start = srcBody.getPosition();
        Body body = null;
        if (step.isZero()) {
            return null;
        }
        
        step.scl(2);
        
        float bestLength = 100000;
        length *= 2;
        for (int i = 0; i < world.bodies.size(); i++) {
            if (world.bodies.get(i) == srcBody) {
                continue;
            }
            Vector2 temp = new Vector2(start);
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

}
