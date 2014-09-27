package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.MathUtils;
import com.sillygames.killingSpree.categories.NonExplodingWeaponCategory;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.WorldBodyUtils;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerBullet extends ServerEntity implements NonExplodingWeaponCategory {

    public static final float RADIUS = 5f;
    private float velocity;
    public float destroyTime;
    public ServerEntity shooter;
    
    public ServerBullet(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.BULLET;
        body = world.addBox(RADIUS, RADIUS, position.x, position.y,
                BodyType.DynamicBody);
        body.setLinearVelocity(velocity, 0);
        body.setGravityScale(0f);
        body.setUserData(this);
        destroyTime = 1f;
    }
    
    @Override
    public void update(float delta) {
        destroyTime -= delta;
        position.set(body.getPosition());
        
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
        
        if (destroyTime < 0) {
            dispose();
        }
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
    public void addKill() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ServerEntity getShooter() {
        return shooter;
    }
}
