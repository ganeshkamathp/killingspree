package com.sillygames.killingSpree.networking.messages;

import java.util.ArrayList;

public class GameStateMessage {
    
    public ArrayList<EntityState> states;
    
    public GameStateMessage() {
        states = new ArrayList<EntityState>();
    }
    
    public void addNewState(EntityState state) {
        states.add(state);
    }

}
