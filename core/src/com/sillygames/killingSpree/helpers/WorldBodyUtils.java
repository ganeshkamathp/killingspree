package com.sillygames.killingSpree.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.serverEntities.ServerArrow;
import com.sillygames.killingSpree.serverEntities.ServerBullet;
import com.sillygames.killingSpree.serverEntities.ServerEntity;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class WorldBodyUtils {
    
    public WorldManager worldManager;
    public ArrayList<ServerEntity> entities;

    public WorldBodyUtils(WorldManager worldManager) {
        this.worldManager = worldManager;
        entities = new ArrayList<ServerEntity>();
    }
    
    public Body addBox(float w, float h, float x, float y, BodyType type){
        Body body = new Body(x - w, y - h, w, h, type);
        body.setWorld(worldManager.getWorld());
        worldManager.getWorld().bodies.add(body);
        return body;
    }

    public void createWorldObject(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Body body = new Body(rectangle);
            worldManager.getWorld().bodies.add(body);
            
            if (rectangle.x < 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.x += WorldRenderer.VIEWPORT_WIDTH;
                body = new Body(rectangle);
                worldManager.getWorld().bodies.add(body);
            }
            
            if (rectangle.x + rectangle.width > WorldRenderer.VIEWPORT_WIDTH - 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.x -= WorldRenderer.VIEWPORT_WIDTH;
                body = new Body(rectangle);
                worldManager.getWorld().bodies.add(body);
            }
            
            if (rectangle.y < 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.y += WorldRenderer.VIEWPORT_HEIGHT;
                body = new Body(rectangle);
                worldManager.getWorld().bodies.add(body);
            }
            if (rectangle.y > WorldRenderer.VIEWPORT_HEIGHT - 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.y -= WorldRenderer.VIEWPORT_WIDTH;
                body = new Body(rectangle);
                worldManager.getWorld().bodies.add(body);
            }
        }
    }
    
    public ServerArrow AddArrow(float x, float y) {
        ServerArrow arrow = new ServerArrow(worldManager.id++, x, y, this);
        arrow.body.setUserData(arrow);
        entities.add(arrow);
        return arrow;
    }
    
    public ServerBullet AddBullet(float x, float y) {
        ServerBullet bullet = new ServerBullet(worldManager.id++, x, y, this);
        bullet.body.setUserData(bullet);
        entities.add(bullet);
        return bullet;
    }

    public void destroyBody(Body body) {
        body.toDestroy = true;
    }

    public ArrayList<Vector2> getPlayers(Vector2 point, float distance) {
        ArrayList<Vector2> playersPosition = new ArrayList<Vector2>();
        distance *= distance;
        for (ServerPlayer player: worldManager.playerList.values()) {
            Vector2 position = player.body.getPosition();
            if (point.dst2(position.x, position.y) < distance) {
                playersPosition.add(new Vector2(position.x, position.y));
            }
            else if (point.dst2(position.x + WorldRenderer.VIEWPORT_WIDTH, position.y) < distance) {
                playersPosition.add(new Vector2(position.x + WorldRenderer.VIEWPORT_WIDTH, position.y));
            }
            else if (point.dst2(position.x - WorldRenderer.VIEWPORT_WIDTH, position.y) < distance) {
                playersPosition.add(new Vector2(position.x - WorldRenderer.VIEWPORT_WIDTH, position.y));
                
            }
            else if (point.dst2(position.x, position.y + WorldRenderer.VIEWPORT_HEIGHT) < distance) {
                playersPosition.add(new Vector2(position.x, position.y + WorldRenderer.VIEWPORT_HEIGHT));
            }
            else if (point.dst2(position.x, position.y - WorldRenderer.VIEWPORT_HEIGHT) < distance) {
                playersPosition.add(new Vector2(position.x, position.y - WorldRenderer.VIEWPORT_HEIGHT));
            }
        }
        return playersPosition;
    }
}
