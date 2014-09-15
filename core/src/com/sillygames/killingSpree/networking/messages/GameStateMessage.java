package com.sillygames.killingSpree.networking.messages;

import java.util.ArrayList;
import com.sillygames.killingSpree.pool.Poolable;

public class GameStateMessage implements Poolable{
    
    public ArrayList<EntityState> states;
    public long time;
    
    public GameStateMessage() {
        states = new ArrayList<EntityState>();
    }
    
    public void addNewState(EntityState state) {
        states.add(state);
    }

    @Override
    public void reset() {
        states.clear();
    }

}
