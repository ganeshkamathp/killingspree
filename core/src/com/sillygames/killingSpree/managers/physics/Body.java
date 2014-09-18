package com.sillygames.killingSpree.managers.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.serverEntities.ServerEntity;

public class Body {
    
    public enum BodyType { StaticBody, DynamicBody }
    public Rectangle rectangle;
    public BodyType bodyType;
    public ServerEntity entity;
    public float restitution;
    private final Vector2 velocity;
    private final Vector2 temp1;
    private final Vector2 temp2;
    private final Vector2 temp3;
    private World world;
    private float gravityScale;
    
    public Body() {
        bodyType = BodyType.StaticBody;
        velocity = new Vector2(0, 0);
        temp1 = new Vector2();
        temp2 = new Vector2();
        temp3 = new Vector2();
        restitution = 0;
        gravityScale = 1f;
    }
    
    public Body(Rectangle rectangle) {
        this();
        this.rectangle = rectangle;
    }
    
    public Body(float x, float y, float width, float height) {
        this();
        rectangle = new Rectangle(x, y, width, height);
        Gdx.app.log("body", rectangle.toString());
    }

    public Body(float x, float y, float w, float h, BodyType type) {
        this(x, y, w, h);
        bodyType = type;
    }
    
    public Body(Rectangle rectangle, BodyType type) {
        this(rectangle);
        bodyType = type;
    }

    public void setWorld(World world) {
        this.world = world;
    }
    
    public Vector2 getPosition() {
        return rectangle.getCenter(temp1);
    }

    public Vector2 getLinearVelocity() {
        return temp3.set(velocity);
    }

    public void setLinearVelocity(float x, float y) {
        velocity.set(x, y);
    } 

    public void setLinearVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }

    public void setTransform(Vector2 position, float angle) {
        rectangle.setCenter(position);
    }


    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public void setUserData(ServerEntity entity) {
        this.entity = entity;
    }

    public void update(float delta) {
        // Calculate velocity
        temp2.set(world.gravity);
        temp2.scl(gravityScale);
        temp2.scl(delta);
        velocity.add(temp2);
        temp2.set(velocity);
        temp2.scl(delta);
        
        // Update position
        rectangle.getPosition(temp1);
        temp1.add(temp2);
        rectangle.setPosition(temp1.x, rectangle.y);
        
        for (Body body: world.staticBodies) {
            if (body.rectangle.overlaps(rectangle)) {
                solveHorizontalCollision(body, temp1);
            }
        }
        
        rectangle.setPosition(rectangle.x, temp1.y);
        for (Body body: world.staticBodies) {
            if (body.rectangle.overlaps(rectangle)) {
                solveVerticalCollision(body, temp1);
            }
        }

    }

    public void applyLinearImpulse(float x, float y) {
        temp1.set(velocity);
        velocity.set(temp1.x + x, temp1.y + y);
    }
    
    public void solveHorizontalCollision(Body body, Vector2 temp1) {
        float xDistance = rectangle.x - body.rectangle.x;
        if (xDistance > 0 &&
                Math.abs(xDistance) <= body.rectangle.width) {
            temp1.x = body.rectangle.x + body.rectangle.width;
            velocity.x *= -restitution;
        } else if(xDistance <= 0 &&
                Math.abs(xDistance) <= body.rectangle.width) {
            temp1.x = body.rectangle.x - rectangle.width;
            velocity.x *= -restitution;
        }
        rectangle.setPosition(temp1.x, rectangle.y);
    }
    
    public void solveVerticalCollision(Body body, Vector2 temp1) {
        float yDistance = rectangle.y - body.rectangle.y;
        if (yDistance < 0 &&
                Math.abs(yDistance) <= rectangle.height) {
            temp1.y = body.rectangle.y - rectangle.height;
            velocity.y *= -restitution;
        } else if(yDistance >= 0 &&
                Math.abs(yDistance) <= body.rectangle.height) {
            temp1.y = body.rectangle.y + body.rectangle.height;
            velocity.y *= -restitution;
        }
        rectangle.setPosition(rectangle.x, temp1.y);
    }

}
