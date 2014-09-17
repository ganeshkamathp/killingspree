package com.sillygames.killingSpree.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.managers.WorldManager;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.serverEntities.ServerArrow;
import com.sillygames.killingSpree.serverEntities.ServerEntity;

public class WorldBodyUtils implements ContactListener {
    
    WorldManager worldManager;
    public ArrayList<ServerEntity> entities;
    private short id = 200;

    public WorldBodyUtils(WorldManager worldManager) {
        this.worldManager = worldManager;
        entities = new ArrayList<ServerEntity>();
        worldManager.getWorld().setContactListener(this);
    }
    public Body addCircle(float r, float x, float y, BodyType type){
        CircleShape circle = new CircleShape();
        circle.setRadius(r);
        return createBody(x, y, type, circle);
    }
    
    public Body addBox(float w, float h, float x, float y, BodyType type){
        PolygonShape square = new PolygonShape();
        square.setAsBox(w, h);
        return createBody(x, y, type, square);
    }
    
    public Body createBody(float x, float y, BodyType type, Shape shape){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = type;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        Body body = worldManager.getWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        shape.dispose();
        return body;
    }
    
    public ServerArrow AddArrow(float x, float y) {
        ServerArrow arrow = new ServerArrow(id++, x, y, this);
        arrow.setTarget(worldManager.blob.body.getPosition());
        arrow.body.setUserData(arrow);
        entities.add(arrow);
        return arrow;
    }
    
    public void createWorldObject(MapObject object) {
        FixtureDef fixture = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        MapProperties properties = object.getProperties();
        bodyDef.position.x = (Float) properties.get("x") /WorldRenderer.SCALE;
        bodyDef.position.y = (Float) properties.get("y") /WorldRenderer.SCALE;
        Body body = worldManager.getWorld().createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        if (object instanceof PolygonMapObject) {
            Polygon polygon = ((PolygonMapObject) object).getPolygon();
            
            polygon.setPosition(polygon.getX() / WorldRenderer.SCALE
                    - body.getPosition().x,
                    polygon.getY() / WorldRenderer.SCALE
                    - body.getPosition().y);
            polygon.setScale(1f/WorldRenderer.SCALE,
                    1f / WorldRenderer.SCALE);
            shape.set(polygon.getTransformedVertices());
            
        } else if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            rectangle.x /= WorldRenderer.SCALE;
            rectangle.y /= WorldRenderer.SCALE;
            rectangle.width /= WorldRenderer.SCALE;
            rectangle.height /= WorldRenderer.SCALE;
            shape.setAsBox(rectangle.width / 2, rectangle.height / 2, 
                    new Vector2(rectangle.x -
                            body.getPosition().x + rectangle.width / 2,
                            rectangle.y - body.getPosition().y +
                            rectangle.height / 2),
                            body.getAngle());
        }
        fixture.shape = shape;
        body.createFixture(fixture);
    }
    
    public World getWorld() {
        return worldManager.getWorld();
    }
    
    public void destroyBody(Body body) {
        worldManager.getWorld().destroyBody(body);
    }
    
    @Override
    public void beginContact(Contact contact) {
    }
    
    @Override
    public void endContact(Contact contact) {
        ServerEntity entity1 = (ServerEntity) contact.getFixtureA().getBody().getUserData();
        ServerEntity entity2 = (ServerEntity) contact.getFixtureA().getBody().getUserData();
        
        if (entity1 != null) {
            if(entity1.id != 0)
            Gdx.app.log(Short.toString(entity1.id), "yeah");
            if (entity1.actorType == ActorType.ARROW) {
//                worldManager.entities.remove(entity1);
//                worldManager.getWorld().destroyBody(((ServerArrow)entity1).body);
//                entity1.dispose();
            }
        }
        
        if (entity2 != null) {
            if(entity2.id != 0)
            Gdx.app.log(Short.toString(entity2.id), "yeah");
            if(entity2.actorType == ActorType.ARROW) {
//                worldManager.entities.remove(entity2);
//                worldManager.getWorld().destroyBody(((ServerArrow)entity2).body);
//                entity2.dispose();
            }
        }
        
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub
        
    }

}
