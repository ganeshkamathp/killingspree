package com.sillygames.killingSpree.networking.messages;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class StateProcessor extends Listener{

    private Client client;
    private GameStateMessage states;
    
    public StateProcessor(Client client) {
        this.client = client;
        client.addListener(this);
        states = new GameStateMessage();
    }
    
    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (object instanceof GameStateMessage) {
            states = (GameStateMessage) object;
        }
    }
    
    public GameStateMessage getCurrentStates() {
        return states;
    }
    
    public void removeListener() {
        client.removeListener(this);
    }

}
