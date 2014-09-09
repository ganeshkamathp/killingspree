package com.sillygames.killingSpree.networking;

import java.util.ArrayList;

import org.objenesis.instantiator.ObjectInstantiator;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryonet.EndPoint;
import com.sillygames.killingSpree.controls.ControlsMessage;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;
import com.sillygames.killingSpree.pooler.ObjectPool;

public class NetworkRegisterer {
    
    static public void register (EndPoint endPoint) {
        Registration registration;
        Kryo kryo = endPoint.getKryo();
        registration = kryo.register(ConnectMessage.class);
        registration.setInstantiator(new ObjectInstantiator<ConnectMessage>() {
            @Override
            public ConnectMessage newInstance() {
                return ObjectPool.instance.connectMessagePool.obtain();
            }
        });
        
        registration = kryo.register(ControlsMessage.class);
        registration.setInstantiator(new 
                ObjectInstantiator<ControlsMessage>() {
            @Override
            public ControlsMessage newInstance() {
                return ObjectPool.instance.controlsMessagePool.obtain();
            }
        });
        
        kryo.register(ArrayList.class);
        kryo.register(Vector2.class);
        kryo.register(String.class);
    }
    
}