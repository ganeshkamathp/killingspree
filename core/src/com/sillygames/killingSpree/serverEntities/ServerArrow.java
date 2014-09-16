package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;

public class ServerArrow extends ServerEntity {

    public static final float RADIUS = 0.4f;
    public Body body;
    private float velocity;
    public Vector2 target;
    
    public ServerArrow(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        actorType = ActorType.ARROW;
        body = world.addCircle(RADIUS, position.x, position.y,
                BodyType.DynamicBody);
        body.setLinearVelocity(velocity, 0);
        body.setLinearDamping(0.1f);
        body.setGravityScale(0.01f);
        body.getFixtureList().get(0).setFriction(1);
    }
    
    @Override
    public void update(float delta) {
//        Gdx.app.log(body.getPosition().toString(), target.toString());
        position.set(body.getPosition());
        Vector2 targetVelocity = new Vector2(target);
        targetVelocity.sub(position);
        Vector2 currentVelocity = body.getLinearVelocity();
        targetVelocity.scl(1/targetVelocity.x);
        targetVelocity.scl(currentVelocity.x);
        if(Math.abs(currentVelocity.x) > 1) {
            if(Math.abs(position.x - target.x) < 10 &&
                    Math.abs(position.y - target.y) < 10) {
                if (currentVelocity.y < targetVelocity.y) {
                    currentVelocity.y += 2f / Math.ceil(Math.abs(position.y - target.y));
                } else {
                    currentVelocity.y -= 2f / Math.ceil(Math.abs(position.y - target.y));
                }
                body.setLinearVelocity(currentVelocity);
                Gdx.app.log(body.getPosition().toString(), target.toString());
            }
        }
        if(Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
    }

    @Override
    public void dispose() {
    }

}
