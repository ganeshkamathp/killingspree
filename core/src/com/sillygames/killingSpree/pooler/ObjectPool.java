package com.sillygames.killingSpree.pooler;

import com.badlogic.gdx.utils.Pool;
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;

public class ObjectPool {
    
    public static ObjectPool instance = new ObjectPool();
    public ConnectMessagePool connectMessagePool;
    public ControlsMessagePool controlsMessagePool;
    
    public ObjectPool() {
        connectMessagePool = new ConnectMessagePool();
        controlsMessagePool = new ControlsMessagePool();
        
    }
    
    public class ConnectMessagePool extends Pool<ConnectMessage> {

        @Override
        protected ConnectMessage newObject() {
            return new ConnectMessage();
        }
        
    }
    
    public class ControlsMessagePool extends Pool<ControlsMessage> {
        
        @Override
        protected ControlsMessage newObject() {
            return new ControlsMessage();
        }
        
    }

}
