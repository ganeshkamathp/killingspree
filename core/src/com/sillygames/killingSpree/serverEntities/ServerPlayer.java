package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerPlayer extends ServerEntity{

    public static final float WIDTH = 12;
    public static final float HEIGHT = 20;
    public static final float YOFFSET = 1f;
    private ControlsMessage currentControls;
    private boolean markForDispose;
    private float reloadTime;
    private Vector2 velocity;
    private float direction;
    private float directionX;
    private float directionY;
    
    public ServerPlayer(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        markForDispose = false;
        currentControls = new ControlsMessage();
        actorType = ActorType.PLAYER;
        body = world.addBox(WIDTH, HEIGHT - YOFFSET * 2, x, y,
                BodyType.DynamicBody);
        body.setUserData(this);
        reloadTime = 0;
        velocity = new Vector2();
        direction = 1;
    }
    
    @Override
    public void update(float delta) {
        if (markForDispose) {
            dispose();
            return;
        }
        processPlayer();
        position.set(body.getPosition());
        reloadTime += delta;
    }
    
    public void markForDispose() {
        markForDispose = true;
    }
    
    private void processPlayer() {
        processControls(currentControls);
    }

    public void processControls(ControlsMessage controls) {
        velocity.set(body.getLinearVelocity());
        position.set(body.getPosition());

        if (velocity.y < -200f) {
            velocity.y = -200f;
        }
        
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
        
        float x = controls.right()? 1 : (controls.left()? -1 : Math.signum(directionX) * 0.01f);
        float y = controls.up()? 1 : (controls.down()? -1 : 0);
        if (Math.abs(x) < 0.02f && y==0) {
            x = direction;
        } else {
            direction = Math.signum(x);
        }
        if (Math.abs(x) ==1 && Math.abs(y) == 1) {
            x = Math.signum(x) * 0.707f;
            y = Math.signum(y) * 0.707f;
        }
        directionX = x;
        directionY = y;

        if (controls.shoot()) {
            if (reloadTime > 1) {
                world.AddBullet(position.x + x * 20, position.y + y * 20).body.
                setLinearVelocity(x * 200, y * 200);;
                reloadTime = 0;
            }
            body.setLinearVelocity(velocity);
        }
        
        if(Math.abs(velocity.x) < 100f) {
            if (controls.right()){
                velocity.x = 100f;
            } else if (controls.left()){
                velocity.x = -100f;
            }
        }
        if(!controls.right() && !controls.left()) {
            velocity.x += -0.1f * velocity.x;
        }
        
        body.setLinearVelocity(velocity);

        if (controls.jump() && body.grounded) {
            body.applyLinearImpulse(0, 290f);
        }
    }

    public void setCurrentControls(ControlsMessage currentControls) {
        this.currentControls = currentControls;
    }

    @Override
    public void dispose() {
        world.destroyBody(body);
    }

    @Override
    public void updateState(EntityState state) {
        super.updateState(state);
        state.vX = body.getLinearVelocity().x;
        state.vY = body.getLinearVelocity().y;
        state.angle = (float) Math.atan2(directionY, directionX);
    }

    @Override
    public float getWidth() {
        return WIDTH;
    }   
}
