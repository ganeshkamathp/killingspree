package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.helpers.EntityUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerPlayer extends ServerEntity{

    private Body body;
    private ControlsMessage currentControls;
    private boolean markForDispose;
    
    public ServerPlayer(float x, float y) {
        super(x, y);
        markForDispose = false;
        currentControls = new ControlsMessage();
        actorType = ActorType.PLAYER;
    }
    
    public void createBody(WorldManager worldManager) {
        body = worldManager.addBox(0.7f, 1f, position.x, position.y,
                BodyType.DynamicBody);
    }
    
    @Override
    public void update(float delta) {
        if (markForDispose) {
            dispose();
            return;
        }
        processPlayer();
        position.set(body.getPosition());
        
    }
    
    public void markForDispose() {
        markForDispose = true;
    }
    
    private void processPlayer() {
        processControls(currentControls);
    }

    public void processControls(ControlsMessage controls) {
        Vector2 direction = Utils.parseDirections(controls);
        Vector2 velocity = body.getLinearVelocity();
        Vector2 position = body.getPosition();

        if (velocity.y < -20f) {
            velocity.y = -20f;
        }

        if (direction.x!=0){
            velocity.x = Math.signum(direction.x) * 10;
        } else {
            velocity.x += -0.1f * velocity.x;
        }
        
        body.setLinearVelocity(velocity);

        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }

        if (controls.action > 1 && velocity.y == 0) {
            body.applyLinearImpulse(0, 100f, 0, 0, true);
        }
    }

    public void setCurrentControls(ControlsMessage currentControls) {
        this.currentControls = currentControls;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void updateState(EntityState state) {
        super.updateState(state);
    }
    
}
