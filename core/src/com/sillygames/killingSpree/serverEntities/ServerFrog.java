package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.categories.EnemyCategory;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.managers.physics.Body.CollisionCategory;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerFrog extends ServerEntity  implements EnemyCategory{

    public static final float WIDTH = 15f;
    public static final float HEIGHT = 10f;
    public static final float YOFFSET = 1f;
    private float velocity;
    private float waitTime;
    private float direction;
    
    public ServerFrog(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.FROG;
        body = world.addBox(WIDTH, HEIGHT - YOFFSET * 2, position.x, position.y,
                BodyType.DynamicBody);
        velocity = 200f;
        body.setUserData(this);
        waitTime = 0;
        direction = 1;
        body.category = CollisionCategory.ENEMY;
    }

    @Override
    public void update(float delta) {
        if (body.grounded) {
            body.restitutionX = 0;
            body.restitutionY = 0;
            body.setLinearVelocity(Vector2.Zero);
            waitTime += delta;
            if (waitTime > 1) {
                body.restitutionX = 1;
                body.restitutionY = 1;
                waitTime = 0;
                Vector2 velocityVector = body.getLinearVelocity();
                velocityVector.x = direction * velocity / 2;
                velocityVector.y = velocity;
                body.setLinearVelocity(velocityVector);
            }
        } else {
            direction = Math.signum(body.getLinearVelocity().x);
            if (direction == 0) {
                direction = 1;
            }
        }
        
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
