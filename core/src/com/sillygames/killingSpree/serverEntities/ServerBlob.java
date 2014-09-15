package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerBlob extends ServerEntity {
    
    private Body body;
    boolean markForDispose;
    private float velocity = 5f;
    
    public ServerBlob(float x, float y) {
        super(x, y);
        markForDispose = false;
        actorType = ActorType.BLOB;
    }
    
    public void createBody(WorldManager worldManager) {
        body = worldManager.addBox(0.7f, 1f, position.x, position.y,
                BodyType.DynamicBody);
        body.setLinearVelocity(velocity, 0);
    }

    @Override
    public void update(float delta) {
        if(Math.abs(body.getLinearVelocity().x) < 2f) {
            velocity *= -1;
            body.setLinearVelocity(velocity, 0);
        }
        position.set(body.getPosition());
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
    }

    @Override
    public void dispose() {
    }

}
