package com.sillygames.killingSpree.controls;

import com.badlogic.gdx.utils.Pool.Poolable;

public class ControlsMessage implements Poolable{
    
    // Using byte for directional controls to save bandwidth
    // 0 - none
    // 1 - north
    // 2 - northwest
    // 3 - west
    // 4 - southwest
    // 5 - south
    // 6 - southeast
    // 7 - east
    // 8 - northeast
    public byte direction = 0;
    // Using byte for action to save bandwidth
    // 0 - none
    // 1 - shoot
    // 2 - jump
    // 3 - shoot and jump
    public byte action = 0;
    @Override
    public void reset() {
        action = 0;
        direction = 0;
    }

}
