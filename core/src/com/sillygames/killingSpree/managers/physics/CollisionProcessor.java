package com.sillygames.killingSpree.managers.physics;

import com.sillygames.killingSpree.serverEntities.ServerArrow;
import com.sillygames.killingSpree.serverEntities.ServerBlob;
import com.sillygames.killingSpree.serverEntities.ServerBullet;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class CollisionProcessor {
    
    public static boolean jumpOn(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
                body1.setLinearVelocity(body1.getLinearVelocity().x,
                        body1.getLinearVelocity().y + 40);
            }
        }
        if (body1.getUserData() instanceof ServerArrow
                || body1.getUserData() instanceof ServerBullet) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
            }
            body1.getUserData().dispose();
        }
        return false;
    }

    public static boolean jumpedOn(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerArrow
                || body1.getUserData() instanceof ServerBullet) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
            }
            body1.getUserData().dispose();
        }
        return false;
        
    }

    public static boolean touchLeft(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerBlob) {
            if (body2.getUserData() instanceof ServerPlayer) {
                body2.setLinearVelocity(body2.getLinearVelocity().x + 35f,
                        body2.getLinearVelocity().y);
            }
        }
        if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof ServerBlob) {
                body1.setLinearVelocity(body1.getLinearVelocity().x + 35f,
                        body1.getLinearVelocity().y);
            }
        }
        if (body1.getUserData() instanceof ServerArrow
                || body1.getUserData() instanceof ServerBullet) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
            }
            body1.getUserData().dispose();
        }
        return false;
        
    }

    public static boolean touchRight(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerBlob) {
            if (body2.getUserData() instanceof ServerPlayer) {
                body2.setLinearVelocity(body2.getLinearVelocity().x - 40f,
                        body2.getLinearVelocity().y);
            }
        }
        if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof ServerBlob) {
                body1.setLinearVelocity(body1.getLinearVelocity().x - 40f,
                        body1.getLinearVelocity().y);
            }
        }
        if (body1.getUserData() instanceof ServerArrow
                || body1.getUserData() instanceof ServerBullet) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
            }
            body1.getUserData().dispose();
        }
        return false;
        
    }

}
