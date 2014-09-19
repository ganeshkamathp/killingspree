package com.sillygames.killingSpree.managers.physics;

import com.sillygames.killingSpree.serverEntities.ServerArrow;
import com.sillygames.killingSpree.serverEntities.ServerBlob;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class CollisionProcessor {
    
    public static void jumpOn(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
                body1.setLinearVelocity(body1.getLinearVelocity().x,
                        body1.getLinearVelocity().y + 40);
            }
        }
        if (body1.getUserData() instanceof ServerArrow) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
                body1.getUserData().dispose();
            }
        }
    }

    public static void jumpedOn(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerArrow) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
                body1.getUserData().dispose();
            }
        }
        // TODO Auto-generated method stub
        
    }

    public static void touchLeft(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerArrow) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
                body1.getUserData().dispose();
            }
        }
        
    }

    public static void touchRight(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerArrow) {
            if (body2.getUserData() instanceof ServerBlob) {
                body2.getUserData().dispose();
                body1.getUserData().dispose();
            }
        }
        
    }

}
