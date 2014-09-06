package com.sillygames.killingSpree.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sillygames.killingSpree.InputController;
import com.sillygames.killingSpree.entities.Player;

public class WorldManager {
    
    World world;
    float ratio;
    float waitTime=0;
    Player player;
    
    public WorldManager(){
        world = new World(new Vector2(0, 0), false);
        player = new Player(20, 20);
        addDebugObjects();
    }

    public void update(float delta) {
        if(Gdx.input.isTouched()){
            if(waitTime > 0.1f){
                addCircle(1, (Gdx.input.getX() / ratio), 
                        (100 - Gdx.input.getY() / ratio), BodyType.DynamicBody);
                waitTime = 0;
            }
        }
        else{
            waitTime += delta;
        }
        world.step(delta, 1, 1);
        player.update();
        if(InputController.instance.rightTrigger()){
            float angle = player.getBody().getAngle() * MathUtils.radiansToDegrees;
            float vX=0, vY=0;
            
            if(angle == 0)
                vX = 40;
            else if(angle == 90)
                vY = 40;
            else if(angle == 180)
                vX = -40;
            else if(angle == 270)
                vY = -40;
            Body bullet = addBox(0.5f, 0.25f, player.getPosition().x + vX/10,
                    player.getPosition().y + vY/10, BodyType.DynamicBody); 
            bullet.setTransform(bullet.getPosition(), angle * MathUtils.degreesToRadians);
            bullet.setLinearVelocity(vX, vY);
        }
    }

    public World getWorld() {
        return world;
    }
    
    public void addDebugObjects(){
        player.setBody(addCircle(2, 30, 30, BodyType.DynamicBody));
        player.getBody().setLinearDamping(5);
        addBox(200, 2, 50, 2, BodyType.StaticBody);
        addBox(200, 2, 50, 98, BodyType.StaticBody);
        addBox(2, 200, 2, 50, BodyType.StaticBody);
        addBox(2, 200, 178, 50, BodyType.StaticBody);
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
        fixtureDef.density = 1;
        fixtureDef.restitution = 1;
        fixtureDef.shape = shape;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        shape.dispose();
        return body;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
    
}
