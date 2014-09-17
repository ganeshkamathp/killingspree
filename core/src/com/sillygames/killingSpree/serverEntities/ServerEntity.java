package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sillygames.killingSpree.helpers.EntityUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.helpers.WorldBodyUtils;
import com.sillygames.killingSpree.networking.messages.EntityState;

public abstract class ServerEntity {
    public ActorType actorType;
    public short id;
    protected boolean toLoadAssets;
    protected final Vector2 position;
    protected WorldBodyUtils world;
    public Body body;
    
    
    public ServerEntity(short id, float x, float y, WorldBodyUtils world){
        position = new Vector2(x, y);
        toLoadAssets = true;
        this.id = id;
        this.world = world;
    }

    public abstract void update(float delta);

    public abstract void dispose();

    public void updateState(EntityState state) {
        state.id = id;
        state.type = EntityUtils.actorTypeToByte(actorType);
        state.x = position.x;
        state.y = position.y;
    }

}
