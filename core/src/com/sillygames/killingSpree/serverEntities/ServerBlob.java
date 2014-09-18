package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;

public class ServerBlob extends ServerEntity {

    public static final float WIDTH = 15f;
    public static final float HEIGHT = 10f;  
    private float velocity;
    
    public ServerBlob(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.BLOB;
        body = world.addBox(WIDTH, HEIGHT, position.x, position.y,
                BodyType.DynamicBody);
        velocity = 50f;
        body.setLinearVelocity(velocity, 0);
        body.setGravityScale(0.5f);
        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        Vector2 velocityVector = new Vector2(body.getLinearVelocity());
        if(Math.abs(velocityVector.x) < 10f) {
            velocity *= -1;
            velocityVector.x = velocity;
        }
        
        body.setLinearVelocity(velocityVector);
        
        position.set(body.getPosition());
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
    }

    @Override
    public void dispose() {
    }

}
