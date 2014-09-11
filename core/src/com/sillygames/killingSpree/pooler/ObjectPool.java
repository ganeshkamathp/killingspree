package com.sillygames.killingSpree.pooler;

import com.badlogic.gdx.utils.Pool;
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;

public class ObjectPool {
    
    public static ObjectPool instance = new ObjectPool();
    public ConnectMessagePool connectMessagePool;
    public ControlsMessagePool controlsMessagePool;
//    private int count = 0;
    
    public ObjectPool() {
        connectMessagePool = new ConnectMessagePool();
        controlsMessagePool = new ControlsMessagePool(2, 32);
        
    }
    
    public class ConnectMessagePool extends Pool<ConnectMessage> {

        @Override
        protected ConnectMessage newObject() {
            return new ConnectMessage();
        }
        
    }
    
    public class ControlsMessagePool extends Pool<ControlsMessage> {
        
        public ControlsMessagePool(int initial, int max) {
            super(initial, max);
        }

        @Override
        protected ControlsMessage newObject() {
            return new ControlsMessage();
        }
        
        @Override
        public ControlsMessage obtain() {
//            count++;
//            Gdx.app.log("obtain", Integer.toString(count));
            return super.obtain();
        }
        
        @Override
        public void free(ControlsMessage object) {
//            count--;
//            Gdx.app.log("free", Integer.toString(count));
            super.free(object);
        }
        
    }

}
