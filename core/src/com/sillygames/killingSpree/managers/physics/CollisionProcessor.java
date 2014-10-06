package com.sillygames.killingSpree.managers.physics;

import com.sillygames.killingSpree.categories.EnemyCategory;
import com.sillygames.killingSpree.categories.ExplodingWeaponCategory;
import com.sillygames.killingSpree.categories.LivingCategory;
import com.sillygames.killingSpree.categories.NonExplodingWeaponCategory;
import com.sillygames.killingSpree.managers.physics.Body.BodyType;
import com.sillygames.killingSpree.serverEntities.ServerPlayer;

public class CollisionProcessor {
    
    public static boolean jumpOn(Body body1, Body body2) {
        if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof LivingCategory) {
                body1.setLinearVelocity(body1.getLinearVelocity().x, 40);
                if (((LivingCategory)body2.getUserData()).kill())
                    body1.getUserData().addKill();
            }
        } else if (body1.getUserData() instanceof EnemyCategory) {
            if (body2.getUserData() instanceof ServerPlayer) {
                ((ServerPlayer)body2.getUserData()).kill();
            }
        } 
        processWeapons(body1, body2);
        processWeapons(body2, body1);
        return false;
    }

    public static boolean jumpedOn(Body body1, Body body2) {
        if (body1.getUserData() instanceof EnemyCategory) {
            if (body2.getUserData() instanceof ServerPlayer) {
                if (((LivingCategory)body1.getUserData()).kill())
                    body2.getUserData().addKill();
                body2.setLinearVelocity(body1.getLinearVelocity().x, 40);
            }
        } else if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof EnemyCategory) {
                ((ServerPlayer)body1.getUserData()).kill();
            } else if (body2.getUserData() instanceof ServerPlayer) {
                ((ServerPlayer)body1.getUserData()).kill();
                ((ServerPlayer)body2.getUserData()).addKill();
            }
        }
        processWeapons(body1, body2);
        processWeapons(body2, body1);
        return false;
        
    }

    public static boolean touchLeft(Body body1, Body body2) {
        if (body1.getUserData() instanceof LivingCategory) {
            if (body2.getUserData() instanceof ServerPlayer) {
                ((ServerPlayer)body2.getUserData()).kill();
            }
        } else if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof EnemyCategory) {
                ((ServerPlayer)body1.getUserData()).kill();
            }
        }
        processWeapons(body1, body2);
        processWeapons(body2, body1);
        return false;
        
    }

    public static boolean touchRight(Body body1, Body body2) {
        if (body1.getUserData() instanceof EnemyCategory) {
            if (body2.getUserData() instanceof ServerPlayer) {
                ((ServerPlayer)body2.getUserData()).kill();
            }
        }
        else if (body1.getUserData() instanceof ServerPlayer) {
            if (body2.getUserData() instanceof EnemyCategory) {
                ((ServerPlayer)body1.getUserData()).kill();
            }
        }
        processWeapons(body1, body2);
        processWeapons(body2, body1);
        return false;
    }
    
    public static void processWeapons(Body body1, Body body2){
        if (body1.getUserData() instanceof ExplodingWeaponCategory) {
            if (body2.bodyType == BodyType.DynamicBody) {
                if (body1.toDestroy)
                    return;
                ((ExplodingWeaponCategory) body1.getUserData()).explode();
            }
        } else if (body1.getUserData() instanceof NonExplodingWeaponCategory) {
            if (body2.toDestroy)
                return;
            if (body2.getUserData() instanceof LivingCategory) {
                if (((LivingCategory)body2.getUserData()).kill()
                    && body2 != body1.getUserData().body) {
                    ((NonExplodingWeaponCategory) body1.getUserData()).
                    getShooter().addKill();
                }
            }
            body1.getUserData().dispose();
        }
    }

}

