package com.sillygames.killingSpree.serverEntities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.categories.LivingCategory;
import com.sillygames.killingSpree.helpers.EntityUtils.ActorType;
import com.sillygames.killingSpree.helpers.Utils;
import com.sillygames.killingSpree.managers.WorldBodyUtils;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.managers.physics.Body.CollisionCategory;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;

public class ServerPlayer extends ServerEntity implements LivingCategory {

    public static final float WIDTH = 12;
    public static final float HEIGHT = 20;
    public static final float YOFFSET = 1f;
    private ControlsMessage currentControls;
    private boolean markForDispose;
    private float reloadTime;
    private Vector2 velocity;
    private float direction;
    private float directionX;
    private float directionY;
    private float startX, startY;
    private float spawnTime;
    public byte score;
    private byte totalBombs;
    private float addBombTimer;
    private String name;
    
    public ServerPlayer(short id, float x, float y, WorldBodyUtils world) {
        super(id, x, y, world);
        startX = x;
        startY = y;
        markForDispose = false;
        currentControls = new ControlsMessage();
        actorType = ActorType.PLAYER;
        body = world.addBox(WIDTH, HEIGHT - YOFFSET * 2, x, y,
                BodyType.DynamicBody);
        body.setUserData(this);
        reloadTime = 0;
        velocity = new Vector2();
        spawnTime = 0.1f;
        direction = 1;
        score = 0;
        totalBombs = 3;
        addBombTimer = 0;
        name = "";
    }
    
    @Override
    public void update(float delta) {
        if (totalBombs < 3) {
            addBombTimer += delta;
            if (addBombTimer >= 10) {
               totalBombs++;
               addBombTimer = 0;
            }
        } else {
            addBombTimer = 0;
        }
//        Gdx.app.log(Short.toString(id), Byte.toString(score));
        if (spawnTime > 0) {
            spawnTime += delta;
            if (spawnTime > 2f) {
                body.bodyType = BodyType.DynamicBody;
                spawnTime = -1f;
            }
//            return;
        }
        if (markForDispose) {
            dispose();
            return;
        }
        processPlayer();
        position.set(body.getPosition());
        reloadTime += delta;
    }
    
    public void markForDispose() {
        markForDispose = true;
    }
    
    private void processPlayer() {
        processControls(currentControls);
    }

    public void processControls(ControlsMessage controls) {
        velocity.set(body.getLinearVelocity());
        position.set(body.getPosition());

        if (velocity.y < -200f) {
            velocity.y = -200f;
        }
        
        if (Utils.wrapBody(position)) {
            body.setTransform(position, 0);
        }
        
        float x = controls.right()? 1 : (controls.left()? -1 : Math.signum(directionX) * 0.01f);
        float y = controls.up()? 1 : (controls.down()? -1 : 0);
        if (Math.abs(x) < 0.02f && y==0) {
            x = direction;
        } else {
            direction = Math.signum(x);
        }
        if (Math.abs(x) ==1 && Math.abs(y) == 1) {
            x = Math.signum(x) * 0.707f;
            y = 0;
        }
        directionX = x;
        directionY = y;

        if (reloadTime > 1) {
            if (controls.shoot()) {
                    world.AddBullet(position.x + x * 15, position.y + y * 15, this).body.
                    setLinearVelocity(x * 200, y * 200);;
                    reloadTime = 0;
            } else if (controls.throwBomb() && totalBombs > 0) {
                ServerBomb bomb = world.AddBomb(position.x + Math.signum(x) * 15, position.y + 10, this);
                if (bomb != null) {
                    bomb.body.setLinearVelocity(Math.signum(x) * 100, 100);
                    reloadTime = 0;
                    totalBombs--;
                }
            }
        }
        
        if(Math.abs(velocity.x) < 100f) {
            if (controls.right()){
                velocity.x = 100f;
            } else if (controls.left()){
                velocity.x = -100f;
            }
        }
        if(!controls.right() && !controls.left()) {
            velocity.x += -0.1f * velocity.x;
        }
        
        body.setLinearVelocity(velocity);

        if (controls.jump() && body.grounded) {
            body.applyLinearImpulse(0, 290f);
            world.audio.jump();
        }
    }

    public void setCurrentControls(ControlsMessage currentControls) {
        this.currentControls.buttonPresses = currentControls.buttonPresses;
    }
    
    @Override
    public boolean kill() {
        if (spawnTime < 0) {
            world.audio.hurt();
            Vector2 position = body.getPosition();
            position.set(startX, startY);
            body.setTransform(position, 0);
            spawnTime = 0.1f;
            totalBombs = 3;
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        world.destroyBody(body);
    }

    @Override
    public void updateState(EntityState state) {
        super.updateState(state);
        state.vX = body.getLinearVelocity().x;
        state.vY = body.getLinearVelocity().y;
        state.angle = (float) Math.atan2(directionY, directionX);
        state.extra |= (short) (spawnTime > 0.01f ? 0 : 1);
        state.extra |= (totalBombs << 1);
        state.extra |= (score << 4);
    }

    @Override
    public float getWidth() {
        return WIDTH;
    }

    @Override
    public void addKill() {
        score++;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public void reduceKill() {
        score--;
    }
    
}
