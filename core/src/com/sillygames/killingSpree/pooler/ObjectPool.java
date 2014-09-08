package com.sillygames.killingSpree.pooler;

import com.badlogic.gdx.utils.Pool;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;

public class ObjectPool {
    
    public static ObjectPool instance = new ObjectPool();
    public ConnectMessagePool connectMessagePool;
    
    public ObjectPool() {
        connectMessagePool = new ConnectMessagePool();
        
    }
    
    public class ConnectMessagePool extends Pool<ConnectMessage> {

        @Override
        protected ConnectMessage newObject() {
            return new ConnectMessage();
        }
        
    }

}
