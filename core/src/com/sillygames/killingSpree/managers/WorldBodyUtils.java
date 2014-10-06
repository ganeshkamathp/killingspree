package com.sillygames.killingSpree.managers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.categories.LivingCategory;
import com.sillygames.killingSpree.managers.physics.Body;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.managers.physics.Ray;
import com.sillygames.killingSpree.managers.physics.World;
import com.sillygames.killingSpree.networking.messages.AudioMessage;
import com.sillygames.killingSpree.serverEntities.ServerArrow;
import com.sillygames.killingSpree.serverEntities.ServerBomb;
import com.sillygames.killingSpree.serverEntities.ServerBullet;
import com.sillygames.killingSpree.serverEntities.ServerEntity;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class WorldBodyUtils {
    
    public WorldManager worldManager;
    public ArrayList<ServerEntity> entities;
    private Circle circle;
    public AudioMessage audio;
    private World world;
    private Vector2 tempPlayerPosition;
    
    public WorldBodyUtils(WorldManager worldManager) {
        circle = new Circle();
        this.worldManager = worldManager;
        entities = new ArrayList<ServerEntity>();
        audio = worldManager.audio;
        this.world = worldManager.getWorld();
        tempPlayerPosition = new Vector2();
    }
    
    public Body addBox(float w, float h, float x, float y, BodyType type){
        Body body = new Body(x - w/2, y - h/2, w, h, type);
        body.setWorld(world);
        world.bodies.add(body);
        return body;
    }

    public void createWorldObject(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Body body = new Body(rectangle);
            world.bodies.add(body);
            
            if (rectangle.x < 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.x += WorldRenderer.VIEWPORT_WIDTH;
                body = new Body(rectangle);
                world.bodies.add(body);
            }
            
            if (rectangle.x + rectangle.width > WorldRenderer.VIEWPORT_WIDTH - 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.x -= WorldRenderer.VIEWPORT_WIDTH;
                body = new Body(rectangle);
                world.bodies.add(body);
            }
            
            if (rectangle.y < 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.y += WorldRenderer.VIEWPORT_HEIGHT;
                body = new Body(rectangle);
                world.bodies.add(body);
            }
            if (rectangle.y > WorldRenderer.VIEWPORT_HEIGHT - 20) {
                rectangle = new Rectangle(rectangle);
                rectangle.y -= WorldRenderer.VIEWPORT_WIDTH;
                body = new Body(rectangle);
                world.bodies.add(body);
            }
        }
    }
    
    public ServerArrow AddArrow(float x, float y) {
        ServerArrow arrow = new ServerArrow(worldManager.id++, x, y, this);
        arrow.body.setUserData(arrow);
        entities.add(arrow);
        return arrow;
    }
    
    public ServerBullet AddBullet(float x, float y, ServerPlayer shooter) {
        ServerBullet bullet = new ServerBullet(worldManager.id++, x, y, this);
        bullet.shooter = shooter;
        bullet.body.setUserData(bullet);
        entities.add(bullet);
        return bullet;
    }
    
    public ServerBomb AddBomb(float x, float y, ServerPlayer bomber) {
        for (Body body: world.bodies) {
            if (body.rectangle.contains(x, y)) {
                return null;
            }
        }
        ServerBomb bomb = new ServerBomb(worldManager.id++, x, y, this);
        bomb.bomber = bomber;
        bomb.body.setUserData(bomb);
        entities.add(bomb);
        return bomb;
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
                playersPosition.add(tempPlayerPosition.set(position.x, position.y));
            }
            else if (point.dst2(position.x + WorldRenderer.VIEWPORT_WIDTH, position.y) < distance) {
                playersPosition.add(tempPlayerPosition.set(position.x + WorldRenderer.VIEWPORT_WIDTH, position.y));
            }
            else if (point.dst2(position.x - WorldRenderer.VIEWPORT_WIDTH, position.y) < distance) {
                playersPosition.add(tempPlayerPosition.set(position.x - WorldRenderer.VIEWPORT_WIDTH, position.y));
                
            }
            else if (point.dst2(position.x, position.y + WorldRenderer.VIEWPORT_HEIGHT) < distance) {
                playersPosition.add(tempPlayerPosition.set(position.x, position.y + WorldRenderer.VIEWPORT_HEIGHT));
            }
            else if (point.dst2(position.x, position.y - WorldRenderer.VIEWPORT_HEIGHT) < distance) {
                playersPosition.add(tempPlayerPosition.set(position.x, position.y - WorldRenderer.VIEWPORT_HEIGHT));
            }
        }
        return playersPosition;
    }

    public void destroyEntities(ServerBomb bomb, float radius, Vector2 position) {
        Body body = bomb.body;
        circle.set(position, radius);
        for (ServerEntity entity: worldManager.entities) {
            if (entity.body == body) {
                continue;
            }
            if (Intersector.overlaps(circle, entity.body.rectangle)) {
                Vector2 step = entity.body.getPosition();
                float length = position.dst(step);
                step.sub(position);
                float max = Math.max(step.x, step.y);
                step.scl(4 / max);
                Body otherBody = Ray.findBody(world,
                        body, step, length, true);
                if (otherBody == null) {
                    if (entity instanceof LivingCategory) {
                        if (((LivingCategory)entity.body.getUserData()).kill()) {
                            if (bomb.bomber != entity.body.getUserData())
                                bomb.bomber.addKill();
                            else {
                                bomb.bomber.reduceKill();
                            }
                        }
                    }
                }
            }
        }
    }
}
