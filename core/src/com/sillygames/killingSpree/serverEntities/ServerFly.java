package com.sillygames.killingSpree.serverEntities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.categories.EnemyCategory;
import com.sillygames.killingSpree.categories.LivingCategory;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.WorldBodyUtils;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.Ray;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.managers.physics.Body.CollisionCategory;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerFly extends ServerEntity implements EnemyCategory, LivingCategory {

    public static final float WIDTH = 15f;
    public static final float HEIGHT = 10f;
    public static final float YOFFSET = 1f;
    private float randomTime;
    private float spawnTime;
    
    public ServerFly(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.FLY;
        body = world.addBox(WIDTH, HEIGHT - YOFFSET * 2, position.x, position.y,
                BodyType.StaticBody);
        body.setGravityScale(0f);
        body.setUserData(this);
        randomTime = 0;
        body.category = CollisionCategory.NONE;
        spawnTime = 0.1f;
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
        boolean targetAcquired = false;
        position.set(body.getPosition());
        ArrayList<Vector2> playersPositions = world.getPlayers(position, 100);
        if (playersPositions.size() != 0) {
            for (Vector2 playerPosition: playersPositions) {
                playerPosition.sub(position);
                float max = Math.max(Math.abs(playerPosition.x), Math.abs(playerPosition.y));
                playerPosition.scl(5 / max);
//                Body targetBody = Ray.findBody(world.worldManager.getWorld(),
//                        body, playerPosition, 150f);
//                if (targetBody != null && targetBody.getUserData() instanceof ServerPlayer) {
//                    Gdx.app.log(body.getPosition().toString(), playerPosition.toString());
                    body.setLinearVelocity(playerPosition.x * 20, playerPosition.y * 20);
                    targetAcquired = true;
                    break;
//                }
            }
        } 
        if (!targetAcquired) {
            randomTime += delta;
            Vector2 currentVelocity = body.getLinearVelocity();
            float max = Math.max(Math.abs(currentVelocity.x), Math.abs(currentVelocity.y));
            currentVelocity.scl(5 / max);
            Body targetBody = Ray.findBody(world.worldManager.getWorld(),
                    body, currentVelocity, 30f);
            if ((Math.abs(body.getLinearVelocity().x) < 20
                  && Math.abs(body.getLinearVelocity().y) < 20)
                  || (targetBody != null && targetBody.bodyType == BodyType.StaticBody) || randomTime > 4) {
                do {
                    currentVelocity = body.getLinearVelocity();
                    currentVelocity.set((MathUtils.random(20, 30)) *
                            (MathUtils.random(-1, 1) < 0? -1 : 1),
                            (MathUtils.random(20, 30)) *
                            (MathUtils.random(-1, 1) < 0? -1 : 1));
                    body.setLinearVelocity(currentVelocity);
                    randomTime = 0;
                    targetBody = Ray.findBody(world.worldManager.getWorld(),
                            body, currentVelocity, 30f);
                } while ((Math.abs(body.getLinearVelocity().x) < 20
                        && Math.abs(body.getLinearVelocity().y) < 20) ||
                        (targetBody != null && targetBody.bodyType == BodyType.StaticBody));
            }
            
        }
        
        Vector2 currentVelocity = body.getLinearVelocity();
        float max = Math.max(Math.abs(currentVelocity.x), Math.abs(currentVelocity.y));
        if (max > 50)
            currentVelocity.scl(50 / max);
        body.setLinearVelocity(currentVelocity);
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
