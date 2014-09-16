package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerBlob extends ServerEntity {

    public static final float WIDTH = 1.5f;
    public static final float HEIGHT = 1f;  
    private Body body;
    private float velocity;
    
    public ServerBlob(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.BLOB;
        body = world.addBox(WIDTH / 2 - 0.1f, HEIGHT / 2, position.x, position.y,
                BodyType.StaticBody);
        body.setLinearVelocity(velocity, 0);
        velocity = 6f;
    }

    @Override
    public void update(float delta) {
//        if(Math.abs(body.getLinearVelocity().x) < 3f) {
//            velocity *= -1;
//            body.setLinearVelocity(velocity, 0);
//        }
        position.set(body.getPosition());
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
    }

    @Override
    public void dispose() {
    }

}
