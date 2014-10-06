package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.categories.NonExplodingWeaponCategory;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.WorldBodyUtils;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.managers.physics.Ray;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerArrow extends ServerEntity implements NonExplodingWeaponCategory {

    public static final float RADIUS = 3.5f;
    private float velocity;
    private Vector2 target;

    public float gravityTime;
    private Vector2 tempVector;
    
    public ServerArrow(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.ARROW;
        body = world.addBox(RADIUS, RADIUS, position.x, position.y,
                BodyType.DynamicBody);
        body.setLinearVelocity(velocity, 0);
        body.setGravityScale(0.01f);
        body.setUserData(this);
        gravityTime = 0.5f;
        tempVector = new Vector2();
    }
    
    @Override
    public void update(float delta) {
        gravityTime -= delta;
        Vector2 velocityVector = body.getLinearVelocity();
        position.set(body.getPosition());
        Body targetBody = Ray.findBody(world.worldManager.getWorld(),
                body, tempVector.set(Math.signum(velocityVector.x), 0), 200f);
        position.set(body.getPosition());
//        Gdx.app.log(body.getPosition().toString(), target.toString());
        if (target != null) {
            Vector2 currentVelocity = body.getLinearVelocity();
            Vector2 targetVelocity = tempVector.set(target);
            targetVelocity.sub(position);
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

    @Override
    public float getWidth() {
        return RADIUS;
    }

    @Override
    public ServerEntity getShooter() {
        // TODO Auto-generated method stub
        return null;
    }
}
