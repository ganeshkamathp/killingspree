package com.sillygames.killingSpree.networking;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.sillygames.killingSpree.networking.messages.ConnectMessage;
import com.sillygames.killingSpree.pooler.ObjectPool;

public class MyServer {
    
    public static MyServer instance = new MyServer();
    public Server server;
    
    public MyServer() {
        Gdx.app.log("Server", "started");
        server = new Server();
        NetworkRegisterer.register(server);
    }
    
    public void start() {
        server.start();
        try {
            server.bind(2000, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        server.stop();
        try {
            server.bind(9999,9999);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void sendHosts() {
        ConnectMessage message = new ConnectMessage();
        for(Connection connection : server.getConnections()) {
            message.insertNewHost(connection
                    .getRemoteAddressTCP().getHostName());
        }
        server.sendToAllTCP(message);
        ObjectPool.instance.connectMessagePool.free(message);
    }

    public void dispose() {
        server.stop();
        server = null;
    }

    public void startGame() {
        server.sendToAllTCP("start");
    }
    
}
