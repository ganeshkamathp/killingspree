package com.sillygames.killingSpree.pooler;

import com.badlogic.gdx.utils.Pool;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;

public class ObjectPool {
    
    public static ObjectPool instance = new ObjectPool();
    public ConnectMessagePool connectMessagePool;
    public ControlsMessagePool controlsMessagePool;
    
    public ObjectPool() {
        connectMessagePool = new ConnectMessagePool();
        controlsMessagePool = new ControlsMessagePool(32);
        
    }
    
    public class ConnectMessagePool extends Pool<ConnectMessage> {

        @Override
        protected ConnectMessage newObject() {
            return new ConnectMessage();
        }
        
    }
    
    public class ControlsMessagePool {
        
        private int max;
        private int current;
        private ControlsMessage[] controlsMessages; 
        
        public ControlsMessagePool(int max) {
            this.max = max;
            current = 0;
            controlsMessages = new ControlsMessage[max];
            for (int i=0; i < max; i++) {
                controlsMessages[i] = new ControlsMessage();
            }
        }

        public ControlsMessage obtain() {
            return controlsMessages[current % max];
        }
        
    }

}
