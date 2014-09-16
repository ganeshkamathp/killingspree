package com.sillygames.killingSpree.networking.messages;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.sillygames.killingSpree.pool.MessageObjectPool;

public class StateProcessor extends Listener{

    private static final int QUEUE_LENGTH = 6;
    private Client client;
    public ArrayList<GameStateMessage> stateQueue;
    public long timeOffset = 0;
    private GameStateMessage previousState;
    private GameStateMessage nextState;
    int lag = 0;
    
    public StateProcessor(Client client) {
        if (client != null) {
            this.client = client;
            client.addListener(this);
        }
        previousState = MessageObjectPool.instance.
                gameStateMessagePool.obtain();
        nextState = MessageObjectPool.instance.
                gameStateMessagePool.obtain();
        previousState.time = 0;
        nextState.time = 0;
        stateQueue = new ArrayList<GameStateMessage>();
    }
    
    @Override
    public void connected(Connection connection) {
    }
    
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameStateMessage) {
            addNewState((GameStateMessage) object);
        }
        super.received(connection, object);
    }
    
    public void addNewState(GameStateMessage state) {
        if (stateQueue.size() == 0) {
            stateQueue.add(state); 
        }
        for (int i = stateQueue.size() - 1; i >= 0; i--) {
            if (stateQueue.get(i).time < state.time) {
//                Gdx.app.log("inserted at ", Integer.toString(i + 1));
                stateQueue.add(i + 1, state);
                break;
            }
        }
    }
    
    public void processStateQueue(long currentTime) {
//        Gdx.app.log("stateQueue size", Integer.toString(stateQueue.size()));
        if (stateQueue.size() < QUEUE_LENGTH) {
            return;
        }
        
        while (stateQueue.size() > QUEUE_LENGTH) {
            stateQueue.remove(0);
        }
        
//        for (int i = 0; i < QUEUE_LENGTH; i++) {
//            Gdx.app.log(Integer.toString(i), Long.toString(stateQueue.get(i).time));
//        }
        
        long currentServerTime = currentTime + timeOffset;
        if (currentServerTime < stateQueue.get(0).time) {
            lag++;
            if(lag > 3) {
                lag = 0;
                timeOffset = stateQueue.get(QUEUE_LENGTH - 2).time - currentTime;
                currentServerTime = currentTime + timeOffset;
            }
        } else if (currentServerTime > stateQueue.get(QUEUE_LENGTH - 1).time) {
            lag++;
            if(lag > 3) {
                lag = 0;
                timeOffset -= 10000;
                currentServerTime = currentTime + timeOffset;
            }
        } else {
            lag = 0;
        }
        
        int totalStates = stateQueue.size();
        int i = 2;
        previousState = stateQueue.get(0);
        nextState = stateQueue.get(0);
        while (nextState.time < currentServerTime && i < totalStates) {
            previousState = nextState;
            nextState = stateQueue.get(i);
            i++;
        }
//        Gdx.app.log("Selected", Integer.toString(i));
    }
    
    public GameStateMessage getPreviousState() {
        return previousState;
    }

    public GameStateMessage getNextState() {
        return nextState;
    }
    
    public void removeListener() {
        client.removeListener(this);
    }

}
