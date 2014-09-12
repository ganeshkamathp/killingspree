package com.sillygames.killingSpree.managers;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sillygames.killingSpree.entities.Blob;
import com.sillygames.killingSpree.entities.Entity;
import com.sillygames.killingSpree.entities.Player;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;

public class WorldManager{
    
    World world;
    Server server;
    private HashMap<Integer, Player> playerList;
    private ArrayList<Entity> entities;
    private WorldManager worldManager = this;
    public Player player;
    
    public WorldManager(Server server){
        
        playerList = new HashMap<Integer, Player>();
        
        world = new World(new Vector2(0, -70f), false);
        
        server.addListener(new WorldManagerServerListener());
        
        this.server = server;
        
        entities = new ArrayList<Entity>();
        
        Blob blob = new Blob(40, 50);
        blob.createBody(this);
        entities.add(blob);

        player = new Player(40, 51);
        player.createBody(worldManager);
        entities.add(player);
        playerList.put(-1, player);
    }

    public void update(float delta) {
        for(Entity entity: entities) {
            entity.update(delta);
        }
        world.step(delta, 1, 1);
        GameStateMessage gameStateMessage = new GameStateMessage();
        for(Entity entity: entities) {
            EntityState state = new EntityState();
            state.x = entity.getPosition().x;
            state.y = entity.getPosition().y;
            gameStateMessage.addNewState(state);
        }
        server.sendToAllUDP(gameStateMessage);
        
    }

    public World getWorld() {
        return world;
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
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        shape.dispose();
        return body;
    }
    
    public void createWorldObject(MapObject object) {
        FixtureDef fixture = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        MapProperties properties = object.getProperties();
        bodyDef.position.x = (Float) properties.get("x") /WorldRenderer.SCALE;
        bodyDef.position.y = (Float) properties.get("y") /WorldRenderer.SCALE;
        Body body = world.createBody(bodyDef);
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

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    private class WorldManagerServerListener extends Listener {
        @Override
        public void connected(Connection connection) {
            Player player = new Player(20,100);
            player.createBody(worldManager);
            playerList.put(connection.getID(), player);
            entities.add(player);
        }
        
        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof ControlsMessage) {
                playerList.get(connection.getID()).
                setCurrentControls((ControlsMessage) object);
            }
        }
        
        @Override
        public void disconnected(Connection connection) {
            Player player = playerList.get(connection.getID());
            player.markForDispose();
            playerList.remove(connection.getID());
            entities.remove(player);
        }
    }

    public void setCurrentControls() {
        // TODO Auto-generated method stub
        
    }
}

