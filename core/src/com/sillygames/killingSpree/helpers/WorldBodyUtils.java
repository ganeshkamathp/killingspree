package com.sillygames.killingSpree.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.serverEntities.ServerArrow;
import com.sillygames.killingSpree.serverEntities.ServerEntity;

public class WorldBodyUtils {
    
    WorldManager worldManager;
    public ArrayList<ServerEntity> entities;

    public WorldBodyUtils(WorldManager worldManager) {
        this.worldManager = worldManager;
        entities = new ArrayList<ServerEntity>();
    }
    
    public Body addBox(float w, float h, float x, float y, BodyType type){
        Body body = new Body(x - w, y - h, w, h, type);
        body.setWorld(worldManager.getWorld());
        worldManager.getWorld().dynamicBodies.add(body);
        return body;
    }

    public void createWorldObject(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Body body = new Body(rectangle);
            worldManager.getWorld().staticBodies.add(body);
        }
    }
    
    public ServerArrow AddArrow(float x, float y) {
        ServerArrow arrow = new ServerArrow(worldManager.id++, x, y, this);
        arrow.setTarget(worldManager.blob.body.getPosition());
        arrow.body.setUserData(arrow);
        entities.add(arrow);
        return arrow;
    }

    public void destroyBody(Body body) {
        // TODO Auto-generated method stub
        
    }
}
