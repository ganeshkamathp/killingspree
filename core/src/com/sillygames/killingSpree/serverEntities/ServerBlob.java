package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.Ray;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerBlob extends ServerEntity {

    public static final float WIDTH = 15f;
    public static final float HEIGHT = 10f;
    public static final float YOFFSET = 1f;
    private float velocity;
    
    public ServerBlob(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.BLOB;
        body = world.addBox(WIDTH, HEIGHT - YOFFSET * 2, position.x, position.y,
                BodyType.DynamicBody);
        velocity = -55f;
        body.setLinearVelocity(velocity, 0);
        body.setGravityScale(0.75f);
        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        Vector2 velocityVector = body.getLinearVelocity();
        position.set(body.getPosition());
        if (body.grounded) {
            Body targetBody = Ray.findBody(world.worldManager.getWorld(),
                    body, new Vector2(Math.signum(velocityVector.x) * 5, 0), 600f);
            if (targetBody != null && targetBody.bodyType != BodyType.StaticBody) {
                body.setLinearVelocity(1.5f * velocityVector.x, velocityVector.y + 100);
            }
        }
        
        velocityVector = new Vector2(body.getLinearVelocity());
        if(Math.abs(velocityVector.x) < 40f) {
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
        world.destroyBody(body);
    }

    @Override
    public float getWidth() {
        return WIDTH;
    }

    @Override
    public void updateState(EntityState state) {
        super.updateState(state);
        state.vX = body.getLinearVelocity().x;
        state.vY = body.getLinearVelocity().y;
    }
}
