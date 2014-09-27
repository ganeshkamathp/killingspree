package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.categories.EnemyCategory;
import com.sillygames.killingSpree.categories.LivingCategory;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.WorldBodyUtils;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.Body.CollisionCategory;
import com.sillygames.killingSpree.managers.physics.Ray;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerBlob extends ServerEntity implements EnemyCategory, LivingCategory {

    public static final float WIDTH = 15f;
    public static final float HEIGHT = 10f;
    public static final float YOFFSET = 1f;
    private float velocity;
    private float spawnTime;
    
    public ServerBlob(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.BLOB;
        body = world.addBox(WIDTH, HEIGHT - YOFFSET * 2, position.x, position.y,
                BodyType.StaticBody);
        velocity = 55f;
        body.setLinearVelocity(velocity, 0);
        body.setGravityScale(0.75f);
        body.setUserData(this);
        spawnTime = 0.1f;
        body.category = CollisionCategory.NONE;
    }
    
    public void setDirection(float direction) {
        velocity *= direction;
        body.setLinearVelocity(velocity, 0);
    }

    @Override
    public void update(float delta) {
        if (spawnTime > 0) {
            spawnTime += delta;
            if (spawnTime > 4) {
                body.category = CollisionCategory.ENEMY;
                body.bodyType = BodyType.DynamicBody;
                spawnTime = -1f;
            }
            return;
        }
        Vector2 velocityVector = body.getLinearVelocity();
        position.set(body.getPosition());
        if (body.grounded) {
            Body targetBody = Ray.findBody(world.worldManager.getWorld(),
                    body, new Vector2(Math.signum(velocityVector.x) * 5, 0), 40f);
            if (targetBody != null && targetBody.getUserData() instanceof ServerPlayer) {
                Gdx.app.log(body.getPosition().toString(), targetBody.getPosition().toString());
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
        state.extra = (byte) (spawnTime > 0.01f ? 0 : 1);
    }

    @Override
    public boolean kill() {
        if (spawnTime < 0 && !body.toDestroy) {
            dispose();
            return true;
        }
        return false;
    }
}
