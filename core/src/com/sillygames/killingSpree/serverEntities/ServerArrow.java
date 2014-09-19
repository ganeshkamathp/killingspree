package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerArrow extends ServerEntity {

    public static final float RADIUS = 3.5f;
    private float velocity;
    private Vector2 target;

    public float gravityTime;
    
    public ServerArrow(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.ARROW;
        body = world.addBox(RADIUS, RADIUS, position.x, position.y,
                BodyType.DynamicBody);
        body.setLinearVelocity(velocity, 0);
        body.setGravityScale(0.01f);
        body.setUserData(this);
        gravityTime = 0.5f;
    }
    
    @Override
    public void update(float delta) {
        gravityTime -= delta;
//        Gdx.app.log(body.getPosition().toString(), target.toString());
        position.set(body.getPosition());
        Vector2 targetVelocity = new Vector2(target);
        targetVelocity.sub(position);
        Vector2 currentVelocity = body.getLinearVelocity();
        targetVelocity.scl(1/targetVelocity.x);
        targetVelocity.scl(currentVelocity.x);
        if(Math.abs(currentVelocity.x) > 1) {
            if(Math.abs(position.x - target.x) < 200 &&
                    Math.abs(position.y - target.y) < 100) {
                if (currentVelocity.y < targetVelocity.y) {
                    currentVelocity.y += 50f / Math.ceil(Math.abs(position.y - target.y));
                } else {
                    currentVelocity.y -= 50f / Math.ceil(Math.abs(position.y - target.y));
                }
                body.setLinearVelocity(currentVelocity);
            }
        }
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
        if (gravityTime < 0) {
            body.setGravityScale(0.5f);
        }
    }
    
    public void setTarget(Vector2 target) {
        this.target = target;
    }

    @Override
    public void dispose() {
        world.destroyBody(body);
    }

    @Override
    public void updateState(EntityState state) {
        super.updateState(state);
        state.angle = MathUtils.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x);
    }
}
