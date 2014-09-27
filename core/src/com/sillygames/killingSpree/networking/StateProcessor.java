package com.sillygames.killingSpree.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.sillygames.killingSpree.clientEntities.ClientBomb;
import com.sillygames.killingSpree.clientEntities.ClientEntity;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;
import com.sillygames.killingSpree.pool.MessageObjectPool;

public class StateProcessor extends Listener{

    private static final int QUEUE_LENGTH = 6;
    private Client client;
    public ArrayList<GameStateMessage> stateQueue;
    public long timeOffset = 0;
    private GameStateMessage nextState;
    int lag = 0;
    AtomicBoolean wait;
    ConcurrentHashMap<Short, ClientEntity> world;
    public boolean disconnected;
    
    public StateProcessor(Client client, ConcurrentHashMap<Short, ClientEntity> worldMap) {
        if (client != null) {
            this.client = client;
            client.addListener(this);
        }
        nextState = MessageObjectPool.instance.
                gameStateMessagePool.obtain();
        nextState.time = 0;
        stateQueue = new ArrayList<GameStateMessage>();
        wait = new AtomicBoolean(false);
        this.world = worldMap;
        disconnected = false;
    }
    
    @Override
    public void connected(Connection connection) {
    }
    
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameStateMessage) {
            addNewState((GameStateMessage) object);
        } else if (object instanceof Short) {
            if(world.get(object) != null ) {
                world.get(object).destroy = true;
                //FIXME Set remove in the client entity
                if (!(world.get(object) instanceof ClientBomb))
                    world.get(object).remove = true;
            }
        }
        super.received(connection, object);
    }
    
    @Override
    public void disconnected(Connection connection){
        disconnected = true;
    }
    
    public void addNewState(GameStateMessage state) {
        if (wait == null) {
            wait = new AtomicBoolean(false);
        }
        if (stateQueue == null) {
            stateQueue = new ArrayList<GameStateMessage>();
        }
        while (!wait.compareAndSet(false, true));
        
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
        wait.set(false);
    }
    
    public void processStateQueue(long currentTime) {
        while (!wait.compareAndSet(false, true));
        if (stateQueue.size() < QUEUE_LENGTH) {
            wait.set(false);
            return;
        }
        
        while (stateQueue.size() > QUEUE_LENGTH) {
            stateQueue.remove(0);
        }
        
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
        

        for (GameStateMessage state: stateQueue) {
            this.nextState = state;
            if (state.time > currentServerTime) {
                break;
            }
        }
//        Gdx.app.log("Selected", Integer.toString(i));
        wait.set(false);
    }
    
    public GameStateMessage getNextState() {
        return nextState;
    }
    
    public void removeListener() {
        client.removeListener(this);
    }

}
