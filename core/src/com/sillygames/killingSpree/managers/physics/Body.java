package com.sillygames.killingSpree.managers.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.serverEntities.ServerEntity;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class Body {
    
    public enum BodyType { StaticBody, DynamicBody }
    public Rectangle rectangle;
    public BodyType bodyType;
    public ServerEntity entity;
    public float restitution;
    public boolean grounded;
    public boolean toDestroy;
    private final Vector2 velocity;
    private final Vector2 temp1;
    private final Vector2 temp2;
    private final Vector2 temp3;
    private final Vector2 temp4;
    private final Vector2 temp5;
    private World world;
    private float gravityScale;
    private enum CollisionCategory { ALL, ENEMY, STATIC, NONE, WEAPON };
    
    public Body() {
        bodyType = BodyType.StaticBody;
        velocity = new Vector2(0, 0);
        temp1 = new Vector2();
        temp2 = new Vector2();
        temp3 = new Vector2();
        temp4 = new Vector2();
        temp5 = new Vector2();
        restitution = 0;
        gravityScale = 1f;
        grounded = false;
        toDestroy = false;
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
    
    public ServerEntity getUserData() {
        return entity;
    }

    public void update(float delta) {
        
        float xOffset = 0;
        
        if (toDestroy) {
            return;
        }
        
        grounded = false;
        // Calculate velocity
        temp2.set(world.gravity);
        temp2.scl(gravityScale);
        temp2.scl(delta);
        velocity.add(temp2);
        temp2.set(velocity);
        temp2.scl(delta);

        if (rectangle.x + rectangle.width > WorldRenderer.VIEWPORT_WIDTH 
                && velocity.x > 0) {
            xOffset = -WorldRenderer.VIEWPORT_WIDTH;
        } else if (rectangle.x < 0 && velocity.x < 0) {
            xOffset = WorldRenderer.VIEWPORT_WIDTH;
        }
        rectangle.x += xOffset;
        // Update position
        rectangle.getPosition(temp1);
        temp1.add(temp2);
        rectangle.getPosition(temp2);
        rectangle.getPosition(temp4);
        temp2.sub(temp1);
        
        rectangle.setPosition(rectangle.x, temp1.y);
        for (Body body: world.bodies) {
            if (body == this || body.toDestroy)
                continue;
            if (body.rectangle.overlaps(rectangle)) {
                solveVerticalCollision(body, temp1);
            }
        }
        
        rectangle.setPosition(temp1.x, rectangle.y);
        for (Body body: world.bodies) {
            if (body == this || body.toDestroy)
                continue;
            if (body.rectangle.overlaps(rectangle)) {
                solveHorizontalCollision(body, temp1);
            }
        }

        rectangle.x -= xOffset;
    }

    public void applyLinearImpulse(float x, float y) {
        temp1.set(velocity);
        velocity.set(temp1.x + x, temp1.y + y);
    }
    
    public void solveHorizontalCollision(Body body, Vector2 temp1) {
        if (velocity.x < 0) {
            float x = body.rectangle.x + body.rectangle.width;
            temp1.x = Math.abs(temp4.x - x) < Math.abs(temp4.x - temp2.x) ?
                    x : temp4.x;
            velocity.x *= -restitution;
            CollisionProcessor.touchLeft(this, body);
        } else if(velocity.x > 0){
            float x = body.rectangle.x - rectangle.width;
            temp1.x = Math.abs(temp4.x - x) < Math.abs(temp4.x - temp2.x) ? x : temp4.x;
            velocity.x *= -restitution;
            CollisionProcessor.touchRight(this, body);
        }
        rectangle.setPosition(temp1.x, rectangle.y);
    }
    
    public void solveVerticalCollision(Body body, Vector2 position) {
        temp5.set(position);
        if (velocity.y > 0){
            float y = body.rectangle.y - rectangle.height;
            position.y = Math.abs(temp4.y - y) < Math.abs(temp4.y - temp2.y) ?
                    y: temp4.y;
            velocity.y *= -restitution;
            CollisionProcessor.jumpedOn(this, body);
        } else if(velocity.y < 0) {
            float y = body.rectangle.y + body.rectangle.height;
            position.y = Math.abs(temp4.y - y) < Math.abs(temp4.y - temp2.y) ?
                    y: temp4.y;
            velocity.y *= -restitution;
            if (body.bodyType == BodyType.StaticBody) {
                grounded = true;
            }
            CollisionProcessor.jumpOn(this, body);
        }
        rectangle.setPosition(rectangle.x, position.y);
    }

}
