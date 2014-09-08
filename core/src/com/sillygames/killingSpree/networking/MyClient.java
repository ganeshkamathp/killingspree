package com.sillygames.killingSpree.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;

public class MyClient {
    
    public static MyClient instance = new MyClient();
    public Client client;
    
    public MyClient() {
        Gdx.app.log("Client", "started");
        client = new Client();
        NetworkRegisterer.register(client);
        client.start();
    }

    public String getName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        };
        return "error";
    }

    public void stop() {
        client.stop();
        client.start();
    }

    public void dispose() {
        client.stop();
        client = null;
    }
}
