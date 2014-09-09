package com.sillygames.killingSpree.managers;

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
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.entities.Player;
import com.sillygames.killingSpree.networking.MyServer;
import com.sillygames.killingSpree.pooler.ObjectPool;



public class WorldManager {
    
    World world;
    
    public WorldManager(){
        world = new World(new Vector2(0, -50f), false);
        final Player player = new Player(0, 0);
        player.setBody(addBox(1f, 1f, 20, 30, BodyType.DynamicBody));
        MyServer.instance.server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ControlsMessage) {
                    player.processControls((ControlsMessage)object);
                    ObjectPool.instance.controlsMessagePool.
                    free((ControlsMessage) object);
                }
            }
            
        });
    }

    public void update(float delta) {
        world.step(delta, 1, 1);
    }

    public World getWorld() {
        return world;
    }
    
    public Body addCircle(float r, float x, float y, BodyType type){
        CircleShape circle = new CircleShape();
        circle.setRadius(r);
        return createBody(x, y, type, circle);
    }
    
    public Body addBox(float h, float w, float x, float y, BodyType type){
        PolygonShape square = new PolygonShape();
        square.setAsBox(h, w);
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

}
