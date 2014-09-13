package com.sillygames.killingSpree.pool;

import com.sillygames.killingSpree.networking.messages.ConnectMessage;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.EntityState;
import com.sillygames.killingSpree.networking.messages.GameStateMessage;

public class MessageObjectPool {
    
    public static MessageObjectPool instance = new MessageObjectPool();
    public Pool<ConnectMessage> connectMessagePool;
    public Pool<ControlsMessage> controlsMessagePool;
    public Pool<EntityState> entityStatePool;
    public Pool<GameStateMessage> gameStateMessagePool;
    
    public MessageObjectPool() {
        connectMessagePool = new Pool<ConnectMessage>() {

            @Override
            protected ConnectMessage getNewObject() {
                return new ConnectMessage();
            }
            
        };
        
        controlsMessagePool = new Pool<ControlsMessage>() {

            @Override
            protected ControlsMessage getNewObject() {
                return new ControlsMessage();
            }
            
        };
        
        entityStatePool = new Pool<EntityState>() {

            @Override
            protected EntityState getNewObject() {
                return new EntityState();
            }
            
        };
        entityStatePool.setMax(512);
        
        gameStateMessagePool = new Pool<GameStateMessage>() {
            
            @Override
            protected GameStateMessage getNewObject() {
                return new GameStateMessage();
            }
        };
    }

}
