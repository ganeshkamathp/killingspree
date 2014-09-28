package com.sillygames.killingSpree.networking.messages;

import com.sillygames.killingSpree.pool.Poolable;

public class AudioMessage implements Poolable{
    public byte audio;

    @Override
    public void reset() {
        audio = 0;
    }
    
    public void jump() {
        audio |= 1 << 0;
    }
    
    public void shoot() {
        audio |= 1 << 1;
    }
    
    public void hurt() {
        audio |= 1 << 2;
    }
    
    public void explode() {
        audio |= 1 << 3;
    }
    
    public void jumpedOn() {
        audio |= 1 << 4;
    }    
    
    public boolean getJump() {
        return ((audio & (1 << 0)) != 0);
    }
    
    public boolean getShoot() {
        return ((audio & (1 << 1)) != 0);
    }
    
    public boolean getHurt() {
        return ((audio & (1 << 2)) != 0);
    }
    
    public boolean getExplode() {
        return ((audio & (1 << 3)) != 0);
    }
    
    public boolean getJumpedOn() {
        return ((audio & (1 << 4)) != 0);
    }
    
}
