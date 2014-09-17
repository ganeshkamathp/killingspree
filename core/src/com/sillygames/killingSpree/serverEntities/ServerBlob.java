package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;

public class ServerBlob extends ServerEntity {

    public static final float WIDTH = 1.5f;
    public static final float HEIGHT = 1f;  
    private float velocity;
    
    public ServerBlob(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.BLOB;
        body = world.addBox(WIDTH / 2 - 0.1f, HEIGHT / 2, position.x, position.y,
                BodyType.DynamicBody);
        body.setLinearVelocity(velocity, 0);
        body.setGravityScale(0.5f);
        body.setUserData(this);
        velocity = 5.5f;
    }

    @Override
    public void update(float delta) {
        Vector2 velocityVector = new Vector2(body.getLinearVelocity());
        if(velocityVector.y < -13f) {
            velocityVector.y = -13f;
        }
        if(Math.abs(velocityVector.x) < 3f) {
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
