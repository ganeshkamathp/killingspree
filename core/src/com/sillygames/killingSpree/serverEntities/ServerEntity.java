package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.helpers.EntityUtils;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.networking.messages.EntityState;

public abstract class ServerEntity {
    public ActorType actorType;
    
    protected boolean toLoadAssets;
    protected final Vector2 position;
    
    public ServerEntity(float x, float y){
        position = new Vector2(x, y);
        toLoadAssets = true;
    }

    public abstract void update(float delta);

    public abstract void dispose();

    public void updateState(EntityState state) {
        state.type = EntityUtils.actorTypeToByte(actorType);
        state.x = position.x * 100;
        state.y = position.y * 100;
    }

}
